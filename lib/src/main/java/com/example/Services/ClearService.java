package com.example.Services;

import com.example.DAO.DatabaseException;
import com.example.DAO.Transaction;

/**
 * Created by Michael on 3/3/2017.
 */

public class ClearService {
    private String errormessageStart = "{\n\t\"message\": \"";
    private String successmessageStart = "{\n\t";
    private String errormessageEnd = "\"\n}\n";
    public String clear(){
        Transaction T = new Transaction();
        try {
            T.startTransaction("jdbc:sqlite:mainData.sqlite");
            T.clear();
            T.endTransaction(true);
        }catch (DatabaseException e){
            return errormessageStart + e.errormessage + errormessageEnd;

        }
        return errormessageStart + "Eveything was deleted" + errormessageEnd;
    }
}
