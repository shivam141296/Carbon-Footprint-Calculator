package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;


public class base_activity extends AppCompatActivity {
    private Context baseContext;
    private megaDataPackHelper DB;
    private BottomNavigation bottomBar;
    private static boolean isJourneyNotification = false;
    private static boolean isUtilityNotification = false;

    public Context getContext(){
        if (baseContext==null){
            baseContext = this;
        }
        return baseContext;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        baseContext = this;


        bottomBar = (BottomNavigation)findViewById(R.id.BottomBar);
        bottomBar.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {
                final int tab1 = R.id.tab_graph;
                final int tab2 = R.id.tab_journey;
                final int tab3 = R.id.tab_utility;
                android.support.v4.app.Fragment startFrag;

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                //ft.setCustomAnimations() TODO: can set switching view animation here
                switch (i){
                    case tab1:
                        startFrag = new main_menu();
                        ft.replace(R.id.activity_frame,startFrag);
                        ft.commit();
                        break;
                    case tab2:
                        startFrag = new journey_menu();
                        ft.replace(R.id.activity_frame,startFrag);
                        ft.commit();
                        break;
                    case tab3:
                        startFrag = new utility_menu();
                        ft.replace(R.id.activity_frame,startFrag);
                        ft.commit();
                        break;
                }
            }
            @Override
            public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {

            }
        });
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        Fragment startFrag;
        if(isJourneyNotification) {
            bottomBar.setDefaultSelectedIndex(1);
            startFrag = new journey_menu();
            isJourneyNotification = false;
        } else if(isUtilityNotification) {
            bottomBar.setDefaultSelectedIndex(2);
            startFrag = new utility_menu();
            isUtilityNotification = false;
        } else {
            bottomBar.setDefaultSelectedIndex(0);
            startFrag = new main_menu();
        }
        ft.replace(R.id.activity_frame,startFrag);
        ft.commit();
    }

    public static void setIsJourneyNotification(boolean bool) {
        isJourneyNotification = bool;
    }

    public static void setIsUtilityNotification(boolean bool) {
        isUtilityNotification = bool;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, base_activity.class);
    }

}
