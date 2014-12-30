package com.muscu.benjamin.muscu.Entity;

import android.util.Log;

/**
 * Created by benjamin on 27/12/2014.
 */
public enum Categorie {
    Musculation("Musculation"),
    Cardio("Cardio");

    private String nom;

    Categorie(String nom){
        this.nom=nom;
    }

    public String toString()
    {
        return this.nom;
    }

    public static Categorie setCategorie(String categorie){
        if(categorie == Categorie.Musculation.toString())
            return Categorie.Musculation;

        else if (categorie == Categorie.Cardio.toString())
            return Categorie.Cardio;

        else
            return Categorie.Musculation;
    }

}
