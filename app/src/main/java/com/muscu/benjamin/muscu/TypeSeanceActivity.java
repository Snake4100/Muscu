package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.muscu.benjamin.muscu.DAO.ExerciceTypeSeanceDAO;
import com.muscu.benjamin.muscu.DAO.TypeSeanceDAO;
import com.muscu.benjamin.muscu.Entity.ExerciceTypeSeance;
import com.muscu.benjamin.muscu.Entity.TypeSeance;

import java.util.ArrayList;


public class TypeSeanceActivity extends Activity {
    private TypeSeance typeSeance;
    private TypeSeanceDAO daoTypeSeance;
    private ExerciceTypeSeanceDAO daoExericeTypeSeance;
    private ArrayAdapter<ExerciceTypeSeance> exercicesAdapter;

    final int RESULT_EXERCICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_seance);

        //init DAOs
        this.daoTypeSeance = new TypeSeanceDAO(this.getBaseContext());
        this.daoTypeSeance.open();
        this.daoExericeTypeSeance = new ExerciceTypeSeanceDAO(this.getBaseContext());
        this.daoExericeTypeSeance.open();


        //on récupére la séance type
        this.typeSeance = getIntent().getParcelableExtra("TypeSeance");

        Button boutonConfirmer = (Button) findViewById(R.id.button_confirmerTypeSeance);

        //si on ouvre un type de séance existant
        if(this.typeSeance != null){
            EditText editText_nom = (EditText) findViewById(R.id.editText_nomTypeSeance);
            editText_nom.setText(this.typeSeance.getNom());
            this.setTitle("Séance type");
            boutonConfirmer.setText("Enregistrer");

            boutonConfirmer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //on récupére le nom
                    EditText editText_nom = (EditText) findViewById(R.id.editText_nomTypeSeance);
                    TypeSeanceActivity.this.typeSeance.setNom(editText_nom.getText().toString());

                    //on crée en base
                    TypeSeanceActivity.this.daoTypeSeance.modifier(TypeSeanceActivity.this.typeSeance);

                    finish();

                }
            });

        }

        //on crée une nouvelle séance
        else{
            this.setTitle("Création séance type");
            boutonConfirmer.setText("Créer");

            this.typeSeance = new TypeSeance(0,"");

            boutonConfirmer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //on récupére le nom
                    EditText editText_nom = (EditText) findViewById(R.id.editText_nomTypeSeance);
                    TypeSeanceActivity.this.typeSeance.setNom(editText_nom.getText().toString());

                    //on crée en base
                    TypeSeanceActivity.this.daoTypeSeance.create(TypeSeanceActivity.this.typeSeance);

                    finish();

                }
            });

        }

        //on initialise l'adapter de la liste des exercices
        this.exercicesAdapter = new ArrayAdapter<ExerciceTypeSeance>(this, android.R.layout.simple_list_item_1, new ArrayList<ExerciceTypeSeance>());
        ListView listExercice = (ListView) findViewById(R.id.listView_exerciceTypeSeance);
        listExercice.setAdapter(this.exercicesAdapter);


        Button bouton_ajouterExerciceTypeSeance = (Button) findViewById(R.id.button_ajoutExerciceTypeSeance);
        bouton_ajouterExerciceTypeSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * Attendre une réponse (un exercice)
                * lorsque l'on validera la séance on stockera tous en base
                *
                */

                Intent intent = new Intent(TypeSeanceActivity.this, ExerciceTypeSeanceActivity.class);
                intent.putExtra("TypeSeance", TypeSeanceActivity.this.typeSeance);
                intent.putExtra("numeroExercice",TypeSeanceActivity.this.exercicesAdapter.getCount()+1);
                startActivityForResult(intent,RESULT_EXERCICE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //updateListExercicesTypeSeance();

    }

    private void updateListExercicesTypeSeance(){
        this.exercicesAdapter.clear();
        this.exercicesAdapter.addAll(this.typeSeance.getListExercices());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode ==  RESULT_EXERCICE ) {
            if(resultCode == RESULT_OK){
                //on récupére et on ajoute la séance
                this.exercicesAdapter.add((ExerciceTypeSeance)data.getParcelableExtra("Exercice"));
                this.exercicesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_type_seance, menu);
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
