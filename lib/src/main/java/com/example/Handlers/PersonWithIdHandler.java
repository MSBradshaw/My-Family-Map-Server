package com.example.Handlers;

import com.example.DAO.DatabaseException;
import com.example.Model.Authorization;
import com.example.Model.Person;
import com.example.Services.FillService;
import com.example.Services.LoadService;
import com.example.Services.PersonService;
import com.example.Services.PersonWithIdService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

/**
 * Created by Michael on 3/6/2017.
 */

public class PersonWithIdHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String command = exchange.getRequestURI().toString();

        List<String> commandList = regexUrlParser(command);
        String personid = "";
        if(commandList.size() == 1){

                PersonHandler personHandler =  new PersonHandler();
                personHandler.handle(exchange);
        }
        try {
            personid = commandList.get(1);
            Headers reqHeaders = exchange.getRequestHeaders();


            if (reqHeaders.containsKey("Authorization")) {

                String authToken = reqHeaders.getFirst("Authorization");
                Authorization authorization = verifyAuthNoComparingUserID(authToken);
                //-------------------------------------------------------------------
                InputStream is = exchange.getRequestBody();


                java.util.Scanner s = new java.util.Scanner(is);
                String jsonString = "";
                StringBuilder sb = new StringBuilder();
                while(s.hasNext()){
                    sb.append(s.next());
                }
                

                OutputStream respBody = exchange.getResponseBody();
                PersonWithIdService personWithIdService = new PersonWithIdService(personid);
                String response = personWithIdService.getPerson();
                //verify userIds from the auth token and the person

                String responseUserId = getDecendant(response);
                if(!authorization.getUserName().equals(responseUserId)){
                    String errormessage = "Auth Token and Person userIDs do not match, cannot access Person Info";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0); //this must be sent before the body
                    respBody = exchange.getResponseBody();
                    writeString(errormessageStart + errormessage + errormessageEnd, respBody);
                    respBody.close();
                }

                //exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 200); //this must be sent before the body
                // I have no idea if this is the correct way to do this but it seems to work and any other way does not send the string
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,response.length());

                response = response.replaceAll("[^a-zA-z\\s{\":\\d,.-}-()']","");

                try {
                    writeString(response, respBody);
                }catch (Exception e){
                    //e.printStackTrace();
                    System.out.println("Error from Handler: " + e.getMessage());
                }
                respBody.close();
                //-------------------------------------------------------------------

            }

        }catch (DatabaseException e){
            //send back an error message
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0); //this must be sent before the body
            OutputStream respBody = exchange.getResponseBody();
            writeString(e.errormessage, respBody);
            respBody.close();
        }
    }
}
