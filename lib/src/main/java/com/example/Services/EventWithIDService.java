package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.Event;
import com.google.gson.Gson;

/**
 * Created by Michael on 3/7/2017.
 */

public class EventWithIDService {
    private String dbPath = "jdbc:sqlite:mainData.sqlite";
    private Transaction trans = new Transaction();
    private String eventID;
    private String errormessageStart = "{\n\t\"message\": \"";
    private String successmessageStart = "{\n\t";
    private String errormessageEnd = "\"\n}\n";
    private String successMessageEnd = "\n}\n";
    private String messageContent;
    public EventWithIDService(String id, String db){
        eventID = id;
        dbPath = db;
    }
    public EventWithIDService(String id){
        eventID = id;
    }
    public String getEvent(){
        try{
            String recieved = "";
            trans.startTransaction(dbPath);
            Event event = trans.E.getEvent(eventID);
            Gson gson = new Gson();
            recieved = gson.toJson(event);
            trans.endTransaction(true);
            return recieved ;
        }catch (DatabaseException e){
            endTransPrematurly();
            return errormessageStart + e.errormessage + errormessageEnd;
        }
    }
    private void endTransPrematurly() {
        try {
            trans.endTransaction(false);
        } catch (DatabaseException e) {
            System.out.println("You should never see this, register user endTransaction");
        }
    }
}
