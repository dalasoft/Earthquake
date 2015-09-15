package com.example.ikalganov.earthquake;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

/**
 * Created by IKalganov on 15.09.2015.
 */
public class FragmentPreferences extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        //return super.isValidFragment(fragmentName);
        return UserPreferenceFragment.class.getName().equals(fragmentName);
    }
}
