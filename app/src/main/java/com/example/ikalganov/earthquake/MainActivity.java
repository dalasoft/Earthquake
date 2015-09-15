package com.example.ikalganov.earthquake;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    static final private int MENU_PREFERENCES = Menu.FIRST+1;
    static final private int MENU_UPDATE = Menu.FIRST+2;
    private static final int SHOW_PREFERENCES = 1;

    public int minimunMagnitude = 0;
    public boolean autoUpdateChecked = false;
    public int updateFreq = 0;

    private void updateFromPreferences() {
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int minMagIndex = prefs.getInt(PreferencesActivity.PREF_MIN_MAG_INDEX, 0);
        if (minMagIndex < 0)
            minMagIndex = 0;

        int freqIndex = prefs.getInt(PreferencesActivity.PREF_UPDATE_FREQ_INDEX, 0);
        if (freqIndex < 0)
            freqIndex = 0;

        autoUpdateChecked = prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);

        Resources r = getResources();
        String[] minMagValues = r.getStringArray(R.array.magnitude);
        String[] freqValues = r.getStringArray(R.array.update_freq_values);

        minimunMagnitude = Integer.valueOf(minMagValues[minMagIndex]);
        updateFreq = Integer.valueOf(freqValues[freqIndex]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        updateFromPreferences();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHOW_PREFERENCES)
            if (resultCode == Activity.RESULT_OK) {
                updateFromPreferences();
                FragmentManager fm = getFragmentManager();
                final EarthquakeListFragment earthquakeList = (EarthquakeListFragment)fm.findFragmentById(R.id.EarthquakeListFragment);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        earthquakeList.refreshEarthquakes();
                    }
                });
                t.start();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        //return super.onOptionsItemSelected(item);

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case (MENU_PREFERENCES): {
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivityForResult(i, SHOW_PREFERENCES);
                return  true;
            }
        }
        return false;
    }
}
