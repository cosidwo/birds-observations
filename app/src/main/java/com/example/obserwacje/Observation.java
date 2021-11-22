package com.example.obserwacje;

public class Observation {
    String email, species, date, category, criterium, accuracy, name, habitat, additional_info, place, county, latLng;
    int number, rare;

    //constructors
    Observation(String email, String species, String date, String category, String criterium, String accuracy, String name, String habitat, String additional_info, String place, String county, String latLng, int number, int rare){
        this.email = email;
        this.species = species;
        this.date = date;
        this.category = category;
        this.criterium = criterium;
        this.accuracy = accuracy;
        this.name = name;
        this.habitat = habitat;
        this.additional_info = additional_info;
        this.place = place;
        this.county = county;
        this.latLng = latLng;
        this.number = number;
        this.rare = rare;
    }

    Observation(){

    }


    // GETTERS AND SETTERS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCriterium() {
        return criterium;
    }

    public void setCriterium(String criterium) {
        this.criterium = criterium;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRare() {
        return rare;
    }

    public void setRare(int rare) {
        this.rare = rare;
    }
}
