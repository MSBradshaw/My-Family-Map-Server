package com.example.Model;

/**
 * Authorization Model class
 * contains the information contained in the Authorization table
 * userID refers to a userID from the User Table
 *
 */

public class Authorization {
    public String authorizationCode;
    public String UserName;
    public String timeIssued;
    public String getAuthorizationCode(){
        return authorizationCode;
    }
    public void setAuthorizationCode(String code){
        authorizationCode = code;
    }
    public String getUserName(){
        return UserName;
    }
    public void setUserName(String input){
        UserName = input;
    }
    public String getTimeIssued(){
        return timeIssued;
    }
    public void setTimeIssued(String input){
        timeIssued = input;
    }
    /**
     * removes Authorizations that are too old to use anymore
     */
    public void clearOldAuthorizations(){}
}
