package com.muscu.benjamin.muscu.Entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        if(categorie.equals(Categorie.Musculation.toString()))
            return Categorie.Musculation;

        else if (categorie.equals(Categorie.Cardio.toString()))
            return Categorie.Cardio;

        else
            return Categorie.Musculation;
    }


}
