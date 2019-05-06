package com.example.mybudget.activities;

public class Spending {


    public  String id;
    public String spending_type;
    public  String libelle_aliment;
    public  String date_debut;
    public  String date_fin;
    public  String cout;


    public Spending(String id,String spending_type,String libelle_aliment,String date_debut,String date_fin,String cout) {
        this.id =  id;
        this.spending_type = spending_type;
        this.libelle_aliment = libelle_aliment;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.cout = cout;
    }

    public String getId() {
        return id;
    }

    public String getSpending_type() {
        return spending_type;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public String getLibelle_aliment() {
        return libelle_aliment;
    }

    public String getDate_debut() {
        return date_debut;
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


    public void setCout(String cout) {
        this.cout = cout;
    }


    public String getCout() {
        return cout;
    }

    public void setSpending_type(String spending_type) {
        this.spending_type = spending_type;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }
}
