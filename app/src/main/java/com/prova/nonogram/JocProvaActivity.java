package com.prova.nonogram;

import android.app.Activity;
import android.graphics.Typeface;

public class JocProvaActivity extends Activity {

    public static final String GAME_PREFERENCES = "GamePrefs";

    public static int  nColors;
    public static int nRows;
    public static int nColumns;
    public  enum Difficulty  {
            EASY,
            MEDIUM,
            DIFFICULT,
            RANDOM};
    public static Difficulty difficulty;
    public static int[] colors;
    public static Form form;
    protected Typeface fontJoc;

    public void init()
    {
        fontJoc = Typeface.createFromAsset(this.getAssets(), "fonts/arkitechbold.ttf");
    }
}