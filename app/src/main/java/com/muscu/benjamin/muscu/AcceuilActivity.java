package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.content.Intent;


public class AcceuilActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        Log.e("debug","Acceuil");
        Button button_mesSeances = (Button) findViewById(R.id.button_mesSeances);
        button_mesSeances.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AcceuilActivity.this, com.muscu.benjamin.muscu.MesSeancesActivity.class);
                startActivity(intent);
            }
        });

        Button button_typesSeances = (Button) findViewById(R.id.button_typesSeances);
        button_typesSeances.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AcceuilActivity.this, TypesSeancesActivity.class);
                startActivity(intent);
            }
        });

        Button button_exercices = (Button) findViewById(R.id.button_exercices);
        button_exercices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AcceuilActivity.this, LesTypesExercicesActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acceuil, menu);
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

    public void onClickMesSeancesButton()
    {

    }
}
