package com.muscu.benjamin.muscu.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by benjamin on 14/01/2015.
 */
public class ExerciceTypeSeance implements Parcelable {
    private long id;
    private long numeroExercice;
    private TypeExercice typeExercice;
    private TypeSeance typeSeance;
    private List<TypeSeanceSerie> listSeries;
    private String indications;
    private int tempsRepos;
    private List<TypeSeanceSerie> listSeriesAsupp;

    //constructeur complet
    public ExerciceTypeSeance(long id, long numeroExercice, TypeExercice typeExercice, TypeSeance typeSeance, String indications, int tempsRepos) {
        this.id = id;
        this.numeroExercice = numeroExercice;
        this.typeExercice = typeExercice;
        this.typeSeance = typeSeance;
        this.indications = indications;
        this.listSeries = new ArrayList<TypeSeanceSerie>();
        this.tempsRepos = tempsRepos;
        this.listSeriesAsupp = new ArrayList<TypeSeanceSerie>();
    }

    //constructeur numero exerice
    public ExerciceTypeSeance(long numeroExercice){
        this.id = -1;
        this.numeroExercice = numeroExercice;
        this.typeExercice = null;
        this.typeSeance = null;
        this.indications = "";
        this.listSeries = new ArrayList<TypeSeanceSerie>();
        this.tempsRepos = 0;
    }



    public ExerciceTypeSeance(Parcel source) {
        this.id = source.readLong();
        this.numeroExercice = source.readLong();
        this.typeExercice = (TypeExercice) source.readValue(TypeExercice.class.getClassLoader());
        this.typeSeance = (TypeSeance) source.readValue(TypeSeance.class.getClassLoader());

        this.listSeries = new ArrayList<TypeSeanceSerie>();
        for(Parcelable obj : source.readParcelableArray(TypeSeanceSerie.class.getClassLoader()))
        {
            this.addSerie((TypeSeanceSerie)obj);
        }

        this.indications = source.readString();
        this.tempsRepos = source.readInt();

        this.listSeriesAsupp = new ArrayList<TypeSeanceSerie>();
        for(Parcelable obj : source.readParcelableArray(TypeSeanceSerie.class.getClassLoader()))
        {
            this.addSerieAsupp((TypeSeanceSerie)obj);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumeroExercice() {
        return numeroExercice;
    }

    public void setNumeroExercice(long numeroExercice) {
        this.numeroExercice = numeroExercice;
    }

    public TypeExercice getTypeExercice() {
        return typeExercice;
    }

    public void setTypeExercice(TypeExercice typeExercice) {
        this.typeExercice = typeExercice;
    }

    public TypeSeance getTypeSeance() {
        return typeSeance;
    }

    public void setTypeSeance(TypeSeance typeSeance) {
        this.typeSeance = typeSeance;
    }

    public List<TypeSeanceSerie> getListSeries() {
        return listSeries;
    }

    public void setListSeries(List<TypeSeanceSerie> listSeries) {
        this.listSeries = listSeries;
    }

    public void addSerie(TypeSeanceSerie serie){
        serie.setExercice(this);
        this.listSeries.add(serie);
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public int getTempsRepos() {
        return tempsRepos;
    }

    public void setTempsRepos(int tempsRepos) {
        this.tempsRepos = tempsRepos;
    }

    public List<TypeSeanceSerie> getListSeriesAsupp() {
        return listSeriesAsupp;
    }

    public void setListSeriesAsupp(List<TypeSeanceSerie> listSeriesAsupp) {
        this.listSeriesAsupp = listSeriesAsupp;
    }

    public void addSerieAsupp(TypeSeanceSerie serie){
        this.listSeries.remove(serie);
        this.listSeriesAsupp.add(serie);
    }

    public String toString(){
        return this.typeExercice.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.numeroExercice);
        dest.writeValue(this.typeExercice);
        dest.writeValue(this.typeSeance);
        dest.writeParcelableArray(this.listSeries.toArray(new Parcelable[]{}), flags);

        dest.writeString(this.indications);
        dest.writeInt(this.tempsRepos);

        dest.writeParcelableArray(this.listSeriesAsupp.toArray(new Parcelable[]{}), flags);
    }

    public static final Parcelable.Creator<ExerciceTypeSeance> CREATOR = new Parcelable.Creator<ExerciceTypeSeance>() {

        @Override
        public ExerciceTypeSeance createFromParcel(Parcel source) {
            return new ExerciceTypeSeance(source);  //using parcelable constructor
        }

        @Override
        public ExerciceTypeSeance[] newArray(int size) {
            return new ExerciceTypeSeance[size];
        }
    };
}
