package com.muscu.benjamin.muscu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.muscu.benjamin.muscu.Entity.ExerciceTypeSeance;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;
import com.muscu.benjamin.muscu.Entity.TypeSeance;
import com.muscu.benjamin.muscu.Entity.TypeSeanceSerie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 15/01/2015.
 */
public class ExerciceTypeSeanceDAO extends DAOBase {
    private TypeExerciceDAO daoTypeExerice;
    private TypeSeanceSerieDAO daoTypeSeanceSerie;

    public static final String EXERCICETYPESEANCE_KEY = "id";
    public static final String EXERCICETYPESEANCE_NUMERO_EXERCICE = "numero_exercice";
    public static final String EXERCICETYPESEANCE_TYPE_EXERCICE = "type_exercice";
    public static final String EXERCICETYPESEANCE_TYPE_SEANCE = "type_seance";
    public static final String EXERCICETYPESEANCE_INDICATIONS = "indications";
    public static final String EXERCICETYPESEANCE_TEMPSREPOS = "temps_repos";

    public static final String EXERCICETYPESEANCE_TABLE_NAME = "ExerciceTypeSeance";
    public static final String EXERCICETYPESEANCE_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + EXERCICETYPESEANCE_TABLE_NAME + " (" +
                    EXERCICETYPESEANCE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EXERCICETYPESEANCE_NUMERO_EXERCICE + " INTEGER, " +
                    EXERCICETYPESEANCE_TYPE_EXERCICE + " INTEGER, " +
                    EXERCICETYPESEANCE_TYPE_SEANCE + " INTEGER, " +
                    EXERCICETYPESEANCE_INDICATIONS + " TEXT, " +
                    EXERCICETYPESEANCE_TEMPSREPOS + " INTEGER, " +
                    "FOREIGN KEY("+EXERCICETYPESEANCE_TYPE_EXERCICE+") REFERENCES "+TypeExerciceDAO.TYPE_EXERCICE_TABLE_NAME+"("+TypeExerciceDAO.TYPE_EXERCICE_KEY+") ON DELETE CASCADE, " +
                    "FOREIGN KEY("+EXERCICETYPESEANCE_TYPE_SEANCE+") REFERENCES "+TypeSeanceDAO.TYPESEANCE_TABLE_NAME+"("+TypeSeanceDAO.TYPESEANCE_KEY+") ON DELETE CASCADE);";

    public ExerciceTypeSeanceDAO(Context pContext) {
        super(pContext);

        this.daoTypeExerice = new TypeExerciceDAO(pContext);
        this.daoTypeExerice.open();

        this.daoTypeSeanceSerie = new TypeSeanceSerieDAO(pContext);
        this.daoTypeSeanceSerie.open();
    }

    public List<ExerciceTypeSeance> getExerciceFromTypeSeance(TypeSeance typeSeance){
        List<ExerciceTypeSeance> lesExercices = new ArrayList<ExerciceTypeSeance>();

        Cursor c = mDb.rawQuery("select "+EXERCICETYPESEANCE_KEY+", "+EXERCICETYPESEANCE_NUMERO_EXERCICE+", "+EXERCICETYPESEANCE_TYPE_EXERCICE+", "+EXERCICETYPESEANCE_TYPE_SEANCE+", "+EXERCICETYPESEANCE_INDICATIONS+", "+EXERCICETYPESEANCE_TEMPSREPOS+" "+
                "from "+EXERCICETYPESEANCE_TABLE_NAME+" " +
                "where "+EXERCICETYPESEANCE_TYPE_SEANCE+" = ? " +
                "order by "+EXERCICETYPESEANCE_NUMERO_EXERCICE,new String[]{String.valueOf(typeSeance.getId())});


        //on parcours la liste
        while(c.moveToNext()){
            //on crée le type exercice
            ExerciceTypeSeance exercice = new ExerciceTypeSeance(c.getLong(0), c.getLong(1), this.daoTypeExerice.selectionner(c.getLong(2)), typeSeance, c.getString(4), c.getInt(5));
            exercice.setListSeries(this.daoTypeSeanceSerie.getSerieFromExerciceTypeSeance(exercice));

            lesExercices.add(exercice);

        }

        return lesExercices;
    }

    public void create(ExerciceTypeSeance exercice){
        ContentValues value = new ContentValues();
        value.put(EXERCICETYPESEANCE_NUMERO_EXERCICE, exercice.getNumeroExercice());
        value.put(EXERCICETYPESEANCE_TYPE_EXERCICE, exercice.getTypeExercice().getId());
        value.put(EXERCICETYPESEANCE_TYPE_SEANCE, exercice.getTypeSeance().getId());
        value.put(EXERCICETYPESEANCE_INDICATIONS, exercice.getIndications());
        value.put(EXERCICETYPESEANCE_TEMPSREPOS, exercice.getTempsRepos());

        exercice.setId(mDb.insert(EXERCICETYPESEANCE_TABLE_NAME, null, value));

        this.persistSeries(exercice);

    }

    public void modifier(ExerciceTypeSeance exercice) {
        ContentValues value = new ContentValues();
        value.put(this.EXERCICETYPESEANCE_NUMERO_EXERCICE, exercice.getNumeroExercice());
        value.put(this.EXERCICETYPESEANCE_INDICATIONS, exercice.getIndications());
        value.put(this.EXERCICETYPESEANCE_TYPE_SEANCE, exercice.getTypeSeance().getId());
        value.put(this.EXERCICETYPESEANCE_TYPE_EXERCICE, exercice.getTypeExercice().getId());
        value.put(EXERCICETYPESEANCE_TEMPSREPOS, exercice.getTempsRepos());

        mDb.update(this.EXERCICETYPESEANCE_TABLE_NAME, value, this.EXERCICETYPESEANCE_KEY  + " = ?", new String[] {String.valueOf(exercice.getId())});

        for(TypeSeanceSerie serie : exercice.getListSeriesAsupp())
            this.daoTypeSeanceSerie.supprimer(serie.getId());

        this.persistSeries(exercice);
    }

    public void deleteList(ArrayList<ExerciceTypeSeance> listExerciceSupp) {
        for(ExerciceTypeSeance exerciceTypeSeance : listExerciceSupp){
            mDb.delete(this.EXERCICETYPESEANCE_TABLE_NAME, this.EXERCICETYPESEANCE_KEY + " = ?", new String[] {String.valueOf(exerciceTypeSeance.getId())});
        }
    }

    private void persistSeries(ExerciceTypeSeance exercice){
        for(TypeSeanceSerie serie : exercice.getListSeries()){
            //si la série n'existait pas en base
            if(serie.getId() == -1){
                this.daoTypeSeanceSerie.create(serie);
            }
            else{
                this.daoTypeSeanceSerie.modifier(serie);
            }
        }
    }

    public ExerciceTypeSeance selection(TypeSeance typeSeance, TypeExercice typeExercice) {

        if(typeSeance != null && typeExercice != null)
        {
            Cursor c = mDb.rawQuery("select "+EXERCICETYPESEANCE_KEY+", "+EXERCICETYPESEANCE_NUMERO_EXERCICE+", "+EXERCICETYPESEANCE_TYPE_EXERCICE+", "+EXERCICETYPESEANCE_TYPE_SEANCE+", "+EXERCICETYPESEANCE_INDICATIONS+", "+EXERCICETYPESEANCE_TEMPSREPOS+" "+
                    "from "+EXERCICETYPESEANCE_TABLE_NAME+" " +
                    "where "+EXERCICETYPESEANCE_TYPE_SEANCE+" = ? " +
                    "and "+EXERCICETYPESEANCE_TYPE_EXERCICE+" = ? ",new String[]{String.valueOf(typeSeance.getId()),String.valueOf(typeExercice.getId())});


            //s'il y a un résultat
            if(c.moveToNext()){
                //on crée le type exercice
                ExerciceTypeSeance exercice = new ExerciceTypeSeance(c.getLong(0), c.getLong(1), typeExercice, typeSeance, c.getString(4), c.getInt(5));
                exercice.setListSeries(this.daoTypeSeanceSerie.getSerieFromExerciceTypeSeance(exercice));

                return exercice;

            }
        }

        return null;
    }
}
