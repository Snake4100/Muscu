package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.muscu.benjamin.muscu.DAO.TypeExerciceDAO;
import com.muscu.benjamin.muscu.Entity.ExerciceTypeSeance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;
import com.muscu.benjamin.muscu.Entity.TypeSeance;

import java.util.Arrays;


public class ExerciceTypeSeanceActivity extends Activity {
    private Spinner spinnerTypeExercice;
    private ExerciceTypeSeance exercice;
    private ArrayAdapter<TypeExercice> typeExerciceArrayAdapter;
    private TypeExerciceDAO daoTypeExercice;
    private EditText editTextIndications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice_type_seance);
        this.setTitle("Exercice");

        //init DAOs
        this.daoTypeExercice = new TypeExerciceDAO(this.getBaseContext());
        this.daoTypeExercice.open();

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

        //on essaye de récupérer l'exercice si on en a passé un
        this.exercice = getIntent().getParcelableExtra("Exercice");

        //si on a pas récupérer d'exercice, c'est une création
        if(this.exercice == null){
            //on crée l'exercice
            this.exercice = new ExerciceTypeSeance(0, getIntent().getIntExtra("numeroExercice",0), null, null, "");
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

            // on récupére les séries

        }

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



    }

    private void getValues(){
        this.exercice.setIndications(this.editTextIndications.getText().toString());
        Log.e("debug","Indications envoyés : "+this.exercice.getIndications());
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
