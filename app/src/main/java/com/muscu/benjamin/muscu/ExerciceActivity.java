package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;


public class ExerciceActivity extends Activity {

    private Exercice sonExercice;
    private int nbRepetition = 0;
    private int tempsRepos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);

        TextView nomExerciceText = (TextView)findViewById(R.id.exerciceName);

        TypeExercice typeExercice = getIntent().getParcelableExtra("typeExercice");
        Seance laSeanceEnCours = getIntent().getParcelableExtra("seance");

        //si typeExercice est différent de null, c'est qu'on crée un exercice
        if(typeExercice != null && laSeanceEnCours != null){
            this.sonExercice = new Exercice(laSeanceEnCours,typeExercice);
            nomExerciceText.setText(this.sonExercice.getTypeExercice().getNom());
        }

        else {

            nomExerciceText.setText("Type exercice ou seance null");
        }

        Button boutonClore = (Button)findViewById(R.id.button_cloreExercice);
        boutonClore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //on renvoi l'exercice à la seance
                Intent resultIntent = new Intent();
                resultIntent.putExtra("exercice", ExerciceActivity.this.sonExercice);
                setResult(RESULT_OK,resultIntent);
                //on ferme l'actvité
                finish();
            }
        });

        /*
            ———————————————————————————————————————$$$$$$$————————————————————————————————————
            FAIRE UNE POPUP QUI DEMANDE LE NOMBRE DE REPETITION SOUHAITÉ ET LE TEMPS DE REPOS
            ———————————————————————————————————————$$$$$$$————————————————————————————————————

         */
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
