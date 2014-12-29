package com.muscu.benjamin.muscu.Entity;

import android.os.Parcel;
import android.os.Parcelable;

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

    private Date date;
    private String nom;
    private boolean close;
    private List<Exercice> exercices = new ArrayList<Exercice>();

    public Seance(Date uneDate) {
        this.setDate(uneDate);
        this.close = false;
    }

    public Seance(Parcel in) {
        //on récupére les données dans le tableau
        String[] data = new String[2];
        in.readStringArray(data);

        //on converti la date string en type date
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            this.setDate(format.parse(data[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //on met close à jours
        if (data[1] == "False") {
            this.close = false;
        } else {
            this.close = true;
        }

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
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String datetext = df.format(this.date);

        this.setNom(datetext);
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

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String datetext = df.format(this.date);

        String isClose = "False";

        if (this.close)
            isClose = "True";

        dest.writeStringArray(new String[]{datetext, isClose});
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
