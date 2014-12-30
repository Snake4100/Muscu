package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.muscu.benjamin.muscu.Entity.TypeExercice;


public class LesTypesExercicesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_les_types_exercices);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.TableListeExercice);
        for (final TypeExercice exercice : donneesTest.getTypesExercices()) {
            Button bouton = new Button(this);
            bouton.setText(exercice.getNom());


            bouton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    /*Intent intent = new Intent(LesExercicesActivity.this, com.muscu.benjamin.muscu.ExerciceActivity.class);
                    startActivity(intent);*/
                    finish();
                }
            });

            //on crée la ligne
            TableRow ligne = new TableRow(this);
            //on ajoute le bouton à la ligne
            ligne.addView(bouton);
            //on ajoute la ligne au tableau
            tableLayout.addView(ligne);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercices, menu);
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
