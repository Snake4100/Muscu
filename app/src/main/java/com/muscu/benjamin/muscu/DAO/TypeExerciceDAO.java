package com.muscu.benjamin.muscu.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.muscu.benjamin.muscu.Entity.Categorie;
import com.muscu.benjamin.muscu.Entity.TypeExercice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 03/01/2015.
 */
public class TypeExerciceDAO extends DAOBase {
    public static final String TYPE_EXERCICE_KEY = "id";
    public static final String TYPE_EXERCICE_NOM = "nom";
    public static final String TYPE_EXERCICE_CATEGORIE = "categorie";
    public static final String TYPE_EXERCICE_ZONES = "zones";
    public static final String TYPE_EXERCICE_DESCRIPTION = "description";
    public static final String TYPE_EXERCICE_TEMPS_DE_REPOS = "temps_repos";

    public static final String TYPE_EXERCICE_TABLE_NAME = "TypeExercice";
    public static final String TYPE_EXERCICE_TABLE_CREATE =
            "CREATE TABLE " + TYPE_EXERCICE_TABLE_NAME + " (" +
                    TYPE_EXERCICE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TYPE_EXERCICE_NOM + " TEXT, " +
                    TYPE_EXERCICE_CATEGORIE + " TEXT," +
                    TYPE_EXERCICE_ZONES + " TEXT," +
                    TYPE_EXERCICE_DESCRIPTION + " TEXT," +
                    TYPE_EXERCICE_TEMPS_DE_REPOS+ " INTEGER);";


    public TypeExerciceDAO(Context pContext) {
        super(pContext);
    }

    public List<TypeExercice> getAll(){
        List<TypeExercice> lesExercices = new ArrayList<TypeExercice>();

        Cursor c = mDb.rawQuery("select id,nom,categorie,zones,description,temps_repos from TypeExercice",new String[]{});


        //on parcours la liste
        while(c.moveToNext()){
            //on crée le type exercice
            lesExercices.add(new TypeExercice(c.getLong(0), c.getString(1), Categorie.setCategorie(c.getString(2)), c.getString(3), c.getString(4), c.getInt(5)));
        }

        return lesExercices;
    }


    public void ajouter(TypeExercice typeExercice){
        this.mDb.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) " +
                "VALUES(\""+typeExercice.getNom()+"\",\""+typeExercice.getCategorie()+"\"," +
                "\""+typeExercice.getZones()+"\",\""+typeExercice.getDescription()+"\",\""+typeExercice.getTempsDeRepos()+"\")");
    }

    public void supprimer(long id){

    }

    public void modifier(TypeExercice typeExercice){

    }

    public TypeExercice selectionner(Long id)
    {
        Cursor c = mDb.rawQuery("select id,nom,categorie,zones,description,temps_repos " +
                "from TypeExercice " +
                "where id = ?",new String[]{String.valueOf(id)});

        //si on a un résultat
        if(c.moveToFirst())
        {
            return new TypeExercice(c.getLong(0), c.getString(1), Categorie.setCategorie(c.getString(2)), c.getString(3), c.getString(4), c.getInt(5));
        }

        return null;
    }

    public static void defaultTypeExercice(SQLiteDatabase db){
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Pompes\",\"Musculation\",null,null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Tractions\",\"Musculation\",null,null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Dips\",\"Musculation\",null,null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Développé couché\",\"Musculation\",null,null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Course à pied\",\"Cardio\",null,null,null)");
    }
}
