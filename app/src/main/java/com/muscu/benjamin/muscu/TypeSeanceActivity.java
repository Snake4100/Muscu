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
import android.widget.ListAdapter;
import android.widget.ListView;

import com.muscu.benjamin.muscu.DAO.ExerciceTypeSeanceDAO;
import com.muscu.benjamin.muscu.DAO.TypeSeanceDAO;
import com.muscu.benjamin.muscu.Entity.ExerciceTypeSeance;
import com.muscu.benjamin.muscu.Entity.TypeSeance;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;


public class TypeSeanceActivity extends Activity {
    private TypeSeance typeSeance;
    private TypeSeanceDAO daoTypeSeance;
    private ExerciceTypeSeanceDAO daoExericeTypeSeance;
    private ArrayAdapter<ExerciceTypeSeance> exercicesAdapter;
    private ArrayList<ExerciceTypeSeance> listExerciceSupp;

    final int RESULT_CREATION_EXERCICE = 1;
    final int RESULT_MODIFICATION_EXERCICE = 2;

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
            //on récupére ses exercices
            this.typeSeance.setListExercices(this.daoExericeTypeSeance.getExerciceFromTypeSeance(this.typeSeance));

            EditText editText_nom = (EditText) findViewById(R.id.editText_nomTypeSeance);
            editText_nom.setText(this.typeSeance.getNom());
            this.setTitle("Séance type");

            //on initialise le bouton de confirmation
            boutonConfirmer.setText("Enregistrer");
            boutonConfirmer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //on récupére le nom
                    EditText editText_nom = (EditText) findViewById(R.id.editText_nomTypeSeance);
                    TypeSeanceActivity.this.typeSeance.setNom(editText_nom.getText().toString());

                    //on crée en base
                    TypeSeanceActivity.this.daoTypeSeance.modifier(TypeSeanceActivity.this.typeSeance);

                    TypeSeanceActivity.this.daoExericeTypeSeance.deleteList(TypeSeanceActivity.this.listExerciceSupp);

                    finish();

                }
            });

            //on initialise la liste des exercices  supprimer
            this.listExerciceSupp = new ArrayList<ExerciceTypeSeance>();

        }

        //on crée une nouvelle séance
        else{
            this.setTitle("Création séance type");
            boutonConfirmer.setText("Créer");

            this.typeSeance = new TypeSeance(0,"");

            boutonConfirmer.setOnClickListener(new OnClickListener() {
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
        listExercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TypeSeanceActivity.this, ExerciceTypeSeanceActivity.class);
                intent.putExtra("Exercice",TypeSeanceActivity.this.exercicesAdapter.getItem(position));
                startActivityForResult(intent,RESULT_MODIFICATION_EXERCICE);
            }
        });

        //on initilise l'action de click long sur un exercice
        listExercice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ExerciceTypeSeance exercice = TypeSeanceActivity.this.exercicesAdapter.getItem(position);
                new AlertDialog.Builder(TypeSeanceActivity.this)
                        .setTitle("Suppression")
                        .setMessage("Voulez-vous vraiment supprimer l'exercice "+exercice.toString()+"?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //on le supprrime de la liste
                                TypeSeanceActivity.this.typeSeance.getListExercices().remove(exercice);
                                //s'il a un id != -1 c'est qu'il est en base. donc on le met dans la liste des exercices à supprimer en base
                                if(exercice.getId()!=-1)
                                    TypeSeanceActivity.this.listExerciceSupp.add(exercice);

                                TypeSeanceActivity.this.updateListExercicesTypeSeance();
                            }
                        }).create().show();

                return true;
            }
        });


        Button bouton_ajouterExerciceTypeSeance = (Button) findViewById(R.id.button_ajoutExerciceTypeSeance);
        bouton_ajouterExerciceTypeSeance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeSeanceActivity.this, ExerciceTypeSeanceActivity.class);
                intent.putExtra("numeroExercice",TypeSeanceActivity.this.exercicesAdapter.getCount()+1);
                startActivityForResult(intent,RESULT_CREATION_EXERCICE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateListExercicesTypeSeance();

    }

    private void updateListExercicesTypeSeance(){
        this.exercicesAdapter.clear();
        this.exercicesAdapter.addAll(this.typeSeance.getListExercices());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode ==  RESULT_CREATION_EXERCICE ) {
            if(resultCode == RESULT_OK){
                //on récupére et on ajoute la séance
                ExerciceTypeSeance exercice = (ExerciceTypeSeance)data.getParcelableExtra("Exercice");
                Log.e("debug","Indications reçus : "+exercice.getIndications());
                this.typeSeance.addExercice(exercice);
            }
        }

        else if(requestCode == RESULT_MODIFICATION_EXERCICE){
            if(resultCode == RESULT_OK) {
                Log.e("debug", "Result modif exercice");
                //on récupére l'exercice
                ExerciceTypeSeance exerciceModifie = (ExerciceTypeSeance) data.getParcelableExtra("Exercice");

                //on récupére la liste des exercices
                List<ExerciceTypeSeance> listExerice = this.typeSeance.getListExercices();
                //on remplace l'exercice par le nouveau

                //on recherche et on rempalce l'exercice
                for (int i = 0; i < listExerice.size(); i++) {
                    ExerciceTypeSeance ex = listExerice.get(i);
                    Log.e("debug", ex.getId() + " == " + exerciceModifie.getId());
                    if (ex.getId() == exerciceModifie.getId()) {
                        Log.e("debug", "true");
                        //ex = exerciceModifie;
                        listExerice.set(i,exerciceModifie);
                    }
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TypeSeanceActivity.super.onBackPressed();
                    }
                }).create().show();
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
