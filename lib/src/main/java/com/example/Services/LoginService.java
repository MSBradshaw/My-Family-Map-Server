package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.*;
import com.google.gson.Gson;

import java.sql.SQLException;

/**
 * Created by Michael on 3/3/2017.
 */

public class LoginService {
    private String jsonParameter;
    private String dbPath = "jdbc:sqlite:mainData.sqlite";
    private String errormessageStart = "{\n\t\"message\": \"";
    private String successmessageStart = "{\n\t";
    private String errormessageEnd = "\"\n}\n";
    private String successMessageEnd = "\n}\n";
    private String messageContent;
    Transaction trans = new Transaction();
    public LoginService(String jsonIn){
        jsonParameter = jsonIn;
    }
    public LoginService(String jsonIn, String db){
        jsonParameter = jsonIn;
        dbPath = db;
    }
    public String logIn() throws SQLException {
        String passwordInput;
        //create object
        User user = createObject();
        passwordInput = user.getPassword();
        Authorization auth = new Authorization();
        //-------------------------------------
        try{
            //starts the transaction
            trans.startTransaction(dbPath);
            //retrieve the user info
            user = trans.U.getUser(user.getUsername());
            // if user object is null it means it was not found
            if(user == null){
                messageContent = "User Name not found";
                endTransPrematurly();
                return errormessageStart + messageContent + errormessageEnd;
            }
            //compare the user object's pass word and the one given as input
            if(!user.getPassword().equals(passwordInput)){
                messageContent = "Invalid Password";
                endTransPrematurly();
                return errormessageStart + messageContent + errormessageEnd;
            }
            //get authToken
            auth.setUserName(user.getUsername());
            auth = trans.A.addToAuthorizationTable(auth);

        }catch (DatabaseException e){
            messageContent = e.errormessage;
            // end the trans prematurely, save nothing
            endTransPrematurly();
            return errormessageStart + messageContent + errormessageEnd;
        }
        //-------------------------------------
        // end the transaction and save changes
        try{
            trans.endTransaction(true);
        }catch(DatabaseException e){
            System.out.println("You should never see this, LOG IN endTransaction true");
            return errormessageStart + e.errormessage + errormessageEnd;
        }
        //convert the auth token to string to be returned
        String returnMe = authObjectTojson(auth);
        return returnMe;
    }

    private User createObject(){
        User U;
        Gson gson = new Gson();
        U = (User)gson.fromJson(jsonParameter, User.class);
        return U;
    }
    public void endTransPrematurly(){
        try{
            trans.endTransaction(false);
        }catch(DatabaseException e){
            System.out.println("You should never see this, register user endTransaction");
        }
    }
    public String authObjectTojson(Authorization A){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(A);
        return jsonStr;
    }
}
