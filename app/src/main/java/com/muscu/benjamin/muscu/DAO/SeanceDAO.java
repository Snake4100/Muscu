package com.muscu.benjamin.muscu.DAO;

import android.content.Context;
import android.database.Cursor;

import com.muscu.benjamin.muscu.Entity.DateConversion;
import com.muscu.benjamin.muscu.Entity.Seance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 03/01/2015.
 */
public class SeanceDAO extends DAOBase{
    //Seance
    public static final String SEANCE_KEY = "id";
    public static final String SEANCE_DATE = "date";
    public static final String SEANCE_NOM = "nom";
    public static final String SEANCE_CLOSE = "close";

    public static final String SEANCE_TABLE_NAME = "Seance";
    public static final String SEANCE_TABLE_CREATE =
            "CREATE TABLE " + SEANCE_TABLE_NAME + " (" +
                    SEANCE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SEANCE_DATE + " TEXT, " +
                    SEANCE_NOM + " TEXT," +
                    SEANCE_CLOSE + " INTEGER);";

    public SeanceDAO(Context pContext) {
        super(pContext);
    }

    public List<Seance> getAll(){
        List<Seance> lesSeances = new ArrayList<Seance>();

        Cursor c = mDb.rawQuery("select id,date,nom,close from Seance",new String[]{});

        //on parcours la liste
        while(c.moveToNext()){
            //on crée le type exercice
            lesSeances.add(new Seance(c.getLong(0), DateConversion.stringToDate(c.getString(1)), c.getString(2), Boolean.valueOf(c.getString(3))));
        }
        return lesSeances;
    }
}
