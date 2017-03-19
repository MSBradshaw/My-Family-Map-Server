package com.example.Handlers;

import com.example.DAO.DatabaseException;
import com.example.Model.Authorization;
import com.example.Services.EventService;
import com.example.Services.EventWithIDService;
import com.example.Services.PersonService;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Created by Michael on 3/7/2017.
 */

public class EventHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Headers reqHeaders = exchange.getRequestHeaders();
            if (reqHeaders.containsKey("Authorization")) {
                String authToken = reqHeaders.getFirst("Authorization");
                Authorization authorization = verifyAuthNoComparingUserID(authToken);
                InputStream is = exchange.getRequestBody();
                java.util.Scanner s = new java.util.Scanner(is);
                StringBuilder sb = new StringBuilder();
                while(s.hasNext()){
                    sb.append(s.next());
                }
                EventService eventService =  new EventService(authorization.getUserName());
                String response = eventService.getEvents();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                response = response.replaceAll("[^a-zA-z\\s{\":\\d,.-}-()']","");
                OutputStream respBody = exchange.getResponseBody();
                writeString(response, respBody);
                respBody.close();
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
