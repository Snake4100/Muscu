package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.muscu.benjamin.muscu.Entity.Exercice;
import com.muscu.benjamin.muscu.Entity.Seance;
import com.muscu.benjamin.muscu.Entity.TypeExercice;

import java.text.ParseException;


public class LesExercicesActivity extends Activity {
    private final int NEW_EXERCICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercices);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.TableListeExercice);
        for (final TypeExercice exercice : donneesTest.getTypesExercices()) {
            Button bouton = new Button(this);
            bouton.setText(exercice.getNom());


            //si cette vue a été déclenché depuis une seance en cours
            if(this.getIntent().getParcelableExtra("seance") != null)
            {
                bouton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(LesExercicesActivity.this, com.muscu.benjamin.muscu.ExerciceActivity.class);
                        intent.putExtra("typeExercice", exercice);
                        intent.putExtra("seance", LesExercicesActivity.this.getIntent().getParcelableExtra("seance"));
                        startActivity(intent);
                        finish();
                    }
                });
            }

            //on crée la ligne
            TableRow ligne = new TableRow(this);
            //on ajoute le bouton à la ligne
            ligne.addView(bouton);
            //on ajoute la ligne au tableau
            tableLayout.addView(ligne);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NEW_EXERCICE:
                if (resultCode == RESULT_OK) {
                    //on récupére l'exercice
                    Bundle bundle = data.getExtras();
                    Exercice exerice = (Exercice) bundle.getParcelable("exercice");

                    //on crée la réponse
                    Intent intent = new Intent();
                    Bundle b = new Bundle();
                    b.putParcelable("exercice", exerice);
                    intent.putExtras(b);

                    setResult(RESULT_OK, intent);
                }
                break;

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
