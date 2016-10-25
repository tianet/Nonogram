package com.prova.nonogram;



import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends JocProvaActivity {
    private GameDisplay joc;

 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
    }

    @Override
    public void onStart() {
    	super.onStart();
        LinearLayout pantalla = (LinearLayout) findViewById(R.id.layoutJoc);
        super.init();
        // i ara cream per programa la zona de dibuix
        try {

            Col colors []  =new Col[3];
            colors[0] = new Col(0x0,Form.SQUARE);
            colors[1] = new Col(0x00FF00,Form.SQUARE);
            colors[2] = new Col(0x0941f5 ,Form.CIRCLE);

            Grid grid = new Grid(5,4,colors);
            TopLeftIndicators tli = new TopLeftIndicators(grid);
            joc = new GameDisplay(this, grid ,  tli);

            // afegeix la pantalla de dibuix al Layout
            pantalla.addView(joc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
