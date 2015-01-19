package com.muscu.benjamin.muscu.Entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 29/12/2014.
 */
public class Exercice implements Parcelable {

    private long id;
    private Seance seance;
    private TypeExercice typeExercice;
    private List<Serie> series;
    private int tempsRepos;
    private ExerciceTypeSeance exerciceTypeSeance;

    //constructeur dao
    public Exercice(long id, Seance seance, TypeExercice typeExercice, int tempsRepos, ExerciceTypeSeance exerciceTypeSeance){
        this.id = id;
        this.seance = seance;
        this.typeExercice = typeExercice;
        this.tempsRepos = tempsRepos;
        this.exerciceTypeSeance = exerciceTypeSeance;
    }

    //Constructeur creation nouvel exercice
    public Exercice(Seance seance, TypeExercice typeExercice, int tempsRepos, ExerciceTypeSeance exerciceTypeSeance) {
        this.seance = seance;
        this.typeExercice = typeExercice;
        this.series = new ArrayList<Serie>();
        this.tempsRepos = tempsRepos;
        this.exerciceTypeSeance = exerciceTypeSeance;
    }

    public Exercice(Parcel in){
        this.id = in.readLong();
        this.seance = (Seance) (Seance) in.readValue(Seance.class.getClassLoader());
        this.typeExercice = (TypeExercice) (TypeExercice) in.readValue(TypeExercice.class.getClassLoader());
        this.tempsRepos = in.readInt();
        this.exerciceTypeSeance = (ExerciceTypeSeance)in.readValue(ExerciceTypeSeance.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public TypeExercice getTypeExercice() {
        return typeExercice;
    }

    public void setTypeExercice(TypeExercice typeExercice) {
        this.typeExercice = typeExercice;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    public void addSerie(Serie uneSerie){
        this.series.add(uneSerie);
    }

    public int getTempsRepos() {
        return tempsRepos;
    }

    public void setTempsRepos(int tempsRepos) {
        this.tempsRepos = tempsRepos;
    }

    public ExerciceTypeSeance getExerciceTypeSeance() {
        return exerciceTypeSeance;
    }

    public void setExerciceTypeSeance(ExerciceTypeSeance exerciceTypeSeance) {
        this.exerciceTypeSeance = exerciceTypeSeance;
    }

    public String toString()
    {
        return this.typeExercice.getNom();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeValue(this.seance);
        dest.writeValue(this.typeExercice);
        dest.writeInt(this.tempsRepos);
        dest.writeValue(this.exerciceTypeSeance);

    }

    public static final Parcelable.Creator<Exercice> CREATOR = new Parcelable.Creator<Exercice>() {

        @Override
        public Exercice createFromParcel(Parcel source) {
            return new Exercice(source);  //using parcelable constructor
        }

        @Override
        public Exercice[] newArray(int size) {
            return new Exercice[size];
        }
    };
}
