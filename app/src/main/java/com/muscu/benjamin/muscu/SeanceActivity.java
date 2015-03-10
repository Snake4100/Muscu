package com.muscu.benjamin.muscu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.muscu.benjamin.muscu.DAO.ExerciceDAO;
import com.muscu.benjamin.muscu.DAO.ExerciceTypeSeanceDAO;
import com.muscu.benjamin.muscu.DAO.SeanceDAO;
import com.muscu.benjamin.muscu.DAO.TypeExerciceDAO;
import com.muscu.benjamin.muscu.DAO.TypeSeanceDAO;
import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.ExerciceTypeSeance;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.Serie;
import com.muscu.benjamin.muscu.Entity.TypeExercice;
import com.muscu.benjamin.muscu.Entity.TypeSeance;

import java.util.ArrayList;
import java.util.Date;

public class SeanceActivity extends Activity {
    private Seance laSeance = null;
    //private final int NEW_EXERCICE=1;
    private ArrayAdapter<Exercice> exercicesAdapter;
    private TypeExerciceDAO daoTypeExercice;
    private ExerciceDAO daoExercice;
    private ExerciceTypeSeanceDAO daoExerciceTypeSeance;
    private SeanceDAO daoSeance;
    private TypeSeanceDAO daoTypeSeance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seance);

        //initialisation des DAOs
        this.daoTypeExercice = new TypeExerciceDAO(this.getBaseContext());
        this.daoTypeExercice.open();
        this.daoExercice = new ExerciceDAO(this.getBaseContext());
        this.daoExercice.open();
        this.daoSeance = new SeanceDAO(this.getBaseContext());
        this.daoSeance.open();
        this.laSeance = getIntent().getParcelableExtra("seance");
        this.daoTypeSeance = new TypeSeanceDAO(this.getBaseContext());
        this.daoTypeSeance.open();
        this.daoExerciceTypeSeance = new ExerciceTypeSeanceDAO(this.getBaseContext());
        this.daoExerciceTypeSeance.open();

        //on récupére les boutons
        Button boutonNouvelExercice = (Button) findViewById(R.id.button_nouvelExercice);
        Button boutonCloreSeance = (Button) findViewById(R.id.button_cloreSeance);


        //si on ouvre une seance existante
        if (this.laSeance != null) {

            //si la seance est close
            if (this.laSeance.isClose()) {

                //on cache le bouton pour créer un nouvel exercice
                boutonNouvelExercice.setVisibility(View.INVISIBLE);

                //on cache le bouton pour clore la seance
                boutonCloreSeance.setVisibility(View.INVISIBLE);
            }

        }

        //sinon, c'est qu'on la crée
        else {
            this.laSeance = this.daoSeance.create();
            this.alertConfigurationSeance();
        }

        this.setTitle(this.laSeance.getNom());

        //on crée les actions sur les boutons
        boutonNouvelExercice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SeanceActivity.this.alertListeTypeExercice();
            }
        });

        boutonCloreSeance.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //on clos la seance
                SeanceActivity.this.laSeance.setClose(true);
                SeanceActivity.this.daoSeance.mofidier(SeanceActivity.this.laSeance);

                finish();

            }
        });


        //on creer le liste adapter avec les exercices
        this.exercicesAdapter = new ArrayAdapter<Exercice>(this, android.R.layout.simple_list_item_1, new ArrayList<Exercice>());
        ListView listExercice = (ListView) findViewById(R.id.listExerciceSeance);
        listExercice.setAdapter(this.exercicesAdapter);
        listExercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SeanceActivity.this, ExerciceActivity.class);
                intent.putExtra("exercice", SeanceActivity.this.exercicesAdapter.getItem(position));
                startActivity(intent);
            }
        });

        listExercice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                SeanceActivity.this.alertSuppressionExercice(SeanceActivity.this.exercicesAdapter.getItem(position));
                SeanceActivity.this.miseAjoursListeExercice();
                return true;
            }
        });

    }

    private void alertSuppressionExercice(final Exercice exercice){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SeanceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Suppression de la "+exercice.toString());

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

                SeanceActivity.this.daoExercice.supprimer(exercice.getId());
                SeanceActivity.this.miseAjoursListeExercice();
                dialog.dismiss();
            }
        });


        builderSingle.show();
    }

    //http://stackoverflow.com/questions/15762905/how-to-display-list-view-in-alert-dialog-in-android
    private void alertListeTypeExercice()
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                SeanceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Choisissez un exercice");
        final ArrayAdapter<TypeExercice> arrayAdapter = new ArrayAdapter<TypeExercice>(
                SeanceActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(this.daoTypeExercice.getAll());

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SeanceActivity.this, ExerciceActivity.class);
                        intent.putExtra("typeExercice", arrayAdapter.getItem(which));
                        intent.putExtra("seance", SeanceActivity.this.laSeance);
                        //startActivityForResult(intent,NEW_EXERCICE);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        builderSingle.show();
    }

    private void alertConfigurationSeance()
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                SeanceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Choisissez un Type de séance.");
        final ArrayAdapter<TypeSeance> arrayAdapter = new ArrayAdapter<TypeSeance>(
                SeanceActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(this.daoTypeSeance.getAll());

        builderSingle.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SeanceActivity.this.daoSeance.supprimer(SeanceActivity.this.laSeance.getId());
                        SeanceActivity.this.finish();
                    }
                });

        builderSingle.setPositiveButton("Aucune",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        //bouton retour
        builderSingle.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    SeanceActivity.this.daoSeance.supprimer(SeanceActivity.this.laSeance.getId());
                    SeanceActivity.this.finish();
                }
                return true;
            }
        });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SeanceActivity.this.laSeance.setTypeSeance(arrayAdapter.getItem(which));
                        SeanceActivity.this.daoSeance.mofidier(SeanceActivity.this.laSeance);
                        SeanceActivity.this.createExercices();
                    }
                });
        builderSingle.show();
    }

    private void createExercices(){
        //on récupére les exercices de la séance
        TypeSeance typeSeance = this.laSeance.getTypeSeance();
        typeSeance.setListExercices(this.daoExerciceTypeSeance.getExerciceFromTypeSeance(typeSeance));

        for(ExerciceTypeSeance exericeTypeSeance : typeSeance.getListExercices()){
            Exercice exercice = new Exercice(this.laSeance,exericeTypeSeance.getTypeExercice(),exericeTypeSeance.getTempsRepos(),exericeTypeSeance);
            this.daoExercice.create(exercice);
        }

        this.miseAjoursListeExercice();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //on récupère ses exercices
        this.laSeance.setExercices(this.daoExercice.getSeanceExercices(this.laSeance));

        this.miseAjoursListeExercice();

    }

    private void miseAjoursListeExercice(){
        this.exercicesAdapter.clear();
        this.laSeance.setExercices(this.daoExercice.getSeanceExercices(this.laSeance));
        this.exercicesAdapter.addAll(this.laSeance.getExercices());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seance, menu);
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
