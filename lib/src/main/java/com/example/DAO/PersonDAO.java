package com.example.DAO;

import com.example.Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 2/9/2017.
 */

public class PersonDAO {
    private String createPersonTableString2 = "CREATE TABLE IF NOT EXISTS Person (PersonID TEXT PRIMARY KEY, FirstName TEXT NOT NULL,LastName TEXT NOT NULL,Gender TEXT NOT NULL,Father TEXT, Mother TEXT, Spouse TEXT, Decedent TEXT, FOREIGN KEY (Decedent) REFERENCES User(UserName));";
    /**
     * creates a table called Person
     *
     */
    private Connection conn;
    PersonDAO(Connection connector) throws DatabaseException {
        conn = connector;
        try{
            createPersonTable();
        }catch(DatabaseException e){
            throw new DatabaseException("failed to create user table");
        }
    }
    public void createPersonTable() throws DatabaseException {
        execute(createPersonTableString2);
    }
    /**
     * Adds a row to Person table
     * @param P is the Person Object of info to be added
     */
    public void addToPersonTable(Person P) throws DatabaseException {
        if(P.personID == null || P.getPersonID().equals("")){
            P.setPersonID(getRandomIDCode());
        }
        String sql = "INSERT INTO Person (PersonID, FirstName, LastName, Gender, Father, Mother, Spouse, Decedent)" +
                " Values ( '" + P.getPersonID() + "', " +
                "'" + P.getFirstname() + "', " +
                "'" + P.getLastname() + "', " +
                "'" + P.getGender() + "', " +
                "'" + P.getFather() + "', " +
                "'" + P.getMother() + "', " +
                "'" + P.getSpouse() + "', " +
                "'" + P.getDescendant() + "'" +
                ");";
        try{
            execute(sql);
        }catch(DatabaseException e){
            e.errormessage = "Fail to add person to data base";
        }


    }
    /**
     * retrieves the Person information of the given user
     * @param personID the given person information will be retrieved from
     */
    public Person getPerson(String personID) throws DatabaseException {
        String sql = "SELECT * FROM Person WHERE PersonID IS '" + personID + "';";
        Person P = new Person();
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if(!rs.next()){
                    DatabaseException e = new DatabaseException("failed to get the person");
                    e.errormessage = "failed to get the person";
                    throw e;
                }else{
                    P.setPersonID(rs.getString("PersonID"));
                    P.setFirstname(rs.getString("FirstName"));
                    P.setLastname(rs.getString("LastName"));
                    P.setGender(rs.getString("Gender"));
                    P.setMother(rs.getString("Mother"));
                    P.setFather(rs.getString("Father"));
                    P.setSpouse(rs.getString("Spouse"));
                    P.setDescendant(rs.getString("Decedent"));
                }
            }finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }catch(SQLException error) {
            error.printStackTrace();
            DatabaseException e = new DatabaseException("createTables failed");
            e.errormessage = "There was an error in the get person function";
            throw e;
        }
        return P;
    }
    public Set<Person> getAllPersons() throws DatabaseException {
        String sql = "SELECT * FROM Person;";
        Set<Person> Pset = new HashSet();

        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                boolean contains = false;
                while(rs.next()){
                    contains = true;
                    Person P = new Person();
                    P.setPersonID(rs.getString("PersonID"));
                    P.setFirstname(rs.getString("FirstName"));
                    P.setLastname(rs.getString("LastName"));
                    P.setGender(rs.getString("Gender"));
                    P.setMother(rs.getString("Mother"));
                    P.setFather(rs.getString("Father"));
                    P.setSpouse(rs.getString("Spouse"));
                    P.setDescendant(rs.getString("Decedent"));
                    Pset.add(P);
                }
                if(!contains){
                    throw new DatabaseException("SelectAllPeople returned an empty set");
                }
            }finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("createTables failed", e);
        }
        return Pset;
    }
    public Set<Person> getAllPersonsWithDecendent(String decendant) throws DatabaseException {
        String sql = "SELECT * FROM Person WHERE Decedent='" + decendant + "' "+";";
        Set<Person> Pset = new HashSet();
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                boolean contains = false;
                while(rs.next()){
                    contains = true;
                    Person P = new Person();
                    P.setPersonID(rs.getString("PersonID"));
                    P.setFirstname(rs.getString("FirstName"));
                    P.setLastname(rs.getString("LastName"));
                    P.setGender(rs.getString("Gender"));
                    P.setMother(rs.getString("Mother"));
                    P.setFather(rs.getString("Father"));
                    P.setSpouse(rs.getString("Spouse"));
                    P.setDescendant(rs.getString("Decedent"));
                    Pset.add(P);
                }
                if(!contains){
                    throw new DatabaseException("SelectAllPeople returned an empty set");
                }
            }finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("createTables failed", e);
        }
        return Pset;
    }
    public void clear() throws DatabaseException {
        String commandDrop = "DROP TABLE IF EXISTS Person;";
        execute(commandDrop);
    }
    public void execute(String command) throws DatabaseException {
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(command);
            }finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("createTables failed");
        }
    }
    public String getRandomIDCode(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
    public void deletePersonsOfUser(String UserName) throws DatabaseException {
        String sql = "DELETE FROM Person WHERE Decedent='" + UserName + "';";
        try{
            execute(sql);
        }catch (DatabaseException e){
            e.errormessage = "failed to delete Decendents from Person table";
            throw e;
        }
    }
    public void deleteFathersOfUser(String UserName) throws DatabaseException {
        String sql = "DELETE FROM Person WHERE Father='" + UserName + "';";
        try{
            execute(sql);
        }catch (DatabaseException e){
            e.errormessage = "failed to delete Fathers from Person table";
            throw e;
        }
    }
    public void deleteMothersOfUser(String UserName) throws DatabaseException {
        String sql = "DELETE FROM Person WHERE Mother='" + UserName + "';";
        try{
            execute(sql);
        }catch (DatabaseException e){
            e.errormessage = "failed to delete Mothers from Person table";
            throw e;
        }
    }
    public void deleteSpousesOfUser(String UserName) throws DatabaseException {
        String sql = "DELETE FROM Person WHERE Spouse='" + UserName + "';";
        try{
            execute(sql);
        }catch (DatabaseException e){
            e.errormessage = "failed to delete Spouses from Person table";
            throw e;
        }
    }
}
