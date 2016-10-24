package com.prova.nonogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class JocProvaMenuActivity extends JocProvaActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(menu.NONE, 0, 0, "Inici");
        menu.add(menu.NONE, 1, 1, "Sortir");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
        	startActivity(new Intent(JocProvaMenuActivity.this, JocProvaPresentacioActivity.class));
            JocProvaMenuActivity.this.finish();
        } else {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}