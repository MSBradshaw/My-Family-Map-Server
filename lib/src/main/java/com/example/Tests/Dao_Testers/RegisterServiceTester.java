package com.example.Tests.Dao_Testers;
import org.junit.*;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.*;
import com.example.Services.ClearService;
import com.example.Services.EventService;
import com.example.Services.FillService;
import com.example.Services.EventWithIDService;
import com.example.Services.LoadService;
import com.example.Services.LoginService;
import com.example.Services.PersonService;
import com.example.Services.PersonWithIdService;
import com.example.Services.RegisterService;

import java.awt.SystemColor;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
/**
 * Created by Michael on 3/1/2017.
 */

public class RegisterServiceTester {
    private String loadTestString = "{\n" +
            "  \"users\": [\n" +
            "    {\n" +
            "      \"userName\": \"sheila\",\n" +
            "      \"password\": \"parker\",\n" +
            "      \"email\": \"sheila@parker.com\",\n" +
            "      \"firstName\": \"Sheila\",\n" +
            "      \"lastName\": \"Parker\",\n" +
            "      \"gender\": \"f\",\n" +
            "      \"personID\": \"Sheila_Parker\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"persons\": [\n" +
            "    {\n" +
            "      \"firstName\": \"Sheila\",\n" +
            "      \"lastName\": \"Parker\",\n" +
            "      \"gender\": \"f\",\n" +
            "      \"personID\": \"Sheila_Parker\",\n" +
            "      \"descendant\": \"sheila\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"events\": [\n" +
            "    {\n" +
            "      \"description\": \"started family map\",\n" +
            "      \"personID\": \"Sheila_Parker\",\n" +
            "      \"city\": \"Salt Lake City\",\n" +
            "      \"country\": \"United States\",\n" +
            "      \"latitude\": \"40.7500\",\n" +
            "      \"longitude\": \"-110.1167\",\n" +
            "      \"year\": \"2016\",\n" +
            "      \"descendant\":\"sheila\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    @Before
    public void setUp(){

    }
    @After
    public void shutDown() throws DatabaseException, SQLException {
        Transaction T = new Transaction();
        T.startTransaction("jdbc:sqlite:mainData.sqlite");
        T.clear();
        T.endTransaction(true);
    }
    @Test
    public void RegisterCreateObjectTest(){
        String test = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    \n" +
                "  \n"+
                "}";
        RegisterService Rs = new RegisterService(test);
        User U = null;
        U = Rs.createObject();
        assertTrue(U.getGender().equals("f"));
        assertTrue(U.getFirstname().equals("Sheila"));
        assertTrue(U.getLastname().equals("Parker"));
        assertTrue(U.getPersonID().equals("Sheila_Parker"));
        assertTrue(U.getEmail().equals("sheila@parker.com"));
        assertTrue(U.getPassword().equals("parker"));
        assertTrue(U.getUsername().equals("sheila"));
    }
    @Test
    public void checkInfoTest1() throws ClassNotFoundException, DatabaseException {
        String test = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parer\"\n" +
                "    \n" +
                "  \n"+
                "}";
        RegisterService Rs = new RegisterService(test,"jdbc:sqlite:mainData.sqlite");
        User U = null;
        //System.out.println(Rs.registerUser());
        Rs.registerUser();
    }
    @Test
    public void testLoginService() throws SQLException, DatabaseException {
        String test = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parer\"\n" +
                "    \n" +
                "  \n"+
                "}";
        String test2 = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parer\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parer\"\n" +
                "    \n" +
                "  \n"+
                "}";
        RegisterService Rs = new RegisterService(test,"jdbc:sqlite:mainData.sqlite");
        //System.out.println(Rs.registerUser());
        Rs.registerUser();
        LoginService login = new LoginService(test,"jdbc:sqlite:mainData.sqlite");
        //System.out.println(login.logIn());
        login.logIn();
    }
    @Test
    public void testClearService(){
        ClearService clearObject = new ClearService();
        //System.out.println(clearObject.clear());
        clearObject.clear();
    }
    @Test
    public void testDeleteBasedonUserID() throws DatabaseException {
        String test = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"mike\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"\"\n" +
                "    \n" +
                "  \n"+
                "}";
        String test2 = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"jim\",\n" +
                "      \"password\": \"parer\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"\"\n" +
                "    \n" +
                "  \n"+
                "}";
        String test3 = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"jeff\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"\"\n" +
                "    \n" +
                "  \n"+
                "}";
        String test4 = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"tim\",\n" +
                "      \"password\": \"parer\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"\"\n" +
                "    \n" +
                "  \n"+
                "}";
        RegisterService Rs1 = new RegisterService(test,"jdbc:sqlite:mainData.sqlite");
        Rs1.registerUser();
        RegisterService Rs2 = new RegisterService(test2,"jdbc:sqlite:mainData.sqlite");
        Rs2.registerUser();
        RegisterService Rs3 = new RegisterService(test3,"jdbc:sqlite:mainData.sqlite");
        Rs3.registerUser();
        RegisterService Rs4 = new RegisterService(test4,"jdbc:sqlite:mainData.sqlite");
        Rs4.registerUser();
    }
    @Test
    public void generationTester(){
        FillService F_it = new FillService("Mike","jdbc:sqlite:mainData.sqlite",3);
        Person temp = new Person();
        temp.setMother("Branda");
        temp.setFather("Scott");
        temp.setPersonID("123456768");
        temp.setFirstname("Michael");
        temp.setLastname("Bradshaw");
        temp.setDescendant("Mikey");
        try{
            F_it.startTransForTestingOnly();
            F_it.generateGenerations(temp);
            F_it.endTransForTestingOnly();
        }catch (DatabaseException e){

            fail("GenerationTest Failed: " + e.errormessage);
        }
        String dumb = null;
    }
    @Test
    public void generateLocationTest() throws DatabaseException, IOException {
        FillService fill = new FillService("Mike","jdbc:sqlite:mainData.sqlite",3);
        Event E = fill.getLocation();
        E.setEventID("goat");
        Person P = new Person();
        P.setDescendant("Me");
        P.setPersonID("abc123");
        try{
            fill.startTransForTestingOnly();
            fill.generateGenerations(P);
            fill.endTransForTestingOnly();
            Transaction tran =  new Transaction();
            tran.startTransaction("jdbc:sqlite:mainData.sqlite");
            Set<Event> eventset = tran.E.getAllEvents("Me");
            tran.endTransaction(true);
        }catch (DatabaseException e){

            fail("GenerationTest Failed: " + e.errormessage);
        }
        String me = null;
    }
    @Test
    public void fillserviceTest() throws DatabaseException {
        FillService fill = new FillService("mike","jdbc:sqlite:mainData.sqlite",3);
        String test = "{\n" +
                "\n" +
                "    \n" +
                "      \"userName\": \"mike\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"mike_Parer\"\n" +
                "    \n" +
                "  \n"+
                "}";
        try{
            RegisterService Rs1 = new RegisterService(test,"jdbc:sqlite:mainData.sqlite");
            Rs1.registerUser();
            fill.fill();
        }catch(DatabaseException e){
            fail("Failed to use the fill service " + e.errormessage);
        }
    }
    @Test
    public void creatingRandomNames() throws IOException, DatabaseException {
        FillService fill = new FillService("mike","jdbc:sqlite:mainData.sqlite",3);
        try {
            fill.getFirstNameFemale();
            fill.getFirstNameMale();
            fill.getLastName();
        }catch (DatabaseException e){
            fail("Failed to use generate random names " +e.errormessage);
        }
    }
    @Test
    public void loadServiceTest() throws DatabaseException {
        LoadService loadService = new LoadService(loadTestString,"jdbc:sqlite:mainData.sqlite");
        loadService.load();
    }
    @Test
    public void personserviceTest() throws DatabaseException {
        fillserviceTest();
        Transaction transaction = new Transaction();
        transaction.startTransaction("jdbc:sqlite:mainData.sqlite");
        User user = transaction.U.getUser("mike");
        transaction.endTransaction(true);
        PersonService personService = new PersonService(user.getUsername(),"jdbc:sqlite:mainData.sqlite");
        String temp = personService.getPerson();

    }
    @Test
    public void eventWithIdTest() throws DatabaseException, SQLException, ClassNotFoundException {
        DAO_TESTER dao_tester = new DAO_TESTER();
        dao_tester.setUp();
        dao_tester.addToEventTable();
        dao_tester.T.endTransaction(true);
        EventWithIDService eventWithIDService = new EventWithIDService("123-456-789-abc-def","jdbc:sqlite:mainData.sqlite");
        String temp = eventWithIDService.getEvent();
        if(temp.length() < 20){
            fail("too short");
        }
    }
    @Test
    public void eventServiceTest() throws DatabaseException {
        try {
            fillserviceTest();
            Transaction transaction = new Transaction();
            transaction.startTransaction("jdbc:sqlite:mainData.sqlite");
            User user = transaction.U.getUser("mike");
            transaction.endTransaction(true);
            EventService eventService = new EventService(user.getUsername(),"jdbc:sqlite:mainData.sqlite");
            String temp = eventService.getEvents();
            if(temp.length() < 50){
                fail("too short");
            }
        }catch (DatabaseException e){
            fail(e.errormessage);
        }

    }
    private String extractAuthID(String string){

        Pattern MY_PATTERN = Pattern.compile("Code\":\"(.*)\"");
        Matcher m = MY_PATTERN.matcher(string);
        String s = "";
        while (m.find()) {
            s = m.group(1);
            // s now contains "BAR"
        }
        return s;
    }
    @Test
    public void regexTesting(){
        String string = "{\n" +
                "\t\"authorizationCode\":\"6f3f6934-459e-42b7-935c-60f238324abf\",\n" +
                "\t\"UserName\":\"username\",\n" +
                "\t\"timeIssued\":\"20170323_175725\"\n" +
                "}";
        String s = extractAuthID(string);
        assertTrue(s.equals("6f3f6934-459e-42b7-935c-60f238324abf"));
    }
    private boolean checkForAuthToken(String word){
        String authString = "authorizationCode";
        if(word == null || word.equals("")){
            return false;
        }
        return word.contains(authString);
    }
    @Test
    public void checkForAuthTest(){
        String string = "{\n" +
                "\t\"authorizationCode\":\"6f3f6934-459e-42b7-935c-60f238324abf\",\n" +
                "\t\"UserName\":\"username\",\n" +
                "\t\"timeIssued\":\"20170323_175725\"\n" +
                "}";
        assertTrue(checkForAuthToken(string));
    }
    @Test
    public void checkForNoAuthTest(){
        String string = "{\n" +
                "Mike Bradshaw"+
                "}";
        assertTrue(!checkForAuthToken(string));
    }
    public Map<String,List<Event>> personEvents;
    @Test
    public void fillpersonEvents(){
        Map<String, Person> people = new HashMap();
        personEvents = new HashMap();
        Map<String, Event> events = new HashMap();
        Person person1 = new Person();
        person1.setPersonID("mike");
        Event event1 = new Event();
        event1.setPersonID("mike");
        Event event2 = new Event();
        event2.setPersonID("mike");
        Event event3 = new Event();
        event3.setPersonID("mike");
        Event event4 = new Event();
        event4.setPersonID("mike");
        Person person2 = new Person();
        person2.setPersonID("jeff");
        Event event5 = new Event();
        event5.setPersonID("jeff");
        Event event6 = new Event();
        event6.setPersonID("jeff");
        Event event7 = new Event();
        event7.setPersonID("jeff");
        Event event8 = new Event();
        event8.setPersonID("jeff");
        event1.setEventID("1");
        event2.setEventID("2");
        event3.setEventID("3");
        event4.setEventID("4");
        event5.setEventID("5");
        event6.setEventID("6");
        event7.setEventID("7");
        event8.setEventID("8");
        people.put(person1.getPersonID(), person1);
        people.put(person2.getPersonID(), person2);
        events.put(event1.getEventID(),event1);
        events.put(event2.getEventID(),event2);
        events.put(event3.getEventID(),event3);
        events.put(event4.getEventID(),event4);
        events.put(event5.getEventID(),event5);
        events.put(event6.getEventID(),event6);
        events.put(event7.getEventID(),event7);
        events.put(event8.getEventID(),event8);
        List<Event> eventsTemp;
        for(String pKey : people.keySet()){
            eventsTemp = new ArrayList();
            for(String eKey : events.keySet()){
                if(events.get(eKey).getPersonID().equals(pKey)){
                    eventsTemp.add(events.get(eKey));
                }
            }
            personEvents.put(pKey,eventsTemp);
        }
        System.out.println(personEvents.get("mike").size());
    }
}

