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
    private TypeSeanceDAO daoTypeSeance;

    //Seance
    public static final String SEANCE_KEY = "id";
    public static final String SEANCE_DATE = "date";
    public static final String SEANCE_NOM = "nom";
    public static final String SEANCE_CLOSE = "close";
    public static final String SEANCE_TYPESEANCE = "type_seance";

    public static final String SEANCE_TABLE_NAME = "Seance";
    public static final String SEANCE_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + SEANCE_TABLE_NAME + " (" +
                    SEANCE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SEANCE_DATE + " TEXT, " +
                    SEANCE_NOM + " TEXT," +
                    SEANCE_CLOSE + " INTEGER," +
                    SEANCE_TYPESEANCE +" INTEGER" +
                    "FOREIGN KEY("+SEANCE_TYPESEANCE+") REFERENCES "+TypeSeanceDAO.TYPESEANCE_TABLE_NAME+"("+TypeSeanceDAO.TYPESEANCE_KEY+") ON DELETE SET NULL);";

    public SeanceDAO(Context pContext) {
        super(pContext);

        this.daoTypeSeance = new TypeSeanceDAO(pContext);
        this.daoTypeSeance.open();
    }

    public Seance create(){
        Seance seance = new Seance(new Date());

        ContentValues value = new ContentValues();
        value.put(SeanceDAO.SEANCE_DATE, DateConversion.dateToString(seance.getDate()));
        value.put(SeanceDAO.SEANCE_NOM, seance.getNom());
        value.put(SeanceDAO.SEANCE_CLOSE,seance.isClose());
        if(seance.getTypeSeance() != null)
            value.put(SeanceDAO.SEANCE_TYPESEANCE,seance.getTypeSeance().getId());
        seance.setId(mDb.insert(SeanceDAO.SEANCE_TABLE_NAME, null, value));

        return seance;
    }

    public Seance selectionner(Long id){
        Cursor c = mDb.rawQuery("select "+this.SEANCE_KEY+","+this.SEANCE_DATE+","+this.SEANCE_NOM+","+this.SEANCE_CLOSE+","+this.SEANCE_TYPESEANCE+" " +
                "from "+this.SEANCE_TABLE_NAME+" " +
                "where id = ?",new String[]{String.valueOf(id)});
        //si on a un résultat
        if(c.moveToFirst())
        {
            boolean isClose = false;
            if (c.getString(3).equals("1"))
                isClose = true;

            return new Seance(c.getLong(0), DateConversion.stringToDate(c.getString(1)), c.getString(2), isClose, this.daoTypeSeance.selectionner(c.getLong(4)));
        }

        return null;
    }

    public void mofidier (Seance laSeance){
        ContentValues value = new ContentValues();
        value.put(this.SEANCE_DATE, DateConversion.dateToString(laSeance.getDate()));
        value.put(this.SEANCE_NOM, laSeance.getNom());
        value.put(this.SEANCE_CLOSE, laSeance.isClose());
        if(laSeance.getTypeSeance() != null)
            value.put(SeanceDAO.SEANCE_TYPESEANCE,laSeance.getTypeSeance().getId());
        mDb.update(this.SEANCE_TABLE_NAME, value, this.SEANCE_KEY  + " = ?", new String[] {String.valueOf(laSeance.getId())});
    }

    public List<Seance> getAll(){
        List<Seance> lesSeances = new ArrayList<Seance>();

        Cursor c = mDb.rawQuery("select "+this.SEANCE_KEY+","+this.SEANCE_DATE+","+this.SEANCE_NOM+","+this.SEANCE_CLOSE+","+this.SEANCE_TYPESEANCE+" " +
                "from "+this.SEANCE_TABLE_NAME,new String[]{});

        //on parcours la liste
        while(c.moveToNext()){
            //on crée le type exercice
            boolean isClose = false;
            if (c.getString(3).equals("1"))
                isClose = true;

            lesSeances.add(new Seance(c.getLong(0), DateConversion.stringToDate(c.getString(1)), c.getString(2), isClose, this.daoTypeSeance.selectionner(c.getLong(4))));
        }
        return lesSeances;
    }

    public void supprimer(long id) {
        mDb.delete(this.SEANCE_TABLE_NAME, this.SEANCE_KEY + " = ?", new String[] {String.valueOf(id)});
    }
}
