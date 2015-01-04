package com.muscu.benjamin.muscu.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benjamin on 27/12/2014.
 */
public class TypeExercice implements Parcelable {
    private long id;
    private String nom;
    private Categorie categorie;
    private String zones;
    private String description;
    //private String photo;
    private int tempsDeRepos;

    //Constructeur minimal
    public TypeExercice(String nom, Categorie categorie)
    {
        this.nom=nom;
        this.categorie=categorie;
    }

    //Constructeur optimal
    public TypeExercice(long id, String nom, Categorie categorie, String zones, String description, int tempsDeRepos) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.zones = zones;
        this.description = description;
        this.tempsDeRepos = tempsDeRepos;
    }

    //Constructeur parcelable
    public TypeExercice(Parcel in) {
        //on récupére les données dans le tableau
        String[] data = new String[6];
        in.readStringArray(data);

        this.id = Long.valueOf(data[0]);
        this.nom = data[1];
        this.categorie = Categorie.setCategorie(data[2]);
        this.zones = data[3];
        this.description = data[4];
        this.tempsDeRepos = Integer.parseInt(data[5]);
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

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getZones() {
        return zones;
    }

    public void setZones(String zones) {
        this.zones = zones;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTempsDeRepos() {
        return tempsDeRepos;
    }

    public void setTempsDeRepos(int tempsDeRepos) {
        this.tempsDeRepos = tempsDeRepos;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.id), this.nom, this.categorie.toString(),this.zones, this.description, String.valueOf(this.tempsDeRepos)});
    }

    public static final Parcelable.Creator<TypeExercice> CREATOR = new Parcelable.Creator<TypeExercice>() {

        @Override
        public TypeExercice createFromParcel(Parcel source) {
            return new TypeExercice(source);  //using parcelable constructor
        }

        @Override
        public TypeExercice[] newArray(int size) {
            return new TypeExercice[size];
        }
    };
}
