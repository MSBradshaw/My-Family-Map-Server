package com.example.DAO;

import com.example.Handlers.SuperGlobalStuffYouShouldNotHave;
import com.example.Model.User;

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

public class UserDAO {
    private Connection conn;
    private String getCreateUserTableString2 = "CREATE TABLE IF NOT EXISTS Users (UserName TEXT PRIMARY KEY, Userpassword TEXT, Email TEXT, FirstName TEXT, LastName TEXT, Gender TEXT, PersonID TEXT, FOREIGN KEY (PersonID) REFERENCES Person(PersonID));";
    public UserDAO(Connection connector) throws DatabaseException {
        conn = connector;
        try{
            createUserTable();
        }catch(DatabaseException e){
            throw new DatabaseException("failed to create user table");
        }
    }
    /**
     * creates a table called User
     *
     */
    public void createUserTable() throws DatabaseException {
        execute(getCreateUserTableString2);
    }
    /**
     * Adds a row to User table
     * throws an exception if User is not added
     * @param U is the User object of info to be added
     */
    public boolean addToUserTable(User U) throws DatabaseException {
        //(UserName, Userpassword, Email, FirstName, LastName, Gender, PersonID)
        SuperGlobalStuffYouShouldNotHave counter = new SuperGlobalStuffYouShouldNotHave();
        counter.newUserCount++;
        if(U.getUsername() == null || U.getUsername().equals("")){
            U.setUsername(getRandomIDCode());
        }
        //check if user is unique else throw error
        User temp = getUser(U.getUsername());
        if(temp != null){
            DatabaseException e = new DatabaseException("");
            e.errormessage = "That user already exists, user not added";
            throw e;
        }
        String sql = "INSERT INTO Users (UserName, Userpassword, Email, FirstName,"
                +" LastName, Gender, PersonID)" +
                " VALUES ('" + U.getUsername() + "', " +
                "'" + U.getPassword() + "', " +
                "'" + U.getEmail() + "', " +
                "'" + U.getFirstname() + "', " +
                "'" + U.getLastname() + "', " +
                "'" + U.getGender() + "', " +
                "'" + U.getPersonID() + "' );";
        try{
            execute(sql);
            return true;
        }catch(DatabaseException e){
            e.errormessage = "failed to add user to database";
            throw e; // should contain the error message
            // where ever it was called from should then rollback all changes, do not save
        }
    }

    /**
     * retrieves the User information of the given user
     * @param userName the given user information will be retrieved from
     */
    public User getUser(String userName) throws DatabaseException {
        String sql = "SELECT * FROM Users WHERE UserName IS '" + userName + "';";
        User U = new User();
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if(!rs.next()){
                    return null;
                }else{
                    U.setPersonID(rs.getString("PersonID"));
                    U.setGender(rs.getString("Gender"));
                    U.setLastname(rs.getString("LastName"));
                    U.setFirstname(rs.getString("FirstName"));
                    U.setEmail(rs.getString("Email"));
                    U.setPassword(rs.getString("Userpassword"));
                    U.setUsername(rs.getString("UserName"));
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
        return U;
    }
    public Set<User> getAllUsers() throws DatabaseException {
        String sql = "SELECT * FROM Users;";
        Set<User> Uset = new HashSet();
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                boolean contains = false;
                while(rs.next()){
                    contains = true;
                    User U = new User();
                    U.setPersonID(rs.getString("PersonID"));
                    U.setGender(rs.getString("Gender"));
                    U.setLastname(rs.getString("LastName"));
                    U.setFirstname(rs.getString("FirstName"));
                    U.setEmail(rs.getString("Email"));
                    U.setPassword(rs.getString("Userpassword"));
                    U.setUsername(rs.getString("UserName"));
                    Uset.add(U);
                }
                if(!contains){
                    throw new DatabaseException("There are no users to return a set of");
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
        return Uset;
    }

    public void execute(String command) throws DatabaseException {
        PreparedStatement stt = null;
        try {
            Statement stmt = null;
            try {
                stt = conn.prepareStatement(command);
                stt.executeUpdate();

            }finally {
                if (stt != null) {
                    stt.close();
                    stt = null;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Fail in UserDao");
        }
    }
    /**
     * clears the table of all content
     *
     */
    public void clear() throws DatabaseException {
        String commandDrop = "DROP TABLE IF EXISTS Users;";
        execute(commandDrop);
    }
    public String getRandomIDCode(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
