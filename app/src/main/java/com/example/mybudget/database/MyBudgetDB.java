package com.example.mybudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import com.example.mybudget.models.PlannedSpending;
import com.example.mybudget.models.Spending;

import java.util.ArrayList;
import java.util.Date;

public class MyBudgetDB extends SQLiteOpenHelper {

    //database name
    public static final String DB_NAME = "myBudgetDB";

    //table names
    public static final String PLANNING_SPENDING_TABLE = "PlanningSpending";
    public static final String UNEXPECTED_SPENDING_TABLE = "UnexpectedSpending";
    public static final String PAST_PLANNING_SPENDING_TABLE = "PastPlanningSpending";
    public static final String SPENDING_TABLE = "Spending";
    public static final String REVENU = "revenu";

    public ArrayList<String> notificationsList;

    //Data
    public static final String ID = "id";
    public static final String SPENDING_TYPE = "spending_type";
    public static final String LIBELLE_ALIMENT = "libelle_aliment";
    public static final String DATE_DEBUT = "date_debut";
    public static final String DATE_FIN = "date_fin";
    public static final String FREQUENCE = "frequence";
    public static final String DUREE = "duree";
    public static final String COUT = "cout";

    public static final String PAST = "past";


    public MyBudgetDB(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table " + PLANNING_SPENDING_TABLE +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, spending_type TEXT NOT NULL, " +
                "libelle_aliment TEXT NOT NULL, date_debut TEXT NOT NULL, date_fin TEXT NOT NULL, " +
                "frequence TEXT NOT NULL, duree INTEGER NOT NULL, cout FLOAT NOT NULL)");

        /*db.execSQL(" create table " + UNEXPECTED_SPENDING_TABLE +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, spending_type TEXT NOT NULL, " +
                "libelle_aliment TEXT NOT NULL, date_debut TEXT NOT NULL, date_fin TEXT NOT NULL, " +
                "cout FLOAT NOT NULL)");*/

        db.execSQL(" create table " + PAST_PLANNING_SPENDING_TABLE +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, spending_type TEXT NOT NULL, " +
                "libelle_aliment TEXT NOT NULL, date_debut TEXT NOT NULL, date_fin TEXT NOT NULL, " +
                "frequence TEXT NOT NULL, duree INTEGER NOT NULL, cout FLOAT NOT NULL)");

        db.execSQL(" create table " + SPENDING_TABLE +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, spending_type TEXT NOT NULL, " +
                "libelle_aliment TEXT NOT NULL, date TEXT NOT NULL, cout FLOAT NOT NULL, past BOOLEAN NOT NULL)");

        db.execSQL(" create table " + REVENU +
                " ( id INTEGER PRIMARY KEY AUTOINCREMENT, mois INTEGER NOT NULL, " +
                "annee INTEGER NOT NULL, revenu FLOAT NOT NULL, epargne FLOAT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists " + PLANNING_SPENDING_TABLE);
        //db.execSQL(" drop table if exists " + UNEXPECTED_SPENDING_TABLE);
        db.execSQL(" drop table if exists " + PAST_PLANNING_SPENDING_TABLE);
        db.execSQL(" drop table if exists " + SPENDING_TABLE);
        db.execSQL(" drop table if exists " + REVENU);
        onCreate(db);
    }


    public ContentValues getContentSpending(String spending_type, String libelle_aliment, String date_debut, String frequence, int duree, float cout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPENDING_TYPE, spending_type);
        contentValues.put(LIBELLE_ALIMENT, libelle_aliment);
        contentValues.put(DATE_DEBUT, date_debut);
        contentValues.put(FREQUENCE, frequence);
        contentValues.put(DUREE, duree);
        contentValues.put(COUT, cout);
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
        return contentValues;
    }

    /***********************************************************************/

    // Table PLANNING_SPENDING_TABLE
    // Insertion
    public boolean insertPlanningSpending(String spending_type, String libelle_aliment, String date_debut, String frequence, int duree, float cout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = this.getContentSpending(spending_type, libelle_aliment, date_debut, frequence, duree, cout);
        long result = db.insert(PLANNING_SPENDING_TABLE, null, contentValues);
        if (result == -1) return false;
        else {
            //On insère les futures dépenses avec l'attribut past à false pour dire que c'est à faire
            //Il faut insérer selon la fréquence et la date de début
            //1- enregistrer le premier jour où la dépense commence
            insertSpending(spending_type, libelle_aliment, date_debut, cout, false);
            //2-enregistrer les autres jours de dépense selon la fréquence définie
            for (int i = 0; i < duree - 1; i++) {
                Cursor date = null;
                if (frequence.equals("Journalier"))
                    date = db.rawQuery(" select date('" + date_debut + "','+" + 1 + " day') ", null);
                if (frequence.equals("Hebdomadaire"))
                    date = db.rawQuery(" select date('" + date_debut + "','+" + 7 + " day') ", null);
                if (frequence.equals("Mensuel"))
                    date = db.rawQuery(" select date('" + date_debut + "','+" + 1 + " month') ", null);
                if (frequence.equals("Annuel"))
                    date = db.rawQuery(" select date('" + date_debut + "','+" + 1 + " year') ", null);
                date.moveToNext();
                date_debut = date.getString(0);
                insertSpending(spending_type, libelle_aliment, date_debut, cout, false);
            }
            return true;
        }
    }

    //    ### Avoir la liste des dépenses planifiées en cours
    public Cursor getCurrentPlanningSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + PLANNING_SPENDING_TABLE + " order by libelle_aliment ", null);
        return res;
    }

    // Avoir les mois des dépenses planifiées
    public Cursor getCurrentMonthsPlanningSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select DISTINCT strftime('%m', date_debut) as month, strftime('%Y', date_debut) as year from " + PLANNING_SPENDING_TABLE + " order by year, month ", null);
        return res;
    }

    public Cursor getPlanningSpendingOfMonth(String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + PLANNING_SPENDING_TABLE + " where cast(strftime('%m', date_debut) as integer) = " + month + " order by libelle_aliment ", null);
        return res;
    }

    /***********************************************************************/
    // Table PAST_PLANNING_SPENDING_TABLE
    //Insertion
    public boolean insertPastPlanningSpending(String spending_type, String libelle_aliment, String date_debut, String date_fin, String frequence, int duree, float cout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPENDING_TYPE, spending_type);
        contentValues.put(LIBELLE_ALIMENT, libelle_aliment);
        contentValues.put(DATE_DEBUT, date_debut);
        contentValues.put(DATE_FIN, date_fin);
        contentValues.put(FREQUENCE, frequence);
        contentValues.put(DUREE, duree);
        contentValues.put(COUT, cout);
        long result = db.insert(PAST_PLANNING_SPENDING_TABLE, null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    //   ### Avoir la liste des dépenses planifiées & achevées
    public Cursor getPastPlanningSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + PAST_PLANNING_SPENDING_TABLE + " order by libelle_aliment ", null);
        return res;
    }

    //    ### Met à jour l'historique

    public void UpdateSpendingHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + PLANNING_SPENDING_TABLE + " order by libelle_aliment ", null);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        while (res.moveToNext()) {

            PlannedSpending spending = new PlannedSpending(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getString(6),
                    res.getString(7)
            );


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(formattedDate);
                Date date2 = sdf.parse(spending.getDate_fin());
                System.out.println("date 1 comp:" + date1);
                System.out.println("date 2 comp:" + date2);

                if (date2.before(date1)) {
//                    ### On n'en a plus besoin
//                    insertPastPlanningSpending(
//                            spending.getSpending_type(),
//                            spending.getLibelle_aliment(),
//                            spending.getDate_debut(),
//                            spending.getDate_fin(),
//                            spending.getFrequence(),
//                            Integer.valueOf(spending.getDuree()),
//                            Float.valueOf(spending.getCout())
//                    );

                    db.execSQL(" delete from " + PLANNING_SPENDING_TABLE +
                            "  WHERE id = " + spending.getId());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//        ### Mettre à jour la table des dépenses
        Cursor resSp = db.rawQuery(" select * from " + SPENDING_TABLE + " order by libelle_aliment ", null);
        while (resSp.moveToNext()) {
            Spending spending = new Spending(
                    resSp.getString(0),
                    resSp.getString(1),
                    resSp.getString(2),
                    resSp.getString(3),
                    resSp.getString(4),
                    resSp.getString(5)
            );
//            System.out.println(spending.toString());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(formattedDate);
                Date date2 = sdf.parse(spending.getDate());

                if (date2.before(date1)) {
                    db.execSQL(" UPDATE " + SPENDING_TABLE + " SET past = " + 1 +
                            "  WHERE id = " + spending.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public boolean notifications(){
        boolean rep = false;
        notificationsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " order by libelle_aliment ", null);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        Cursor resSp = db.rawQuery(" select * from " + SPENDING_TABLE + " order by libelle_aliment ", null);
        while (resSp.moveToNext()) {
            Spending spending = new Spending(
                    resSp.getString(0),
                    resSp.getString(1),
                    resSp.getString(2),
                    resSp.getString(3),
                    resSp.getString(4),
                    resSp.getString(5)
            );
//            System.out.println(spending.toString());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(formattedDate);
                Date date2 = sdf.parse(spending.getDate());

                if ((date1.compareTo(date2) == 0)){
                    notificationsList.add("Dépense : Achat de "+spending.getLibelle_aliment()+" , coût : "+spending.getCout()+"€ a été prélevée avec succès.");
                    rep = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rep;
    }

    public ArrayList<String> getNotificationsList(){
      return notificationsList;
    }

    /***********************************************************************/
    // Table SPENDING_TABLE pour les dépenses éffectuées
    //Insertion
    public boolean insertSpending(String spending_type, String libelle_aliment, String date, float cout, boolean past) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPENDING_TYPE, spending_type);
        contentValues.put(LIBELLE_ALIMENT, libelle_aliment);
        contentValues.put("date", date);
        contentValues.put(COUT, cout);
        contentValues.put(PAST, past);
        long result = db.insert(SPENDING_TABLE, null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    //   ### Avoir la liste des dépenses achevées
    public Cursor getSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " WHERE past =" + 1 + " order by date ASC ", null);
        return res;
    }

    public Cursor getSpendingByDateDesc() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " WHERE past =" + 1 +" order by date DESC ", null);
        return res;
    }

    public Cursor getSpendingByPrice() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " WHERE past =" + 1 +" order by cout ASC ", null);
        return res;
    }

    public Cursor getSpendingByPriceDesc() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " WHERE past =" + 1 +" order by cout DESC ", null);
        return res;
    }




    public Cursor getSpendingOfMonth(String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " where cast(strftime('%m', date) as integer) = " + month + " and past = 1 order by date ASC ", null);
        return res;
    }

    public Cursor getSpendingOfMonthDateDesc(String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " where cast(strftime('%m', date) as integer) = " + month + " and past = 1 order by date DESC ", null);
        return res;
    }

    public Cursor getSpendingOfMonthPrice(String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " where cast(strftime('%m', date) as integer) = " + month + " and past = 1 order by cout ASC ", null);
        return res;
    }

    public Cursor getSpendingOfMonthPriceDesc(String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + SPENDING_TABLE + " where cast(strftime('%m', date) as integer) = " + month + " and past = 1 order by cout DESC ", null);
        return res;
    }

    // Avoir les mois des dépenses effectuées
    public Cursor getCurrentMonthsSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select DISTINCT strftime('%m', date) as month, strftime('%Y', date) as year from " + SPENDING_TABLE + " where past = 1 order by year, month ", null);
        return res;
    }

    public Cursor getSumSpendingOfMonth(String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select sum(cout) as cout_total from " + SPENDING_TABLE + " where cast(strftime('%m', date) as integer) = " + month + " and past = 1 order by libelle_aliment ", null);
        return res;
    }

    // Table REVENU
    //Insertion
    public boolean insertRevenu(int mois, int annee, float revenu, float epargne) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mois", mois);
        contentValues.put("annee", annee);
        contentValues.put("revenu", revenu);
        contentValues.put("epargne", epargne);
        long result = db.insert(REVENU, null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    public Cursor getRevenueMonths() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select DISTINCT mois, annee from " + REVENU + " order by annee, mois ", null);
        return res;
    }

    public Cursor getRevenuesOfMonth(int month) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + REVENU + " where mois = " + month + " order by annee, mois ASC ", null);
        return res;
    }

    public boolean updateRevenu(int mois, int annee, float revenu, float epargne) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mois", mois);
        contentValues.put("annee", annee);
        contentValues.put("revenu", revenu);
        contentValues.put("epargne", epargne);
        long result = db.update(REVENU, contentValues, "mois = ? and annee = ?", new String[]{Integer.toString(mois), Integer.toString(annee)});
        if (result == -1) return false;
        else return true;
        /*db.execSQL("update "+REVENU+" set revenu = "+ revenu+", epargne = "+epargne+" where mois = "+mois+" and annee = "+annee);
        //db.update()
        //Cursor res = db.rawQuery(" select * from " + REVENU + " where mois = " + month + " order by annee, mois ASC ", null);
        //return res;
        return true;*/
    }

    /* Table UNEXPECTED_SPENDING_TABLE
    //Insertion
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


    //    ### Avoir la liste des dépenses imprévues
    public Cursor getUnexpectedSpending() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from " + UNEXPECTED_SPENDING_TABLE + " order by libelle_aliment ", null);
        return res;
    }*/

}
