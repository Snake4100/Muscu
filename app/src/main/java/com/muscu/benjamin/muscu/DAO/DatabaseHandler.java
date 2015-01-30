package com.muscu.benjamin.muscu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.muscu.benjamin.muscu.Entity.Categorie;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.TypeSeance;

/**
 * Created by benjamin on 03/01/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TypeExerciceDAO.TYPE_EXERCICE_TABLE_CREATE);
        db.execSQL(SeanceDAO.SEANCE_TABLE_CREATE);
        db.execSQL(ExerciceDAO.EXERCICE_TABLE_CREATE);
        db.execSQL(SerieDAO.SERIE_TABLE_CREATE);
        db.execSQL(TypeSeanceDAO.TYPESEANCE_TABLE_CREATE);
        db.execSQL(ExerciceTypeSeanceDAO.EXERCICETYPESEANCE_TABLE_CREATE);
        db.execSQL(TypeSeanceSerieDAO.TYPESEANCESERIE_TABLE_CREATE);

        //on insére les exercices par défaults
        TypeExerciceDAO.defaultTypeExercice(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        this.udpateDB(db);
    }

    private void udpateDB(SQLiteDatabase db){
        Log.e("debug", "udptate database");

        /*ContentValues value = new ContentValues();
        value.put(TypeExerciceDAO.TYPE_EXERCICE_CATEGORIE, Categorie.Repetition.toString());
        db.update(TypeExerciceDAO.TYPE_EXERCICE_TABLE_NAME, value, null, new String[]{});

        value = new ContentValues();
        value.put(TypeExerciceDAO.TYPE_EXERCICE_CATEGORIE, Categorie.Chronometre.toString());
        db.update(TypeExerciceDAO.TYPE_EXERCICE_TABLE_NAME, value, TypeExerciceDAO.TYPE_EXERCICE_NOM  + " = ? ", new String[]{"Gainage (planche)"});

        value = new ContentValues();
        value.put(TypeExerciceDAO.TYPE_EXERCICE_CATEGORIE, Categorie.Chronometre.toString());
        db.update(TypeExerciceDAO.TYPE_EXERCICE_TABLE_NAME, value, TypeExerciceDAO.TYPE_EXERCICE_NOM  + " = ? ", new String[]{"Course à pied"});
        */


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


}
