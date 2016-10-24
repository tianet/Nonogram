package com.prova.nonogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class JocProvaPresentacioActivity extends JocProvaActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentacio);
        startAnimating();
    }

    private void startAnimating() {
        // Fade in Titol1
        TextView logo1 = (TextView) findViewById(R.id.Titol1);
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo1.startAnimation(fade1);
        // Fade in Titol2
        TextView logo2 = (TextView) findViewById(R.id.Titol2);
        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
        logo2.startAnimation(fade2);
        // Quan acabi el segon titol ves a la pantalla de menu
        fade2.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                
               startActivity(new Intent(JocProvaPresentacioActivity.this, MainActivity.class));
                //JocProvaPresentacioActivity.this.finish();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        // Anima tots els elements del TableLayout
        Animation spinin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        LayoutAnimationController controller = new LayoutAnimationController(spinin);
        TableLayout table = (TableLayout) findViewById(R.id.TableLayout01);
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            row.setLayoutAnimation(controller);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Atura les animacions
        TextView logo1 = (TextView) findViewById(R.id.Titol1);
        logo1.clearAnimation();
        TextView logo2 = (TextView) findViewById(R.id.Titol2);
        logo2.clearAnimation();
        TableLayout table = (TableLayout) findViewById(R.id.TableLayout01);
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            row.clearAnimation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Torna a comen�ar les animacions
        startAnimating();
    }
}
