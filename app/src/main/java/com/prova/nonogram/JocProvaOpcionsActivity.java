package com.prova.nonogram;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class JocProvaOpcionsActivity extends JocProvaActivity {
    Button buttonArray [];
    private int [] colorButton = new int[6];
    private Button areaButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        colors = new int[6];
        //initiatValues();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcions);
        setColorButtons();

        /*cb = new Button[6];
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
        */

        findViewById(R.id.settings_button_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSettings();
            }
        });
        /*findViewById(R.id.settings_colors_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayColorMenu(v);
            }
        }); */



    }


    private void setSettings() {
        initiateValues();
        boolean valid;
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

        //RadioButton radioRandom = (RadioButton) findViewById(R.id.settings_difficult_random);
        //if(radioRandom.isChecked()) difficulty = Difficulty.RANDOM;
        /*
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
        */
        int aux=0;
        for(int i = 0; i < buttonArray.length; i++) {
            if(buttonArray[i].getText()=="SEL") {
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

    private int getIndexFromView(View v){
        try {
            Button b = (Button) v;
            for (int i = 0 ; i <6; i++){
                if (b.equals(buttonArray[i]))
                    return i;
            }
            return -1;
        }catch (Exception e) {
            return -1;
        }
    }

    private void setColorButtons() {
        colorButton[0] = 0xFF000000;
        colorButton[1] = 0xFF00FF00;
        colorButton[2] = 0xFF0000FF;
        colorButton[3] = 0xFFFF00FF;
        colorButton[4] = 0xFF00FFFF;
        colorButton[5] = 0xFFFFFF00;
        buttonArray = new Button[6];
        buttonArray[0] = (Button) findViewById(R.id.colorButton0);
        buttonArray[1] = (Button) findViewById(R.id.colorButton1);
        buttonArray[2] = (Button) findViewById(R.id.colorButton2);
        buttonArray[3] = (Button) findViewById(R.id.colorButton3);
        buttonArray[4] = (Button) findViewById(R.id.colorButton4);
        buttonArray[5] = (Button) findViewById(R.id.colorButton5);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int heigh = displaymetrics.heightPixels;
        for (int i =0; i<buttonArray.length ; i ++){
            buttonArray[i].setBackgroundColor(colorButton[i]);
            buttonArray[i].setText(""+(i+1));
            int e=0;
            for (int j = 0 ; j < 3; j++){
                int aux = ((colorButton[i]>>(8*j))&0xFF);
                e += aux * aux;
            }
            if (e<65536){
                buttonArray[i].setTextColor(0xFFFFFFFF);
            }else {
                buttonArray[i].setTextColor(0xFF000000);
            }
            buttonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    int num = getIndexFromView(v);
                    if (b.getText()!=("SEL")){
                        b.setText("SEL");
                    }else {
                        b.setText(""+(num+1));
                    }
                }
            });

            buttonArray[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    displayColorMenu(v);
                    return  true;
                }
            });
            buttonArray[i].setHeight(heigh/10);
            buttonArray[i].setMaxWidth(width/4);
            buttonArray[i].setWidth(20);
        }

    }

    private void initiateValues() {
        nRows = 0;
        nColors = 0;
        nColumns = 0;

    }

    public void displayColorMenu(View v){
        final Button colorbutton = (Button)v;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_color_dialog, null);
        dialogBuilder.setView(dialogView);
        int index = getIndexFromView(v);
        final SeekBar [] bars = new SeekBar[3];
        bars[0]  = (SeekBar) dialogView.findViewById(R.id.sb0);
        bars[1]  = (SeekBar) dialogView.findViewById(R.id.sb1);
        bars[2]  = (SeekBar) dialogView.findViewById(R.id.sb2);
        areaButton = (Button) dialogView.findViewById(R.id.colorArea);
        areaButton.setClickable(false);
        areaButton.setBackgroundColor(colors[index]);

        for (int i = 0 ; i <3 ; i++){
            bars[i].setMax(255);
            bars[i].setProgress((colors[index]>>8*i)&0xFF);
            bars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBar.setProgress(progress);
                    int red = bars[0].getProgress();
                    int green = bars[1].getProgress()<<8;
                    int blue = bars[2].getProgress()<<16;
                    int c = 0xFF000000+blue+green+red;
                    areaButton.setBackgroundColor(c);
                    colorButton[getIndexFromView(colorbutton)]=c;
                    colorbutton.setBackgroundColor(c);
                    colorbutton.setText("SEL");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            bars[0].setProgress(colorButton[index]&0xFF);
            bars[1].setProgress((colorButton[index]>>8)&0xFF);
            bars[2].setProgress((colorButton[index]>>16)&0xFF);
        }

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }





}