package com.example.DAO;

/**
 * Created by Michael on 2/23/2017.
 */

public class DatabaseException extends Exception {
    public String errormessage = "";
    public DatabaseException(String s) {
        super(s);
        errormessage = s;
    }

    public DatabaseException(String s, Throwable throwable) {
        super(s, throwable);
        errormessage = s;
    }
}
