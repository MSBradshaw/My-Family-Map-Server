package com.example.Handlers;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;
import com.example.Model.Authorization;
import com.example.Model.Event;
import com.example.Model.Person;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.crypto.Data;

/**
 * Created by Michael on 3/6/2017.
 */

public class HandlerParent {
    public String errormessageStart = "{\n\t\"message\": \"";
    public String successmessageStart = "{\n\t";
    public String errormessageEnd = "\"\n}\n";
    public String successMessageEnd = "\n}\n";
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
    public Authorization verifyAuthNoComparingUserID(String authtoken) throws DatabaseException {
        Transaction transaction = new Transaction();
        try {
            transaction.startTransaction("jdbc:sqlite:mainData.sqlite");
            Authorization authorization = transaction.A.checkAuthorization(authtoken);
            //if it gets passed this point it means there was an authtoken found
            //transaction.endTransaction(true);
            transaction.endTransaction(false);
            return authorization;
        }catch(DatabaseException e){
            transaction.endTransaction(false);
            e.errormessage = errormessageStart + "failed to very authorization token" + errormessageEnd;
            throw e;
        }
    }
    public String getDecendant(String jsonIn){
        Gson gson = new Gson();
        Person temp = (Person)gson.fromJson(jsonIn, Person.class);
        return temp.getDescendant();
    }
    public String getDecendantFromEvent(String jsonIn){
        Gson gson = new Gson();
        Event temp = (Event)gson.fromJson(jsonIn, Event.class);
        return temp.getDescendant();
    }
    public List<String> regexUrlParser(String command){
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("[/][-\\w]+")
                .matcher(command);
        while (m.find()) {
            StringBuilder sb = new StringBuilder(m.group());
            sb.deleteCharAt(0);
            allMatches.add(sb.toString());
        }
        return allMatches;
    }
}
