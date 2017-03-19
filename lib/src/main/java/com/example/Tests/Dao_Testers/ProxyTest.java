package com.example.Tests.Dao_Testers;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Handlers.SuperGlobalStuffYouShouldNotHave;
import com.example.Model.*;
import com.example.Server.Server;
import com.example.Services.FillService;
import com.example.Services.LoadService;
import com.example.Services.RegisterService;
import com.google.gson.Gson;

import org.junit.*;

import java.io.IOException;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Created by Michael on 3/7/2017.
 */

public class ProxyTest {
    SuperGlobalStuffYouShouldNotHave counter =  new SuperGlobalStuffYouShouldNotHave();
    private ServerProxy serverProxy;
    private Server server;
    private User userMike = new User();
    private User userJeff = new User();

    @Before
    public void startUp(){
        try {
            String[] args = new String[1];
            args[0] = "8080";
            serverProxy = new ServerProxy();
          //  Server server = new Server();
            //server.main(args);
        }catch (Exception e){
            e.printStackTrace();
            fail("failed to start the server");
        }
        userMike.setGender("m");
        userMike.setPersonID("1234567890");
        userMike.setUsername("mbrad96");
        userMike.setPassword("secret");
        userMike.setEmail("michaelscottbradshaw@gmail.com");
        userMike.setFirstname("Mike");
        userMike.setLastname("Bradshaw");

        userJeff.setGender("m");
        userJeff.setPersonID("8675309");
        userJeff.setUsername("jeff007");
        userJeff.setPassword("secret");
        userJeff.setEmail("jeff@gmail.com");
        userJeff.setFirstname("Jeff");
        userJeff.setLastname("Brown");

        Event eventBirth = new Event();
        eventBirth.setEventID("1");
        eventBirth.setCity("New York");
        eventBirth.setLatitude("100");
        eventBirth.setLongitude("200");
        eventBirth.setYear("1990");
        eventBirth.setDescription("Birth");

    }
    @After
    public void closeDown() throws DatabaseException {
        Transaction transaction =  new Transaction();
        transaction.startTransaction("jdbc:sqlite:mainData.sqlite");
        transaction.clear();
        transaction.endTransaction(true);
    }
    @Test
    public void regesterOneUserTest(){

        try {
            String output = serverProxy.register(userMike);
            if (output == null) {
                
                fail("Failed to register a user, returned null");
            }
        }catch (Exception e){
            e.printStackTrace();
            fail("Failed to register a user, an error was thrown");
        }

    }
    @Test
    public void regesterOneDifferentUserTest(){

        try {
            if (serverProxy.register(userJeff) == null) {
                
                fail("Failed to register a user, returned null");
            }
        }catch (Exception e){
            e.printStackTrace();
            fail("Failed to register a user, an error was thrown");
        }

    }
    @Test
    public void RegesterNullUser(){
        userMike.setPassword(null);
        try {
            if (!serverProxy.register(userMike).equals("{\t\"message\": \"You must have a password\"}")) {
                
                fail("Failed to register a user, returned false");
            }
        }catch (Exception e){
            e.printStackTrace();
            fail("Failed to register a user, an error was thrown");
        }
        userMike.setPassword("secret");

    }
    @Test
    public void RegesterTwoUsersTest(){

        try {
            if (serverProxy.register(userMike) == null) {
                fail("Failed to register a user, returned false");
            }
            if (serverProxy.register(userJeff) == null) {
                fail("Failed to register a user, returned false");
            }
        }catch (Exception e){
            // e.printStackTrace();
            fail("Failed to register a user, an error was thrown");
        }


    }
    @Test
    public void RegesterUserTwiceTest(){

        try {
            if (serverProxy.register(userMike) == null) {
                fail("Failed to register a user, returned false 1");
            }
            if (serverProxy.register(userMike) != null) {
                fail("Failed to register a user, returned false 2");
            }
        }catch (Exception e){
            // e.printStackTrace();
            fail("Failed to register a user, an error was thrown");
        }


    }
    @Test
    public void RegesterAddTwoUsersThenTryTheFirstAgainTest(){

        try {
            if (serverProxy.register(userMike) == null) {
                fail("Failed to register a user, returned false");
            }
            if (serverProxy.register(userJeff) == null) {
                fail("Failed to register a user, returned false");
            }
            if (serverProxy.register(userMike) != null) {
                fail("Failed to register a user, returned false 2");
            }
        }catch (Exception e){
            // e.printStackTrace();
            fail("Failed to register a user, an error was thrown");
        }

    }
    @Test
    public void loginSingleUserTest(){
        try{
            regesterOneUserTest();
            String auth = serverProxy.login(userMike);
            if(auth.length() < 100){
                fail(auth + " is too short");
            }
        }catch (Exception e){
            fail("Failed to login");
        }
    }
    @Test
    public void loginDoubleUserTest(){
        try{
            RegesterTwoUsersTest();
            String auth = serverProxy.login(userMike);
            String auth2 = serverProxy.login(userJeff);
            if(auth.length() < 100){
                fail(auth + " is too short");
            }
            if(auth2.length() < 100){
                fail(auth + " is too short");
            }
        }catch (Exception e){
            fail("Failed to login");
        }
    }
    @Test
    public void loginUnregisteredUserTest(){
        try{
            regesterOneUserTest();
            String auth = serverProxy.login(userJeff);
            if(auth.length() > 100){
                fail(auth + " is too Long");
            }
        }catch (Exception e){
            fail("Failed to login");
        }
    }
    @Test
    public void loginSingleUserBadPassWordTest(){
        try{
            regesterOneUserTest();
            userMike.setPassword("WRONG PASSWORD");
            String auth = serverProxy.login(userMike);
            userMike.setPassword("secret");
            if(auth.length() > 100){
                fail(auth + " is too short");
            }
        }catch (Exception e){
            fail("Failed to login");
        }
    }
    @Test
    public void loginSingleUserNULLPassWordTest(){
        try{
            regesterOneUserTest();
            User tempMike = userMike;
            tempMike.setPassword(null);
            String auth = serverProxy.login(tempMike);
            userMike.setPassword("secret");
            if(auth.length() > 100){
                fail(auth + " is too long");
            }
        }catch (Exception e){
            fail("Failed to login");
        }
    }
    @Test
    public void loginSingleUserNULLUserNameTest(){
        try{
            regesterOneUserTest();
            User tempMike = userMike;
            tempMike.setUsername(null);
            String auth = serverProxy.login(tempMike);
            userMike.setPassword("secret");
            if(auth.length() > 100){
                fail(auth + " is too long");
            }
        }catch (Exception e){
            fail("Failed to login");
        }
    }
    @Test
    public void loginSingleUserBadUserNameTest(){
        try{
            regesterOneUserTest();
            User tempMike = userMike;
            tempMike.setUsername("foobar");
            String auth = serverProxy.login(tempMike);
            userMike.setPassword("secret");
            if(auth.length() > 50){
                fail(auth + " is too long");
            }
        }catch (Exception e){
            fail("Failed to login");
        }
    }
    @Test
    public void clearThreeGensTest(){
        try{
            RegisterServiceTester registerServiceTester = new RegisterServiceTester();
            registerServiceTester.generateLocationTest();
            String output = serverProxy.clear();
            if(!output.equals("{\t\"message\": \"Eveything was deleted\"}")){
                fail("Did not clear all the data");
            }
        }catch (Exception e){
            fail("Did not clear the data");
        }
    }
    @Test
    public void clearEmptyTest(){
        try{
            String output = serverProxy.clear();
            if(!output.equals("{\t\"message\": \"Eveything was deleted\"}")){
                fail("Something when wrong clearing the data");
            }
        }catch (Exception e){
            fail("Did not clear the data");
        }
    }
    @Test
    public void fillDefaultTest(){
        regesterOneUserTest();
        String output = serverProxy.fill(userMike.getUsername());
        assertTrue(output.equals("{\t\"message\": \"Successfully added 31 persons and 93 events to the data base\"}"));
    }
    @Test
    public void fillOneGenTest() {
        try{
        serverProxy.register(userMike);
        String output = serverProxy.fill(userMike.getUsername(),1);
        assertTrue(output.equals("{\t\"message\": \"Successfully added 3 persons and 9 events to the data base\"}"));
        }catch (Exception e){
            fail("exception was thrown");
        }
    }
    @Test
    public void fillTwoGenTest(){
        regesterOneUserTest();
        String output = serverProxy.fill(userMike.getUsername(),2);
        assertTrue(output.equals("{\t\"message\": \"Successfully added 7 persons and 21 events to the data base\"}"));
    }
    @Test
    public void fillTwoGenWithDifferentUserTest(){
        regesterOneDifferentUserTest();
        String output = serverProxy.fill(userJeff.getUsername(),2);
        assertTrue(output.equals("{\t\"message\": \"Successfully added 7 persons and 21 events to the data base\"}"));
    }
    @Test
    public void fillZeroGenTest(){
        regesterOneUserTest();
        
        String output = serverProxy.fill(userMike.getUsername(),0);
        assertTrue(output.equals("{\t\"message\": \"Successfully added 1 persons and 3 events to the data base\"}"));
    }
    @Test
    public void fillNegativeGenTest(){
        regesterOneUserTest();
        String output = serverProxy.fill(userMike.getUsername(),-1);
        assertTrue(output.equals("{\t\"message\": \"Generation must be a non-negative integer\"}"));
    }
    @Test
    public void loadSmallSetTest(){
        try {
            try {
                FillService fillService = new FillService("mike_brad");
                User[] users = new User[1];
                users[0] = userMike;
                Person[] persons = new Person[1];
                persons[0] = fillService.generateRandomPerson(userMike.getUsername(), "m");
                persons[0].setPersonID("123456");
                Event[] events = new Event[1];
                events[0] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[0].setEventID("747");
                String output = serverProxy.load(users,persons,events);
                assertTrue(output.equals("{\t\"message\": \"Successfully added 1 users, 1 person, and 1 events\"}"));
            } catch (DatabaseException e) {
                fail(e.errormessage);
            }
        }catch (IOException e){
            fail("Fail Loading a small set IOException");
        }
    }
    @Test
    public void loadLargerSetTest(){
        try {
            try {
                FillService fillService = new FillService("mike_brad");
                User[] users = new User[2];
                users[0] = userMike;
                users[1] = userJeff;
                Person[] persons = new Person[2];
                persons[0] = fillService.generateRandomPerson(userJeff.getUsername(), "m");
                persons[1] = fillService.generateRandomPerson(userMike.getUsername(), "f");
                persons[1].setPersonID("123456");
                Event[] events = new Event[2];
                events[0] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[01] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                String output = serverProxy.load(users,persons,events);
                assertTrue(output.equals("{\t\"message\": \"Successfully added 2 users, 2 person, and 2 events\"}"));
            } catch (DatabaseException e) {
                fail(e.errormessage);
            }
        }catch (IOException e){
            fail("Fail Loading a small set IOException");
        }
    }
    @Test
    public void loadLargeAmountOfEventsSetTest(){
        try {
            try {
                FillService fillService = new FillService("mike_brad");
                User[] users = new User[2];
                users[0] = userMike;
                users[1] = userJeff;
                Person[] persons = new Person[2];
                persons[0] = fillService.generateRandomPerson(userJeff.getUsername(), "m");
                persons[1] = fillService.generateRandomPerson(userJeff.getUsername(), "f");
                Event[] events = new Event[10];
                events[0] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[1] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                events[2] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[3] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[4] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[5] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[6] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[7] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[8] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
                events[9] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
                String output = serverProxy.load(users,persons,events);
                assertTrue(output.equals("{\t\"message\": \"Successfully added 2 users, 2 person, and 10 events\"}"));
            } catch (DatabaseException e) {
                fail(e.errormessage);
            }
        }catch (IOException e){
            fail("Fail Loading a small set IOException");
        }
    }
    @Test
    public void loadHugeAmountOfEventsSetTest(){
        try {
            try {
                FillService fillService = new FillService("mike_brad");
                User[] users = new User[2];
                users[0] = userMike;
                users[1] = userJeff;
                Person[] persons = new Person[2];
                persons[0] = fillService.generateRandomPerson(userJeff.getUsername(), "m");
                persons[1] = fillService.generateRandomPerson(userJeff.getUsername(), "f");
                Event[] events = new Event[70];
                events[0] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[1] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                events[2] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[3] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[4] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[5] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[6] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[7] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[8] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
                events[9] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
                events[10] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[11] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                events[12] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[13] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[14] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[15] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[16] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[17] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[18] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
                events[19] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
                events[20] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[21] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                events[22] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[23] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[24] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[25] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[26] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[27] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[28] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
                events[29] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
                events[30] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[31] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                events[32] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[33] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[34] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[35] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[36] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[37] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[38] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
                events[39] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
                events[40] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[41] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                events[42] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[43] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[44] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[45] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[46] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[47] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[48] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
                events[49] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
                events[50] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[51] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                events[52] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[53] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[54] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[55] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[56] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[57] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[58] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
                events[59] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
                events[60] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
                events[61] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
                events[62] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[63] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[64] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[65] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[66] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
                events[67] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
                events[68] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
                events[69] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
                String output = serverProxy.load(users,persons,events);
                assertTrue(output.equals("{\t\"message\": \"Successfully added 2 users, 2 person, and 70 events\"}"));
            } catch (DatabaseException e) {
                fail(e.errormessage);
            }
        }catch (IOException e){
            fail("Fail Loading a small set IOException");
        }
    }
    @Test
    public void loadEmptySetTest(){
        try {
            FillService fillService = new FillService("mike_brad");
            User[] users = new User[0];
            Person[] persons = new Person[0];
            Event[] events = new Event[0];
            String output = serverProxy.load(users,persons,events);
            assertTrue(output.equals("{\t\"message\": \"Successfully added 0 users, 0 person, and 0 events\"}"));
        } catch (Exception e) {
            fail("Exception was thrown");
        }
    }
    @Test
    public void personWithIdsimpleTest(){
        loadSmallSetTest();
        String auth = serverProxy.login(userMike);
        try{
           String output = serverProxy.person("/"+"123456",getAuthString(auth));
            if(output.length() < 200){
                fail("The Result is incorrect: " + output);
            }
        }catch (Exception e){
            fail("An exeption was thrown");
        }
    }
    @Test
    public void personWithIdWrongUserIdTest(){
        loadLargerSetTest();
        String auth = serverProxy.login(userJeff);
        try{
            String output = serverProxy.person("/"+"123456",getAuthString(auth));
            
            if(output.length() > 100){
                fail("Auth Tokens Matched but should not have");
            }
        }catch (Exception e){
            fail("An exeption was thrown");
        }
    }
    @Test
    public void personWithBadAuthTest(){
        loadSmallSetTest();
        
        String auth = serverProxy.login(userMike);
        
        try{
            String output = serverProxy.person("/"+"123456","bad_auth_token");
            
            if(output.length() > 100){
                fail("Auth token should have been invalid");
            }
        }catch (Exception e){
            fail("An exeption was thrown");
        }
    }
    @Test
    public void personWithBadPersonIdTest(){
        loadSmallSetTest();
        
        String auth = serverProxy.login(userMike);
        
        try{
            String output = serverProxy.person("/"+"Not_a_real_id","123456");
            
            if(output.length() > 100){
                fail("Person Should not have been found, let alone returned");
            }
        }catch (Exception e){
            fail("An exeption was thrown");
        }
    }

    @Test
    public void personAllFamilyMembersSmallFamilyTest(){
        fillOneGenTest();
        
        try{
            
            String auth = serverProxy.login(userMike);
            serverProxy.person("",getAuthString(auth));
        }catch (Exception e){
            fail("An Exception was thrown in SmallFamilyTest");
        }
    }
    @Test
    public void personAllFamilyMembersMediumFamilyTest(){
        fillTwoGenTest();
        
        try{
            
            String auth = serverProxy.login(userMike);
            String output = serverProxy.person("",getAuthString(auth));
            Person[] people = toPersonArray(output);
            
            if(people.length != 7){
                fail("wrong number of people returned");
            }
        }catch (Exception e){
            fail("An Exception was thrown in SmallFamilyTest");
        }
    }
    @Test
    public void personAllFamilyMembersNoFamilyTest(){
        fillZeroGenTest();
        
        try{
            
            String auth = serverProxy.login(userMike);
            String output = serverProxy.person("",getAuthString(auth));
            try {
                Person[] people = toPersonArray(output);
                if(people.length !=1) {
                    fail("The Family Should be empty you fool");
                }
            }catch (Exception e){
                //good
            }
        }catch (Exception e){
            fail("An Exception was thrown in SmallFamilyTest");
        }
    }
    @Test
    public void personAllFamilyMembersMultipleFamilies(){
        userMike.setUsername("MIKE");
        fillTwoGenWithDifferentUserTest();
        fillTwoGenTest();
        
        try{
            
            String auth = serverProxy.login(userMike);
            userMike.setUsername("0987654321");
            String output = serverProxy.person("",getAuthString(auth));
            Person[] people = toPersonArray(output);
            
            if(people.length != 7){
                fail("wrong number of people returned");
            }
        }catch (Exception e){
            fail("An Exception was thrown in SmallFamilyTest");
        }
    }
    @Test
    public void eventwithNoIdeaSingleEvent(){
        loadSmallSetTest();
        try{
            String auth = serverProxy.login(userMike);
            String output = serverProxy.event("",getAuthString(auth));
            
            Event[] events = toEventArray(output);
            
            if(events.length != 1){
                fail("Length Should be 1 not " + events.length);
            }
        }catch (Exception e){
            fail("Exception was thrown");
        }
    }
    @Test
    public void eventwithNoIDManyEvents(){

        try{
            loadLargeAmountOfEventsSetTest();
            String auth = serverProxy.login(userJeff);
            String output = serverProxy.event("",getAuthString(auth));
            
            Event[] events = toEventArray(output);
            
            if(events.length != 10){
                fail("Length Should be 10 not " + events.length);
            }
        }catch (Exception e){
            fail("Exception was thrown");
        }
    }
    @Test
    public void eventwithNoID_TONS_ofEventsTest(){

        try{
            loadHugeAmountOfEventsSetTest();
            String auth = serverProxy.login(userJeff);
            String output = serverProxy.event("",getAuthString(auth));
            
            Event[] events = toEventArray(output);
            
            if(events.length != 70){
                fail("Length Should be 70 not " + events.length);
            }
        }catch (Exception e){
            fail("Exception was thrown");
        }
    }
    @Test
    public void eventWithIDSimpleTest(){
        try{
            FillService fillService = new FillService("mike_brad");
            User[] users = new User[1];
            users[0] = userMike;
            Person[] persons = new Person[1];
            persons[0] = fillService.generateRandomPerson(userMike.getUsername(), "m");
            persons[0].setPersonID("123456");
            Event[] events = new Event[1];
            events[0] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
            events[0].setEventID("747");
            String output = serverProxy.load(users,persons,events);

            String auth = serverProxy.login(userMike);
            output = serverProxy.event("/747",getAuthString(auth));
            
            Event event = toEvent(output);
            if(output.length() < 100){
                fail("Too short");
            }
        }catch (Exception e){
            fail("Exception was thrown");
        }
    }
    @Test
    public void eventWithIDComplexTest(){
        try{
            FillService fillService = new FillService("mike_brad");
            User[] users = new User[2];
            users[0] = userMike;
            users[1] = userJeff;
            Person[] persons = new Person[2];
            persons[0] = fillService.generateRandomPerson(userJeff.getUsername(), "m");
            persons[1] = fillService.generateRandomPerson(userJeff.getUsername(), "f");
            Event[] events = new Event[10];
            events[0] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
            events[0].setEventID("1");
            events[1] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
            events[1].setEventID("2");
            events[2] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
            events[2].setEventID("3");
            events[3] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
            events[3].setEventID("4");
            events[4] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
            events[5] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
            events[6] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
            events[7] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
            events[8] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
            events[9] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
            events[9].setEventID("747");
            String output = serverProxy.load(users,persons,events);
            String auth = serverProxy.login(userJeff);
            output = serverProxy.event("/747",getAuthString(auth));
            
            Event event = toEvent(output);
            if(output.length() < 100){
                fail("Too short");
            }
        }catch (Exception e){
            fail("Exception was thrown");
        }
    }
    @Test
    public void eventWithIDComplexBadUserIDTest(){
        try{
            FillService fillService = new FillService("mike_brad");
            User[] users = new User[2];
            users[0] = userMike;
            users[1] = userJeff;
            Person[] persons = new Person[2];
            persons[0] = fillService.generateRandomPerson(userJeff.getUsername(), "m");
            persons[1] = fillService.generateRandomPerson(userJeff.getUsername(), "f");
            Event[] events = new Event[10];
            events[0] = createEventsForTestingOnly(persons[0],"birth",2001, fillService);
            events[0].setEventID("1");
            events[1] = createEventsForTestingOnly(persons[1],"birth",1994, fillService);
            events[1].setEventID("2");
            events[2] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
            events[2].setEventID("3");
            events[3] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
            events[3].setEventID("4");
            events[4] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
            events[5] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
            events[6] = createEventsForTestingOnly(persons[0],"marriage",1980, fillService);
            events[7] = createEventsForTestingOnly(persons[1],"marriage",1984, fillService);
            events[8] = createEventsForTestingOnly(persons[0],"death",1980, fillService);
            events[9] = createEventsForTestingOnly(persons[1],"death",1984, fillService);
            events[9].setEventID("747");
            String output = serverProxy.load(users,persons,events);
            String auth = serverProxy.login(userMike);
            output = serverProxy.event("/747",getAuthString(auth));
            if(!output.equals("Invalid User Id to access that person's information")){
                fail("UserIds were valid and should not have been");
            }
        }catch (Exception e){
            fail("Exception was thrown");
        }
    }
//-------

    //-------------




    public Person[] toPersonArray(String jsonIn){
        Gson gson = new Gson();
        Person[] people = (Person[]) gson.fromJson(jsonIn, Person[].class);
        return people;
    }
    public Event[] toEventArray(String jsonIn){
        Gson gson = new Gson();
        Event[] events = (Event[]) gson.fromJson(jsonIn, Event[].class);
        return events;
    }
    public Event toEvent(String jsonIn){
        Gson gson = new Gson();
        Event event = (Event) gson.fromJson(jsonIn, Event.class);
        return event;
    }
    public String getAuthString(String authJson){
        Gson gson = new Gson();
        Authorization auth;
        auth = (Authorization) gson.fromJson(authJson, Authorization.class);
        
        return auth.getAuthorizationCode();
    }
    public Event createEventsForTestingOnly(Person P, String event, int year, FillService fillService) throws DatabaseException, IOException {
        Event E = new Event();
        E = fillService.getLocation();
        E.setDescendant(P.descendant);
        E.setPersonID(P.getPersonID());
        E.setDescription(event);
        String yeartemp = Integer.toString(year);
        E.setYear(yeartemp);
        return E;
    }

}
