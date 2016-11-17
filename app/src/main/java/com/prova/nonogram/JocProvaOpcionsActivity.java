package com.prova.nonogram;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class JocProvaOpcionsActivity extends JocProvaActivity {
    boolean valid = true;
    Button[] cb ;
    int [] colorButton;
    boolean[] pressedColors ;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        nRows = 0;
        nColors = 0;
        nColumns = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcions);

        cb = new Button[6];
        for (int i = 0; i < cb.length;i++) {
            cb[i] = new Button(this);
        }
        colorButton = new int[6];
        colors = new int[6];
        colorButton[0] = 0xFF000000;
        colorButton[1] = 0xFF00FF00;
        colorButton[2] = 0xFF0000FF;
        colorButton[3] = 0xFFFF00FF;
        colorButton[4] = 0xFF00FFFF;
        colorButton[5] = 0xFFFFFF00;
        pressedColors = new boolean[6];

        findViewById(R.id.settings_button_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSettings();
            }
        });
        findViewById(R.id.settings_colors_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayColorMenu(v);
            }
        });



    }

    private void setSettings() {
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_rows);
        nRows = spinner1.getSelectedItemPosition() +2;

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_columns);
        nColumns = spinner2.getSelectedItemPosition() +2;


        RadioButton radioRound = (RadioButton) findViewById(R.id.settings_form_round);
        if(radioRound.isChecked()) {
            form = Form.CIRCLE;
        }
        RadioButton radioSquare = (RadioButton) findViewById(R.id.settings_form_square);
        if(radioSquare.isChecked()) {
            form = Form.SQUARE;
        }
        RadioButton radioEasy = (RadioButton) findViewById(R.id.settings_difficulty_easy);
        if(radioEasy.isChecked()) difficulty = Difficulty.EASY;

        RadioButton radioMedium = (RadioButton) findViewById(R.id.settings_difficulty_medium);
        if(radioMedium.isChecked()) difficulty = Difficulty.MEDIUM;

        RadioButton radioDifficult = (RadioButton) findViewById(R.id.settings_difficulty_difficult);
        if(radioDifficult.isChecked()) difficulty = Difficulty.DIFFICULT;

        RadioButton radioRandom = (RadioButton) findViewById(R.id.settings_difficult_random);
        if(radioRandom.isChecked()) difficulty = Difficulty.RANDOM;

        int aux=0;
        for(int i = 0; i < cb.length; i++) {
            if(cb[i].getText()=="OK") {
                nColors++;
                colors[aux] = colorButton[i];
                aux++;
            }
        }
        if(nColors!=0) {
            valid = true;
        } else {
            valid = false;
            Toast toast = Toast.makeText(this, "Select the colors", Toast.LENGTH_LONG);
            toast.show();
        }

        if(valid) {
            Intent i = new Intent(JocProvaOpcionsActivity.this, MainActivity.class);
            startActivity(i);

        }




    }

    public void displayColorMenu(View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_color_layout, null);
        dialogBuilder.setView(dialogView);
        LinearLayout gl = (LinearLayout) dialogView.findViewById(R.id.linearLayout);
        LinearLayout l1 = new LinearLayout(this);
        LinearLayout l2 = new LinearLayout(this);
        l1.setOrientation(LinearLayout.HORIZONTAL);
        l2.setOrientation(LinearLayout.HORIZONTAL);


        for (int i = 0; i < 6; i++){
            cb[i].setText("");
            cb[i].setTextSize(30);
            cb[i].setWidth(200);
            cb[i].setHeight(200);
            int e=0;
            for (int j = 0 ; j < 3; j++){
                e += ((colors[i]>>16*j)&0xFF)*((colors[i]>>16*j)&0xFF);
            }
            System.out.println(e);
            if (e<3*127*127){
                cb[i].setTextColor(0xFFFFFFFF);
            }else {
                cb[i].setTextColor(0xFF000000);
            }
            cb[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button)v;
                    if(b.getText()=="OK")
                        b.setText("");
                    else
                        b.setText("OK");
                }
            });
            cb[i].setBackgroundColor(colorButton[i]);
            if (i <3){
                l1.addView(cb[i]);
            }else{
                l2.addView(cb[i], ViewGroup.LayoutParams.MATCH_PARENT);
            }

            /*cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Button)v).setText("hola");
                }
            });*/
        }
        gl.addView(l1);
        gl.addView(l2);
        //EditText editText = (EditText) dialogView.findViewById(R.id.label_field);
        //editText.setText("test label");
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }



}