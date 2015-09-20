package com.example.ikalganov.earthquake;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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

        minimunMagnitude = Integer.parseInt(prefs.getString(PreferencesActivity.PREF_MIN_MAG, "3"));
        updateFreq = Integer.parseInt(prefs.getString(PreferencesActivity.PREF_UPDATE_FREQ, "60"));
        autoUpdateChecked = prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        updateFromPreferences();

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());

        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setSearchableInfo(searchableInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHOW_PREFERENCES)
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
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case (MENU_PREFERENCES): {
                Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? PreferencesActivity.class : FragmentPreferences.class;
                Intent i = new Intent(this, c);
                startActivityForResult(i, SHOW_PREFERENCES);
                return  true;
            }
        }
        return false;
    }

}
