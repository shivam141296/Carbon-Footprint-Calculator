package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;
import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.extra.feedbackDialog;

import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Car;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Journey;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Route;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
* This class controls adding&editing route actions
* */

public class add_route_page extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DataPack data = DataPack.getData();
    private String mode;
    private long routeID;
    private Route newRoute;
    private Context thisContext = this;
    private static final String ROUTE_ID = "com.example.ottot.carbontracker.UI.add_route_page - DB use ID";
    private static final String ROUTE_CITY_KEY = "com.example.ottot.carbontracker.UI.add_route_page - Return route city distance";
    private static final String ROUTE_HIGHWAY_KEY = "com.example.ottot.carbontracker.UI.add_route_page - Return route highway distance";
    private static final String ROUTE_INDEX_KEY = "com.example.ottot.carbontracker.UI.add_route_page - Return route index";
    private static final String EMISSION_KEY = "com.example.ottot.carbontracker.UI.add_route_page - Return emission";
    private static megaDataPackHelper DB;
    private long carID;
    private String transportMode;
    private Route routeInJourney;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route_page);

        toolbar = (Toolbar) findViewById(R.id.add_route_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        TextView title = (TextView) findViewById(R.id.add_route_toolbar_title);
        title.setText(R.string.add_new_route);

        //setup db
        DB = new megaDataPackHelper(this);

        getTransModeFromIntent();
        setupView();
        setupEditTextListeners(); //possibly rename
    }

    //actionbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mode.equals("edit")) {
            inflater.inflate(R.menu.editmenu, menu);
        }
        else {
            inflater.inflate(R.menu.subpagemenu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_confirm:
                final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_saveRoute);
                EditText routeNameInput = (EditText) findViewById(R.id.edit_routeNickname);
                EditText cityDistanceInput = (EditText) findViewById(R.id.edit_city);
                EditText highwayDistanceInput = (EditText) findViewById(R.id.edit_highway);
                String routeName = routeNameInput.getText().toString();
                String cityDistance = cityDistanceInput.getText().toString();
                String highwayDistance = highwayDistanceInput.getText().toString();
                //check fields aren't empty, checkbox, and edit mode
                double cityDist = 0.0;
                double highwayDist = 0.0;
                if(routeName.length() == 0) {
                    Toast.makeText(add_route_page.this, R.string.emptyRouteName,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(cityDistance.length() == 0&&highwayDistance.length() == 0) {
                    Toast.makeText(add_route_page.this, R.string.emptyDistance,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (cityDistance.length() == 0){
                    highwayDist = Double.parseDouble(highwayDistance);
                }
                else if(cityDistance.equals(".")){
                    Toast.makeText(add_route_page.this, R.string.invalidDistance,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if(highwayDistance.length()==0){
                    cityDist = Double.parseDouble(cityDistance);
                }
                else if(highwayDistance.equals(".")){
                    Toast.makeText(add_route_page.this, R.string.invalidDistance,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    highwayDist = Double.parseDouble(highwayDistance);
                    cityDist = Double.parseDouble(cityDistance);
                }
                if (highwayDist+cityDist<=0){
                    Toast.makeText(add_route_page.this, R.string.invalidDistance, Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(checkBox.isChecked()){
                    DB.RR_addNewRoute(routeName,cityDist,highwayDist);
                    setResult(Activity.RESULT_OK);
                    finish();
                } else if(mode.equals("edit")) {

                    DB.RR_editRoute(routeID, cityDist,highwayDist,routeName);

                    setResult(Activity.RESULT_OK);
                    finish();
                } else if (mode.equals("edit_journey")){
                    routeInJourney.setCityDistance(cityDist);
                    routeInJourney.setHighwayDistance(highwayDist);
                    routeInJourney.setTotalDistance(cityDist+highwayDist);
                    routeInJourney.setName(routeName);
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                else {
                    //"do not save" implementation, to be done when implementing passing data through activities and adding to list of journeys

                    newRoute = new Route(routeName, cityDist, highwayDist); //temp route object for calculating emissions

                    //setup picking date dialog
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dateDialog = new DatePickerDialog(add_route_page.this,add_route_page.this,
                            year, month, day);
                    dateDialog.setTitle(getString(R.string.select_date));
                    dateDialog.show();

                    setResult(Activity.RESULT_CANCELED);
                }
                break;
            case R.id.action_delete:
                DB.RR_deleteRoute(routeID);
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB.close();
    }


    private void setupEditTextListeners() {
        final EditText editCity = (EditText) findViewById(R.id.edit_city);
        final EditText editHighway = (EditText) findViewById(R.id.edit_highway);
        editCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cityDistance = editCity.getText().toString();
                String highwayDistance = editHighway.getText().toString();
                if(cityDistance.length() == 0 || highwayDistance.length() == 0 || highwayDistance.equals(".")||cityDistance.equals(".")) {
                    //do nothing
                } else {
                    double cityDist = Double.parseDouble(cityDistance);
                    double highwayDist = Double.parseDouble(highwayDistance);
                    double total = cityDist + highwayDist;
                    TextView totalText = (TextView) findViewById(R.id.txt_totalDistance);
                    totalText.setText("" + total);
                }
            }
        });

        editHighway.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cityDistance = editCity.getText().toString();
                String highwayDistance = editHighway.getText().toString();
                if(cityDistance.length() == 0 || highwayDistance.length() == 0 || highwayDistance.equals(".")||cityDistance.equals(".")) {
                    //do nothing
                } else {
                    double cityDist = Double.parseDouble(cityDistance);
                    double highwayDist = Double.parseDouble(highwayDistance);
                    double total = cityDist + highwayDist;
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    TextView totalText = (TextView) findViewById(R.id.txt_totalDistance);
                    totalText.setText("" + decimalFormat.format(total));
                }
            }
        });
    }

    private void setupView() {
        //get data
        Intent intent = getIntent();
        routeID = intent.getLongExtra(ROUTE_ID,0);
        mode = intent.getStringExtra("mode");

        Route tempRoute = DB.RR_getRouteByID(routeID);
        String editRouteName = tempRoute.getName();
        double editRouteCityDist = tempRoute.getCityDistance();
        double editRouteHighwayDist = tempRoute.getHighwayDistance();
        double editRouteTotalDist = tempRoute.getTotalDistance();

        EditText nameEdit = (EditText) findViewById(R.id.edit_routeNickname);
        EditText cityEdit = (EditText) findViewById(R.id.edit_city);
        EditText highwayEdit = (EditText) findViewById(R.id.edit_highway);
        TextView totalShown = (TextView)findViewById(R.id.txt_totalDistance);
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox_saveRoute);
        //check if it is suppose to be edit or add
        if (mode.equals("edit")){
            checkbox.setVisibility(View.INVISIBLE);
            nameEdit.setText(editRouteName);
            cityEdit.setText("" + editRouteCityDist);
            highwayEdit.setText("" + editRouteHighwayDist);
            TextView title = (TextView) findViewById(R.id.add_route_toolbar_title);
            title.setText(R.string.edit_route_title);
            TextView instruction = (TextView)findViewById(R.id.txt_help);
            instruction.setVisibility(View.INVISIBLE);
            totalShown.setText(editRouteTotalDist+"");
        }
        else if (mode.equals("edit_journey")){
            routeInJourney = data.getEditJourney().getRoute();
            Route toEdit = data.getEditJourney().getRoute();
            checkbox.setVisibility(View.INVISIBLE);
            nameEdit.setText(toEdit.getName());
            cityEdit.setText("" + toEdit.getCityDistance());
            highwayEdit.setText("" + toEdit.getHighwayDistance());
            TextView title = (TextView) findViewById(R.id.add_route_toolbar_title);
            title.setText(R.string.edit_route_in_journey);
            TextView instruction = (TextView)findViewById(R.id.txt_help);
            instruction.setVisibility(View.INVISIBLE);
            totalShown.setText(toEdit.getTotalDistance()+"");
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, add_route_page.class);
    }



    private Journey createJourney(Car car, Route route, String date) {
        Journey journey = new Journey(transportMode, car, route, date);
        DB.RJ_addJourney(car, route, transportMode, date,0);

        return journey;
    }

    private void emissionsDialog(Journey journey) {
        double totalEmission = journey.getTotalEmission(data.getUnit_setting());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String unit = data.getUnit_setting();
        String strToDisplay;
        switch (unit){
            case "Kg":
                strToDisplay= String.valueOf(decimalFormat.format(totalEmission) + " "+ getResources().getString(R.string.kg_CO2));//this is to set to the amount of CO2 emitted in the trip
                break;
            case "Tree day":
                strToDisplay= String.valueOf(decimalFormat.format(totalEmission) + " "+ getResources().getString(R.string.tree_day_CO2));//this is to set to the amount of CO2 emitted in the trip
                break;
            default:
                strToDisplay= String.valueOf(decimalFormat.format(totalEmission) + " "+ getResources().getString(R.string.kg_CO2));//this is to set to the amount of CO2 emitted in the trip
                break;
        }

        feedbackDialog fd = new feedbackDialog();
        fd.setStrToDisplay(strToDisplay);
        fd.setIsEmissions(true);
        fd.show(getSupportFragmentManager(),"emission dialog");
    }

    private Car getCarFromIntent() {
        Intent intent = getIntent();
        carID = intent.getLongExtra("carID",0);
        Car car = DB.RC_getCarByID(carID);
        return car;
    }

    private void getTransModeFromIntent() {
        Intent intent = getIntent();
        transportMode = intent.getStringExtra("transMode");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        String dateOfJourney = dateFormat.format(date);

        //increment journeys entered today count
        SharedPreferences sharedPreferences = thisContext.getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int numJourneysEnteredToday = sharedPreferences.getInt("numJourneysEnteredToday", 0);
        numJourneysEnteredToday++;
        Log.i("ADDROUTE", "" + numJourneysEnteredToday);
        editor.putInt("numJourneysEnteredToday", numJourneysEnteredToday);
        editor.commit();
        //launch emissions dialog
        if(!transportMode.equals("car")) {
            //car is null
            emissionsDialog(createJourney(null, newRoute, dateOfJourney));
        }
        else {
            //use car from previous activity
            emissionsDialog(createJourney(getCarFromIntent(), newRoute, dateOfJourney));
        }
    }

}
