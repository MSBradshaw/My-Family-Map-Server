package com.example.Handlers;

import com.example.DAO.DatabaseException;
import com.example.Services.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;

import javax.xml.crypto.Data;

/**
 * Created by Michael on 3/6/2017.
 */

public class RegisterHandler extends HandlerParent implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            InputStream is = exchange.getRequestBody();
            
            String temp = exchange.getRequestURI().toString();
            
            
            
            java.util.Scanner s = new java.util.Scanner(is);
            String jsonString = "";
            StringBuilder sb = new StringBuilder();
            while(s.hasNext()){
                sb.append(s.next());
            }
            
            Gson gson = new Gson();

            RegisterService registerService = new RegisterService(sb.toString());
            String response = registerService.registerUser();

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); //this must be sent before the body
            response = response.replaceAll("[^a-zA-z\\s{\":\\d,.-}-()']","");
            OutputStream respBody = exchange.getResponseBody();
            writeString(response, respBody);
            respBody.close();
        } catch (DatabaseException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0); //this must be sent before the body
            OutputStream respBody = exchange.getResponseBody();
            String sendMe = errormessageStart + e.errormessage + errormessageEnd;
            writeString(sendMe, respBody);
            respBody.close();
        }
    }

}
