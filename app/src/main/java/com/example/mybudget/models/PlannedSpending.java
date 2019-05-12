package com.example.mybudget.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PlannedSpending implements Parcelable, Serializable {

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

    protected PlannedSpending(Parcel in) {
        id = in.readString();
        spending_type = in.readString();
        libelle_aliment = in.readString();
        date_debut = in.readString();
        date_fin = in.readString();
        frequence = in.readString();
        duree = in.readString();
        cout = in.readString();
    }

    public static final Creator<PlannedSpending> CREATOR = new Creator<PlannedSpending>() {
        @Override
        public PlannedSpending createFromParcel(Parcel in) {
            return new PlannedSpending(in);
        }

        @Override
        public PlannedSpending[] newArray(int size) {
            return new PlannedSpending[size];
        }
    };

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

    @Override
    public String toString() {
        return "PlannedSpending{" +
                "id='" + id + '\'' +
                ", spending_type='" + spending_type + '\'' +
                ", libelle_aliment='" + libelle_aliment + '\'' +
                ", date_debut='" + date_debut + '\'' +
                ", date_fin='" + date_fin + '\'' +
                ", frequence='" + frequence + '\'' +
                ", duree='" + duree + '\'' +
                ", cout='" + cout + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(spending_type);
        dest.writeString(libelle_aliment);
        dest.writeString(date_debut);
        dest.writeString(date_fin);
        dest.writeString(frequence);
        dest.writeString(duree);
        dest.writeString(cout);
    }
}
