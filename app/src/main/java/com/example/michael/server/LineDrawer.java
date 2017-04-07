package com.example.michael.server;

import android.content.Context;
import android.graphics.Color;

import com.example.Model.Event;
import com.example.Model.Person;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 4/6/2017.
 */

public class LineDrawer {
    public void drawLine(Event one, Event two, GoogleMap map, Context context, int color){
        PolylineOptions line=
                new PolylineOptions().add(new LatLng(Double.parseDouble(one.getLatitude()),
                        Double.parseDouble(one.getLongitude())),
                        new LatLng(Double.parseDouble(two.getLatitude()),
                        Double.parseDouble(two.getLongitude())))
                        .width(5).color(context.getResources().getColor(color));
        map.addPolyline(line);
    }
    public void drawFamilyLines(GoogleMap googleMap, Context context){
        //get all of their relatives first events
        for(String key : ClientModel.getInstance().people.keySet()){
            Person person  = ClientModel.getInstance().people.get(key);
            drawSinglePersonFamilyLines(googleMap,context,person);
        }
    }
    private void drawSinglePersonFamilyLines(GoogleMap googleMap, Context context, Person person){
        //get the person's family
        List<Person> family = new ArrayList();
        Person mom = getMother(person);
        Person dad = getFather(person);
        Person child = getChild(person);
        if(mom != null){
            family.add(mom);
        }
        if(dad != null){
            family.add(dad);
        }
        if(child != null){
            family.add(child);
        }
        if(family.size() == 0){
            return;
        }
        //get each family member's first event
        List<Event> events = new ArrayList();
        for(Person p : family){
            List<Event> temp = ClientModel.getInstance().personEvents.get(p.getPersonID());
            if(temp.size()>0){
                sortEvents(temp);
                events.add(temp.get(0)); //adds the first event to the events list
            }
        }
        //add the person's first event
        List<Event> temp = ClientModel.getInstance().personEvents.get(person.getPersonID());
        sortEvents(temp);
        events.add(temp.get(0)); //adds the first event to the events list
        //connect the dots
        String color  = Settings.getInstance().getFamily_story_lines_color();
        dotconnector(googleMap,context,events,color);
    }
    public void drawLifeLines(GoogleMap googleMap, Context context){
        //draw lines for each person chronilogically
        String color =  Settings.getInstance().getLife_story_lines_color();
        for(String key : ClientModel.getInstance().personEvents.keySet()){
            List<Event> events = ClientModel.getInstance().personEvents.get(key);
            sortEvents(events);
            dotconnector(googleMap,context,events,color);
        }
    }
    public void drawSpouseLines(GoogleMap googleMap, Context context){
        //for each person connect them and their spouse
        for(String key : ClientModel.getInstance().people.keySet()){
            Person person = ClientModel.getInstance().people.get(key);
            if(person.getSpouse() == null || person.getSpouse().equals("")){
                continue;
            }
            Person spouse = ClientModel.getInstance().people.get(person.getSpouse());
            if(person == null || spouse == null){
                continue;
            }
            Event one = getFirstEvent(person);
            Event two = getFirstEvent(spouse);
            if(one != null && two != null){
                drawLine(one,two,googleMap,context,getColor(Settings.getInstance().getSpouse_lines_color()));
            }
        }

    }
    private Event getFirstEvent(Person person){
        Event event =  null;
        List<Event> events = ClientModel.getInstance().personEvents.get(person.getPersonID());
        if(events.size() == 0){
            return null;
        }
        sortEvents(events);
        event = events.get(0);
        return event;
    }
    private void dotconnector(GoogleMap googleMap, Context context, List<Event> events, String color){
        for(int i =0; i < events.size()-1;i++){
            Event one = events.get(i);
            Event two = events.get(i+1);
            drawLine(one,two,googleMap, context,getColor(color));
        }
    }
    private int getColor(String color){
        if(color.equals("Red")){
            return(R.color.red);
        }else if(color.equals("Blue")){
            return(R.color.blue);
        }else if(color.equals("Green")){
            return(R.color.green);
        }else if(color.equals("Orange")){
            return(R.color.orange);
        }else if(color.equals("Yellow")){
            return(R.color.yellow);
        }else if(color.equals("Purple")){
            return(R.color.purple);
        }
        //you should never see pink
        return R.color.pink;
    }
    private void sortEvents(List<Event> events){
        List<Event> temp = events;
        //birth first
        //death last
        //sorted by date or ABC if no dae
        basicSort(temp);
        //order the birth and death dates
        boolean changed = true;
        while(changed) {
            changed = false;
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getDescription().toLowerCase().equals("birth") && i != 0) {
                    Event e = temp.get(i);
                    temp.remove(i);
                    temp.add(0, e);
                    changed = true;
                } else if (temp.get(i).getDescription().toLowerCase().equals("death") && i != temp.size()-1) {
                    Event e = temp.get(i);
                    temp.remove(i);
                    temp.add(temp.size() - 1, e);
                    changed = true;
                }
            }
        }
        events = temp;
    }
    private void basicSort(List<Event> temp){
        boolean changed = true;
        while(changed) {
            changed = false;
            for (int i = 0; i < temp.size() - 1; i++) {
                if(temp.get(i).getYear() != null && !temp.get(i).getYear().equals("")
                        && temp.get(i+1).getYear() != null && !temp.get(i+1).getYear().equals("")
                        ){
                    int year1 = Integer.parseInt(temp.get(i).getYear());
                    int year2 = Integer.parseInt(temp.get(i + 1).getYear());
                    if (year1 > year2) {
                        //switch the two of them
                        Event e = temp.get(i);
                        temp.set(i,temp.get(i+1));
                        temp.set(i+1,e);
                        changed = true;
                    }
                }else{
                    String first = temp.get(i).getDescription().toLowerCase();
                    String second = temp.get(i+1).getDescription().toLowerCase();
                    if(first.compareTo(second) > 0){
                        Event e = temp.get(i);
                        temp.set(i,temp.get(i+1));
                        temp.set(i+1,e);
                        changed = true;
                    }
                }
            }
        }
    }
    public Person getChild(Person person){
        Person child = null;
        for(String key: ClientModel.getInstance().people.keySet()){
            if(ClientModel.getInstance().people.get(key).getMother().equals(person.getPersonID())){
                child = ClientModel.getInstance().people.get(key);
            }
            if(ClientModel.getInstance().people.get(key).getFather().equals(person.getPersonID())){
                child = ClientModel.getInstance().people.get(key);
            }
        }
        return child;
    }
    public Person getMother(Person person){
        Person mom = null;
        for(String key: ClientModel.getInstance().people.keySet()){
            if(key.equals(person.getMother())){
                mom = ClientModel.getInstance().people.get(key);
                break;
            }
        }
        return mom;
    }
    public Person getFather(Person person){
        Person dad = null;
        for(String key: ClientModel.getInstance().people.keySet()){
            if(key.equals(person.getFather())){
                dad = ClientModel.getInstance().people.get(key);
                break;
            }
        }
        return dad;
    }
}
