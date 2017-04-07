package com.example.michael.server;

import com.example.Model.Event;
import com.example.Model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.graphics.Color.*;

/**
 * Created by Michael on 3/23/2017.
 */

public class ClientModel {
    public  Map<String, Integer> possibleColors = new HashMap();
    private static Person currentPerson;
    public static Person getCurrentPerson() {
        return currentPerson;
    }
    public static void setCurrentPerson(Person currentPerson) {
        ClientModel.currentPerson = currentPerson;
    }
    private static ClientModel instance = null;
    protected ClientModel() {

    }
    public static ClientModel getInstance(){
        if(instance == null) {
            instance = new ClientModel();
        }
        return instance;
    }
    private String userPersonID;
    public String getUserPersonID() {
        return userPersonID;
    }
    public void setUserPersonID(String userPersonID) {
        this.userPersonID = userPersonID;
    }
    public Map<String,Person> people = new HashMap();
    public Map<String,Event> events = new HashMap();
    private Map<String,Event> eventsSuper = new HashMap();
    public Map<String,List<Event>> personEvents = new HashMap();
    public Set<String> eventTypes;
    public Map<String,String> eventTypeAndColor = new HashMap();
    public Set<String> paternalAncestors = new HashSet();
    public Set<String> maternalAncestors = new HashSet();
    public Map<String,String> personChildren =  new HashMap();
    private void fillpersonEvents(){
        for(String pKey : people.keySet()){
            List<Event> eventsTemp = new ArrayList();
            for(String eKey : events.keySet()){
                if(events.get(eKey).getPersonID().equals(pKey)){
                    eventsTemp.add(events.get(eKey));
                }
            }
            personEvents.put(pKey,eventsTemp);
        }
        System.out.println(personEvents.size());
    }
    public void generateColorandEventList(){
        //get set of events
        getEventsTypes();
        setColors();
        fillpersonEvents();
        //set colors for each
    }
    private void getEventsTypes(){
        eventTypes = new HashSet();
        for(String key : events.keySet()){
            String eventtype = events.get(key).getDescription();
            eventTypes.add(eventtype);
        }
    }
    private void setColors(){
        Integer color = 0;
        initializePossibleColors();
        for(String event : eventTypes){
            ClientModel.getInstance().eventTypeAndColor.put(event,color.toString());

        }
    }
    private void initializePossibleColors(){
        possibleColors.put("Red",0);
        possibleColors.put("Orange",25);
        possibleColors.put("Yellow",45);
        possibleColors.put("Green",135);
        possibleColors.put("Blue",225);
        possibleColors.put("Purple",315);
    }
    public Person getChild(){
        Person child = null;
        for(String key:people.keySet()){
            if(people.get(key).getMother().equals(currentPerson.getPersonID())){
                child = people.get(key);
            }
            if(people.get(key).getFather().equals(currentPerson.getPersonID())){
                child = people.get(key);
            }
        }
        return child;
    }
    public Person getMother(){
       Person mom = null;
        for(String key: people.keySet()){
            if(key.equals(currentPerson.getMother())){
                mom = people.get(key);
                break;
            }
        }
        return mom;
    }
    public Person getFather(){
        Person dad = null;
        for(String key: people.keySet()){
            if(key.equals(currentPerson.getFather())){
                dad = people.get(key);
                break;
            }
        }
        return dad;
    }
    public Person getSpouse(){
        Person spouse = null;
        for(String key: people.keySet()){
            if(key.equals(currentPerson.getSpouse())){
                spouse = people.get(key);
                break;
            }
        }
        return spouse;
    }
    public void getSideOfFamilyStarter(){
        Person userPerson = people.get(userPersonID);
        getSideOfFamily(userPerson.getMother(), maternalAncestors);
        getSideOfFamily(userPerson.getFather(), paternalAncestors);
    }
    public void getSideOfFamily(String personID,Set<String> ancestors){
        if(!people.containsKey(personID)){
            return;
        }
        Person child = people.get(personID);
        if (people.containsKey(child.getMother())){
            Person mother = people.get(child.getMother());
            ancestors.add(mother.getPersonID());
            getSideOfFamily(mother.getPersonID(),ancestors);
        }
        if (people.containsKey(child.getFather())){
            Person father = people.get(child.getFather());
            ancestors.add(father.getPersonID());
            getSideOfFamily(father.getPersonID(),ancestors);
        }
    }
    public void setEventsSuper(){
        eventsSuper = events;
    }
    public void filterEvents(){
        //filter out mother
        //filter out father
        //filter out males
        //filter out females
        //filter out events
    }
    public void filterOutMotherSide(){
        getSideOfFamilyStarter();
        //remove the maternal ancestors
    }

}
