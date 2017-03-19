package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.DAO.UserDAO;
import com.example.Model.Authorization;
import com.example.Model.Person;
import com.example.Model.User;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michael on 3/1/2017.
 */

public class RegisterService {
    private String jsonParameter;
    private String dbPath = "jdbc:sqlite:mainData.sqlite";
    private Transaction trans = new Transaction();
    private User U;
    private String errormessageStart = "{\n\t\"message\": \"";
    private String successmessageStart = "{\n\t";
    private String errormessageEnd = "\"\n}\n";
    private String successMessageEnd = "\n}\n";
    private String messageContent;

    public RegisterService(String jstring){
        jsonParameter = jstring;
    }
    public RegisterService(String jstring, String dbPathtemp){
        jsonParameter = jstring;
        dbPath = dbPathtemp;
    }
    public String registerUser() throws DatabaseException {
        U = createObject();
        Authorization A = null;
        try{
            trans.startTransaction(dbPath);
            trans.endTransaction(true);
            trans.startTransaction(dbPath);
            //check the info, if fails checkinfo return error message the checkinfo function set
            if(!checkInfo()){
                trans.endTransaction(false);
                return errormessageStart + messageContent + errormessageEnd;
            }
            //send to the data base
            trans.U.addToUserTable(U);
            trans.P.addToPersonTable(userTopersonCoverter(U));
            trans.endTransaction(true);
            trans.startTransaction(dbPath);
            //get auth token
            //call fill service with a the username
            FillService fill =  new FillService(U.getUsername());
            String output = fill.fill();
            A = trans.A.addToAuthorizationTable(createAuthObject(U.getUsername()));
            //end trans
            trans.endTransaction(true);
        }catch(DatabaseException e){
            //System.out.println("You should never see this, register user endTransaction true");
            trans.endTransaction(false);
            throw e;
            //return errormessageStart + e.errormessage + errormessageEnd;
        }
        //convert auth object back to json object and return it
        String returnMe = authObjectTojson(A);
        return returnMe;
    }
    public Authorization createAuthObject(String UserName){
        Authorization A = new Authorization();
        A.UserName = UserName;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        A.timeIssued = timeStamp;
        return A;
    }
    public Person userTopersonCoverter(User U){
        Person P = new Person();
        P.setPersonID(U.getPersonID());
        P.setFirstname(U.getFirstname());
        P.setLastname(U.getLastname());
        P.setGender(U.getGender());
        P.setDescendant(U.getUsername());
        return P;
    }
    public boolean checkInfo() {
        //check userName is not used
        //start transaction

        //check for the username
        try{
            trans.U.getUser(U.getUsername());
        }catch (DatabaseException e){
            messageContent = "username already in use";
            return false;//good, if the username is not found an exception should be thrown
        }

        //check all else is not null
        if(nulloremptyString(U.getEmail())){
            messageContent = "invalid email";
            return false;
        }
        if(nulloremptyString(U.getFirstname())){
            messageContent = "invalid first name;";
            return false;
        }
        if(nulloremptyString(U.getLastname())){
            messageContent = "invalid lastname";
            return false;
        }
        if(!(U.getGender().equals("f") || U.getGender().equals("m") || U.getGender().equals("F") || U.getGender().equals("M"))){
            messageContent = "invalid gender";
            return false;
        }
        if(nulloremptyString(U.getPassword())){
            messageContent = "You must have a password";
            return false;
        }
        if(nulloremptyString(U.getUsername())){
            messageContent = "invalid username";
            return false;
        }
        return true;
    }
    //checks if the string is null or empty if so returns false
    public boolean nulloremptyString(String s){
        if(s == null){return true;}
        return(s.equals(""));
    }
    //returns the User object created from the json string
    public User createObject(){
        User U = new User();
        Gson gson = new Gson();
        U = (User)gson.fromJson(jsonParameter, User.class);
        U.getGender();
        return U;
    }
    public String authObjectTojson(Authorization A){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(A);
        return jsonStr;
    }
    public void endTransPrematurly(){
        try{
            trans.endTransaction(false);
        }catch(DatabaseException e){
            System.out.println("You should never see this, register user endTransaction");
        }
    }
}
