package com.muscu.benjamin.muscu.Entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by benjamin on 27/12/2014.
 */
public class Seance implements Parcelable {

    private long id;
    private Date date;
    private String nom;
    private boolean close;
    private List<Exercice> exercices = new ArrayList<Exercice>();

    //constructeur complet
    public Seance(long id, Date date, String nom, boolean close){
        this.id = id;
        this.date = date;
        this.nom = nom;
        this.close = close;
    }

    //Constructeur date
    public Seance(Date uneDate) {
        this.setDate(uneDate);
        this.close = false;
    }

    //Constructeur parcelable
    public Seance(Parcel in) {
        //on récupére les données dans le tableau
        String[] data = new String[3];
        in.readStringArray(data);

        this.id = Long.valueOf(data[0]);

        //on converti la date string en type date
        this.setDate(DateConversion.stringToDate(data[1]));

        //on met close à jours
        if (data[2].equals("False")) {
            this.close = false;
        } else {
            this.close = true;
        }

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

    public void setNom(String dateText) {
        this.nom = "Séance du " + dateText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;

        this.setNom(DateConversion.dateToString(this.date));
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public List<Exercice> getExercices() {
        return exercices;
    }

    public void setExercices(List<Exercice> exercices) {
        this.exercices = exercices;
    }

    public void addExercice(Exercice exercice){
        this.exercices.add(exercice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String isClose = "False";

        if (this.close)
            isClose = "True";

        dest.writeStringArray(new String[]{String.valueOf(this.id),DateConversion.dateToString(this.date), isClose});
    }

    public static final Parcelable.Creator<Seance> CREATOR = new Parcelable.Creator<Seance>() {

        @Override
        public Seance createFromParcel(Parcel source) {
            return new Seance(source);  //using parcelable constructor
        }

        @Override
        public Seance[] newArray(int size) {
            return new Seance[size];
        }
    };
}
