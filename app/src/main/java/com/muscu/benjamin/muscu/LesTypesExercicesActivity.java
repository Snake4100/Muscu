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
import android.widget.TableLayout;
import android.widget.TableRow;

import com.muscu.benjamin.muscu.DAO.TypeExerciceDAO;
import com.muscu.benjamin.muscu.Entity.TypeExercice;

import java.util.ArrayList;


public class LesTypesExercicesActivity extends Activity {
    ArrayAdapter<TypeExercice> typeExerciceArrayAdapter;
    TypeExerciceDAO daoTypeExercice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_les_types_exercices);

        this.daoTypeExercice = new TypeExerciceDAO(this.getBaseContext());
        this.daoTypeExercice.open();

        ListView listExercices = (ListView) findViewById(R.id.listView_exercices);
        this.typeExerciceArrayAdapter = new ArrayAdapter<TypeExercice>(this, android.R.layout.simple_list_item_1, new ArrayList<TypeExercice>());
        listExercices.setAdapter(this.typeExerciceArrayAdapter);
        listExercices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LesTypesExercicesActivity.this, TypeExerciceActivity.class);
                intent.putExtra("typeExercice",LesTypesExercicesActivity.this.typeExerciceArrayAdapter.getItem(position));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.miseAjoursListeTypeExercice();
    }

    private void miseAjoursListeTypeExercice()
    {
        this.typeExerciceArrayAdapter.clear();
        this.typeExerciceArrayAdapter.addAll(this.daoTypeExercice.getAll());
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
