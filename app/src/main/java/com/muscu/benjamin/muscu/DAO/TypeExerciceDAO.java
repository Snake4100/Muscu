package com.muscu.benjamin.muscu.DAO;

import android.content.ContentValues;
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
            "CREATE TABLE IF NOT EXISTS " + TYPE_EXERCICE_TABLE_NAME + " (" +
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

        Cursor c = mDb.rawQuery("select id,nom,categorie,zones,description,temps_repos from TypeExercice order by nom",new String[]{});


        //on parcours la liste
        while(c.moveToNext()){
            //on crée le type exercice
            lesExercices.add(new TypeExercice(c.getLong(0), c.getString(1), Categorie.setCategorie(c.getString(2)), c.getString(3), c.getString(4), c.getInt(5)));
        }

        return lesExercices;
    }


    public void create(TypeExercice typeExercice){
        this.mDb.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) " +
                "VALUES(\""+typeExercice.getNom()+"\",\""+typeExercice.getCategorie()+"\"," +
                "\""+typeExercice.getZones()+"\",\""+typeExercice.getDescription()+"\",\""+typeExercice.getTempsDeRepos()+"\")");
    }

    public void supprimer(long id){
        mDb.delete(this.TYPE_EXERCICE_TABLE_NAME, this.TYPE_EXERCICE_KEY + " = ?", new String[] {String.valueOf(id)});
    }

    public void modifier(TypeExercice typeExercice){
        ContentValues value = new ContentValues();
        value.put( this.TYPE_EXERCICE_NOM, typeExercice.getNom());
        value.put( this.TYPE_EXERCICE_CATEGORIE, typeExercice.getCategorie().toString());
        value.put( this.TYPE_EXERCICE_ZONES, typeExercice.getZones());
        value.put( this.TYPE_EXERCICE_DESCRIPTION, typeExercice.getDescription());
        value.put( this.TYPE_EXERCICE_TEMPS_DE_REPOS, typeExercice.getTempsDeRepos());

        mDb.update(this.TYPE_EXERCICE_TABLE_NAME, value, this.TYPE_EXERCICE_KEY  + " = ?", new String[] {String.valueOf(typeExercice.getId())});
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
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Pompes\",\"Repetition\",null,null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Tractions\",\"Repetition\",null,null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Dips\",\"Repetition\",\"Pectoraux\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Développé couché\",\"Repetition\",\"Pectoraux\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Développé incliné avec haltères\",\"Repetition\",\"Pectoraux\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Poulie vis à vis\",\"Repetition\",\"Pectoraux\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Butterfly\",\"Repetition\",\"Pectoraux\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Extension barre front\",\"Repetition\",\"Triceps\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Extension corde poulie haute\",\"Repetition\",\"Triceps\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Presse\",\"Repetition\",\"Cuisses\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Squat\",\"Repetition\",\"Cuisses\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Legs extension\",\"Repetition\",\"Cuisses\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Legs curl\",\"Repetition\",\"Cuisses\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Presse debout\",\"Repetition\",\"Mollets\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Crunch\",\"Repetition\",\"Abdos\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Tirage vertical devant\",\"Repetition\",\"Dos\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Tirage nuque\",\"Repetition\",\"Dos\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Soulevé de terre\",\"Repetition\",\"Dos\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Rowing assis\",\"Repetition\",\"Dos\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Curls barre EZ\",\"Repetition\",\"Biceps\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Curls barre droite pronation\",\"Repetition\",\"Biceps\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Curls alternés\",\"Repetition\",\"Biceps\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Développé haltères\",\"Repetition\",\"Epaules\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Rowing menton\",\"Repetition\",\"Epaules\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Elévations latérales\",\"Repetition\",\"Epaules\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Oiseau\",\"Repetition\",\"Epaules\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Shrugs\",\"Repetition\",\"Trapèzes\",null,null)");

        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Chaise romaine\",\"Repetition\",\"Abdos\",null,null)");
        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Gainage (planche)\",\"Chronometre\",\"Abdos\",null,null)");


        db.execSQL("INSERT INTO TypeExercice(nom,categorie,zones,description,temps_repos) VALUES(\"Course à pied\",\"Chronometre\",null,null,null)");
    }
}
