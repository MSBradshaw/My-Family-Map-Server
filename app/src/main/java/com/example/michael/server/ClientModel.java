package com.example.michael.server;

import com.example.Model.Event;
import com.example.Model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Michael on 3/23/2017.
 */

public class ClientModel {
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

    public Map<String,Person> people = new HashMap();
    public Map<String,Event> events = new HashMap();
    public Map<String,List<Event>> personEvents = new HashMap();

    public Set<String> eventTypes;
    // Map<evenType,color for array>
    public Map<String,String> eventTypeAndColor = new HashMap();
    public Set<String> paternalAncestors = new HashSet();
    public Set<String> maternalAncestors = new HashSet();
    // Map<PersonID, Person's Child as Person Object (List of all that person's children)>
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
        for(String key : ClientModel.getInstance().events.keySet()){
            String eventtype = ClientModel.getInstance().events.get(key).getDescription();
            eventTypes.add(eventtype);
        }
    }
    private void setColors(){
        Integer color = 0;
        for(String event : eventTypes){
            ClientModel.getInstance().eventTypeAndColor.put(event,color.toString());
            color = color + 20;
            if(color > 360){
                color = 0;
            }
        }
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
}
