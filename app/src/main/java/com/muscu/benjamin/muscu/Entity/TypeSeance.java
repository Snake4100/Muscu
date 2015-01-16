package com.muscu.benjamin.muscu.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by benjamin on 14/01/2015.
 */
public class TypeSeance implements Parcelable {
    private long id;
    private String nom;
    private List<ExerciceTypeSeance> listExercices;

    public TypeSeance(long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public TypeSeance(Parcel source) {
        this.id = source.readLong();
        this.nom = source.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<ExerciceTypeSeance> getListExercices() {
        return listExercices;
    }

    public void setListExercices(List<ExerciceTypeSeance> listExercices) {
        this.listExercices = listExercices;
    }

    public String toString(){
        return this.nom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.nom);
    }

    public static final Parcelable.Creator<TypeSeance> CREATOR = new Parcelable.Creator<TypeSeance>() {

        @Override
        public TypeSeance createFromParcel(Parcel source) {
            return new TypeSeance(source);  //using parcelable constructor
        }

        @Override
        public TypeSeance[] newArray(int size) {
            return new TypeSeance[size];
        }
    };
}
