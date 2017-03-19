package com.example.Handlers;

import com.example.DAO.DatabaseException;
import com.example.Services.FillService;
import com.example.Services.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Michael on 3/6/2017.
 */

public class FillHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            int defaultgenerations = 4;
            InputStream is = exchange.getRequestBody();
            String command = exchange.getRequestURI().toString();
            //parse the command

            List<String> commandList = regexUrlParser(command);
            int generations;
            String username;
            if(commandList.size() == 2){
                username = commandList.get(1);
                generations = defaultgenerations;
            }else if(commandList.size() > 2){
                username = commandList.get(1);
                generations = Integer.parseInt(commandList.get(2));
            }else{
                DatabaseException e = new DatabaseException("");
                e.errormessage = "There are not url arguments you fool";
                throw e;
                // error
            }
            java.util.Scanner s = new java.util.Scanner(is);
            String jsonString = "";
            StringBuilder sb = new StringBuilder();
            while(s.hasNext()){
                sb.append(s.next());
            }
            FillService fillService = new FillService(username,generations);
            String response = fillService.fill();
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
