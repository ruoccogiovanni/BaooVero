package com.example.giovanni.baoovero;

import java.util.List;
import java.util.Map;

public class Utente {

    private Map<String,String> Preferiti;
    private String email, nome, cognome;



    public Utente(){

  }

    public Utente(Map<String,String> preferiti, String email) {
        this.Preferiti = preferiti;
        this.email = email;
    }
    public Utente (String email, String nome, String cognome)
    {
        this.cognome=cognome;
        this.nome=nome;
        this.email=email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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

