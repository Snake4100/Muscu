package com.muscu.benjamin.muscu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.muscu.benjamin.muscu.Entity.DateConversion;
import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 03/01/2015.
 */
public class ExerciceDAO extends DAOBase {
    public static final String EXERCICE_KEY = "id";
    public static final String EXERCICE_SEANCE = "seance";
    public static final String EXERCICE_TYPE_EXERCICE = "type_exercice";
    public static final String EXERCICE_TEMPSREPOS = "temps_repos";

    public static final String EXERCICE_TABLE_NAME = "Exercice";
    public static final String EXERCICE_TABLE_CREATE =
            "CREATE TABLE " + EXERCICE_TABLE_NAME + " (" +
                    EXERCICE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EXERCICE_SEANCE + " INTEGER, " +
                    EXERCICE_TYPE_EXERCICE + " INTEGER," +
                    EXERCICE_TEMPSREPOS+ " INTEGER," +
                    "FOREIGN KEY(seance) REFERENCES Seance(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(type_exercice) REFERENCES TypeExercice(id) ON DELETE CASCADE);";

    private TypeExerciceDAO daoTypeExercice;

    public ExerciceDAO(Context pContext) {
        super(pContext);

        this.daoTypeExercice = new TypeExerciceDAO(pContext);
        this.daoTypeExercice.open();
    }

    public List<Exercice> getSeanceExercices(Seance seance){
        List<Exercice> lesExercices = new ArrayList<Exercice>();
        Cursor c = mDb.rawQuery("select id,seance,type_exercice,temps_repos " +
                "from Exercice " +
                "where seance = ?",new String[]{String.valueOf(seance.getId())});

        //on parcours la liste
        while(c.moveToNext()){
            TypeExercice typeExercice = this.daoTypeExercice.selectionner(c.getLong(2));

            //on crée le type exercice
            lesExercices.add(new Exercice(c.getLong(0), seance, typeExercice,c.getInt(3)));
        }

        return lesExercices;
    }

    public Long create(Exercice exercice){
        ContentValues value = new ContentValues();
        value.put(ExerciceDAO.EXERCICE_SEANCE, exercice.getSeance().getId());
        value.put(ExerciceDAO.EXERCICE_TYPE_EXERCICE , exercice.getTypeExercice().getId());
        value.put(ExerciceDAO.EXERCICE_TEMPSREPOS, exercice.getTempsRepos());

        return mDb.insert(ExerciceDAO.EXERCICE_TABLE_NAME, null, value);
    }

    public void supprimer(long id) {
        mDb.delete(this.EXERCICE_TABLE_NAME, this.EXERCICE_KEY + " = ?", new String[] {String.valueOf(id)});
    }

    public void modifier(Exercice sonExercice) {
        ContentValues value = new ContentValues();
        value.put(ExerciceDAO.EXERCICE_SEANCE, sonExercice.getSeance().getId());
        value.put(ExerciceDAO.EXERCICE_TYPE_EXERCICE , sonExercice.getTypeExercice().getId());
        value.put(ExerciceDAO.EXERCICE_TEMPSREPOS, sonExercice.getTempsRepos());
        mDb.update(ExerciceDAO.EXERCICE_TABLE_NAME, value, this.EXERCICE_KEY  + " = ?", new String[] {String.valueOf(sonExercice.getId())});
    }
}
