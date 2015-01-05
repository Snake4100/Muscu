package com.muscu.benjamin.muscu.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


}
