package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.Person;
import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Michael on 3/7/2017.
 */

public class PersonWithIdService {
    private String dbPath = "jdbc:sqlite:mainData.sqlite";
    private Transaction trans = new Transaction();
    private String personID;
    private String errormessageStart = "{\n\t\"message\": \"";
    private String successmessageStart = "{\n\t";
    private String errormessageEnd = "\"\n}\n";
    private String successMessageEnd = "\n}\n";
    private String messageContent;
    public PersonWithIdService( String id){
        personID = id;
    }
    public PersonWithIdService(String id, String db){
        dbPath = db;
        personID = id;
    }
    public String getPerson(){
        String recieved = "";
        try{
            trans.startTransaction(dbPath);
            Person person = trans.P.getPerson(personID);
            Gson gson = new Gson();
            recieved = gson.toJson(person);
            trans.endTransaction(true);
        }catch(DatabaseException e){
            endTransPrematurly();
            return errormessageStart + e.errormessage + errormessageEnd;
        }
        return recieved ;
    }
    private void endTransPrematurly() {
        try {
            trans.endTransaction(false);
        } catch (DatabaseException e) {
            System.out.println("You should never see this, register user endTransaction");
        }
    }
}
