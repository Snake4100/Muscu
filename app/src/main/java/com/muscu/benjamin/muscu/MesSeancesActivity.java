package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.muscu.benjamin.muscu.DAO.DAOBase;
import com.muscu.benjamin.muscu.DAO.SeanceDAO;
import com.muscu.benjamin.muscu.Entity.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MesSeancesActivity extends Activity {

    private List<Button> lesBoutonsSeance = new ArrayList<Button>();
    private SeanceDAO seanceDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_seances);

        /* *********************** A SUPPRIMER ************************ */
        //this.getBaseContext().deleteDatabase("database.muscu");
        /* *********************** A SUPPRIMER ************************ */

        this.seanceDAO = new SeanceDAO(this.getBaseContext());
        this.seanceDAO.open();

        LinearLayout listLayout = (LinearLayout) findViewById(R.id.listButton);


        //for(final Seance seance : donneesTest.getSeances()){
        for (final Seance seance : this.seanceDAO.getAll()) {
            Button bouton = new Button(this);
            bouton.setText(seance.getNom());

            bouton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MesSeancesActivity.this, com.muscu.benjamin.muscu.SeanceActivity.class);
                    intent.putExtra("seance", seance);
                    startActivity(intent);
                }
            });

            this.lesBoutonsSeance.add(bouton);
            listLayout.addView(bouton);

        }


        Button buttonNewSeance = (Button) findViewById(R.id.button_newSeance);
        buttonNewSeance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MesSeancesActivity.this, com.muscu.benjamin.muscu.SeanceActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mes_seances, menu);
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
