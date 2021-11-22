package com.example.obserwacje.entities;

public class Image {

    private String email, imageURI, species, name, place, county, date;

    //constructors
    public Image(String email, String imageURI, String species, String name, String place, String county, String date){
        this.email = email;
        this.imageURI = imageURI;
        this.species = species;
        this.name = name;
        this.place = place;
        this.county = county;
        this.date = date;
    }

    public Image(){
        this.email="";
        this.imageURI="";
    }

    //Getters and setters below

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
