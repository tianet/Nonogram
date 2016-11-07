package com.prova.nonogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class JocProvaOpcionsActivity extends JocProvaActivity {
    boolean valid = true;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcions);


        findViewById(R.id.settings_button_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSettings();
            }
        });


    }

    private void setSettings() {
        EditText editColors  = (EditText) findViewById(R.id.settings_colors_in);
        int aux;
        valid = true;
        if(!editColors.getText().toString().equals("")) {
            aux =  Integer.parseInt(editColors.getText().toString());
            if (aux <= 6) {
                nColors = aux;
            } else  {
                valid = false;
                Toast toast = Toast.makeText(getApplicationContext(), "Too many Colors", Toast.LENGTH_LONG);
                toast.show();
            }
        } else  {
            valid = false;
            Toast toast = Toast.makeText(getApplicationContext(), "Number of colors is empty", Toast.LENGTH_LONG);
            toast.show();
        }

        EditText editRows = (EditText) findViewById(R.id.settings_rows_in);
        if(!editRows.getText().toString().equals("")) {
            aux =  Integer.parseInt(editRows.getText().toString());
            if (aux <= 15) {
                nRows = aux;
            } else  {
                valid = false;
                Toast toast = Toast.makeText(getApplicationContext(), "Too many Rows", Toast.LENGTH_LONG);
                toast.show();
            }
        } else  {
            valid = false;
            Toast toast = Toast.makeText(getApplicationContext(), "Number of rows is empty", Toast.LENGTH_LONG);
            toast.show();
        }

        EditText editColumns= (EditText) findViewById(R.id.settings_columns_in);
        if(!editColumns.getText().toString().equals("")) {
            aux =  Integer.parseInt(editColumns.getText().toString());
            if (aux <= 15) {
                nColumns = aux;
            } else  {
                valid = false;
                Toast toast = Toast.makeText(getApplicationContext(), "Too many Columns", Toast.LENGTH_LONG);
                toast.show();
            }
        } else  {
            valid = false;
            Toast toast = Toast.makeText(getApplicationContext(), "Number of columns is empty", Toast.LENGTH_LONG);
            toast.show();
        }

        RadioButton radioRound = (RadioButton) findViewById(R.id.settings_form_round);
        if(radioRound.isChecked()) {
            form = Form.CIRCLE;
        }
        RadioButton radioSquare = (RadioButton) findViewById(R.id.settings_form_square);
        if(radioSquare.isChecked()) {
            form = Form.SQUARE;
        }



        if(valid) {
            Intent i = new Intent(JocProvaOpcionsActivity.this, MainActivity.class);
            startActivity(i);
        }

    }



}