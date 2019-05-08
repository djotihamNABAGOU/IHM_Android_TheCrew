package com.example.mybudget.models;

public class Spending {


    public  String id;
    public String spending_type;
    public  String libelle_aliment;
    public  String date;
    public  String cout;
    public boolean past;


    public Spending(String id,String spending_type,String libelle_aliment,String date,String cout, String past) {
        this.id =  id;
        this.spending_type = spending_type;
        this.libelle_aliment = libelle_aliment;
        this.date = date;
        this.cout = cout;
        this.past = Boolean.parseBoolean(past);
    }

    public String getId() {
        return id;
    }

    public String getSpending_type() {
        return spending_type;
    }

    public String getLibelle_aliment() {
        return libelle_aliment;
    }

    public String getDate() {
        return date;
    }

    public String getCout() {
        return cout;
    }

    public boolean isPast() {
        return past;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLibelle_aliment(String libelle_aliment) {
        this.libelle_aliment = libelle_aliment;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setCout(String cout) {
        this.cout = cout;
    }

    public void setSpending_type(String spending_type) {
        this.spending_type = spending_type;
    }

    public void setPast(boolean past) {
        this.past = past;
    }
}
