package com.example.mybudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mybudget.activities.PlannedSpending;
import com.example.mybudget.activities.Spending;

import java.util.List;

public class MyBudgetDB extends SQLiteOpenHelper {

    //database name
    public static final String DB_NAME = "myBudgetDB";
    //Planning Spending table
    public static final String PLANNING_SPENDING_TABLE = "PlanningSpending";
    public static final String UNEXPECTED_SPENDING_TABLE = "UnexpectedSpending";
    public static final String PAST_SPENDING_TABLE = "PastSpending";
    public static final String ID = "id";
    public static final String SPENDING_TYPE = "spending_type";
    public static final String LIBELLE_ALIMENT = "libelle_aliment";
    public static final String DATE_DEBUT = "date_debut";
    public static final String DATE_FIN = "date_fin";
    public static final String FREQUENCE = "frequence";
    public static final String DUREE = "duree";
    public static final String COUT = "cout";


    public MyBudgetDB(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table " + PLANNING_SPENDING_TABLE +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, spending_type TEXT NOT NULL, " +
                "libelle_aliment TEXT NOT NULL, date_debut TEXT NOT NULL, date_fin TEXT NOT NULL, " +
                "frequence TEXT NOT NULL, duree INTEGER NOT NULL, cout FLOAT NOT NULL)");

        db.execSQL(" create table " + UNEXPECTED_SPENDING_TABLE +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, spending_type TEXT NOT NULL, " +
                "libelle_aliment TEXT NOT NULL, date_debut TEXT NOT NULL, date_fin TEXT NOT NULL, " +
                "cout FLOAT NOT NULL)");

        db.execSQL(" create table " + PAST_SPENDING_TABLE +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, spending_type TEXT NOT NULL, " +
                "libelle_aliment TEXT NOT NULL, date_debut TEXT NOT NULL, date_fin TEXT NOT NULL, " +
                "frequence TEXT NOT NULL, duree INTEGER NOT NULL, cout FLOAT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists " + PLANNING_SPENDING_TABLE);
        db.execSQL(" drop table if exists " + UNEXPECTED_SPENDING_TABLE);
        db.execSQL(" drop table if exists " + PAST_SPENDING_TABLE);
        onCreate(db);
    }

    public boolean insertSpending(String spending_type, String libelle_aliment, String date_debut, String frequence, int duree, float cout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPENDING_TYPE, spending_type);
        contentValues.put(LIBELLE_ALIMENT, libelle_aliment);
        contentValues.put(DATE_DEBUT, date_debut);
        //Calcul de la date de fin
        Cursor dateFin = null;
        if (frequence.equals("Journalier"))
            dateFin = db.rawQuery(" select date('" + date_debut + "','+" + duree + " day') ", null);
        if (frequence.equals("Hebdomadaire"))
            dateFin = db.rawQuery(" select date('" + date_debut + "','+" + duree * 7 + " day') ", null);
        if (frequence.equals("Mensuel"))
            dateFin = db.rawQuery(" select date('" + date_debut + "','+" + duree + " month') ", null);
        if (frequence.equals("Annuel"))
            dateFin = db.rawQuery(" select date('" + date_debut + "','+" + duree + " year') ", null);
        dateFin.moveToNext();
        //System.out.println("Date fin:"+dateFin.getString(0));
        contentValues.put(DATE_FIN, dateFin.getString(0));

        contentValues.put(FREQUENCE, frequence);
        contentValues.put(DUREE, duree);
        contentValues.put(COUT, cout);

        long result = db.insert(PLANNING_SPENDING_TABLE, null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    public boolean insertUnexpectedSpending(String spending_type, String libelle_aliment, String date_debut, float cout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPENDING_TYPE, spending_type);
        contentValues.put(LIBELLE_ALIMENT, libelle_aliment);
        contentValues.put(DATE_DEBUT, date_debut);
        contentValues.put(DATE_FIN, date_debut);
        contentValues.put(COUT, cout);
        long result = db.insert(UNEXPECTED_SPENDING_TABLE, null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    //    ### Met à jour lhistorique
    public void UpdateSpendingHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + PLANNING_SPENDING_TABLE + " order by libelle_aliment ", null);
        while (res.moveToNext()) {
            PlannedSpending spending = new PlannedSpending(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(7),
                    res.getString(5),
                    res.getString(6)
            );
            System.out.println("Date " + spending.getDate_fin());
        }
    }

    //Getters par défaut (Toutes les infos de la BD)
    public Cursor getSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + PLANNING_SPENDING_TABLE + " order by libelle_aliment ", null);
        return res;
    }


    //    ### Avoir la liste des dépenses imprévues
    public Cursor getUnexpectedSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + UNEXPECTED_SPENDING_TABLE + " order by libelle_aliment ", null);
        return res;
    }

}
