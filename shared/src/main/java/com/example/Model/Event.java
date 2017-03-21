package com.example.Model;
/**
 * Event Model class
 * contains the information about a person
 * descendant is the userId the Person of the event is connected to
 * personID is the Person the event is about
 *
 */
public class Event {
    public String description;
    public String personID;
    public String city;
    public String country;
    public String latitude;
    public String longitude;
    public String year;
    public String descendant;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String eventID;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (!description.equals(event.description)) return false;
        if (!personID.equals(event.personID)) return false;
        if (!city.equals(event.city)) return false;
        if (!country.equals(event.country)) return false;
        if (!latitude.equals(event.latitude)) return false;
        if (!longitude.equals(event.longitude)) return false;
        if (!year.equals(event.year)) return false;
        if (!descendant.equals(event.descendant)) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + personID.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + country.hashCode();
        result = 31 * result + latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + descendant.hashCode();
        result = 31 * result + eventID.hashCode();
        return result;
    }
}
