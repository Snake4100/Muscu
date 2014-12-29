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

    public Exercice(Seance seance, TypeExercice typeExercice) {
        this.seance = seance;
        this.typeExercice = typeExercice;
        this.series = new ArrayList<Serie>();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray({seance,typeExercice},2);
        dest.writeStringArray(new String[]{seance, typeExercice,});
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
