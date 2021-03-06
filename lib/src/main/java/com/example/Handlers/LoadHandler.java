package com.example.Handlers;

import com.example.DAO.DatabaseException;
import com.example.Services.LoadService;
import com.example.Services.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Created by Michael on 3/9/2017.
 */

public class LoadHandler extends HandlerParent implements HttpHandler {

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
            LoadService loadService =  new LoadService(sb.toString());
            String response = loadService.load();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); //this must be sent before the body
            response = response.replaceAll("[^a-zA-z\\s{\":\\d,.-}-()']","");
            OutputStream respBody = exchange.getResponseBody();
            writeString(response, respBody);
            respBody.close();
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0); //this must be sent before the body
            OutputStream respBody = exchange.getResponseBody();
            String sendMe = errormessageStart + "Exception was thrown in LoadHandler" + errormessageEnd;
            writeString(sendMe, respBody);
            respBody.close();
        }
    }

}
