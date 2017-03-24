package com.example.DAO;
import com.example.Model.Event;
import com.sun.corba.se.impl.legacy.connection.USLPort;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.xml.crypto.Data;

/**
 * access and edits the database, all work on the database is done through this DAO
 *
 */
public class EventDAO {
    private Connection conn;
    private String getCreateEventTableString2 = "CREATE TABLE IF NOT EXISTS Event ( EventID TEXT PRIMARY KEY, PersonID TEXT, Latitude TEXT, Longitude TEXT, Country TEXT, City TEXT, EventType TEXT, Year TEXT ,UserName TEXT, FOREIGN KEY(UserName) REFERENCES Users(UserName));";
    EventDAO(Connection connector) throws DatabaseException {
        conn = connector;
        try{
            createEventTable();
        }catch(DatabaseException e){
            throw new DatabaseException("failed to create user table");
        }
    }
    /**
     * creates a table called Event
     *
     */
    public void createEventTable() throws DatabaseException {
        execute(getCreateEventTableString2);
    }
    /**
     * Adds a row to Event table
     * @param E is the Event object of info to be added
     */
    public void addToEventTable(Event E) throws DatabaseException {
        //(PersonID, Latitude, Longitude, Country, City, EventType, Year, UserID)
        if(E.getEventID() == null || E.getEventID().equals("")){
            E.setEventID(getRandomIDCode());
        }
        String sql = "INSERT INTO Event(EventID, PersonID, Latitude, Longitude, Country, City, EventType, Year, UserName) " +
                " VALUES ('" + E.getEventID() + "'," +
                "'" + E.getPersonID() + "'" + "," +
                "'" + E.getLatitude() + "'" + "," +
                "'" + E.getLongitude()+ "'" + "," +
                "'" + E.getCountry()+ "'" + "," +
                "'" + E.getCity()+ "'" + "," +
                "'" + E.getDescription()+ "'" + "," +
                "'" + E.getYear() + "'" + "," +
                "'" + E.getDescendant()+ "'" + ");";
        try{
            execute(sql);
        }catch(DatabaseException e){
            e.errormessage = "Failed to insert a new Event into Event table";
            throw e;
        }
    }
    /**
     * retrieves the Event information of the given user
     * @param eventid the given user information will be retrieved from
     */
    public Event getEvent(String eventid) throws DatabaseException {
        String sql = "SELECT * FROM Event WHERE EventID IS '" + eventid + "';";
        Event E = new Event();
        ResultSet rs;
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if(!rs.next()){
                    DatabaseException e = new DatabaseException("");
                    e.errormessage = "You fool no event was found";
                    throw e;
                }else{
                    E.setEventID(rs.getString("EventID"));
                    E.setPersonID(rs.getString("PersonID"));
                    E.setLatitude(rs.getString("Latitude"));
                    E.setLongitude(rs.getString("Longitude"));
                    E.setCountry(rs.getString("Country"));
                    E.setCity(rs.getString("City"));
                    E.setDescription(rs.getString("EventType"));
                    E.setYear(rs.getString("Year"));
                    E.setDescendant(rs.getString("UserName"));
                }
            }finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Getting Info from single Event Query failed", e);
        }
        return E;
    }
    public Set<Event> getAllEvents(String UserName) throws DatabaseException {
        Set<Event> ESet = new HashSet();
        String sql = "SELECT * FROM Event WHERE UserName IS '" + UserName + "';";
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery(sql);
                boolean hasContent = false;
                while(rs.next()){
                    hasContent = true;
                    Event E =  new Event();
                    E.setEventID(rs.getString("EventID"));
                    E.setPersonID(rs.getString("PersonID"));
                    E.setLatitude(rs.getString("Latitude"));
                    E.setLongitude(rs.getString("Longitude"));
                    E.setCountry(rs.getString("Country"));
                    E.setCity(rs.getString("City"));
                    E.setDescription(rs.getString("EventType"));
                    E.setYear(rs.getString("Year"));
                    E.setDescendant(rs.getString("UserName"));
                    ESet.add(E);
                }
                if(!hasContent){
                    throw new DatabaseException("getAllEvents returned an empty set");
                }
            }finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Getting Info from single Event Query failed", e);
        }
        return ESet;
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
    public void clear() throws DatabaseException {
        String commandDrop = "DROP TABLE IF EXISTS Event;";
        execute(commandDrop);
    }
    public void deleteEventsOfUser(String UserName) throws DatabaseException {
        String sql = "DELETE FROM Event WHERE UserName='" + UserName + "';";
        try{
            execute(sql);
        }catch (DatabaseException e){
            e.errormessage = "failed to delete from Event table";
            throw e;
        }
    }
    public String getRandomIDCode(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
