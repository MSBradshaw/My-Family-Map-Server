package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.Event;
import com.example.Model.Person;
import com.example.Model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.ErrorManager;

/**
 * Created by Michael on 3/3/2017.
 */

public class FillService {
    private boolean condition = true;
    private int successPersonCount = 0;
    private int successEventsCount = 0;
    private int generations = 4; //default value\
    private String username = "";
    private String errormessageStart = "{\n\t\"message\": \"";
    private String successmessageStart = "{\n\t";
    private String errormessageEnd = "\"\n}\n";
    private String successMessageEnd = "\n}\n";
    private String messageContent;
    private String dbPath = "jdbc:sqlite:mainData.sqlite";
    private Transaction trans = new Transaction();
    private int genCount = 0;

    public FillService(String userNameIn) {
        username = userNameIn;
    }

    public FillService(String userNameIn, int gen) {
        username = userNameIn;
        generations = gen;
    }

    public FillService(String userNameIn, String db) {
        username = userNameIn;
        dbPath = db;
    }

    public FillService(String userNameIn, String db, int gen) {
        username = userNameIn;
        generations = gen;
        dbPath = db;
    }
    public String fill(boolean bool) throws DatabaseException {
        //check if the user exists
        User user = new User();
        //check the value of generation
        if (generations < 0) {
            return errormessageStart + "Generation must be a non-negative integer" + errormessageEnd;
        }
        try {
            trans.startTransaction(dbPath);
            user = checkForUser();
            deleteEveryThingConnectedToUser(user.getUsername());
            Person P = userToPerson(user);
            successPersonCount++;
            generateGenerations(P);
        } catch (DatabaseException e) {
            messageContent = e.errormessage;
            endTransPrematurly();
            return errormessageStart + messageContent + errormessageEnd;
        }
        endTransForTestingOnly();
        return errormessageStart + "Successfully added " + successPersonCount + " persons and " + successEventsCount + " events to the data base" + errormessageEnd;
    }

    public String fill() throws DatabaseException {
        //check if the user exists
        User user = new User();
        //check the value of generation
        if (generations < 0) {
            return errormessageStart + "Generation must be a non-negative integer" + errormessageEnd;
        }
        try {
            trans.startTransaction(dbPath);
            user = checkForUser();
            deleteEveryThingConnectedToUser(user.getUsername());
            Person P = userToPerson(user);
            trans.P.addToPersonTable(P);
            successPersonCount++;
            generateGenerations(P);
        } catch (DatabaseException e) {
            messageContent = e.errormessage;
            endTransPrematurly();
            return errormessageStart + messageContent + errormessageEnd;
        }
        endTransForTestingOnly();
        return errormessageStart + "Successfully added " + successPersonCount + " persons and " + successEventsCount + " events to the data base" + errormessageEnd;
    }

    public void deleteEveryThingConnectedToUser(String UserName) throws DatabaseException {
        try {
            trans.P.deleteSpousesOfUser(UserName);
            trans.P.deleteMothersOfUser(UserName);
            trans.P.deleteFathersOfUser(UserName);
            trans.P.deletePersonsOfUser(UserName);
            trans.E.deleteEventsOfUser(UserName);
        } catch (DatabaseException e) {
            throw e;
        }
    }

    public User checkForUser() throws DatabaseException {
        User user = new User();
        //if he does not exists this is a problem
        user = trans.U.getUser(username);
        // if user object is null it means it was not found
        if (user == null) {
            messageContent = "User Name not found";
            throw new DatabaseException(errormessageStart + messageContent + errormessageEnd);
        }
        return user;
    }

    public void endTransPrematurly() {
        try {
            trans.endTransaction(false);
        } catch (DatabaseException e) {
            System.out.println("You should never see this, register user endTransaction");
        }
    }

    public void generateGenerations(Person P) throws DatabaseException {
        Map<Person, Integer> newPersons = new HashMap();
        Map<Person, Integer> temp = new HashMap();
        Random rand = new Random();
        int  decendantBirthYear = rand.nextInt(27) + 1990; // age at when decendent was born
        temp.put(P,decendantBirthYear);
        //create events for the original person
        generateEvents(P,decendantBirthYear);
        //add P to the templist
        //for gen
        for(int i =0; i < generations;i++){
            //for map of family members
            newPersons = new HashMap();
            for(Person j : temp.keySet()){
                try {
                    Person tempMother = generateRandomPerson(P.getDescendant(), "f");
                    int motherBirthYear = generateEvents(tempMother, temp.get(j));
                    newPersons.put(tempMother, motherBirthYear);
                    trans.P.addToPersonTable(tempMother);
                    successPersonCount++;

                    Person tempFather = generateRandomPerson(P.getDescendant(), "m");
                    int fatherBirthYear = generateEvents(tempFather, temp.get(j));
                    newPersons.put(tempFather, fatherBirthYear);
                    trans.P.addToPersonTable(tempFather);
                    successPersonCount++;
                }catch (Exception error){
                    DatabaseException e = new DatabaseException("");
                    e.errormessage = error.getMessage();
                    throw e;
                }
            }
            temp = newPersons;
        }

    }
    public int generateEvents(Person P, int birthYearofChild) throws DatabaseException{
        Event E = new Event();
        // get birth year
        //get marraige year
        //get death year
        Random rand = new Random();
        int  ageAtChildBirth = rand.nextInt(20) + 16; // age at when child was born
        int birthYear = birthYearofChild - ageAtChildBirth;
        rand = new Random();
        int  ageAtMarrage = rand.nextInt(20) + 16; // age at when person was married
        int marrage = birthYear + ageAtMarrage;
        rand = new Random();
        int  yearsAfterChildBithTheyDie = rand.nextInt(20) + 16;
        int death = birthYearofChild + yearsAfterChildBithTheyDie;
        try {
            try {
                createEvents(P, "birth", birthYear);
                createEvents(P, "marraige", marrage);
                createEvents(P, "death", death);
            } catch (DatabaseException e) {
                DatabaseException exc = new DatabaseException("");
                exc.errormessage = "Failed to generate events for a person";
                throw exc;
            }
        }catch (IOException ioe){
            DatabaseException exc = new DatabaseException("");
            exc.errormessage = "Failed to generate events for a person";
            throw exc;
        }
        return birthYear;
    }
    public Event getLocation() throws DatabaseException, IOException {
        Event eventArray[];
        Gson gson = new Gson();
        String locationData = new String(Files.readAllBytes(Paths.get("lib/src/main/java/com/example/Services/locations.json")));
        Data D = (Data)gson.fromJson(locationData, Data.class);
        Random rand = new Random();
        int  n = rand.nextInt(D.data.size()-1) + 0;
        //pick random events here
        D.data.get(n).setCity(D.data.get(n).getCity().replace("'","''"));
        D.data.get(n).setCountry(D.data.get(n).getCountry().replace("'","''"));

        return D.data.get(n);
    }
    public Event createEvents(Person P, String event, int year) throws DatabaseException, IOException {
        Event E = new Event();
        E = getLocation();
        E.setDescendant(P.descendant);
        E.setPersonID(P.getPersonID());
        E.setDescription(event);
        String yeartemp = Integer.toString(year);
        E.setYear(yeartemp);
        try {
            trans.E.addToEventTable(E);
            successEventsCount++;
        }catch(DatabaseException e){
            e.errormessage = "Failed to add an event";
            throw e;
        }
        return E;
    }
    public String getRandomIDCode() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public Person generateRandomPerson(String decendant, String gender) throws IOException, DatabaseException {
        Person P = new Person();
        P.setGender(gender);
        if(gender.equals("f") || gender.equals("F")){
            P.setFirstname(getFirstNameFemale());
        }else{
            P.setFirstname(getFirstNameMale());
        }
        P.setLastname(getLastName());
        P.setPersonID(getRandomIDCode());
        P.setSpouse(getRandomIDCode());
        P.setFather(getRandomIDCode());
        P.setMother(getRandomIDCode());
        P.setDescendant(decendant);
        return P;
    }
    public String getFirstNameFemale() throws IOException, DatabaseException {
        try {
            String nameArray[];
            Gson gson = new Gson();
            String nameData = new String(Files.readAllBytes(Paths.get("lib/src/main/java/com/example/Services/fnames.json")));
            NameData names = (NameData) gson.fromJson(nameData, NameData.class);
            Random rand = new Random();
            int n = rand.nextInt(names.data.size() - 1) + 0;
            //pick random events here
            String name = names.data.get(n);

            name.replace("'","''");
            return names.data.get(n);
        }catch (Exception error){
            DatabaseException e =  new DatabaseException("");
            e.errormessage = "Failed to generated a first name";
            throw e;
        }
    }
    private Person userToPerson(User user){
        Person person =  new Person();
        person.setDescendant(user.getUsername());
        person.setFirstname(user.getFirstname());
        person.setLastname(user.getLastname());
        person.setGender(user.getGender());
        person.setPersonID(user.getPersonID());
        return person;
    }
    public String getFirstNameMale() throws IOException, DatabaseException {
        try {
            String nameArray[];
            Gson gson = new Gson();
            String nameData = new String(Files.readAllBytes(Paths.get("lib/src/main/java/com/example/Services/mnames.json")));
            NameData names = (NameData) gson.fromJson(nameData, NameData.class);
            Random rand = new Random();
            int n = rand.nextInt(names.data.size() - 1) + 0;
            //pick random events here
            String name = names.data.get(n);

            name.replace("'","''");
            return names.data.get(n);
        }catch (Exception error){
            DatabaseException e =  new DatabaseException("");
            e.errormessage = "Failed to generated a first name";
            throw e;
        }
    }
    public String getLastName() throws IOException, DatabaseException {
        try{
        String nameArray[];
        Gson gson = new Gson();
        String nameData = new String(Files.readAllBytes(Paths.get("lib/src/main/java/com/example/Services/snames.json")));
        NameData names = (NameData)gson.fromJson(nameData, NameData.class);
        Random rand = new Random();
        int  n = rand.nextInt(names.data.size()-1) + 0;
        //pick random events here
            String name = names.data.get(n);
            name.replace("'","''");
            return name;
        }catch (Exception error){
            DatabaseException e = new DatabaseException("");
            e.errormessage = "Failed to generate a last name";
            throw e;
        }
    }
    public void startTransForTestingOnly() throws DatabaseException {
        trans.startTransaction(dbPath);
    }
    public void endTransForTestingOnly() throws DatabaseException {
        trans.endTransaction(true);
    }
    class Data{
       public List<Event> data;
    }
    class NameData{
        public List<String> data;
    }

}




















