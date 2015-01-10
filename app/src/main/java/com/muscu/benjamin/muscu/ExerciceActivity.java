package com.muscu.benjamin.muscu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.muscu.benjamin.muscu.DAO.ExerciceDAO;
import com.muscu.benjamin.muscu.DAO.SerieDAO;
import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.Serie;
import com.muscu.benjamin.muscu.Entity.TypeExercice;

import java.util.ArrayList;
import java.util.List;


public class ExerciceActivity extends Activity {

    private Exercice sonExercice;
    private ArrayAdapter<Serie> seriesAdapter;
    private ExerciceDAO daoExercice;
    private SerieDAO daoSerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);

        //init DAOs
        this.daoExercice = new ExerciceDAO(this.getBaseContext());
        this.daoExercice.open();
        this.daoSerie = new SerieDAO(this.getBaseContext());
        this.daoSerie.open();

        TypeExercice typeExercice = getIntent().getParcelableExtra("typeExercice");
        Seance laSeanceEnCours = getIntent().getParcelableExtra("seance");
        this.sonExercice = getIntent().getParcelableExtra("exercice");
        //si typeExercice est différent de null, c'est qu'on crée un exercice
        if (typeExercice != null && laSeanceEnCours != null) {

            //on créer l'exercice
            this.sonExercice = new Exercice(laSeanceEnCours, typeExercice, this.defaultNbSeries(),  this.defaultTempsRepos());
            this.sonExercice.setId(this.daoExercice.create(this.sonExercice));

            //on affiche l'alert pour configurer les temps de repos et le nombre de séries souhaité
            this.alertConfigurationExercice(laSeanceEnCours,typeExercice);


        }
        //si on a passé un exercice en parametre, c'est une visualisation d'un exercice deja fait
        else if(this.sonExercice != null){
            this.sonExercice.setSeries(this.daoSerie.getSeriesExercice(this.sonExercice));
        }
        else {

            this.setTitle("Type exercice ou seance null");
        }

        this.setTitle(this.sonExercice.getTypeExercice().getNom());

        //bouton pour clore l'exercice
        Button boutonClore = (Button) findViewById(R.id.button_cloreExercice);
        boutonClore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //on ferme l'actvité
                finish();
            }
        });


        //on crée l'adapter de la listeview des series
        this.seriesAdapter = new ArrayAdapter<Serie>(this, android.R.layout.simple_list_item_1, this.sonExercice.getSeries());
        ListView listSeries = (ListView) findViewById(R.id.listView_series);
        listSeries.setAdapter(this.seriesAdapter);
        listSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExerciceActivity.this.alertModificationResultatSerie(ExerciceActivity.this.seriesAdapter.getItem(position));
            }
        });



    }

    

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertConfigurationExercice(Seance laSeanceEnCours,TypeExercice typeExercice) {

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


        //on met une valeur par default le nombre de série et le temps de repos
        NumberPicker numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_nbSerie);
        numberPicker.setOnValueChangedListener(( new NumberPicker.
                OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.e("Debug","NB série récupéré : "+picker.getValue());
                ExerciceActivity.this.sonExercice.setNbSeriesSouhaite(picker.getValue());
            }
        }));
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(this.defaultNbSeries());

        numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_tempsRepos);
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
                LinearLayout layout = (LinearLayout) LinearLayout.inflate(ExerciceActivity.this, R.layout.configuration_exercice, null);

                //on lance les series
                ExerciceActivity.this.startSerie();

                //on ferme la fenetre
                dialog.dismiss();
            }
        });


        builderSingle.show();
    }

    private int defaultNbSeries(){
        return 4;
    }

    private int defaultTempsRepos(){
        return 60;
    }

    private void startSerie(){
        //temps qu'on a pas fait le nombre de série souhaité
        if(this.sonExercice.getNbSeriesSouhaite() != this.sonExercice.getSeries().size()){
            //on affiche l'alerte pour saisir les résultats de la série
            alertResultatSerie(this.sonExercice.getSeries().size()+1);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertResultatSerie(int numeroSerie){
        LinearLayout layout = (LinearLayout) LinearLayout.inflate(this, R.layout.resultat_serie, null);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExerciceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Série "+numeroSerie);


        //on crée la liste view qui va contenir les éléments de l'alert
        try{
            builderSingle.setView(R.layout.resultat_serie);

        }catch (NoSuchMethodError e) {
            Log.e("Debug", "Older SDK, using old Builder");
            builderSingle.setView(layout);
        }

        //on crée et on ajoute la série à l'exercice
        this.seriesAdapter.add(new Serie(this.defaultPoids(), this.defaultNbRepetitions(), this.sonExercice));

        //on met une valeur par default le poid et le nombre de répétition
        NumberPicker numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_poids);
        numberPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.e("debug","picker poids : "+picker.getValue());
                List<Serie> listSerie = ExerciceActivity.this.sonExercice.getSeries();
                listSerie.get(listSerie.size()-1).setPoids(picker.getValue());
            }
        });
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(0);

        numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_repetitions);
        numberPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.e("debug","picker nbrepetitions : "+picker.getValue());
                List<Serie> listSerie = ExerciceActivity.this.sonExercice.getSeries();
                listSerie.get(listSerie.size()-1).setRepetitions(picker.getValue());
            }
        });
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(0);

        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExerciceActivity.this.seriesAdapter.notifyDataSetChanged();
                List<Serie> listSerie = ExerciceActivity.this.sonExercice.getSeries();
                ExerciceActivity.this.daoSerie.create(listSerie.get(listSerie.size()-1));

                //on lance le chrono
                ExerciceActivity.this.alertTimerRepos();

                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertModificationResultatSerie(final Serie serie){
        LinearLayout layout = (LinearLayout) LinearLayout.inflate(this, R.layout.resultat_serie, null);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExerciceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Modification série");


        //on crée la liste view qui va contenir les éléments de l'alert
        try{
            builderSingle.setView(R.layout.resultat_serie);

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
    private void alertTimerRepos(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExerciceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Repos");


        final LinearLayout layout = (LinearLayout) LinearLayout.inflate(this, R.layout.timer, null);
        try{
            builderSingle.setView(R.layout.resultat_serie);

        }catch (NoSuchMethodError e) {
            Log.e("Debug", "Older SDK, using old Builder");
            builderSingle.setView(layout);
        }

        //on crée le timer
        new CountDownTimer(this.sonExercice.getTempsRepos()*1000, 1000) {

            TextView timer = (TextView)layout.findViewById(R.id.textView_timer);

            public void onTick(long millisUntilFinished) {
                long secondesUntilFinished = millisUntilFinished/1000;
                long minutes = secondesUntilFinished/60;
                long secondes = secondesUntilFinished%60;

                String stringMinutes = String.valueOf(minutes);
                String stringSecondes = String.valueOf(secondes);

                if(minutes<10){
                    stringMinutes = "0"+stringMinutes;
                }
                if(secondes<10){
                    stringSecondes = "0"+stringSecondes;
                }

                timer.setText(stringMinutes+":"+stringSecondes);
            }

            public void onFinish() {
                timer.setText("00:00");
            }
        }.start();

        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //on lance la série suivante
                ExerciceActivity.this.startSerie();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
