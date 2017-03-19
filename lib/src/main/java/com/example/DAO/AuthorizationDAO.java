package com.example.DAO;

import com.example.Model.Authorization;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.xml.crypto.Data;

/**
 * Class for acessesing and creating the Authorization Table
 *
 */
public class AuthorizationDAO {
    private Connection conn;
    public AuthorizationDAO(Connection connector) throws DatabaseException {
        conn = connector;
        try{
            createAuthorizationTable();
        }catch(DatabaseException e){
            throw new DatabaseException("failed to create user table");
        }
    }
    public String tableString2 ="CREATE TABLE IF NOT EXISTS Authorization ( AuthID TEXT PRIMARY KEY, Time TEXT NOT NULL, UserName TEXT, FOREIGN KEY(UserName) REFERENCES User(UserName));";
    /**
     * creates a table called Authorization
     *
     */
    public void createAuthorizationTable() throws DatabaseException {
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(tableString2);
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
    }
    /**
     * Adds a row to Authorization table
     * @param A is the authorization object that needs to be loaded into the database
     *
     */
    public Authorization addToAuthorizationTable(Authorization A) throws DatabaseException {
        if(A.getAuthorizationCode() == null || A.getAuthorizationCode().equals("")){
            A.setAuthorizationCode(getRandomIDCode());
        }
        if(A.getTimeIssued() == null || A.getTimeIssued().equals("")){
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            A.setTimeIssued(timeStamp);
        }
        String sql = "INSERT INTO Authorization (AuthID, Time,UserName)" +
                " Values ('" + A.getAuthorizationCode() + "'," +
                " '" + A.getTimeIssued() + "', " +
                "'" + A.getUserName() + "' );";
        try{
            execute(sql);
        }catch(DatabaseException e){
            e.errormessage = "failed to add auth to table";
            throw e;
        }
        return A;
    }
    /**
     * checks is the auth code is good or not
     * @param authorizationString the auth token you want to we verified
     */
    public Authorization checkAuthorization(String authorizationString) throws DatabaseException {
        String checkFor = "SELECT * FROM Authorization WHERE AuthID='" + authorizationString + "';";
        Authorization authorization = new Authorization();
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(checkFor);
                boolean contains = false;
                while(rs.next()){
                    contains = true;
                    authorization.setAuthorizationCode(rs.getString("AuthID"));
                    authorization.setUserName(rs.getString("UserName"));
                    authorization.setTimeIssued(rs.getString("Time"));
                }
                if(!contains){
                    throw new DatabaseException("");
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
        return authorization;
    }
    /**
     * clears the table of all content
     *
     */
    public void clear() throws DatabaseException {
        String commandDrop = "DROP TABLE IF EXISTS Authorization;";
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
}
