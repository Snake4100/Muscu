package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.muscu.benjamin.muscu.DAO.TypeSeanceDAO;
import com.muscu.benjamin.muscu.Entity.TypeSeance;

import java.util.ArrayList;


public class TypesSeancesActivity extends Activity {
    private ArrayAdapter<TypeSeance> typesSeancesAdapter;
    private TypeSeanceDAO daoTypeSeance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types_seances);
        this.setTitle("Types Séances");

        //init DAOs
        this.daoTypeSeance = new TypeSeanceDAO(this.getBaseContext());
        this.daoTypeSeance.open();

        this.typesSeancesAdapter = new ArrayAdapter<TypeSeance>(this, android.R.layout.simple_list_item_1, new ArrayList<TypeSeance>());
        ListView listTypesSeances = (ListView) findViewById(R.id.listView_typesSeances);
        listTypesSeances.setAdapter(this.typesSeancesAdapter);
        listTypesSeances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TypesSeancesActivity.this, TypeSeanceActivity.class);
                intent.putExtra("TypeSeance", TypesSeancesActivity.this.typesSeancesAdapter.getItem(position));
                startActivity(intent);
            }
        });

        Button bouton_ajoutSeanceType = (Button)findViewById(R.id.button_ajouterTypeSeance);
        bouton_ajoutSeanceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypesSeancesActivity.this, TypeSeanceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateListTypesSeances();


    }

    private void updateListTypesSeances(){
        this.typesSeancesAdapter.clear();
        this.typesSeancesAdapter.addAll(this.daoTypeSeance.getAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_types_seances, menu);
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
