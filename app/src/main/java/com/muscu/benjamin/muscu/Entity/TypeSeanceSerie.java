package com.muscu.benjamin.muscu.Entity;

/**
 * Created by benjamin on 14/01/2015.
 */
public class TypeSeanceSerie {
    private long id;
    private long numeroSerie;
    private long nbRepetition;
    private ExerciceTypeSeance exercice;

    public TypeSeanceSerie(long id, long numeroSerie, long nbRepetition, ExerciceTypeSeance exercice) {
        this.id = id;
        this.numeroSerie = numeroSerie;
        this.nbRepetition = nbRepetition;
        this.exercice = exercice;
    }

    //constructeur minimal
    public TypeSeanceSerie(long numeroSerie, ExerciceTypeSeance exercice){
        this.numeroSerie = numeroSerie;
        this.id = -1;
        this.nbRepetition = 0;
        this.exercice = exercice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(long numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public long getNbRepetition() {
        return nbRepetition;
    }

    public void setNbRepetition(long nbRepetition) {
        this.nbRepetition = nbRepetition;
    }

    public ExerciceTypeSeance getExercice() {
        return exercice;
    }

    public void setExercice(ExerciceTypeSeance exercice) {
        this.exercice = exercice;
    }

    public String toString(){
        return this.nbRepetition+" répétitions";
    }
}
