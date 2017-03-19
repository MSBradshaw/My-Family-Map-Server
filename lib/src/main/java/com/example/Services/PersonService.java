package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.Person;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 3/7/2017.
 */

public class PersonService {
    private String dbPath = "jdbc:sqlite:mainData.sqlite";
    private Transaction trans = new Transaction();
    private String UserName;
    private String errormessageStart = "{\n\t\"message\": \"";
    private String successmessageStart = "{\n\t";
    private String errormessageEnd = "\"\n}\n";
    private String successMessageEnd = "\n}\n";
    private String messageContent;
    public PersonService( String userName){
        UserName = userName;
    }
    public PersonService(String username, String db){
        dbPath = db;
        UserName = username;
    }
    public String getPerson(){
        String recieved = "";
        try{
            trans.startTransaction(dbPath);
            Set<Person> personSet = trans.P.getAllPersonsWithDecendent(UserName);
            Data dataObject = new Data();
            dataObject.data.addAll(personSet);
            Gson gson = new Gson();
            recieved = gson.toJson(dataObject.data);
            trans.endTransaction(true);
            return recieved ;
        }catch (DatabaseException e){
            endTransPrematurly();
            return errormessageStart + e.errormessage + errormessageEnd;
        }
    }
    class Data{
        public List<Person> data =  new ArrayList();
    }
    private void endTransPrematurly() {
        try {
            trans.endTransaction(false);
        } catch (DatabaseException e) {
            System.out.println("You should never see this, register user endTransaction");
        }
    }
}
