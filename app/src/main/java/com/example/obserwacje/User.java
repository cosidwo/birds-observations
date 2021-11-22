package com.example.obserwacje;

public class User {

    public String email, haslo, DisplayName;

    //constructors
    public User(String email,String haslo,String imie,String nazwisko){
        this.email=email;
        this.haslo=haslo;
        this.DisplayName = imie + " " + nazwisko;
    }

    public User(){

    }


    //GETTERS AND SETTERS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getDisplayName2() {
        return DisplayName;
    }

    public void setDisplayName2(String displayName) {
        DisplayName = displayName;
    }
}
