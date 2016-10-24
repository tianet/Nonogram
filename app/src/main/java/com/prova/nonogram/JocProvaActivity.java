package com.prova.nonogram;

import android.app.Activity;
import android.graphics.Typeface;

public class JocProvaActivity extends Activity {

    public static final String GAME_PREFERENCES = "GamePrefs";

    protected Typeface fontJoc;

    public void init()
    {
        fontJoc = Typeface.createFromAsset(this.getAssets(), "fonts/arkitechbold.ttf");
    }
}