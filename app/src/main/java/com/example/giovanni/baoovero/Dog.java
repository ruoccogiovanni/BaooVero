package com.example.giovanni.baoovero;


public class Dog {

    private String Name;
    private String Breed ;
    private String Description ;
    private String Gender ;
    private String Tel ;
    private String Email ;
    private String City;
    private String Age ;
    private String Thumbnail ;
    private String utente;

    public Dog() { }

    public Dog(String name, String breed, String description, String gender, String city, String age, String tel, String email, String thumbnail) {
        Name = name;
        Breed = breed;
        Description = description;
        Gender = gender;
        City = city;
        Age = age;
        Tel = tel;
        Email = email;
        Thumbnail=thumbnail;

    }


    public String getName() {
        return Name;
    }

    public String getBreed() {
        return Breed;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getDescription() {
        return Description;
    }
    public String getGender() {
        return Gender;
    }
    public String getCity() {
        return City;
    }
    public String getAge() {
        return Age;
    }
    public String getTel() {
        return Tel;
    }
    public String getEmail() {
        return Email;
    }


    public String getThumbnail() {
        return Thumbnail;
    }


    public void setName(String name) {
        Name = name;
    }

    public void setBreed(String breed) {
        Breed = breed;
    }

    public void setDescription(String description) {
        Description = description;
    }
    public void setGender(String gender) {
        Gender = gender;
    }
    public void setTel(String tel) {
        Tel = tel;
    }
    public void setEmail(String email) {
        Email = email;
    }

    public void setCity(String city) {
        City = city;
    }
    public void setAge(String age) {
        Age = age;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }
}


