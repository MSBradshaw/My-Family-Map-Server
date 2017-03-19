package com.example.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.*;

import java.io.IOException;
import java.util.Scanner;

import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import static javax.imageio.ImageIO.read;

/**
 * Created by Michael on 3/7/2017.
 */

public class DefaultHandler extends HandlerParent implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI myuri = exchange.getRequestURI();
        
        String path = "HTML/index.html";
        if(myuri.toString().equals("/css/main.css")){
            path = "HTML/css/main.css";
        }
        //check the url
        //if it has the css page change the file path for the css
        File file = new File(path);
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        }catch(Exception e){
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();

        while(scan.hasNext()){
            sb.append(new StringBuilder(scan.nextLine()+"\n"));
        }

        String filetext = sb.toString();

        exchange.sendResponseHeaders(200, filetext.length());

        OutputStream os = exchange.getResponseBody();
        os.write(filetext.getBytes());
        os.close();


    }
}
