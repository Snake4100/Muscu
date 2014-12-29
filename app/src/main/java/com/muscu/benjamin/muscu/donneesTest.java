package com.muscu.benjamin.muscu;

import com.muscu.benjamin.muscu.Entity.Categorie;
import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by benjamin on 27/12/2014.
 */
public class donneesTest {

    public static List<Seance> getSeances() throws ParseException {

        int jours=2;
        String mois ="December";
        String annee = "2014";
        List<Seance> lesSeances = new ArrayList<Seance>();

        for(int i = 0; i<15; i++){
            //on crée la date
            String string = mois+" "+jours+", "+annee;//"January 2, 2010";
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            Date date = null;
            try {
                date = format.parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //on crée et on ajoute la seance à la liste
            Seance laSeance = new Seance(date);
            laSeance.setClose(true);
            lesSeances.add(laSeance);

            //on incrémente la date
            jours++;
        }

        return lesSeances;
    }

    public static List<TypeExercice> getTypesExercices()
    {
        List<TypeExercice> lesExercices = new ArrayList<TypeExercice>();

        //Type exercice 1
        TypeExercice exercice1 = new TypeExercice("Tractions", Categorie.Musculation);
        lesExercices.add(exercice1);

        //Type exercice 2
        TypeExercice exercice2 = new TypeExercice("Pompes", Categorie.Musculation);
        lesExercices.add(exercice2);

        //Type exercice 3
        TypeExercice exercice3 = new TypeExercice("Développé couché", Categorie.Musculation);
        lesExercices.add(exercice3);


        return lesExercices;
    }
}
