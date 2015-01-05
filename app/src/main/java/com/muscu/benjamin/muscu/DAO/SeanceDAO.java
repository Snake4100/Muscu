package com.muscu.benjamin.muscu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.muscu.benjamin.muscu.Entity.DateConversion;
import com.muscu.benjamin.muscu.Entity.Seance;

import java.util.ArrayList;
import java.util.Date;
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

    public Seance create(){
        Seance seance = new Seance(new Date());

        ContentValues value = new ContentValues();
        value.put(SeanceDAO.SEANCE_DATE, DateConversion.dateToString(seance.getDate()));
        value.put(SeanceDAO.SEANCE_NOM, seance.getNom());
        value.put(SeanceDAO.SEANCE_CLOSE,seance.isClose());
        seance.setId(mDb.insert(SeanceDAO.SEANCE_TABLE_NAME, null, value));

        return seance;
    }

    public Seance selectionner(Long id){
        Cursor c = mDb.rawQuery("select id,date,nom,close " +
                "from Seance " +
                "where id = ?",new String[]{String.valueOf(id)});

        //si on a un résultat
        if(c.moveToFirst())
        {
            return new Seance(c.getLong(0), DateConversion.stringToDate(c.getString(1)), c.getString(2), Boolean.valueOf(c.getString(3)));
        }

        return null;
    }

    public List<Seance> getAll(){
        List<Seance> lesSeances = new ArrayList<Seance>();

        Cursor c = mDb.rawQuery("select id,date,nom,close from Seance",new String[]{});

        //on parcours la liste
        while(c.moveToNext()){
            //on crée le type exercice
            //Log.e("debug","close string : "+c.getString(3)+". Close boolean : "+Boolean.valueOf(c.getString(3)));
            lesSeances.add(new Seance(c.getLong(0), DateConversion.stringToDate(c.getString(1)), c.getString(2), Boolean.valueOf(c.getString(3))));
        }
        return lesSeances;
    }

    public void supprimer(long id) {
        mDb.delete(this.SEANCE_TABLE_NAME, this.SEANCE_KEY + " = ?", new String[] {String.valueOf(id)});
    }
}
