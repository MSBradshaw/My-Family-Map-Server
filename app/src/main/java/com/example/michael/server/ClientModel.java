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
    private  boolean firstload = true;
    private  boolean reload = false;
    public  boolean isFirstload() {
        return firstload;
    }
    public  void setFirstload(boolean firstload) {
        this.firstload = firstload;
    }
    public  boolean isReload() {
        return reload;
    }
    public  void setReload(boolean reload) {
        this.reload = reload;
    }
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
    public Map<String,Event> eventsSuper = new HashMap();
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
        setColors();
        fillpersonEvents();
        //set colors for each
    }
    private void getEventsTypes(){
        if(firstload || reload) {
            eventTypes = new HashSet();
            for (String key : eventsSuper.keySet()) {
                String eventtype = events.get(key).getDescription();
                eventTypes.add(eventtype);
            }
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
        maternalAncestors.add(userPerson.getMother());
        getSideOfFamily(userPerson.getFather(), paternalAncestors);
        paternalAncestors.add(userPerson.getFather());
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
    public void reloadEventsFromSuperEvents(){
        if(firstload || reload){
            events.putAll(eventsSuper);
            getEventsTypes();
        }
        firstload = false;
        reload = false;
    }
    public void filterEvents(){
        filterOutEvents();
        if(!Settings.getInstance().isMotherSideOn()){
            filterOutMotherSide();
        }
        if(!Settings.getInstance().isFatherSideOn()){
            filterOutFatherSide();
        }
        if(!Settings.getInstance().isFemalesOn()){
            filterOutWomen();
        }
        if(!Settings.getInstance().isMalesOn()){
            filterOutMen();
        }
    }
    public void filterOutMotherSide(){
        getSideOfFamilyStarter();
        //remove the maternal ancestors
        List<String> deleteMe = new ArrayList();
        for(String key:events.keySet()){
            Event e = events.get(key);
            if(maternalAncestors.contains(e.getPersonID())){
                //mark the event for death
                deleteMe.add(key);
            }
        }
        for(String key:deleteMe){
            events.remove(key);
        }
    }
    public void filterOutFatherSide(){
        getSideOfFamilyStarter();
        //remove the paternal ancestors
        List<String> deleteMe = new ArrayList();
        for(String key:events.keySet()){
            Event e = events.get(key);
            if(paternalAncestors.contains(e.getPersonID())){
                //mark the event for death
                deleteMe.add(key);
            }
        }
        for(String key:deleteMe){
            events.remove(key);
        }
    }
    public void filterOutMen(){
        List<String> deleteMe = new ArrayList();
        //get list of men
        List<String> men = new ArrayList();
        for(String key:people.keySet()){
            if(people.get(key).getGender().equals("m") || people.get(key).getGender().equals("M")){
                men.add(key);
            }
        }
        for(String key:events.keySet()){
            Event e = events.get(key);
            if(men.contains(e.getPersonID())){
                //mark the event for death
                deleteMe.add(key);
            }
        }
        for(String key:deleteMe){
            events.remove(key);
        }
    }
    public void filterOutWomen(){
        List<String> deleteMe = new ArrayList();
        //get list of women
        List<String> women = new ArrayList();
        for(String key:people.keySet()){
            if(people.get(key).getGender().equals("f") || people.get(key).getGender().equals("F")){
                women.add(key);
            }
        }
        for(String key:events.keySet()){
            Event e = events.get(key);
            if(women.contains(e.getPersonID())){
                //mark the event for death
                deleteMe.add(key);
            }
        }
        for(String key:deleteMe){
            events.remove(key);
        }
    }
    public void filterOutEvents(){
        List<String> deleteMe = new ArrayList();
        setReload(true);
        reloadEventsFromSuperEvents();
        Settings.getInstance().loadFilterSettings();
        //get list of women
        if(Settings.getInstance().filterSettings == null){
            return;
        }
        for(String key:events.keySet()){
            Event e = events.get(key);
            if(Settings.getInstance().filterSettings.containsKey(e.getDescription())){
                if(!Settings.getInstance().filterSettings.get(e.getDescription())){
                    deleteMe.add(key);
                }
            }

        }
        for(String key:deleteMe){
            events.remove(key);
        }
    }
    public List<Person> searchPeople(String word){
        List<Person> searchPeople = new ArrayList();
        for(String key:people.keySet()){
            if(personContains(people.get(key),word)){
                searchPeople.add(people.get(key));
            }
        }
        return searchPeople;
    }
    public List<Event> searchEvents(String word){
        //search the super set
        List<Event> searchEvents = new ArrayList();
        for(String key:events.keySet()){
            if(eventContain(events.get(key),word)){
                searchEvents.add(events.get(key));
            }
        }
        return searchEvents;
    }
    private boolean personContains(Person person, String word){
        if(person.getFirstname().contains(word)){
            return true;
        }
        if(person.getLastname().contains(word)){
            return true;
        }
        return false;
    }
    private boolean eventContain(Event event, String word){
        if(event.getDescription().contains(word)){
            return true;
        }
        if(event.getCountry().contains(word)){
            return true;
        }
        if(event.getCity().contains(word)){
            return true;
        }
        if(event.getYear().contains(word)){
            return true;
        }
        return false;
    }
}
