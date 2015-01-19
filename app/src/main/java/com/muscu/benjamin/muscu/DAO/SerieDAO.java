package com.muscu.benjamin.muscu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Serie;

import java.util.ArrayList;
import java.util.List;

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
            "CREATE TABLE IF NOT EXISTS " + SERIE_TABLE_NAME + " (" +
                    SERIE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SERIE_POIDS + " INTEGER, " +
                    SERIE_NB_REPETITIONS + " INTEGER," +
                    SERIE_TEMPS_TOTAL + " INTEGER," +
                    SERIE_EXERCICE + " INTEGER," +
                    "FOREIGN KEY(exercice) REFERENCES Exercice(id) ON DELETE CASCADE);";

    public SerieDAO(Context pContext) {
        super(pContext);
    }

    public void create(Serie serie){
        ContentValues value = new ContentValues();
        value.put(SerieDAO.SERIE_POIDS, serie.getPoids());
        value.put(SerieDAO.SERIE_NB_REPETITIONS, serie.getRepetitions());
        value.put(SerieDAO.SERIE_TEMPS_TOTAL, serie.getTempsTotal());
        value.put(SerieDAO.SERIE_EXERCICE, serie.getExercice().getId());

        serie.setId(mDb.insert(SerieDAO.SERIE_TABLE_NAME, null, value));
    }

    public List<Serie> getSeriesExercice(Exercice exercice){
        List<Serie> lesSeries = new ArrayList<Serie>();

        Cursor c = mDb.rawQuery("select id,poids,nb_repetitions,temps_total,exercice " +
                "from Serie " +
                "where exercice = ?",new String[]{String.valueOf(exercice.getId())});

        //on parcours la liste
        while(c.moveToNext()){

            //on crée le type exercice
            lesSeries.add(new Serie(c.getLong(0), c.getInt(1), c.getInt(2), c.getInt(3),exercice));
        }

        return lesSeries;
    }

    public void supprimer(long id) {
        mDb.delete(this.SERIE_TABLE_NAME, this.SERIE_KEY + " = ?", new String[] {String.valueOf(id)});
    }

    public void modifier(Serie serie) {
        ContentValues value = new ContentValues();
        value.put(SerieDAO.SERIE_POIDS, serie.getPoids());
        value.put(SerieDAO.SERIE_NB_REPETITIONS, serie.getRepetitions());
        value.put(SerieDAO.SERIE_TEMPS_TOTAL, serie.getTempsTotal());
        mDb.update(this.SERIE_TABLE_NAME, value, this.SERIE_KEY  + " = ?", new String[] {String.valueOf(serie.getId())});
    }
}
