package com.prova.nonogram;



import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends JocProvaActivity {
    private GameDisplay joc;
    private static TextView timerTextView;
    private static long startTime =0;
    public static Handler timerHandler = new Handler();
    public static Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        timerTextView = (TextView) findViewById(R.id.textClock);
    }

    @Override
    public void onStart() {
    	super.onStart();
        LinearLayout pantalla = (LinearLayout) findViewById(R.id.layoutJoc);
        super.init();
        // i ara cream per programa la zona de dibuix
        try {
            //colors = new int[6];
            //colors[0] = 0x0;
            //colors[1] = 0x00FF00;
            //colors[2] = 0x0000FF;
            //colors[3] = 0xFF00FF;
            //colors[4] = 0x00FFFF;
            //colors[5] = 0xFFFF00;
            Col figures []  =new Col[nColors];
            for (int i =0; i < nColors; i++) {
                figures[i] = new Col(colors[i], form);
            }


            Grid grid = new Grid(nRows,nColumns,figures);
            TopLeftIndicators tli = new TopLeftIndicators(grid);
            if (difficulty == Difficulty.RANDOM) {
                joc = new GameDisplay(this, grid ,  tli);
                // afegeix la pantalla de dibuix al Layout
                pantalla.addView(joc);
            } else {
                if(!(nColors>3 && nRows<=4 && nColumns<=4 )) {
                    while (TopLeftIndicators.getDifficulty(tli, grid)!=difficulty) {
                        grid = new Grid(nRows,nColumns,figures);
                        tli = new TopLeftIndicators(grid);
                    }
                } else {
                    grid = new Grid(nRows,nColumns,figures);
                    tli = new TopLeftIndicators(grid);
                }

                joc = new GameDisplay(this, grid ,  tli);
                // afegeix la pantalla de dibuix al Layout
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                pantalla.addView(joc);
            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public static void stopTime() {
        timerHandler.removeCallbacks(timerRunnable);
    }
}

