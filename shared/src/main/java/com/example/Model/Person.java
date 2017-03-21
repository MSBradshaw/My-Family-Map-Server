package com.example.Model;

/**
 * Person Model class
 * contains the information about a person
 * descendant refers to the userID of the person
 * mother father and spouse are the personId of their respective relation
 *
 */
public class Person {
    public String personID;
    public String descendant;
    public String firstname;
    public String lastname;
    public String gender;
    public String mother;
    public String father;
    public String spouse;
    public String[] ancestors;
    public String[] descendants;
    public String[] events;

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String[] getAncestors() {
        return ancestors;
    }

    public void setAncestors(String[] ancestors) {
        this.ancestors = ancestors;
    }

    public String[] getDescendants() {
        return descendants;
    }

    public void setDescendants(String[] descendants) {
        this.descendants = descendants;
    }

    public String[] getEvents() {
        return events;
    }

    public void setEvents(String[] events) {
        this.events = events;
    }

    /**
     * Recursively finds all ancestors of the person
     *
     * @return an array of all ancestors of the person
     */
    public String[] findAncestors(){
        return null;
    }

    /**
     * Recursively finds all descendants of the person
     *
     * @return an array of all descendants of the person
     */
    public String[] findDescendants(){
        return null;
    }

    /**
     *Finds all events connected to a person
     *
     * @return an array of all the events connected to a personID
     */
    public String[] findEvents(){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (descendant != null ? !descendant.equals(person.descendant) : person.descendant != null)
            return false;
        if (!firstname.equals(person.firstname)) return false;
        if (!lastname.equals(person.lastname)) return false;
        if (!gender.equals(person.gender)) return false;
        if (mother != null ? !mother.equals(person.mother) : person.mother != null) return false;
        if (father != null ? !father.equals(person.father) : person.father != null) return false;
        if (spouse != null ? !spouse.equals(person.spouse) : person.spouse != null) return false;
        return true;

    }

}
