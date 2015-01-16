package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice_type_seance);
        this.setTitle("Exercice");

        //init DAOs
        this.daoTypeExercice = new TypeExerciceDAO(this.getBaseContext());
        this.daoTypeExercice.open();

        //on crée l'exercice
        this.exercice = new ExerciceTypeSeance(0, getIntent().getIntExtra("numeroExercice",0), null, null, "");

        Button bouton_validerExerciceTypeSeance = (Button) findViewById(R.id.button_valierExerciceTypeSeance);
        bouton_validerExerciceTypeSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Exercice",ExerciceTypeSeanceActivity.this.exercice);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        //initialisation du spinner
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
