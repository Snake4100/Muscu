package com.muscu.benjamin.muscu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.muscu.benjamin.muscu.Entity.ExerciceTypeSeance;
import com.muscu.benjamin.muscu.Entity.TypeSeanceSerie;

import java.util.ArrayList;
import java.util.List;

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
                    TYPESEANCESERIE_NUMEROSERIE + " INTEGER, " +
                    TYPESEANCESERIE_NBREPETITIONS + " INTEGER, " +
                    TYPESEANCESERIE_EXERCICE + " INTEGER, " +
                    "FOREIGN KEY("+TYPESEANCESERIE_EXERCICE+") REFERENCES "+ExerciceTypeSeanceDAO.EXERCICETYPESEANCE_TABLE_NAME+"("+ExerciceTypeSeanceDAO.EXERCICETYPESEANCE_KEY+") ON DELETE CASCADE " +
                    ");";

    public TypeSeanceSerieDAO(Context pContext) {
        super(pContext);
    }

    public List<TypeSeanceSerie> getSerieFromExerciceTypeSeance(ExerciceTypeSeance exercice){
        List<TypeSeanceSerie> lesSeries = new ArrayList<TypeSeanceSerie>();

        Cursor c = mDb.rawQuery("select "+TYPESEANCESERIE_KEY+", "+TYPESEANCESERIE_NUMEROSERIE+", "+TYPESEANCESERIE_NBREPETITIONS+", "+TYPESEANCESERIE_EXERCICE+" "+
                "from "+TYPESEANCESERIE_TABLE_NAME+" " +
                "where "+TYPESEANCESERIE_EXERCICE+" = ? " +
                "order by "+TYPESEANCESERIE_NUMEROSERIE,new String[]{String.valueOf(exercice.getId())});


        //on parcours la liste
        while(c.moveToNext()){
            //on crée le type exercice
            lesSeries.add(new TypeSeanceSerie(c.getLong(0), c.getLong(1), c.getLong(2), exercice));
        }

        return lesSeries;
    }

    public long create(TypeSeanceSerie serie){
        ContentValues value = new ContentValues();
        value.put(this.TYPESEANCESERIE_NUMEROSERIE, serie.getNumeroSerie());
        value.put(this.TYPESEANCESERIE_NBREPETITIONS, serie.getNbRepetition());
        value.put(this.TYPESEANCESERIE_EXERCICE, serie.getExercice().getId());

        return mDb.insert(TYPESEANCESERIE_TABLE_NAME, null, value);
    }

    public void modifier(TypeSeanceSerie serie) {
        ContentValues value = new ContentValues();
        value.put(this.TYPESEANCESERIE_NUMEROSERIE, serie.getNumeroSerie());
        value.put(this.TYPESEANCESERIE_NBREPETITIONS, serie.getNbRepetition());
        value.put(this.TYPESEANCESERIE_EXERCICE, serie.getExercice().getId());

        mDb.update(this.TYPESEANCESERIE_TABLE_NAME, value, this.TYPESEANCESERIE_KEY  + " = ?", new String[] {String.valueOf(serie.getId())});
    }
}
