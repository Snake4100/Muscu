package com.muscu.benjamin.muscu.DAO;

import android.content.Context;

/**
 * Created by benjamin on 03/01/2015.
 */
public class SerieDAO extends DAOBase {
    public static final String SERIE_KEY = "id";
    public static final String SERIE_POIDS = "poids";
    public static final String SERIE_NB_REPETITIONS = "nb_repetitions";
    public static final String SERIE_TEMPS_TOTAL = "temps_total";
    public static final String SERIE_EXERCICE = "exercice";

    public static final String SERIE_TABLE_NAME = "Serie";
    public static final String SERIE_TABLE_CREATE =
            "CREATE TABLE " + SERIE_TABLE_NAME + " (" +
                    SERIE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SERIE_POIDS + " INTEGER, " +
                    SERIE_NB_REPETITIONS + " INTEGER," +
                    SERIE_TEMPS_TOTAL + " INTEGER," +
                    SERIE_EXERCICE + " INTEGER," +
                    "FOREIGN KEY(exercice) REFERENCES Exercice(id));";

    public SerieDAO(Context pContext) {
        super(pContext);
    }

}
