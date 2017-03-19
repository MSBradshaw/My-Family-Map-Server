package com.example.DAO;

import java.sql.*;
import java.util.*;

/**
 * Created by Michael on 2/23/2017.
 */

public class Transaction {
    public Connection conn;
    public AuthorizationDAO A;
    public EventDAO E;
    public PersonDAO P;
    public UserDAO U;
    public Transaction(){
    }

    /**
     * starts the transactiona nd opens the database
     * @param dataBasePath the data base that will be opened, the test on or real DB
     * @throws DatabaseException
     */
    public void startTransaction(String dataBasePath)throws DatabaseException {
        try {
            Class.forName("org.sqlite.JDBC");
        }catch(ClassNotFoundException e){
            throw new DatabaseException("Class failure in startTransation");
        }
        try {
            final String CONNECTION_URL = dataBasePath;//"jdbc:sqlite:myFile.sqlite";
            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);
            // Start a transaction
            conn.setAutoCommit(false);
            A = new AuthorizationDAO(conn);
            E = new EventDAO(conn);
            U = new UserDAO(conn);
            P = new PersonDAO(conn);
            A.createAuthorizationTable();
            E.createEventTable();
            U.createUserTable();
            P.createPersonTable();
        }
        catch (SQLException e) {
            throw new DatabaseException("openConnection failed", e);
        }
    }
    public void endTransaction(boolean commit) throws DatabaseException {
        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
            }
            conn.close();
            conn = null;
        }
        catch (SQLException e) {
            throw new DatabaseException("closeConnection failed", e);
        }
    }

    public void printTables() throws SQLException {
        String sqlStatement = "SELECT name FROM myFile WHERE type='table'";
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        System.out.println("--Printing Tables--");
        while (rs.next()) {
            System.out.println(rs.getString(3));
        }
        System.out.println("-------------------");
    }
    /**
     * clears the table of all content
     *
     */
    public void clear() throws DatabaseException {
        A.clear();
        E.clear();
        U.clear();
        P.clear();
    }
    public void executeT(String command) throws DatabaseException {
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                System.out.println( stmt.executeUpdate(command));
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
}
