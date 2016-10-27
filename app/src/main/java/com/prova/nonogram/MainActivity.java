package com.prova.nonogram;



import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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

            Col colors []  =new Col[6];
            colors[0] = new Col(0x0,Form.SQUARE);
            colors[1] = new Col(0x00FF00,Form.SQUARE);
            colors[2] = new Col(0x0000FF ,Form.CIRCLE);
            colors[3] = new Col(0xFF0000 ,Form.CIRCLE);
            colors[4] = new Col(0xFF00FF ,Form.SQUARE);
            colors[5] = new Col(0x00FFFF ,Form.CIRCLE);

            Grid grid = new Grid(5,5,colors);
            TopLeftIndicators tli = new TopLeftIndicators(grid);
            joc = new GameDisplay(this, grid ,  tli);

            // afegeix la pantalla de dibuix al Layout
            pantalla.addView(joc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
