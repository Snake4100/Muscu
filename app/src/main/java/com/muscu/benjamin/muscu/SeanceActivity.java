package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;

import java.util.Date;



public class SeanceActivity extends Activity {
    private Seance laSeance = null;
    private final int NEW_EXERCICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seance);

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
            this.laSeance = new Seance(new Date());

        }

        //on affiche le nom de la séance
        TextView nomSeanceText = (TextView) findViewById(R.id.nomSeance_text);
        nomSeanceText.setText(this.laSeance.getNom());

        //on crée les actions sur les boutons
        boutonNouvelExercice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //on envoi vers la vue de selection d'un exercice
                Intent intent = new Intent(SeanceActivity.this, LesExercicesActivity.class);
                intent.putExtra("seance",SeanceActivity.this.laSeance);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NEW_EXERCICE:
                if (resultCode == RESULT_OK) {

                    //read the bundle and get the country object
                    Bundle bundle = data.getExtras();
                    Exercice exercice = (Exercice) bundle.getParcelable("exercice");

                    this.laSeance.addExercice(exercice);
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("debug", "nombre d'exercices : " + this.laSeance.getExercices().size());
        this.actualiserListeExercice();

        TextView nomSeanceText = (TextView) findViewById(R.id.nomSeance_text);
        nomSeanceText.setText(this.laSeance.getNom());

    }

    private void actualiserListeExercice() {
        ListView listExercice = (ListView) findViewById(R.id.listExerciceSeance);
        for (Exercice exercice : this.laSeance.getExercices()) {
            Button bouton = new Button(this);
            bouton.setText(exercice.getTypeExercice().getNom());
        }


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
