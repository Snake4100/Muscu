package com.muscu.benjamin.muscu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.muscu.benjamin.muscu.DAO.TypeExerciceDAO;
import com.muscu.benjamin.muscu.DAO.TypeSeanceSerieDAO;
import com.muscu.benjamin.muscu.Entity.ExerciceTypeSeance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;
import com.muscu.benjamin.muscu.Entity.TypeSeance;
import com.muscu.benjamin.muscu.Entity.TypeSeanceSerie;

import java.util.ArrayList;
import java.util.Arrays;


public class ExerciceTypeSeanceActivity extends Activity {
    private Spinner spinnerTypeExercice;
    private ExerciceTypeSeance exercice;
    private ArrayAdapter<TypeExercice> typeExerciceArrayAdapter;
    private EditText editTextIndications;
    private EditText editTextTempsRepos;
    private ArrayAdapter<TypeSeanceSerie> typeSeanceSerieArrayAdapter;
    private TypeExerciceDAO daoTypeExercice;
    private TypeSeanceSerieDAO daoTypeSeanceSerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice_type_seance);
        this.setTitle("Exercice");

        //init DAOs
        this.daoTypeExercice = new TypeExerciceDAO(this.getBaseContext());
        this.daoTypeExercice.open();
        this.daoTypeSeanceSerie = new TypeSeanceSerieDAO(this.getBaseContext());
        this.daoTypeSeanceSerie.open();

        //initialisation du spinner type exercice
        this.spinnerTypeExercice = (Spinner)findViewById(R.id.spinner_typeExercice);
        this.typeExerciceArrayAdapter = new ArrayAdapter<TypeExercice>(this, android.R.layout.simple_spinner_item, this.daoTypeExercice.getAll());
        this.spinnerTypeExercice.setAdapter(this.typeExerciceArrayAdapter);
        this.spinnerTypeExercice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ExerciceTypeSeanceActivity.this.exercice.setTypeExercice(ExerciceTypeSeanceActivity.this.typeExerciceArrayAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //on récupére l'edit text qui contiendra les indicaitons pour l'exercice
        this.editTextIndications = (EditText)findViewById(R.id.editText_indicationsTypeExercice);

        //on récupére l'édit text pour le temps de repos
        this.editTextTempsRepos =(EditText)findViewById(R.id.editText_tempsReposExerciceTypeSeance);

        //on essaye de récupérer l'exercice si on en a passé un
        this.exercice = getIntent().getParcelableExtra("Exercice");

        //si on a pas récupérer d'exercice, c'est une création
        if(this.exercice == null){
            //on crée l'exercice
            this.exercice = new ExerciceTypeSeance(getIntent().getIntExtra("numeroExercice",0));
        }

        //sinon, on initialise la vue avec les bonnes infirmations
        else{

            //on itinitailise le spinner avec la bonne valeur
            for(int i=0; i<this.typeExerciceArrayAdapter.getCount(); i++)
            {
                if(this.typeExerciceArrayAdapter.getItem(i).getId() == this.exercice.getTypeExercice().getId()){
                    this.spinnerTypeExercice.setSelection(i);
                    break;
                }
            }
            //on initialise l'edit text
            this.editTextIndications.setText(this.exercice.getIndications());
            this.editTextTempsRepos.setText(String.valueOf(this.exercice.getTempsRepos()));

        }

        Button bouton_ajouterTypeSeanceSerie = (Button) findViewById(R.id.button_ajouterTypeSeanceSerie);
        bouton_ajouterTypeSeanceSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciceTypeSeanceActivity.this.alertCreerSerie();
            }
        });


        Button bouton_validerExerciceTypeSeance = (Button) findViewById(R.id.button_valierExerciceTypeSeance);
        bouton_validerExerciceTypeSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on récupére les valeurs
                ExerciceTypeSeanceActivity.this.getValues();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("Exercice",ExerciceTypeSeanceActivity.this.exercice);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        //on initialise la listview des séries
        ListView listViewSeries = (ListView)findViewById(R.id.listView_typeSeanceSeries);
        this.typeSeanceSerieArrayAdapter = new ArrayAdapter<TypeSeanceSerie>(this, android.R.layout.simple_spinner_item, new ArrayList<TypeSeanceSerie>());
        listViewSeries.setAdapter(this.typeSeanceSerieArrayAdapter);

        //on met à jours la liste des series
        updateListSeries();
    }

    private void updateListSeries()
    {
        this.typeSeanceSerieArrayAdapter.clear();
        this.typeSeanceSerieArrayAdapter.addAll(this.exercice.getListSeries());
    }

    private void getValues(){
        this.exercice.setIndications(this.editTextIndications.getText().toString());
        this.exercice.setTempsRepos(Integer.valueOf(this.editTextTempsRepos.getText().toString()));
        Log.e("debug","Indications envoyés : "+this.exercice.getIndications());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertCreerSerie(){
        int numeroSerie = this.exercice.getListSeries().size()+1;

        LinearLayout layout = (LinearLayout) LinearLayout.inflate(this, R.layout.type_seance_serie, null);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExerciceTypeSeanceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Série "+numeroSerie);


        //on ajoute le layout resultat_serie à l'alert
        try{
            builderSingle.setView(R.layout.resultat_serie);

        }catch (NoSuchMethodError e) {
            Log.e("Debug", "Older SDK, using old Builder");
            builderSingle.setView(layout);
        }

        //on crée la série
        final TypeSeanceSerie serie = new TypeSeanceSerie(numeroSerie);

        //on met une valeur par default le poid et le nombre de répétition
        NumberPicker numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_serieNbRepetitions);
        numberPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                serie.setNbRepetition(picker.getValue());
            }
        });
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(0);

        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //on ajoute la série à la liste
                ExerciceTypeSeanceActivity.this.exercice.addSerie(serie);
                ExerciceTypeSeanceActivity.this.updateListSeries();

                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercice_type_seance, menu);
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
