package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.Event;
import com.example.Model.Person;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 3/7/2017.
 */

public class EventService {
    private String dbPath = "jdbc:sqlite:mainData.sqlite";
    private Transaction trans = new Transaction();
    private String decendentID;
    private String errormessageStart = "{\n\t\"message\": \"";
    private String successmessageStart = "{\n\t";
    private String errormessageEnd = "\"\n}\n";
    private String successMessageEnd = "\n}\n";
    private String messageContent;
    public EventService(String id, String db){
        decendentID = id;
        dbPath = db;
    }
    public EventService(String id){
        decendentID = id;
    }
    public String getEvents(){
        try{
            String recieved = "";
            trans.startTransaction(dbPath);
            Set<Event> eventSet = trans.E.getAllEvents(decendentID);
            Data dataObject = new Data();
            dataObject.data.addAll(eventSet);
            Gson gson = new Gson();
            recieved = gson.toJson(dataObject.data);
            trans.endTransaction(true);
            return recieved;
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
    class Data{
        public List<Event> data =  new ArrayList();
    }
}
