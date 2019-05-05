package com.example.mybudget.activities;

public class Spending {


    public  String id;
    public  String libelle_aliment;
    public  String date_debut;
    public  String frequence;
    public  String duree;
    public  String cout;


    public Spending(String id,String libelle_aliment,String date_debut,String frequence,String duree,String cout) {
        this.id =  id;
        this.libelle_aliment = libelle_aliment;
        this.date_debut = date_debut;
        this.frequence = frequence;
        this.duree = duree;
        this.cout = cout;
    }

    public String getId() {
        return id;
    }

    public String getLibelle_aliment() {
        return libelle_aliment;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLibelle_aliment(String libelle_aliment) {
        this.libelle_aliment = libelle_aliment;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public void setCout(String cout) {
        this.cout = cout;
    }

    public String getDuree() {
        return duree;
    }

    public String getCout() {
        return cout;
    }
}
