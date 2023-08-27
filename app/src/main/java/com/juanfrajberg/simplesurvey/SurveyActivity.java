package com.juanfrajberg.simplesurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Random;

public class SurveyActivity extends AppCompatActivity {

    //Variable para saber si ya se puede seleccionar una opción
    private boolean optionClickable = true;

    //Variable para el número de la pregunta en el títutlo
    private int numberQuestion = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Código básico para que se muestre la interfaz
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity);
    }

    //Función cuando se hace clic en una opción (tarjeta)
    public void optionSelected(View view) {
        //Se verifica si se puede hacer clic en la opción, primero tienen que reproducirse las animaciones
        if (optionClickable) {
            YoYo.with(Techniques.Pulse)
                    .duration(300)
                    .repeat(0)
                    .playOn(view);

            //Para que no se pueda seleccionar ninguna opción hasta que se reproduzcan las animaciones
            optionClickable = false;

            //Se espera a que se termine de reproducir la animación al hacer clic en una opción
            Handler waitClickAnimation = new Handler();
            waitClickAnimation.postDelayed(new Runnable() {
                public void run() {
                    //Se reproduce la animación en el título y se actualiza el número de pregunta
                    TextView title = (TextView) findViewById(R.id.survey_title_textview);
                    numberQuestion++;
                    title.setText("Pregunta N°" + numberQuestion);
                    YoYo.with(Techniques.FadeInUp)
                            .duration(300)
                            .repeat(0)
                            .playOn(title);

                    //Se eliminan todas las opciones del GridLayout
                    GridLayout inflatedOptions = (GridLayout) findViewById(R.id.survey_options_gridlayout);
                    inflatedOptions.removeAllViews();

                    //Se crea un número random que determina la cantidad de opciones de esa pregunta
                    final int minNumberOptions = 2;
                    final int maxNumberOptions = 6;
                    final int randomNumberOptions = new Random().nextInt((maxNumberOptions - minNumberOptions) + 1) + minNumberOptions;

                    //Se crea (infla) el layout con las opciones
                    for (int i = 0; i < randomNumberOptions; i++) {
                        View optionToAdd = getLayoutInflater().inflate(R.layout.option_layout, inflatedOptions, false);
                        //Para la última tarjeta que se infla, si es impar, se estira y usa toda la pantalla
                        if (i == (randomNumberOptions - 1) && (randomNumberOptions % 2 != 0)) {
                            optionToAdd = getLayoutInflater().inflate(R.layout.unevenlastoption_layout, inflatedOptions, false);
                        }
                        inflatedOptions.addView(optionToAdd);

                        //Se le coloca el número de la opción a cada tarjeta
                        TextView inflatedText = (TextView) optionToAdd.findViewById(R.id.option_number_textview);
                        inflatedText.setText("Opción N°" + (i + 1));

                        //Cada opción ejecuta su animación
                        YoYo.with(Techniques.FadeInUp)
                                .duration(450)
                                .repeat(0)
                                .playOn(optionToAdd);

                        //Se espera a que se reproduzca la animación de las tarjetas para que se pueda seleccionar otra opción
                        Handler waitOptionAnimation = new Handler();
                        waitOptionAnimation.postDelayed(new Runnable() {
                            public void run() {
                                optionClickable = true;
                            }
                        }, 450);
                    }
                }
            }, 300);
        }
    }
}