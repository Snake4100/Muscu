package com.muscu.benjamin.muscu.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benjamin on 27/12/2014.
 */
public class Serie implements Parcelable {

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

    //Constructeur Parcelable
    public Serie(Parcel source) {
        String[] data = new String[3];
        source.readStringArray(data);

        this.poids = Integer.valueOf(data[0]);
        this.repetitions = Integer.valueOf(data[1]);
        this.tempsTotal = Integer.valueOf(data[2]);
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
        dest.writeStringArray(new String[]{String.valueOf(this.poids), String.valueOf(this.repetitions), String.valueOf(this.tempsTotal)});

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
