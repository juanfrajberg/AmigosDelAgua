package com.juanfrajberg.simplesurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {

    private int numberQuestion = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void optionSelected(View view) {
        YoYo.with(Techniques.Pulse)
                    .duration(300)
                    .repeat(0)
                    .playOn(view);

        TextView title = (TextView) findViewById(R.id.survey_title_textview);
        numberQuestion++;
        title.setText("Pregunta N°" + numberQuestion);

        YoYo.with(Techniques.FadeInUp)
                .duration(300)
                .repeat(0)
                .playOn(title);

        //Se crea (infla) el layout con las propuestas
        GridLayout inflatedOptions = (GridLayout) findViewById(R.id.survey_options_gridlayout);
        View optionToAdd = getLayoutInflater().inflate(R.layout.option_layout, inflatedOptions, false);
        inflatedOptions.addView(optionToAdd);
    }
}