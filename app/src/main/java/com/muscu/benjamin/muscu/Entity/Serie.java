package com.muscu.benjamin.muscu.Entity;

/**
 * Created by benjamin on 27/12/2014.
 */
public class Serie {

    //pour les exercices de musculation
    private int poids;
    private int repetitions;

    //pour les exercices cardio
    private int tempsTotal;

    //Constructeur serie musculation
    public Serie(int poids, int repetitions)
    {
        this.poids = poids;
        this.repetitions = repetitions;
    }

    //Constructeur exercice cardio
    public Serie(int tempsTotal)
    {
        this.tempsTotal = tempsTotal;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public String toString(){
        return this.repetitions+" répétitions à "+this.poids+"kg";
    }
}
