package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;

import com.muscu.benjamin.muscu.DAO.TypeExerciceDAO;
import com.muscu.benjamin.muscu.Entity.Categorie;
import com.muscu.benjamin.muscu.Entity.TypeExercice;

import java.util.Arrays;

public class TypeExerciceActivity extends Activity {

    TypeExercice leTypeExercice;
    EditText editTextNom;
    EditText editTextZones;
    EditText editTextDescription;
    Spinner spinnerCategorie;

    ArrayAdapter<Categorie> categorieSpinnerAdapter;

    TypeExerciceDAO daoTypeExercice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_exercice);

        this.setTitle("Modification exercice");
        
        this.leTypeExercice = getIntent().getParcelableExtra("typeExercice");

        this.recuperationElementsVue();

        Button boutonValidation = (Button)findViewById(R.id.button_validerTypeExercice);

        this.daoTypeExercice = new TypeExerciceDAO(this.getBaseContext());
        this.daoTypeExercice.open();

        //si c'est une création
        if(this.leTypeExercice == null){
            this.leTypeExercice = new TypeExercice();
            boutonValidation.setText("Créer");
            boutonValidation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TypeExerciceActivity.this.daoTypeExercice.create(TypeExerciceActivity.this.leTypeExercice);
                    finish();
                }
            });
        }

        //si c'est une modification
        else{
            boutonValidation.setText("Enregistrer");
            boutonValidation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //on modifie
                    TypeExerciceActivity.this.daoTypeExercice.modifier(TypeExerciceActivity.this.leTypeExercice);
                    finish();
                }
            });

            //on met les valeurs par défault
            this.editTextNom.setText(this.leTypeExercice.getNom());
            this.editTextZones.setText(this.leTypeExercice.getZones());
            this.editTextDescription.setText(this.leTypeExercice.getDescription());

            // on séléctionne la bonne catégorie
            for(int i = 0; i<Categorie.values().length; i++)
            {
                Log.e("Debug", Categorie.values()[i].toString()+" = "+this.leTypeExercice.getCategorie().toString());
                if(Categorie.values()[i].toString().equals(this.leTypeExercice.getCategorie().toString())){
                    Log.e("Debug", "yes");
                    this.spinnerCategorie.setSelection(i);
                }
            }

        }

        this.initialisationListenerElementsVue();
    }

    private void recuperationElementsVue(){
        //On initialise le text edit du nom
        this.editTextNom = (EditText)findViewById(R.id.editText_nomTypeExercice);

        //on initialise le text edit de la zone
        this.editTextZones = (EditText)findViewById(R.id.editText_zonesTypeExercice);

        //on initialise le text edit de la description
        this.editTextDescription = (EditText)findViewById(R.id.editText_indicationsTypeExercice);

        //on initilaise le spinner
        this.spinnerCategorie = (Spinner)findViewById(R.id.spinner_categorieTypeExercice);
        this.categorieSpinnerAdapter = new ArrayAdapter<Categorie>(this, android.R.layout.simple_spinner_item, Arrays.asList(Categorie.values()));
        this.spinnerCategorie.setAdapter(this.categorieSpinnerAdapter);

    }

    private void initialisationListenerElementsVue(){
        this.editTextNom.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TypeExerciceActivity.this.leTypeExercice.setNom(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.editTextZones.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TypeExerciceActivity.this.leTypeExercice.setZones(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TypeExerciceActivity.this.leTypeExercice.setDescription(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TypeExerciceActivity.this.leTypeExercice.setCategorie(TypeExerciceActivity.this.categorieSpinnerAdapter.getItem(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_type_exercice, menu);
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
