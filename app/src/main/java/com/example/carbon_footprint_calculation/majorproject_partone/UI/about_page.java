package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
/*
* This class controls about page, nothing much.
* */
public class about_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        Toolbar toolbar = (Toolbar)findViewById(R.id.about_page_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle(R.string.about);
        toolbar.setTitleTextColor(getResources().getColor(R.color.actionBarText));

        setLinks();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, about_page.class);
    }

    private void setLinks() {
        TextView CShomepage = (TextView) findViewById(R.id.txt_CShomepage);
        TextView splashBG = (TextView) findViewById(R.id.link_splash_bg);
        TextView appBG = (TextView) findViewById(R.id.link_app_bg);
        TextView utilityBG = (TextView) findViewById(R.id.link_utility_bg);
        TextView routeBG = (TextView) findViewById(R.id.link_route_bg);
        TextView materialIcons = (TextView) findViewById(R.id.link_material_icons);
        TextView carIcons = (TextView) findViewById(R.id.link_car_icons);
        TextView walkIcon = (TextView) findViewById(R.id.link_transmode_icon);
        TextView routeIcon = (TextView) findViewById(R.id.link_route_icon);

        CShomepage.setMovementMethod(LinkMovementMethod.getInstance());
        splashBG.setMovementMethod(LinkMovementMethod.getInstance());
        appBG.setMovementMethod(LinkMovementMethod.getInstance());
        utilityBG.setMovementMethod(LinkMovementMethod.getInstance());
        routeBG.setMovementMethod(LinkMovementMethod.getInstance());
        materialIcons.setMovementMethod(LinkMovementMethod.getInstance());
        carIcons.setMovementMethod(LinkMovementMethod.getInstance());
        walkIcon.setMovementMethod(LinkMovementMethod.getInstance());
        routeIcon.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
