package com.example.jor.hospital;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by jor on 01.04.2016.
 */
public class Settings extends PreferenceActivity{

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // call segment fragment
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content,new SettingsFragment())
                    .commit();
        }
}
