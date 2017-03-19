package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.Event;
import com.example.Model.Person;
import com.example.Model.User;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Michael on 3/7/2017.
 */

public class LoadService {
    private String dbPath = "jdbc:sqlite:mainData.sqlite";
    private Transaction trans = new Transaction();
    private Data data;
    private String jsonIn;
    private String messageStart = "{\n\t\"message\": \"";
    private String messageEnd = "\"\n}\n";
    public LoadService(String jsonTemp){
        jsonIn = jsonTemp;
        Gson gson = new Gson();
        data = (Data)gson.fromJson(jsonIn, Data.class);
    }
    public LoadService(String jsonTemp, String db){
        dbPath = db;
        jsonIn = jsonTemp;
        Gson gson = new Gson();
        data = (Data)gson.fromJson(jsonIn, Data.class);
    }
    public String load(){
        int userCount = 0;
        int personCount = 0;
        int eventCount = 0;
        try {
            trans.startTransaction(dbPath);
            trans.clear();
            trans.endTransaction(true);
            trans.startTransaction(dbPath);
            userCount = loadUsers(userCount);
            personCount = loadPersons(personCount);
            eventCount = loadEvents(eventCount);
            trans.endTransaction(true);
        }catch (DatabaseException e){
            endTransPrematurly();
            return messageStart + e.errormessage + messageEnd;
        }
        return messageStart + "Successfully added " + userCount + " users, " + personCount
                            + " person, and " + eventCount + " events"+ messageEnd;
    }
    public int loadUsers(int count) throws DatabaseException {
        try {
            for (User user : data.users) {
                trans.U.addToUserTable(user);
                trans.endTransaction(true);
                trans.startTransaction(dbPath);
                trans.endTransaction(true);
                trans.startTransaction(dbPath);
                count++;
            }
        }catch (DatabaseException e){
            //e.errormessage = "Failed to add User from loadservice";
            throw e;
        }
        return count;
    }
    public int loadPersons(int count) throws DatabaseException {
        try {
            for (Person person : data.persons) {
                try {
                    trans.P.getPerson(person.personID);
                }catch (DatabaseException e){
                    trans.P.addToPersonTable(person);
                    count++;
                }
            }
        }catch (DatabaseException e){
            e.errormessage = "Failed to add Person from loadservice";
            throw e;
        }
        return count;
    }
    public int loadEvents(int count) throws DatabaseException {
        try {
            for (Event event : data.events){
                trans.E.addToEventTable(event);
                count++;
            }
        }catch (DatabaseException e){
            e.errormessage = "Failed to add Event from loadservice";
            throw e;
        }
        return count;
    }
    public void endTransPrematurly() {
        try {
            trans.endTransaction(false);
        } catch (DatabaseException e) {
            System.out.println("You should never see this, register user endTransaction");
        }
    }
    class Data{
        public List<User> users;
        public List<Person> persons;
        public List<Event> events;
    }
}
