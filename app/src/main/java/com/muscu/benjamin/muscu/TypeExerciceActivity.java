package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.muscu.benjamin.muscu.Entity.Categorie;
import com.muscu.benjamin.muscu.Entity.TypeExercice;
import com.muscu.benjamin.muscu.R;

import java.lang.reflect.Type;
import java.util.Arrays;

public class TypeExerciceActivity extends Activity {

    TypeExercice leTypeExercice;
    EditText editTextNom;
    EditText editTextZones;
    EditText editTextDescription;
    Spinner spinnerCategorie;

    ArrayAdapter<Categorie> categorieSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_exercice);

        this.setTitle("Modification exercice");
        
        this.leTypeExercice = getIntent().getParcelableExtra("typeExercice");

        //on crée et on ajoute la ligne qui contiendra le bouton de validation
        LinearLayout linearLayout_TypeExercice = (LinearLayout) findViewById(R.id.LinearLayout_TypeExercice);
        TableRow ligneBoutonValidation = new TableRow(TypeExerciceActivity.this);
        Button boutonValidation = new Button(TypeExerciceActivity.this);
        ligneBoutonValidation.addView(boutonValidation);
        linearLayout_TypeExercice.addView(ligneBoutonValidation);

        //on récupérer les elements de la vue
        this.editTextNom = (EditText)findViewById(R.id.editText_nomTypeExercice);
        this.editTextZones = (EditText)findViewById(R.id.editText_zonesTypeExercice);
        this.editTextDescription = (EditText)findViewById(R.id.editText_descriptionTypeExercice);
        this.spinnerCategorie = (Spinner)findViewById(R.id.spinner_categorieTypeExercice);
        this.categorieSpinnerAdapter = new ArrayAdapter<Categorie>(this, android.R.layout.simple_spinner_item, Arrays.asList(Categorie.values()));
        this.spinnerCategorie.setAdapter(this.categorieSpinnerAdapter);

        //si c'est une création
        if(this.leTypeExercice == null){
            boutonValidation.setText("Créer");
            boutonValidation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        //si c'est une modification
        else{
            boutonValidation.setText("Créer");
            boutonValidation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
