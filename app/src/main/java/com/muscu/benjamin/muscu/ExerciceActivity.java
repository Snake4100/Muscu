package com.muscu.benjamin.muscu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.muscu.benjamin.muscu.DAO.ExerciceDAO;
import com.muscu.benjamin.muscu.DAO.SerieDAO;
import com.muscu.benjamin.muscu.Entity.Categorie;
import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.Serie;
import com.muscu.benjamin.muscu.Entity.TypeExercice;
import com.muscu.benjamin.muscu.Entity.TypeSeanceSerie;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ExerciceActivity extends Activity {

    private Exercice sonExercice;
    private ArrayAdapter<Serie> seriesAdapter;
    private ExerciceDAO daoExercice;
    private SerieDAO daoSerie;
    private TextView timer;
    private CountDownTimer countDown = null;

    /*
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Pour les séries chronométrées, quand on clique sur ajouter, afficher une popup avec un chronométre. Si le temps a faire est définis
        le chronométre décontera, sinon, il comptera
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);

        //init DAOs
        this.daoExercice = new ExerciceDAO(this.getBaseContext());
        this.daoExercice.open();
        this.daoSerie = new SerieDAO(this.getBaseContext());
        this.daoSerie.open();

        this.timer = (TextView) findViewById(R.id.textView_chrono);

        //On récupére le type d'éxercice, la séance et l'exercice s'ils ont été passé en paramétre
        TypeExercice typeExercice = getIntent().getParcelableExtra("typeExercice");
        Seance laSeanceEnCours = getIntent().getParcelableExtra("seance");
        this.sonExercice = getIntent().getParcelableExtra("exercice");

        //si typeExercice et laSeanceEnCours est différent de null, c'est qu'on crée un exercice
        if (typeExercice != null && laSeanceEnCours != null) {
            //on créer l'exercice
            this.sonExercice = new Exercice(laSeanceEnCours, typeExercice, this.defaultTempsRepos(), null);
            this.daoExercice.create(this.sonExercice);

            //on affiche l'alert pour configurer les temps de repos et le nombre de séries souhaité
            this.alertConfigurationExercice(laSeanceEnCours,typeExercice);


        }

        //si on a passé un exercice en parametre, c'est une visualisation d'un exercice deja fait
        else if(this.sonExercice != null){
            this.sonExercice.setSeries(this.daoSerie.getSeriesExercice(this.sonExercice));
            this.setTime(this.sonExercice.getTempsRepos(), ExerciceActivity.this.timer);
        }
        else {

            this.setTitle("Type exercice ou seance null");
        }

        this.setTitle(this.sonExercice.getTypeExercice().getNom());

        //bouton pour ajouter série
        Button boutonAjouter = (Button) findViewById(R.id.button_ajouterSerie);
        //bouton démarer timer
        Button boutonDemarrer = (Button) findViewById(R.id.button_demarrerChrono);

        //on crée l'adapter de la listeview des series
        this.seriesAdapter = new ArrayAdapter<Serie>(this, android.R.layout.simple_list_item_1, this.sonExercice.getSeries());
        ListView listSeries = (ListView) findViewById(R.id.listView_series);
        listSeries.setAdapter(this.seriesAdapter);


        //si la séance n'est pas close
        if(!this.sonExercice.getSeance().isClose())
        {

            boutonAjouter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    resultatSerie(ExerciceActivity.this.sonExercice.getSeries().size() + 1);
                }
            });


            boutonDemarrer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //s'il y avait deja un timer on l'arréte
                    if(ExerciceActivity.this.countDown != null){
                        ExerciceActivity.this.countDown.cancel();
                    }

                    //on crée le timer
                    ExerciceActivity.this.countDown = new CountDownTimer(ExerciceActivity.this.sonExercice.getTempsRepos()*1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long secondesUntilFinished = millisUntilFinished / 1000;
                            ExerciceActivity.this.setTime(secondesUntilFinished, ExerciceActivity.this.timer);
                        }

                        public void onFinish() {
                            ExerciceActivity.this.setTime(0, ExerciceActivity.this.timer);
                        }
                    }.start();
                }
            });

            //modification d'une série
            listSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ExerciceActivity.this.modificationResultatSerie(ExerciceActivity.this.seriesAdapter.getItem(position));
                }
            });

            //suppression d'une série
            listSeries.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ExerciceActivity.this.alertSuppressionSerie(ExerciceActivity.this.seriesAdapter.getItem(position));
                    return true;
                }
            });
        }

        else{
            boutonAjouter.setVisibility(View.GONE);
            boutonDemarrer.setVisibility(View.GONE);
            this.timer.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.e("debug","onResume ExerciceActivity");

    }


    private void setTime(long secondesUntilFinished, TextView textView){
        long minutes = secondesUntilFinished / 60;
        long secondes = secondesUntilFinished % 60;

        String stringMinutes = String.valueOf(minutes);
        String stringSecondes = String.valueOf(secondes);

        if (minutes < 10) {
            stringMinutes = "0" + stringMinutes;
        }
        if (secondes < 10) {
            stringSecondes = "0" + stringSecondes;
        }

        textView.setText(stringMinutes + ":" + stringSecondes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertConfigurationExercice(Seance laSeanceEnCours, TypeExercice typeExercice) {

        LinearLayout layout = (LinearLayout) LinearLayout.inflate(this, R.layout.configuration_exercice, null);


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                ExerciceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Configuration exercice");


        //on crée la liste view qui va contenir les éléments de l'alert
        ListView listView = new ListView(this);

        try{
            builderSingle.setView(R.layout.configuration_exercice);

        }catch (NoSuchMethodError e) {
            Log.e("Debug", "Older SDK, using old Builder");
            builderSingle.setView(layout);

        }




        //Parametrage du numberPicker pour le temps de repos
        NumberPicker numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_tempsRepos);
        numberPicker.setOnValueChangedListener(( new NumberPicker.
                OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ExerciceActivity.this.sonExercice.setTempsRepos(picker.getValue());
            }
        }));
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(this.defaultTempsRepos());


        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //LinearLayout layout = (LinearLayout) LinearLayout.inflate(ExerciceActivity.this, R.layout.configuration_exercice, null);
                ExerciceActivity.this.daoExercice.modifier(ExerciceActivity.this.sonExercice);

                //on initialise le timer
                ExerciceActivity.this.setTime(ExerciceActivity.this.sonExercice.getTempsRepos(), ExerciceActivity.this.timer);

                //on ferme la fenetre
                dialog.dismiss();
            }
        });


        builderSingle.show();
    }

    private int defaultTempsRepos(){
        return 60;
    }

    private void resultatSerie(int numeroSerie){
        //si c'est un exercice de repetition
        if(this.sonExercice.getTypeExercice().getCategorie().toString().equals(Categorie.Repetition.toString()))
            alertResultatSerieRepetition(numeroSerie);

        //si c'est un exercice chronometré
        else if(this.sonExercice.getTypeExercice().getCategorie().toString().equals(Categorie.Chronometre.toString()))
            alertResultatSerieChronometre(numeroSerie);

    }

    private void modificationResultatSerie(final Serie serie){
        //si c'est un exercice de repetition
        if(this.sonExercice.getTypeExercice().getCategorie().toString().equals(Categorie.Repetition.toString()))
            alertModificationResultatSerieRepetition(serie);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertResultatSerieRepetition(int numeroSerie){
        LinearLayout layout = (LinearLayout) LinearLayout.inflate(this, R.layout.resultat_serie_repetition, null);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExerciceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Série "+numeroSerie);


        //on ajoute le layout resultat_serie_repetition à l'alert
        try{
            builderSingle.setView(R.layout.resultat_serie_repetition);

        }catch (NoSuchMethodError e) {
            Log.e("Debug", "Older SDK, using old Builder");
            builderSingle.setView(layout);
        }

        //on crée la série
        final Serie serie = new Serie(this.defaultPoids(), this.defaultNbRepetitions(), this.sonExercice);

        //on met une valeur par default le poid et le nombre de répétition
        NumberPicker numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_poids);
        numberPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                serie.setPoids(picker.getValue());
            }
        });
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(0);

        numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_repetitions);
        numberPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                serie.setRepetitions(picker.getValue());
            }
        });
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(0);

        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //on ajoute la série à l'adapter
                ExerciceActivity.this.seriesAdapter.add(serie);
                //on prévient l'adapter que les données ont changées
                ExerciceActivity.this.seriesAdapter.notifyDataSetChanged();
                //on ajoute la série à la bd
                ExerciceActivity.this.daoSerie.create(serie);

                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertResultatSerieChronometre(int numeroSerie){
        int mStackLevel = 1;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        long tempsAfaire = -1;


        //s'il y a une une indication pour cet exercice
        if(sonExercice.getExerciceTypeSeance() != null)
        {
            Log.e("debug","il y a une indication d'exercice. numero serie : "+numeroSerie+". Liste : "+sonExercice.getExerciceTypeSeance().getListSeries().size());
            //si la série que l'on veut faire était prévue
            if(numeroSerie-1 < sonExercice.getExerciceTypeSeance().getListSeries().size())
            {
                Log.e("debug","nombre de série correcte");

                //si ce n'est pas une série maximum
                if(!sonExercice.getExerciceTypeSeance().getListSeries().get(numeroSerie-1).isMaximum()){
                    tempsAfaire = sonExercice.getExerciceTypeSeance().getListSeries().get(numeroSerie-1).getNbRepetition();
                    Log.e("debug","temps à faire : "+tempsAfaire);
                }
            }

        }

        // Create and show the dialog.
        DialogFragment newFragment = Chronometre_serie.newInstance(mStackLevel, tempsAfaire);
        newFragment.show(ft, "dialog");


    }

    public void addSerieChronometre(int resultat)
    {
        Serie serie = new Serie(this.defaultPoids(), this.defaultNbRepetitions(), this.sonExercice);
        serie.setTempsTotal(resultat);
        //on ajoute la série à l'adapter
        this.seriesAdapter.add(serie);
        //on prévient l'adapter que les données ont changées
        this.seriesAdapter.notifyDataSetChanged();
        //on ajoute la série à la bd
        this.daoSerie.create(serie);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertModificationResultatSerieRepetition(final Serie serie){
        LinearLayout layout = (LinearLayout) LinearLayout.inflate(this, R.layout.resultat_serie_repetition, null);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExerciceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Modification série");


        //on crée la liste view qui va contenir les éléments de l'alert
        try{
            builderSingle.setView(R.layout.resultat_serie_repetition);

        }catch (NoSuchMethodError e) {
            Log.e("Debug", "Older SDK, using old Builder");
            builderSingle.setView(layout);
        }

        //on met une valeur par default le poid et le nombre de répétition
        NumberPicker numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_poids);
        numberPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.e("debug","picker poids : "+picker.getValue());
                serie.setPoids(picker.getValue());
            }
        });
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(serie.getPoids());

        numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_repetitions);
        numberPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.e("debug","picker nbrepetitions : "+picker.getValue());
                serie.setRepetitions(picker.getValue());
            }
        });
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(serie.getRepetitions());

        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExerciceActivity.this.seriesAdapter.notifyDataSetChanged();
                ExerciceActivity.this.daoSerie.modifier(serie);

                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

    private int defaultPoids()
    {
        return 0;
    }

    private int defaultNbRepetitions(){
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertInfos(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                ExerciceActivity.this);

        ScrollView layout = (ScrollView) LinearLayout.inflate(this, R.layout.infos_exercice, null);

        try{
            builderSingle.setView(R.layout.infos_exercice);

        }catch (NoSuchMethodError e) {
            Log.e("Debug", "Older SDK, using old Builder");
            builderSingle.setView(layout);
        }

        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Infos.");

        TextView textView_series = (TextView)layout.findViewById(R.id.textView_series);
        TextView textView_typeSeanceSerie = (TextView)layout.findViewById(R.id.textView_typeSeanceSerie);


        //si l'exercice est associé à une instance de Exercice Type Seance
        if(this.sonExercice.getExerciceTypeSeance() != null)
        {
            String repetitions = "";
            List<TypeSeanceSerie> listSeries = this.sonExercice.getExerciceTypeSeance().getListSeries();
            for(TypeSeanceSerie serie : listSeries){
                if(serie.isMaximum())
                    repetitions+="maximum";
                else
                    repetitions+=String.valueOf(serie.getNbRepetition());

                if(listSeries.indexOf(serie) != listSeries.size()-1){
                    repetitions+=" - ";
                }
            }

            textView_typeSeanceSerie.setText(repetitions);

        }
        //sinon on masque les séries à effectuer
        else{
            textView_series.setVisibility(View.INVISIBLE);
            textView_typeSeanceSerie.setVisibility(View.INVISIBLE);
        }

        LinearLayout layoutHistorique = (LinearLayout)layout.findViewById(R.id.linearLayout_historique);

        //on crée l'historique
        List<Exercice> listExercice = this.daoExercice.getExercicesOfTypeExercice(this.sonExercice.getTypeExercice());
        Collections.reverse(listExercice);

        for(Exercice exercice : listExercice){
            TextView textView = new TextView(this);

            String text = "";
            List<Serie> listSeries = exercice.getSeries();
            for(Serie serie : listSeries){
                text+=serie.getRepetitions()+" * "+serie.getPoids()+"kg";

                if(listSeries.indexOf(serie) != listSeries.size()-1){
                    text+=" - ";
                }
            }

            textView.setText(text);

            layoutHistorique.addView(textView);
        }

        builderSingle.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        builderSingle.show();
    }

    private void alertSuppressionSerie(final Serie serie){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExerciceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Suppression de la série.");

        //Boutton pour annuler la suppression
        builderSingle.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //Boutton pour supprimer
        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                ExerciceActivity.this.daoSerie.supprimer(serie.getId());

                ExerciceActivity.this.seriesAdapter.remove(serie);
                //on prévient l'adapter que les données ont changées
                ExerciceActivity.this.seriesAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        builderSingle.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_infos) {
            this.alertInfos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
