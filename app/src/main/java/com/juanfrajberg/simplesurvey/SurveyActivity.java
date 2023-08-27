package com.juanfrajberg.simplesurvey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.w3c.dom.Text;

import java.util.Random;

public class SurveyActivity extends AppCompatActivity {

    //Variable para saber si ya se puede seleccionar una opción
    private boolean optionClickable = true;

    //Variable para el número de la pregunta en el títutlo
    private int numberQuestion = 1;

    //Pära saber si se ha reproducido la animación de festejo
    private boolean animationPlayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Código básico para que se muestre la interfaz
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity);
    }

    //Función cuando se hace clic en una opción (tarjeta)
    public void optionSelected(View view) {
        //Se verifica si ya es la última preguntar, en caso de ser así se festeja
        if (numberQuestion == 10 && !animationPlayed) {
            //Se reproduce la animación de clic en la opción elegida
            YoYo.with(Techniques.Pulse)
                    .duration(300)
                    .repeat(0)
                    .playOn(view);

            //Se oscurece el fondo y se muestra la animación
            LottieAnimationView lottieAnimation = (LottieAnimationView) findViewById(R.id.survey_thumbsupanimation_lottieanimationview);
            View greyPanel = (View) findViewById(R.id.survey_panel_view);
            TextView thanksText = (TextView) findViewById(R.id.survey_thanks_textview);
            greyPanel.setVisibility(View.VISIBLE);
            greyPanel.setAlpha(0f);
            greyPanel.animate().alpha(0.75f).setDuration(750);

            //Se espera a que termine la animación del panel (View) y aparece el GIF
            Handler waitPanelAnimation = new Handler();
            waitPanelAnimation.postDelayed(new Runnable() {
                public void run() {
                    lottieAnimation.setVisibility(View.VISIBLE);
                    thanksText.setVisibility(View.VISIBLE);
                    thanksText.setTextColor(Color.TRANSPARENT);

                    lottieAnimation.setScaleX(0);
                    lottieAnimation.setScaleY(0);

                    lottieAnimation.animate().scaleX(3).setDuration(750);
                    lottieAnimation.animate().scaleY(3).setDuration(750);

                    Handler waitDogAnimation = new Handler();
                    waitDogAnimation.postDelayed(new Runnable() {
                        public void run() {
                            thanksText.setTextColor(Color.WHITE);
                            YoYo.with(Techniques.SlideInUp)
                                    .duration(750)
                                    .repeat(0)
                                    .playOn(thanksText);
                        }
                    }, 750);
                }
            }, 750);

            //Para que no se puedan seleccionar más opciones
            optionClickable = false;
            animationPlayed = true;
        }

        //Se verifica si se puede hacer clic en la opción, primero tienen que reproducirse las animaciones
        if (optionClickable && !animationPlayed) {
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