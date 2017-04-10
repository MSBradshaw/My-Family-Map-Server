package com.example.michael.server;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.*;
/**
 * Created by Michael on 4/4/2017.
 */

public class Settings {
    private boolean life_story_lines = false;
    private boolean family_sotry_lines = false;
    private boolean spouse_lines = false;
    private String life_story_lines_color = "Red";
    private String family_story_lines_color = "Red";
    private String spouse_lines_color = "Red";
    private String map_type = "Normal";
    public Map<String,Boolean> filterSettings = new HashMap();
    private boolean fatherSideOn = true;
    private boolean motherSideOn = true;
    private boolean malesOn = true;
    private boolean femalesOn = true;

    public boolean isFatherSideOn() {
        return fatherSideOn;
    }

    public void setFatherSideOn(boolean fatherSideOn) {
        this.fatherSideOn = fatherSideOn;
    }

    public boolean isMotherSideOn() {
        return motherSideOn;
    }

    public void setMotherSideOn(boolean motherSideOn) {
        this.motherSideOn = motherSideOn;
    }

    public boolean isMalesOn() {
        return malesOn;
    }

    public void setMalesOn(boolean malesOn) {
        this.malesOn = malesOn;
    }

    public boolean isFemalesOn() {
        return femalesOn;
    }

    public void setFemalesOn(boolean femalesOn) {
        this.femalesOn = femalesOn;
    }

    public String getMap_type() {
        return map_type;
    }

    public void setMap_type(String map_type) {
        this.map_type = map_type;
    }

    public String getLife_story_lines_color() {
        return life_story_lines_color;
    }

    public void setLife_story_lines_color(String life_story_lines_color) {
        this.life_story_lines_color = life_story_lines_color;
    }

    public String getFamily_story_lines_color() {
        return family_story_lines_color;
    }

    public void setFamily_story_lines_color(String family_story_lines_color) {
        this.family_story_lines_color = family_story_lines_color;
    }

    public String getSpouse_lines_color() {
        return spouse_lines_color;
    }

    public void setSpouse_lines_color(String spouse_lines_color) {
        this.spouse_lines_color = spouse_lines_color;
    }

    public boolean isLife_story_lines() {
        return life_story_lines;
    }

    public void setLife_story_lines(boolean life_story_lines) {
        this.life_story_lines = life_story_lines;
    }

    public boolean isFamily_sotry_lines() {
        return family_sotry_lines;
    }

    public void setFamily_sotry_lines(boolean family_sotry_lines) {
        this.family_sotry_lines = family_sotry_lines;
    }

    public boolean isSpouse_lines() {
        return spouse_lines;
    }

    public void setSpouse_lines(boolean spouse_lines) {
        this.spouse_lines = spouse_lines;
    }

    private static Settings instance = null;

    protected Settings() {
        // Exists only to defeat instantiation.
    }

    public static Settings getInstance() {
        if(instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public void loadFilterSettings(){
        if(filterSettings.size() == 0){
            for(String type : ClientModel.getInstance().eventTypes){
                filterSettings.put(type,true);
            }
        }
    }
    public void destryoTheData(){
        instance = null;
    }
}
