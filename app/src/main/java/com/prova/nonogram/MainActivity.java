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
            colors = new int[6];
            colors[0] = 0x0;
            colors[1] = 0x00FF00;
            colors[2] = 0x0000FF;
            colors[3] = 0xFF00FF;
            colors[4] = 0x00FFFF;
            colors[5] = 0xFFFF00;
            Col figures []  =new Col[nColors];
            for (int i =0; i < nColors; i++) {
                figures[i] = new Col(colors[i], form);
            }


            Grid grid = new Grid(nRows,nColumns,figures);
            TopLeftIndicators tli = new TopLeftIndicators(grid);
            joc = new GameDisplay(this, grid ,  tli);

            // afegeix la pantalla de dibuix al Layout
            pantalla.addView(joc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
