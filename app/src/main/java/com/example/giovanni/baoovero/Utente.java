package com.example.giovanni.baoovero;

import java.util.List;
import java.util.Map;

public class Utente {

    private Map<String,String> Preferiti;
    private String email;


    public Utente(){

  }

    public Utente(Map<String,String> preferiti, String email) {
        this.Preferiti = preferiti;
        this.email = email;
    }

    public Map<String,String> getPreferiti() {
        return Preferiti;
    }

    public void setPreferiti(Map<String,String> preferiti) {
        this.Preferiti = preferiti;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

