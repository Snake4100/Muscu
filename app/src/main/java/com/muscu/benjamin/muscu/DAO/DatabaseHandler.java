package com.muscu.benjamin.muscu.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by benjamin on 03/01/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    //Exercice
    public static final String EXERCICE_KEY = "id";
    public static final String EXERCICE_SEANCE = "seance";
    public static final String EXERCICE_TYPE_EXERCICE = "type_exercice";
    public static final String EXERCICE_NBSERIESSOUHAITES = "nb_series_souhaites";
    public static final String EXERCICE_TEMPSREPOS = "temps_repos";

    public static final String EXERCICE_TABLE_NAME = "Exercice";
    public static final String EXERCICE_TABLE_CREATE =
            "CREATE TABLE " + EXERCICE_TABLE_NAME + " (" +
                    EXERCICE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EXERCICE_SEANCE + " INTEGER, " +
                    EXERCICE_TYPE_EXERCICE + " INTEGER," +
                    EXERCICE_NBSERIESSOUHAITES + " INTEGER," +
                    EXERCICE_TEMPSREPOS+ " INTEGER," +
                    "FOREIGN KEY(EXERCICE_SEANCE) REFERENCES Seance(id)," +
                    "FOREIGN KEY(EXERCICE_TYPE_EXERCICE) REFERENCES TypeExercice(id));";

    //Serie
    public static final String SERIE_KEY = "id";
    public static final String SERIE_POIDS = "poids";
    public static final String SERIE_NB_REPETITIONS = "nb_repetitions";
    public static final String SERIE_TEMPS_TOTAL = "temps_total";
    public static final String SERIE_EXERCICE = "exercice";

    public static final String SERIE_TABLE_NAME = "Exercice";
    public static final String SERIE_TABLE_CREATE =
            "CREATE TABLE " + SERIE_TABLE_NAME + " (" +
                    SERIE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SERIE_POIDS + " INTEGER, " +
                    SERIE_NB_REPETITIONS + " INTEGER," +
                    SERIE_TEMPS_TOTAL + " INTEGER," +
                    SERIE_EXERCICE + " INTEGER," +
                    "FOREIGN KEY(SERIE_EXERCICE) REFERENCES Exercice(id));";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TypeExerciceDAO.TYPE_EXERCICE_TABLE_CREATE);
        db.execSQL(SeanceDAO.SEANCE_TABLE_CREATE);
        //db.execSQL(EXERCICE_TABLE_CREATE);
        //db.execSQL(SERIE_TABLE_CREATE);

        //on insére les exercices par défaults
        TypeExerciceDAO.defaultTypeExercice(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
        onCreate(db);
    }


}
