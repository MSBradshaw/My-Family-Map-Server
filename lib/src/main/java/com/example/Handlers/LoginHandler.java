package com.example.Handlers;

import com.example.DAO.DatabaseException;
import com.example.Services.LoginService;
import com.example.Services.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.sql.SQLException;

/**
 * Created by Michael on 3/6/2017.
 */

public class LoginHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            java.util.Scanner s = new java.util.Scanner(is);
            StringBuilder sb = new StringBuilder();
            while (s.hasNext()) {
                sb.append(s.next());
            }
            LoginService loginService = new LoginService(sb.toString());
            String response = "";
            response = loginService.logIn();

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); //this must be sent before the body
            response = response.replaceAll("[^a-zA-z\\s{\":\\d,.-}-()']","");
            OutputStream respBody = exchange.getResponseBody();
            writeString(response, respBody);
            respBody.close();
        } catch (SQLException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0); //this must be sent before the body
            OutputStream respBody = exchange.getResponseBody();
            String sendMe = errormessageStart + "There was an sql error" + errormessageEnd;
            writeString(sendMe, respBody);
            respBody.close();
        }
    }
}
