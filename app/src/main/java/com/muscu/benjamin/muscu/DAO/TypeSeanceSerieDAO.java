package com.muscu.benjamin.muscu.DAO;

import android.content.Context;

/**
 * Created by benjamin on 18/01/2015.
 */
public class TypeSeanceSerieDAO extends DAOBase {

    public static final String TYPESEANCESERIE_KEY = "id";
    public static final String TYPESEANCESERIE_NUMEROSERIE = "numero_serie";
    public static final String TYPESEANCESERIE_NBREPETITIONS = "nb_repetitions";
    public static final String TYPESEANCESERIE_EXERCICE = "exercice";

    public static final String TYPESEANCESERIE_TABLE_NAME = "TypeSeanceSerie";
    public static final String TYPESEANCESERIE_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TYPESEANCESERIE_TABLE_NAME + " (" +
                    TYPESEANCESERIE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TYPESEANCESERIE_NUMEROSERIE + " INTEGER " +
                    TYPESEANCESERIE_NBREPETITIONS + " INTEGER " +
                    TYPESEANCESERIE_EXERCICE + " INTEGER " +
                    "FOREIGN KEY("+TYPESEANCESERIE_EXERCICE+") REFERENCES "+ExerciceTypeSeanceDAO.EXERCICETYPESEANCE_TABLE_NAME+"("+ExerciceTypeSeanceDAO.EXERCICETYPESEANCE_KEY+") ON DELETE CASCADE " +
                    ");";

    public TypeSeanceSerieDAO(Context pContext) {
        super(pContext);
    }
}
