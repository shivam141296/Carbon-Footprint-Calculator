package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.carbon_footprint_calculation.majorproject_partone.R;



public class preferencesSetting extends PreferenceFragment {

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


            setUpAboutPage();
        }



        private void setUpAboutPage() {
            Preference about = findPreference("about");
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent goToAbout = about_page.makeIntent(getActivity());
                    startActivity(goToAbout);
                    return false;
                }
            });
        }


}
