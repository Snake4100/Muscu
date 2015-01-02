package com.muscu.benjamin.muscu.Entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 29/12/2014.
 */
public class Exercice implements Parcelable {
    private Seance seance;
    private TypeExercice typeExercice;
    private List<Serie> series;
    private int nbSeriesSouhaite;
    private int tempsRepos;

    public Exercice(Seance seance, TypeExercice typeExercice,int nbSeriesSouhaite, int tempsRepos) {
        this.seance = seance;
        this.typeExercice = typeExercice;
        this.series = new ArrayList<Serie>();
        this.nbSeriesSouhaite = nbSeriesSouhaite;
        this.tempsRepos = tempsRepos;
    }

    public Exercice(Parcel in){
        this.seance = (Seance) (Seance) in.readValue(Seance.class.getClassLoader());
        this.typeExercice = (TypeExercice) (TypeExercice) in.readValue(TypeExercice.class.getClassLoader());
        this.series = new ArrayList<Serie>();
        this.series = in.readArrayList(Serie.class.getClassLoader());
        this.nbSeriesSouhaite = in.readInt();
        this.tempsRepos = in.readInt();
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

    public int getNbSeriesSouhaite() {
        return nbSeriesSouhaite;
    }

    public void setNbSeriesSouhaite(int nbSeriesSouhaite) {
        this.nbSeriesSouhaite = nbSeriesSouhaite;
    }

    public int getTempsRepos() {
        return tempsRepos;
    }

    public void setTempsRepos(int tempsRepos) {
        this.tempsRepos = tempsRepos;
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
        dest.writeValue(this.seance);
        dest.writeValue(this.typeExercice);
        dest.writeList(this.series);
        dest.writeInt(this.nbSeriesSouhaite);
        dest.writeInt(this.tempsRepos);

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
