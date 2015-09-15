package com.example.ikalganov.earthquake;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by IKalganov on 15.09.2015.
 */
public class UserPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.userpreferences);
    }

}
