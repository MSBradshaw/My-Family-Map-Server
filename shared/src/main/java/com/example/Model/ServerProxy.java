package com.example.Model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * this class has the methods that are used by the client to access the server
 * it does NOT deal directly with the database
 * it merely collects information from the user, sends it to the server and recieves data from the server
 *
 */
public class ServerProxy {
    public String getAuthCode() {
        return authCode;
    }
    public static String url;
    public ServerProxy(String host, String port){
        url = "http://" + host + ":" + port + "/";
    }
    public ServerProxy(){

    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    private static String authCode = "";
    /**
     * create json of the provided information and passes it to the server
     * returns true or false whether a new user was successfully added
     *
     *
     *
     */
    public String register(User user) throws IOException {
        return genericFunction(user,"http://10.0.2.2:8080/user/register");
    }

    public String login(User user){
        return genericFunction(user,"http://10.0.2.2:8080/user/login");
    }
    public String genericFunction(Object user, String urlStr){
        try {
            Authorization authorization = null;
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            OutputStream requestBody = connection.getOutputStream();
            Gson gson = new Gson();
            String jsonStr = gson.toJson(user);
            requestBody.write(jsonStr.getBytes());
            requestBody.close();
            StringBuilder sb = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String returnedData = connection.getResponseMessage();
                
                //it should be the string of an Auth
                //get the input stream http.getinput
                InputStream in = connection.getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(in));
                sb = new StringBuilder();
                String line = "";
                while((line = buff.readLine()) != null){
                    sb.append(line);
                }
                String str = sb.toString();
                return sb.toString();
            }else{
                return null;
            }
        }catch (IOException e){
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }
    /**
     * deleted all data from the data bases and create new clean ones
     * displays a messages whether is was cleared or not
     *
     */
    public String clear(){
        return genericFunction("http://localhost:8080/clear");
    }
    public String genericFunction(String urlstr){
        try {
            Authorization authorization = null;
            URL url = new URL(urlstr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Authorization", "afj232hj2332");
            connection.setDoOutput(true);
            connection.connect();
            OutputStream requestBody = connection.getOutputStream();
            StringBuilder sb = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String returnedData = connection.getResponseMessage();
                
                //it should be the string of an Auth
                //get the input stream http.getinput
                InputStream in = connection.getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(in));
                sb = new StringBuilder();
                String line = "";
                while((line = buff.readLine()) != null){
                    sb.append(line);
                }
                String str = sb.toString();
                return sb.toString();
            }else{
                return null;
            }
        }catch (IOException e){
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }
    /**
     * overloaded version
     * Populates the server's database with generated data for the specified user name.
     * The required username parameter must be a user already registered with the server. If there is
     * any data in the database already associated with the given user name, it is deleted. The
     * optional generations parameter lets the caller specify the number of generations of ancestors
     * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     *
     * @param username the username
     * @param generations number of generations to be populated, optional
     */
    public String fill(String username, int generations){
        return actualFill(username,generations);
    }
    public String fill(String username){
        int defaultnumberOfGenerations = 4;
        return actualFill(username,defaultnumberOfGenerations);
    }
    public String actualFill(String username, int gen){
        try {
            Authorization authorization = null;
            String urlStr = username + "/" + gen;
            URL url = new URL("http://localhost:8080/fill/" + urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            StringBuilder sb = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String returnedData = connection.getResponseMessage();
                
                InputStream in = connection.getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(in));
                sb = new StringBuilder();
                String line = "";
                while((line = buff.readLine()) != null){
                    sb.append(line);
                }
                String str = sb.toString();
                
                return sb.toString();
            }else{
                return null;
            }
        }catch (IOException e){
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }
    public String load(User[] users, Person[] persons, Event[] events){
        LoadData data =  new LoadData(users,persons,events);
        return genericFunction(data,"http://localhost:8080/load");
    }

    /**
     * finds the provided personID and return the corresponding Person Object
     * Auth required: yes
     * http://10.0.2.2:8080/user/login
     * @param personID
     * @return
     */
    public String person(String personID, String authID){
        Person person = new Person();
        if(!personID.equals("")){
            personID = "/" + personID;
        }
        String inputUrl = "http://10.0.2.2:8080/person" + personID;
        return genericEventoPerson(inputUrl,authID);
    }
    private String genericEventoPerson(String inputUrl, String authID){
        try {
            Authorization authorization = null;
            URL url = new URL(inputUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Authorization", authID);
            connection.setDoOutput(true);
            connection.connect();
            OutputStream requestBody = connection.getOutputStream();
            StringBuilder sb = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String returnedData = connection.getResponseMessage();
                
                InputStream in = connection.getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(in));
                sb = new StringBuilder();
                String line = "";
                while((line = buff.readLine()) != null){
                    sb.append(line);
                }

                String str = sb.toString();
                
                return sb.toString();
            }else{
                String returnedData = connection.getResponseMessage();
                
                return "Invalid User Id to access that person's information";

            }
        }catch (IOException e){
            e.printStackTrace();
            
            return e.getLocalizedMessage();
        }
    }

    /**
     * takes in an eventID and returns that specific event object
     * @param eventId
     * @return
     */
    public String event(String eventId, String authID){
        Person person = new Person();
        String inputUrl = "http://10.0.2.2:8080/event" + eventId;
        return genericEventoPerson(inputUrl,authID);
    }


    class LoadData{
        public User[] users;
        public Person[] persons;
        public Event[] events;
        public LoadData(User[] u, Person[] p, Event[] e){
            users = u;
            persons = p;
            events = e;
        }
    }

}
