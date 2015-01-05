package com.muscu.benjamin.muscu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.muscu.benjamin.muscu.DAO.DAOBase;
import com.muscu.benjamin.muscu.DAO.SeanceDAO;
import com.muscu.benjamin.muscu.Entity.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;

public class MesSeancesActivity extends Activity {

    private SeanceDAO seanceDAO;
    private ArrayAdapter<Seance> seancesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_seances);

        /* *********************** A SUPPRIMER ************************ */
        ///////////////this.getBaseContext().deleteDatabase("database.muscu");
        /* *********************** A SUPPRIMER ************************ */

        this.seanceDAO = new SeanceDAO(this.getBaseContext());
        this.seanceDAO.open();

        //on crée l'adapter de la listeview des seances
        this.seancesAdapter = new ArrayAdapter<Seance>(this, android.R.layout.simple_list_item_1, this.seanceDAO.getAll());
        ListView listSeances = (ListView) findViewById(R.id.listView_mesSeances);
        listSeances.setAdapter(this.seancesAdapter);
        listSeances.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MesSeancesActivity.this, com.muscu.benjamin.muscu.SeanceActivity.class);
                intent.putExtra("seance", MesSeancesActivity.this.seancesAdapter.getItem(position));
                startActivity(intent);
            }
        });

        listSeances.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                MesSeancesActivity.this.alertSuppressionSeance(MesSeancesActivity.this.seancesAdapter.getItem(position));
                return true;
            }
        });


        Button buttonNewSeance = (Button) findViewById(R.id.button_newSeance);
        buttonNewSeance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MesSeancesActivity.this, com.muscu.benjamin.muscu.SeanceActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.seancesAdapter.clear();
        this.seancesAdapter.addAll(this.seanceDAO.getAll());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void alertSuppressionSeance(final Seance laSeance){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MesSeancesActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Suppression de la "+laSeance.getNom());

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

                MesSeancesActivity.this.seanceDAO.supprimer(laSeance.getId());
                dialog.dismiss();
            }
        });


        builderSingle.show();
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
