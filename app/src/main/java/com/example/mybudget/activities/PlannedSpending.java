package com.example.mybudget.activities;


public class PlannedSpending  extends Spending{

    public  String frequence;
    public  String duree;


    public PlannedSpending(String id,String spending_type,String libelle_aliment,String date_debut,String date_fin,String cout, String frequence, String duree) {
        super(id,spending_type,libelle_aliment,date_debut,date_fin,cout);
        this.frequence = frequence;
        this.duree = duree;
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
}
