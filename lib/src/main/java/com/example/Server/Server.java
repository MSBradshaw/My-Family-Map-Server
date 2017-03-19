package com.example.Server;

import com.example.DAO.DatabaseException;
import com.example.Handlers.ClearHandler;
import com.example.Handlers.DefaultHandler;
import com.example.Handlers.EventHandler;
import com.example.Handlers.DefaultHandler;
import com.example.Handlers.EventWithIdHandler;
import com.example.Handlers.FillHandler;
import com.example.Handlers.LoadHandler;
import com.example.Handlers.LoginHandler;
import com.example.Handlers.PersonHandler;
import com.example.Handlers.PersonWithIdHandler;
import com.example.Handlers.RegisterHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Michael on 3/6/2017.
 */

public class Server {
    private HttpServer server;
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private void run(String portNumber){
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null); // use the default executor
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/", new PersonWithIdHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/event/", new EventWithIdHandler());
        server.createContext("/", new DefaultHandler());

        server.start();
    }
    public void stopServer(){
        server.stop(0);
    }
    public static void main(String[] args) throws DatabaseException {
        try {
            Class.forName("org.sqlite.JDBC");
        }catch(ClassNotFoundException e){
            throw new DatabaseException("Class failure in startTransation");
        }
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
