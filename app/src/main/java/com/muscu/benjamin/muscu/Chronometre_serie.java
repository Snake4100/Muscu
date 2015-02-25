package com.muscu.benjamin.muscu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muscu.benjamin.muscu.Entity.Serie;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Chronometre_serie.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Chronometre_serie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chronometre_serie extends DialogFragment{
    int mNum;
    Chronometer chronometer = null;
    boolean isChronometerRunning = false;
    private CountDownTimer countDown = null;
    int tempsAfaire = -1;
    int tempsFait = 0;
    TextView textView_chrono;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static Chronometre_serie newInstance(int num, long tempsAfaire) {
        Chronometre_serie frag = new Chronometre_serie();
        Bundle args = new Bundle();
        args.putInt("title", num);
        args.putInt("temps", (int)tempsAfaire);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
        tempsAfaire = getArguments().getInt("temps");

        // Pick a style based on the num.
        /*int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_chronometre_serie, container, false);


        this.chronometer = (Chronometer) v.findViewById(R.id.chronometer);
        this.chronometer.setTextSize(36);

        this.textView_chrono = (TextView) v.findViewById(R.id.textView_chronometer);
        //si c'est un chrono maximum
        if(tempsAfaire == -1)
        {
            //on vire le text view pour le chrono décompte
            this.textView_chrono.setVisibility(View.GONE);

            //on récupér le bouton
            final Button button_stop = (Button)v.findViewById(R.id.button_stopChronoSerie);

            Button button_start = (Button)v.findViewById(R.id.button_startChronoSerie);
            button_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //on démare le timer
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    isChronometerRunning = true;

                }
            });

            button_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temps = 0;
                    //on récupére le temps du chronometre
                    if(isChronometerRunning){
                        temps = (int)(SystemClock.elapsedRealtime() - Chronometre_serie.this.chronometer.getBase())/1000;
                        isChronometerRunning = false;
                        Chronometre_serie.this.chronometer.stop();
                    }

                    ((ExerciceActivity) getActivity()).addSerieChronometre(temps);
                    Chronometre_serie.this.dismiss();
                }
            });
        }

        else{
            //on vire le chrono
            v.findViewById(R.id.chronometer).setVisibility(View.GONE);
            setTime(this.tempsAfaire,this.textView_chrono);
            //on récupér le bouton
            final Button button_stop = (Button)v.findViewById(R.id.button_stopChronoSerie);

            Button button_start = (Button)v.findViewById(R.id.button_startChronoSerie);

            button_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //s'il y avait deja un timer on l'arréte
                    if(Chronometre_serie.this.countDown != null){
                        Chronometre_serie.this.countDown.cancel();
                    }

                    //on crée le timer
                    Chronometre_serie.this.countDown = new CountDownTimer(Chronometre_serie.this.tempsAfaire*1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            tempsFait++;
                            long secondesUntilFinished = millisUntilFinished / 1000;
                            Chronometre_serie.this.setTime(secondesUntilFinished, Chronometre_serie.this.textView_chrono);
                        }

                        public void onFinish() {
                            Chronometre_serie.this.setTime(0, Chronometre_serie.this.textView_chrono);
                        }
                    }.start();

                }
            });

            button_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temps = 0;
                    //on récupére le temps du chronometre
                    if(isChronometerRunning){
                        isChronometerRunning = false;
                        Chronometre_serie.this.chronometer.stop();
                    }

                    ((ExerciceActivity) getActivity()).addSerieChronometre(tempsFait);
                    Chronometre_serie.this.dismiss();
                }
            });
        }

        return v;
    }

    private void setTime(long secondesUntilFinished, TextView textView){
        long minutes = secondesUntilFinished / 60;
        long secondes = secondesUntilFinished % 60;

        String stringMinutes = String.valueOf(minutes);
        String stringSecondes = String.valueOf(secondes);

        if (minutes < 10) {
            stringMinutes = "0" + stringMinutes;
        }
        if (secondes < 10) {
            stringSecondes = "0" + stringSecondes;
        }

        textView.setText(stringMinutes + ":" + stringSecondes);
    }


}
