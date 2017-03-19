package com.example.Tests.Dao_Testers;

import com.example.DAO.AuthorizationDAO;
import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.Authorization;
import com.example.Model.Event;
import com.example.Model.Person;
import com.example.Model.ServerProxy;
import com.example.Model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * Created by Michael on 2/23/2017.
 */

public class DAO_TESTER {
    private User U;
    private ServerProxy S;
    private Person P;
    private Event E;
    private Authorization A;
    public Transaction T;
    private AuthorizationDAO AD;

    @Before
    public void setUp() throws ClassNotFoundException, DatabaseException, SQLException {

        T = new Transaction();
        T.startTransaction("jdbc:sqlite:mainData.sqlite");
        U = new User();
        //S = new ServerProxy();
        P = new Person();
        E = new Event();
        A = new Authorization();
        /*
        T.A.createAuthorizationTable();
        endAndStartNewTransaction(true);
        T.U.createUserTable();
        endAndStartNewTransaction(true);
        T.E.createEventTable();
        endAndStartNewTransaction(true);
        T.P.createPersonTable();
        endAndStartNewTransaction(true);
        */
    }

    @After
    public void closeDown() throws DatabaseException {
        T.endTransaction(true);
        T.startTransaction("jdbc:sqlite:mainData.sqlite");
        T.clear();
        T.endTransaction(true);
        U = null;
        P = null;
        //S = null;
        E = null;
        A = null;
        T = null;
    }

    //tests the ability to open and close a transaction
    @Test
    public void addToAuthTable() throws DatabaseException, SQLException {
        String[] testData = {"567","137"};
        Authorization A = new Authorization();
        A.authorizationCode = "yxz789";
        A.timeIssued = "6:30";
        A.UserName = "123";
        try{
            T.A.addToAuthorizationTable(A);
        }catch(DatabaseException e){
            fail("failed to add to auth table");
        }
        endAndStartNewTransaction(true);
        String command = "SELECT * FROM Authorization;";
        try{
            T.A.execute(command);
        }catch (DatabaseException e){
            fail("failed to add to auth table");
        }
        // check if the table exists
    }
    @Test
    public void checkForAuthCode() throws DatabaseException {
        String s = "1";
        Authorization A = new Authorization();
        A.authorizationCode = "yxz789";
        A.timeIssued = "6:30";
        A.UserName = "123";
        T.A.addToAuthorizationTable(A);
        endAndStartNewTransaction(true);
        Authorization authorization = null;
        try {
            authorization = T.A.checkAuthorization("yxz789");
        }catch(DatabaseException e){
            fail("bad auth code");
        }
        assertTrue(authorization.getUserName().equals(A.getUserName()));
        assertTrue(authorization.getAuthorizationCode().equals(A.getAuthorizationCode()));
        assertTrue(authorization.getTimeIssued().equals(A.getTimeIssued()));
    }
    @Test
    public void checkForBadAuthCode() throws DatabaseException {
        String s = "100";
        String[] testData = {"567","137"};
        Authorization A = new Authorization();
        A.authorizationCode = "yxz789";
        A.timeIssued = "6:30";
        A.UserName = "123";
        T.A.addToAuthorizationTable(A);
        endAndStartNewTransaction(true);
        try {
            T.A.checkAuthorization(s);
            fail("bad auth code");
        }catch(DatabaseException e){

        }
    }

    /**
     * ends the current transaction and starts a new one, useful for use in complex test cases
     * @param condition should the endTransaction return true or false?
     * @throws DatabaseException
     */
    private void endAndStartNewTransaction(boolean condition) throws DatabaseException {
        T.endTransaction(condition);
        T.startTransaction("jdbc:sqlite:mainData.sqlite");
    }
    //------------------------------------------------------------------------------------------
    //-----------------------------END AUTHORIZATION_DOO TESTS----------------------------------
    //------------------------------------------------------------------------------------------
    //-----------------------------START EVENT DAO TESTS----------------------------------------
    @Test
    public void addToEventTable() throws DatabaseException, SQLException {
        Event E = new Event();
        E.setPersonID("123");
        E.setCity("Provo");
        E.setCountry("USA");
        E.setDescendant("Jeff");
        E.setDescription("death record");
        E.setLatitude("3.1492");
        E.setLongitude("9.876543");
        E.setYear("2017");
        E.setEventID("123-456-789-abc-def");
        try{
            T.E.addToEventTable(E);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            T.E.getEvent("1000");
            fail("failed to find Event table 2");
        }catch (DatabaseException e){

        }
    }
    @Test
    public void addToEventTable2() throws DatabaseException, SQLException {
        String[] testData = {"'1'","'2'","'3'","'4'","'5'","'DEATH'","'2017'","'8'"};
        Event E = new Event();
        E.setPersonID("123");
        E.setCity("Provo");
        E.setCountry("USA");
        E.setDescendant("Jeff");
        E.setDescription("death record");
        E.setLatitude("3.1492");
        E.setLongitude("9.876543");
        E.setYear("2017");
        E.setEventID("1");
        try{
            T.E.addToEventTable(E);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            T.E.getEvent("1");
        }catch (DatabaseException e){
            fail("failed to find Event table 2");
        }
    }
    @Test
    public void addToEventTable3() throws DatabaseException, SQLException {
        Event E = new Event();
        Event EPrime = null;
        E.setPersonID("123");
        E.setCity("Provo");
        E.setCountry("USA");
        E.setDescendant("Jeff");
        E.setDescription("death record");
        E.setLatitude("3.1492");
        E.setLongitude("9.876543");
        E.setYear("2017");
        E.setEventID("1");
        try{
            T.E.addToEventTable(E);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
           EPrime =  T.E.getEvent("1");
        }catch (DatabaseException e){
            fail("failed to find Event table 2");
        }
        if(!E.equals(EPrime)){
            fail("The Event objects are not equal you fool");
        }

    }
    @Test
    public void addToEventTable4() throws DatabaseException, SQLException {
        Event E = new Event();
        Event EPrime = null;
        E.setPersonID("123");
        E.setCity("Provo");
        E.setCountry("USA");
        E.setDescendant("Jeff");
        E.setDescription("death record");
        E.setLatitude("3.1492");
        E.setLongitude("9.876543");
        E.setYear("2017");
        E.setEventID("2");
        Event D = new Event();
        D.setPersonID("1203");
        D.setCity("Orem");
        D.setCountry("USA");
        D.setDescendant("Jeffery");
        D.setDescription("death record");
        D.setLatitude("3.1492");
        D.setLongitude("9.876543");
        D.setYear("2017");
        D.setEventID("1");
        try{
            T.E.addToEventTable(E);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            EPrime =  T.E.getEvent("2");
        }catch (DatabaseException e){
            fail("failed to find Event table 2");
        }
        if(E.equals(D)){
            fail("The Event objects are not equal you fool");
        }

    }
    @Test
    public void addToEventTable5() throws DatabaseException, SQLException {
        Event E = new Event();
        Event EPrime = null;
        Set<Event> Events = null;
        E.setPersonID("123");
        E.setCity("Provo");
        E.setCountry("USA");
        E.setDescendant("Jeffery");
        E.setDescription("birth record");
        E.setLatitude("3.1492");
        E.setLongitude("9.876543");
        E.setYear("2017");

        Event D = new Event();
        D.setPersonID("1203");
        D.setCity("Orem");
        D.setCountry("USA");
        D.setDescendant("Jeffery");
        D.setDescription("death record");
        D.setLatitude("3.1492");
        D.setLongitude("9.876543");
        D.setYear("2017");

        Event D1 = new Event();
        D1.setPersonID("1203");
        D1.setCity("Orem");
        D1.setCountry("Mexico");
        D1.setDescendant("Jeffery");
        D1.setDescription("death record");
        D1.setLatitude("3.1492");
        D1.setLongitude("9.876543");
        D1.setYear("2017");
        try{
            T.E.addToEventTable(E);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            T.E.addToEventTable(D);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            T.E.addToEventTable(D1);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            Events =  T.E.getAllEvents("Jeffery");
        }catch (DatabaseException e){
            fail("failed to find Event table 2");
        }
        if(Events.size() != 3){
            fail("There is an incorrect number of Event rows");
        }

    }
    @Test
    public void addToEventTable6() throws DatabaseException, SQLException {
        Event E = new Event();
        Event EPrime = null;
        Set<Event> Events = null;
        try{
            Events =  T.E.getAllEvents("me");
            fail("Event tables has content but should not");
        }catch (DatabaseException e){
            //good to go
        }
    }
    @Test
    public void addToEventTable7() throws DatabaseException, SQLException {
        Event E = new Event();
        Event EPrime = null;
        Set<Event> Events = null;
        E.setPersonID("123");
        E.setCity("Provo");
        E.setCountry("USA");
        E.setDescendant("Jeff");
        E.setDescription("birth record");
        E.setLatitude("3.1492");
        E.setLongitude("9.876543");
        E.setYear("2017");

        Event D = new Event();
        D.setPersonID("1203");
        D.setCity("Orem");
        D.setCountry("USA");
        D.setDescendant("Jeffery");
        D.setDescription("death record");
        D.setLatitude("3.1492");
        D.setLongitude("9.876543");
        D.setYear("2017");

        Event D1 = new Event();
        D1.setPersonID("1203");
        D1.setCity("Orem");
        D1.setCountry("Mexico");
        D1.setDescendant("Jeffery");
        D1.setDescription("death record");
        D1.setLatitude("3.1492");
        D1.setLongitude("9.876543");
        D1.setYear("2017");
        try{
            T.E.addToEventTable(E);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            T.E.addToEventTable(D);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            T.E.addToEventTable(D1);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            T.E.deleteEventsOfUser("JohnSmith");
        }catch (DatabaseException e){
            fail(e.errormessage);
        }
        endAndStartNewTransaction(true);
        String me = null;

    }
    //------------------------------------------------------------------------------------------
    //-----------------------------END EVENT_DAO TESTS------------------------------------------
    //------------------------------------------------------------------------------------------
    //-----------------------------START USER DAO TESTS-----------------------------------------
    @Test
    public void addtoUser1() throws DatabaseException, SQLException {
        String[] testData = {"'mbrad94'","'secret'","'mike@gmail.com'","'mike'","'bradshaw'","'M'","'1234'"};
        User U = new User();
        U.setUsername("mbrad94");
        U.setPassword("secret");
        U.setEmail("mike@gmail.com");
        U.setFirstname("mike");
        U.setLastname("bradshaw");
        U.setGender("f");
        U.setPersonID("423353115");
        try{
            T.U.addToUserTable(U);
        }catch(DatabaseException e){
            fail("failed to add to Event table");
        }
        endAndStartNewTransaction(true);
        try{
            T.U.getUser("mbrad94");
        }catch (DatabaseException e){
            fail("failed to find Event table 2");
        }
        endAndStartNewTransaction(true);
        if(!U.equals(U)){
            fail("User are not equal and should be addtoUser1");
        }
    }
    @Test
    public void addtoUser2() throws DatabaseException, SQLException {
        String[] testData = {"'mbrad94'","'secret'","'mike@gmail.com'","'mike'","'bradshaw'","'M'","'1234'"};
        User U = new User();
        U.setUsername("mbrad94");
        U.setPassword("secret");
        U.setEmail("mike@gmail.com");
        U.setFirstname("mike");
        U.setLastname("bradshaw");
        U.setGender("f");
        U.setPersonID("423353115");
        User Up = new User();
        Up.setUsername("mbrad94");
        Up.setPassword("secret");
        Up.setEmail("mike@gmail.com");
        Up.setFirstname("mike");
        Up.setLastname("bradshaw");
        Up.setGender("f");
        Up.setPersonID("423355");
        try{
            T.U.addToUserTable(U);
        }catch(DatabaseException e){
            fail("failed to add to user table");
        }
        endAndStartNewTransaction(true);
        try{
            if(T.U.getUser("MONSTER ENERGY") != null){
                fail("Found a user it should not have");
            }

        }catch (DatabaseException e){
            fail("add to user function 2 failed");
        }
        endAndStartNewTransaction(true);
        if(U.equals(Up)){
            fail("User are equal and should be addtoUser1");
        }
    }
    @Test
    public void addtoUser4() throws DatabaseException, SQLException {
        User U = new User();
        U.setUsername("mbrad94");
        U.setPassword("secret");
        U.setEmail("mike@gmail.com");
        U.setFirstname("mike");
        U.setLastname("bradshaw");
        U.setGender("f");
        U.setPersonID("423353115");
        User Up = new User();
        Up.setUsername("mbrad95");
        Up.setPassword("secret");
        Up.setEmail("mike@gmail.com");
        Up.setFirstname("mike");
        Up.setLastname("bradshaw");
        Up.setGender("f");
        Up.setPersonID("423355");
        try{
            T.U.addToUserTable(U);
        }catch(DatabaseException e){
            fail("failed to add to user table");
        }
        endAndStartNewTransaction(true);
        try{
            T.U.addToUserTable(Up);
        }catch(DatabaseException e){
            fail("failed to add to user table");
        }
        endAndStartNewTransaction(true);
        Set<User> Uset;
        Uset = T.U.getAllUsers();
        try{
            Uset = T.U.getAllUsers();
        }catch(DatabaseException e){
            fail("failed to get all users");
        }
        if(Uset.size() !=2){
            fail("There is an incorrect number of users");
        }
    }
    @Test
    public void addtoUser3() throws DatabaseException, SQLException {
        User U = new User();
        U.setUsername("mbrad95");
        U.setPassword("secret");
        U.setEmail("mike@gmail.com");
        U.setFirstname("mike");
        U.setLastname("bradshaw");
        U.setGender("f");
        U.setPersonID("423353115");
        User Up = new User();
        Up.setUsername("mbrad94");
        Up.setPassword("secret");
        Up.setEmail("mike@gmail.com");
        Up.setFirstname("mike");
        Up.setLastname("bradshaw");
        Up.setGender("f");
        Up.setPersonID("423355");
        try{
            T.U.addToUserTable(U);
        }catch(DatabaseException e){
            fail("failed to add to user table 1");
        }
        endAndStartNewTransaction(true);
        try{
            T.U.addToUserTable(Up);
        }catch(DatabaseException e){
            fail("failed to add to user table 2");
        }
        endAndStartNewTransaction(true);
        Set<User> Uset;
        Uset = T.U.getAllUsers();
        try{
            Uset = T.U.getAllUsers();
        }catch(DatabaseException e){
            fail("failed to get all users 3");
        }

        if(Uset.size() !=2){
            fail("There is an incorrect number of users, this one attempts to add duplicate users");
        }
    }
    //--------------------------------------------------------------------------------------
    //-----------------------------END USER_DAO TESTS---------------------------------------
    //--------------------------------------------------------------------------------------
    //-----------------------------START PERSON DAO TESTS-----------------------------------
    @Test
    public void addPerson1() throws DatabaseException {
        String[] testData = {"'Michael'","'Bradshaw'","'M'","'2'","'1'","'8'","'4'"};
        Person P =  new Person();
        P.setFirstname("Mike");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("4");
        P.setPersonID("1");

        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
            T.P.getPerson("1");
        }catch (DatabaseException e){
            fail("failed to find user table 2");
        }
        endAndStartNewTransaction(true);
        if(!P.equals(P)){
            fail("The people were not created equal, addPerson1");
        }
    }
    @Test
    public void addPerson2() throws DatabaseException {
        String[] testData = {"'Michael'","'Bradshaw'","'M'","'2'","'1'","'8'","'4'"};
        Person P =  new Person();
        P.setFirstname("Mike");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("4");
        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
            T.P.getPerson("100");
            fail("failed to find user table 2");
        }catch (DatabaseException e){
        }
        endAndStartNewTransaction(true);
    }
    @Test
    public void addPerson3() throws DatabaseException {
        String[] testData = {"'Michael'","'Bradshaw'","'M'","'2'","'1'","'8'","'4'"};
        Person P =  new Person();
        Person Pprime = null;
        P.setFirstname("Mike");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("4");
        P.setPersonID("1");

        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
           Pprime = T.P.getPerson("1");
        }catch (DatabaseException e){
            fail("failed to find user table 2");
        }
        endAndStartNewTransaction(true);
        if(!P.equals(Pprime)){
            fail("The people were not created equal, addPerson3");
        }
    }
    @Test
    public void addPerson4() throws DatabaseException {
        String[] testData = {"'Michael'","'Bradshaw'","'M'","'2'","'1'","'8'","'4'"};
        Person P =  new Person();
        Person Pprime = null;
        Person Pa = new Person();
        Pa.setFirstname("Mike");
        Pa.setLastname("Padgett");
        Pa.setGender("F");
        Pa.setFather("1");
        Pa.setMother("2");
        Pa.setSpouse("3");
        Pa.setDescendant("4");
        P.setFirstname("Mikey");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("4");
        P.setPersonID("1");
        Pa.setPersonID("1");
        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
            Pprime = T.P.getPerson("1");
        }catch (DatabaseException e){
            fail("failed to find user table 2");
        }
        endAndStartNewTransaction(true);
        if(Pprime.equals(Pa)){
            fail("The people were not created equal, addPerson4");
        }
    }
    @Test
    public void addPerson5() throws DatabaseException {
        String[] testData = {"'Michael'","'Bradshaw'","'M'","'2'","'1'","'8'","'4'"};
        Person P =  new Person();
        Person Pprime = null;
        Person Pa = new Person();
        Pa.setFirstname("Mike");
        Pa.setLastname("Padgett");
        Pa.setGender("F");
        Pa.setFather("1");
        Pa.setMother("2");
        Pa.setSpouse("3");
        Pa.setDescendant("4");
        P.setFirstname("Mikey");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("4");
        P.setPersonID("1");
        Pa.setPersonID("2");
        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table1");
        }
        endAndStartNewTransaction(true);

        try{
            T.P.addToPersonTable(Pa);
        }catch(DatabaseException e){
            fail("failed to add to person table3");
        }
        endAndStartNewTransaction(true);
        Set<Person> Pset = new HashSet();
        try{
            Pset = T.P.getAllPersons();
        }catch (DatabaseException e){
            fail("failed to find user table 9");
        }
        endAndStartNewTransaction(true);
        if(Pset.size()!=2){
            fail("The people were not created equal, addPerson5");
        }
    }
    @Test
    public void addPerson6() throws DatabaseException {
        Person P =  new Person();
        Person Pprime = null;
        Person Pa = new Person();
        Pa.setFirstname("Mike");
        Pa.setLastname("Padgett");
        Pa.setGender("F");
        Pa.setFather("1");
        Pa.setMother("2");
        Pa.setSpouse("3");
        Pa.setDescendant("9");
        P.setFirstname("Mikey");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("4");
        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
            T.P.addToPersonTable(Pa);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        Set<Person> Pset = T.P.getAllPersonsWithDecendent("9");
        if(Pset.size() != 1){
            fail("Failed addperson6 too many or too few people");
        }
    }
    @Test
    public void addPerson7() throws DatabaseException {
        Person P =  new Person();
        Person Pprime = null;
        Person Pa = new Person();
        Pa.setFirstname("Mike");
        Pa.setLastname("Padgett");
        Pa.setGender("F");
        Pa.setFather("1");
        Pa.setMother("2");
        Pa.setSpouse("3");
        Pa.setDescendant("9");
        P.setFirstname("Mikey");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("farfarfaro");
        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
            T.P.deletePersonsOfUser("farfaro");
        }catch (DatabaseException e){
            fail(e.errormessage);
        }
        endAndStartNewTransaction(true);
        String me = null;

    }
    @Test
    public void addPerson8() throws DatabaseException {
        Person P =  new Person();
        Person Pprime = null;
        Person Pa = new Person();
        Pa.setFirstname("Mike");
        Pa.setLastname("Padgett");
        Pa.setGender("F");
        Pa.setFather("James");
        Pa.setMother("2");
        Pa.setSpouse("3");
        Pa.setDescendant("9");
        P.setFirstname("Mikey");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("farfarfaro");
        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
            T.P.deleteFathersOfUser("James");
        }catch (DatabaseException e){
            fail(e.errormessage);
        }
        endAndStartNewTransaction(true);
        String me = null;

    }
    @Test
    public void addPerson9() throws DatabaseException {
        Person P =  new Person();
        Person Pprime = null;
        Person Pa = new Person();
        Pa.setFirstname("Mike");
        Pa.setLastname("Padgett");
        Pa.setGender("F");
        Pa.setFather("1");
        Pa.setMother("Jane");
        Pa.setSpouse("3");
        Pa.setDescendant("9");
        P.setFirstname("Mikey");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("3");
        P.setDescendant("farfarfaro");
        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
            T.P.deleteMothersOfUser("Jane");
        }catch (DatabaseException e){
            fail(e.errormessage);
        }
        endAndStartNewTransaction(true);
        try{
            T.P.deleteMothersOfUser("Jae");
        }catch (DatabaseException e){
            fail(e.errormessage);
        }
        endAndStartNewTransaction(true);
        String me = null;

    }
    @Test
    public void addPerson10() throws DatabaseException {
        Person P =  new Person();
        Person Pprime = null;
        Person Pa = new Person();
        Pa.setFirstname("Mike");
        Pa.setLastname("Padgett");
        Pa.setGender("F");
        Pa.setFather("1");
        Pa.setMother("Jane");
        Pa.setSpouse("3");
        Pa.setDescendant("9");
        P.setFirstname("Mikey");
        P.setLastname("Padgett");
        P.setGender("F");
        P.setFather("1");
        P.setMother("2");
        P.setSpouse("Mette");
        P.setDescendant("farfarfaro");
        try{
            T.P.addToPersonTable(P);
        }catch(DatabaseException e){
            fail("failed to add to person table");
        }
        endAndStartNewTransaction(true);
        try{
            T.P.deleteSpousesOfUser("Mette");
        }catch (DatabaseException e){
            fail(e.errormessage);
        }
        endAndStartNewTransaction(true);
        try{
            T.P.deleteSpousesOfUser("meta");
        }catch (DatabaseException e){
            fail(e.errormessage);
        }
        endAndStartNewTransaction(true);
        String me = null;

    }

}
