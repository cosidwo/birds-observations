package com.example.obserwacje;

public class ObservationItem {
    String date, species, number, name, email;

    //constructors
    ObservationItem(String date,String species,String number,String name,String email){
        this.date = date;
        this.species = species;
        this.number = number;
        this.name = name;
        this.email = email;
    }

    ObservationItem(){

    }


    //GETTERS AND SETTERS

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
