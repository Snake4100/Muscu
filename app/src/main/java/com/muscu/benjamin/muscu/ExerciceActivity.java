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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;


public class ExerciceActivity extends Activity {

    private Exercice sonExercice;
    private int nbSerieSouhaite = 0;
    private int tempsRepos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);

        TextView nomExerciceText = (TextView) findViewById(R.id.exerciceName);

        TypeExercice typeExercice = getIntent().getParcelableExtra("typeExercice");
        Seance laSeanceEnCours = getIntent().getParcelableExtra("seance");

        //si typeExercice est différent de null, c'est qu'on crée un exercice
        if (typeExercice != null && laSeanceEnCours != null) {
            //on affiche l'alert pour configurer les temps de repos et le nombre de séries souhaité
            this.alertConfigurationExercice();

            //on créer l'exercice
            this.sonExercice = new Exercice(laSeanceEnCours, typeExercice, this.nbSerieSouhaite, this.tempsRepos);
            nomExerciceText.setText(this.sonExercice.getTypeExercice().getNom());

        } else {

            nomExerciceText.setText("Type exercice ou seance null");
        }

        Button boutonClore = (Button) findViewById(R.id.button_cloreExercice);
        boutonClore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //on renvoi l'exercice à la seance
                Intent resultIntent = new Intent();
                resultIntent.putExtra("exercice", ExerciceActivity.this.sonExercice);
                setResult(RESULT_OK, resultIntent);
                //on ferme l'actvité
                finish();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertConfigurationExercice() {

        LinearLayout layout = (LinearLayout) LinearLayout.inflate(this, R.layout.configuration_exercice, null);


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                ExerciceActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Configuration exercice");


        //on crée la liste view qui va contenir les éléments de l'alert
        ListView listView = new ListView(this);
        builderSingle.setView(R.layout.configuration_exercice);

        //on met une valeur par default le nombre de série et le temps de repos
        NumberPicker numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_nbSerie);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(4);

        numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_tempsRepos);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10000);
        numberPicker.setValue(60);

        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //récupération du nombre de série souhaite
                LinearLayout layout = (LinearLayout) LinearLayout.inflate(ExerciceActivity.this, R.layout.configuration_exercice, null);
                NumberPicker numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_nbSerie);
                ExerciceActivity.this.nbSerieSouhaite = numberPicker.getValue();

                //récupération du temps de repos souhaité
                numberPicker = (NumberPicker)layout.findViewById(R.id.numberPicker_tempsRepos);
                ExerciceActivity.this.tempsRepos = numberPicker.getValue();

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
