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

    public void generateColorandEventList(){
        //get set of events
        getEventsTypes();
        setColors();
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
}
