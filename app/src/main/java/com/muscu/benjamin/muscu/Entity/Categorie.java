package com.muscu.benjamin.muscu.Entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 27/12/2014.
 */
public enum Categorie {
    Repetition("Repetition"),
    Chronometre("Chronometre");

    private String nom;

    Categorie(String nom){
        this.nom=nom;
    }

    public String toString()
    {
        return this.nom;
    }

    public static Categorie setCategorie(String categorie){
        if(categorie.equals(Categorie.Repetition.toString()))
            return Categorie.Repetition;

        else if (categorie.equals(Categorie.Chronometre.toString()))
            return Categorie.Chronometre;

        else
            return Categorie.Repetition;
    }


}
