package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.muscu.benjamin.muscu.DAO.ExerciceDAO;
import com.muscu.benjamin.muscu.DAO.SeanceDAO;
import com.muscu.benjamin.muscu.DAO.TypeExerciceDAO;
import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;

import java.util.Date;

public class SeanceActivity extends Activity {
    private Seance laSeance = null;
    //private final int NEW_EXERCICE=1;
    private ArrayAdapter<Exercice> seancesAdapter;
    private TypeExerciceDAO daoTypeExercice;
    private ExerciceDAO daoExercice;
    private SeanceDAO daoSeance;

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
        }

        //on affiche le nom de la séance
        TextView nomSeanceText = (TextView) findViewById(R.id.nomSeance_text);
        nomSeanceText.setText(this.laSeance.getNom());

        //on crée les actions sur les boutons
        boutonNouvelExercice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SeanceActivity.this.alertListeTypeExercice();
            }
        });


        //on creer le liste adapter avec les exercices
        this.seancesAdapter = new ArrayAdapter<Exercice>(this, android.R.layout.simple_list_item_1, this.laSeance.getExercices());
        ListView listExercice = (ListView) findViewById(R.id.listExerciceSeance);
        listExercice.setAdapter(this.seancesAdapter);
        listExercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SeanceActivity.this, ExerciceActivity.class);
                intent.putExtra("exercice", SeanceActivity.this.seancesAdapter.getItem(position));
                startActivity(intent);
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();

        //on récupère ses exercices
        this.laSeance.setExercices(this.daoExercice.getSeanceExercices(this.laSeance));

        this.seancesAdapter.clear();
        this.seancesAdapter.addAll(this.laSeance.getExercices());



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
