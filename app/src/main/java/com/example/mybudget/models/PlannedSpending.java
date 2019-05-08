package com.example.mybudget.models;


public class PlannedSpending{

    public  String id;
    public String spending_type;
    public  String libelle_aliment;
    public  String date_debut;
    public  String date_fin;
    public  String frequence;
    public  String duree;
    public  String cout;


    public PlannedSpending(String id, String spending_type, String libelle_aliment, String date_debut, String date_fin, String frequence, String duree, String cout) {
        this.id = id;
        this.spending_type = spending_type;
        this.libelle_aliment = libelle_aliment;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.frequence = frequence;
        this.duree = duree;
        this.cout = cout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpending_type() {
        return spending_type;
    }

    public void setSpending_type(String spending_type) {
        this.spending_type = spending_type;
    }

    public String getLibelle_aliment() {
        return libelle_aliment;
    }

    public void setLibelle_aliment(String libelle_aliment) {
        this.libelle_aliment = libelle_aliment;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getCout() {
        return cout;
    }

    public void setCout(String cout) {
        this.cout = cout;
    }
}
