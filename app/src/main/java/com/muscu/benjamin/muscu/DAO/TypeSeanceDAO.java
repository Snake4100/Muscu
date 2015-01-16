package com.muscu.benjamin.muscu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.muscu.benjamin.muscu.Entity.ExerciceTypeSeance;
import com.muscu.benjamin.muscu.Entity.TypeSeance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 15/01/2015.
 */
public class TypeSeanceDAO extends DAOBase {
    private ExerciceTypeSeanceDAO daoExerciceTypeSeance;

    public static final String TYPESEANCE_KEY = "id";
    public static final String TYPESEANCE_NOM = "nom";

    public static final String TYPESEANCE_TABLE_NAME = "TypeSeance";
    public static final String TYPESEANCE_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TYPESEANCE_TABLE_NAME + " (" +
                    TYPESEANCE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TYPESEANCE_NOM + " TEXT " +
                    ");";


    public TypeSeanceDAO(Context pContext) {
        super(pContext);
        this.daoExerciceTypeSeance = new ExerciceTypeSeanceDAO(pContext);
        this.daoExerciceTypeSeance.open();
    }

    public long create(TypeSeance typeSeance){
        ContentValues value = new ContentValues();
        value.put(TYPESEANCE_NOM, typeSeance.getNom());

        //on execute la requete et on récupére l'id
        typeSeance.setId(mDb.insert(TYPESEANCE_TABLE_NAME, null, value));

        //on sauvegarde les exercices
        for(ExerciceTypeSeance exercice : typeSeance.getListExercices()){
            this.daoExerciceTypeSeance.create(exercice);
        }

        return typeSeance.getId();
    }

    public List<TypeSeance> getAll(){
        List<TypeSeance> lesSeances = new ArrayList<TypeSeance>();

        Cursor c = mDb.rawQuery("select "+TYPESEANCE_KEY+", "+TYPESEANCE_NOM+" from "+TYPESEANCE_TABLE_NAME,new String[]{});

        //on parcours la liste
        while(c.moveToNext()){
            //on crée le type seance
            lesSeances.add(new TypeSeance(c.getLong(0), c.getString(1)));
        }
        return lesSeances;
    }

    public void modifier(TypeSeance typeSeance) {
        ContentValues value = new ContentValues();
        value.put(this.TYPESEANCE_NOM, typeSeance.getNom());
        mDb.update(TYPESEANCE_TABLE_NAME, value, this.TYPESEANCE_KEY  + " = ?", new String[] {String.valueOf(typeSeance.getId())});
    }
}
