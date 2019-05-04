package com.example.mybudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyBudgetDB extends SQLiteOpenHelper {

    //database name
    public static final String DB_NAME = "myBudgetDB";
    //Planning Spending table
    public static final String PLANNING_SPENDING_TABLE = "PlanningSpending";
    public static final String ID = "id";
    public static final String LIBELLE_ALIMENT = "libelle_aliment";
    public static final String DATE_DEBUT = "date_debut";
    public static final String FREQUENCE = "frequence";
    public static final String DUREE = "duree";
    public static final String COUT = "cout";


    public MyBudgetDB(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table " + PLANNING_SPENDING_TABLE +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "libelle_aliment TEXT NOT NULL, date_debut TEXT NOT NULL, " +
                "frequence TEXT NOT NULL, duree INTEGER NOT NULL, cout FLOAT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists " + PLANNING_SPENDING_TABLE);
        onCreate(db);
    }

    public boolean insertSpending(String libelle_aliment, String date_debut, String frequence, int duree, float cout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, 0);
        contentValues.put(LIBELLE_ALIMENT, libelle_aliment);
        contentValues.put(DATE_DEBUT, date_debut);
        contentValues.put(FREQUENCE, frequence);
        contentValues.put(DUREE, duree);
        contentValues.put(COUT, cout);

        long result = db.insert(PLANNING_SPENDING_TABLE, null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    //Getters par défaut (Toutes les infos de la BD)
    public Cursor getSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + PLANNING_SPENDING_TABLE + " order by libelle_aliment ", null);
        return res;
    }
}
