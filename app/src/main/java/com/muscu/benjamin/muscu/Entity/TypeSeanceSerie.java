package com.muscu.benjamin.muscu.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benjamin on 14/01/2015.
 */
public class TypeSeanceSerie implements Parcelable {
    private long id;
    private long numeroSerie;
    private long nbRepetition;
    private boolean maximum;
    private ExerciceTypeSeance exercice;

    public TypeSeanceSerie(long id, long numeroSerie, long nbRepetition, boolean maximum, ExerciceTypeSeance exercice) {
        this.id = id;
        this.numeroSerie = numeroSerie;
        this.nbRepetition = nbRepetition;
        this.maximum = maximum;
        this.exercice = exercice;
    }

    //constructeur minimal
    public TypeSeanceSerie(long numeroSerie){
        this.numeroSerie = numeroSerie;
        this.id = -1;
        this.nbRepetition = 0;
        this.maximum = false;
        this.exercice = null;
    }

    public TypeSeanceSerie(Parcel source) {
        this.id = source.readLong();
        this.numeroSerie = source.readLong();
        this.nbRepetition = source.readLong();
        this.maximum = (1 == source.readInt());
        this.exercice = null;
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

    public boolean isMaximum() {
        return maximum;
    }

    public void setMaximum(boolean maximum) {
        this.maximum = maximum;
    }

    public String toString(){
        if(this.maximum)
            return "maximum";
        else
            return this.nbRepetition+" répétitions";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.numeroSerie);
        dest.writeLong(this.nbRepetition);
        dest.writeInt(this.maximum? 1 : 0);
    }

    public static final Parcelable.Creator<TypeSeanceSerie> CREATOR = new Parcelable.Creator<TypeSeanceSerie>() {

        @Override
        public TypeSeanceSerie createFromParcel(Parcel source) {
            return new TypeSeanceSerie(source);  //using parcelable constructor
        }

        @Override
        public TypeSeanceSerie[] newArray(int size) {
            return new TypeSeanceSerie[size];
        }
    };
}
