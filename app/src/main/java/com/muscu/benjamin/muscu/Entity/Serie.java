package com.muscu.benjamin.muscu.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benjamin on 27/12/2014.
 */
public class Serie implements Parcelable {

    private long id;
    //pour les exercices de musculation
    private int poids;
    private int repetitions;

    //pour les exercices cardio
    private int tempsTotal;

    //Constructeur complet
    public Serie(long id, int poids, int repetitions)
    {
        this.id = id;
        this.poids = poids;
        this.repetitions = repetitions;
    }

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

    //Constructeur Parcelable
    public Serie(Parcel source) {
        String[] data = new String[4];
        source.readStringArray(data);

        this.id = Long.valueOf(data[0]);
        this.poids = Integer.valueOf(data[1]);
        this.repetitions = Integer.valueOf(data[2]);
        this.tempsTotal = Integer.valueOf(data[3]);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTempsTotal() {
        return tempsTotal;
    }

    public void setTempsTotal(int tempsTotal) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.id),String.valueOf(this.poids), String.valueOf(this.repetitions), String.valueOf(this.tempsTotal)});

    }

    public static final Parcelable.Creator<Serie> CREATOR = new Parcelable.Creator<Serie>() {

        @Override
        public Serie createFromParcel(Parcel source) {
            return new Serie(source);  //using parcelable constructor
        }

        @Override
        public Serie[] newArray(int size) {
            return new Serie[size];
        }
    };
}
