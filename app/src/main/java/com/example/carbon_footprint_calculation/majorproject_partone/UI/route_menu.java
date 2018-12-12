package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;

public class route_menu extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Context thisContext = this;
    private ListView routeList;
    private String strToDisplay;
    private Car selectedCar;
    private Route selectedRoute;
    private megaDataPackHelper DB;
    private SQLiteDatabase megaDB;
    private List<Route> allRoutes;
    public static Activity routeMenu;
    public static final int ADD_EDIT_ROUTE_CODE = 1;
    private static final String ROUTE_ID = "com.example.ottot.carbontracker.UI.add_route_page - DB use ID";
    private String transMode;
    private long carID;
    private long routeID;
    private DataPack data = DataPack.getData();
    final private String TRANS_MODE_CAR = "car";
    final private String TRANS_MODE_WALK = "walk/bike";
    final private String TRANS_MODE_BUS = "bus";
    final private String TRANS_MODE_SKYTRAIN = "skytrain";
    private Toolbar toolbar;
    private String unit;
    private int unitRID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_menu);
        routeMenu = this; //created to call finish on this object in other activity

        //--------------setup Database--------------
        DB = new megaDataPackHelper(this);
        megaDB = SQLiteDatabase.openOrCreateDatabase(DB.DB_PATH + DB.DB_NAME,null);
        DB.close();
        megaDB.close();
        //------------------------------------------
        allRoutes = DB.RR_getAllRoutes();

        unit = data.getUnit_setting();

        if (unit.equals("Kg")){
            unitRID = R.string.kg_CO2;
        }
        else {
            unitRID = R.string.tree_day_CO2;
        }

        toolbar = (Toolbar) findViewById(R.id.route_menu_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        TextView title = (TextView) findViewById(R.id.route_menu_toolbar_title);
        title.setText(R.string.route_menu);

        getTransitMode();
        setupAddRouteButton();
        populateRouteList();
        setupItemClick();
        setupItemLongClick();
    }

    //actionbar methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
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

    private void getTransitMode() {
        Intent intent = getIntent();

        transMode = intent.getStringExtra("transMode");
        if (transMode.equals("car")){
            carID = intent.getLongExtra("carID",0);
        }
        else {

        }
    }

    private void populateRouteList() {
        ArrayAdapter<Route> adapter = new routeArrayAdapter();
        routeList = (ListView)findViewById(R.id.list_route);
        routeList.setAdapter(adapter);

        //display message if list is empty
        TextView emptyRouteTxt = (TextView) findViewById(R.id.emptyRouteTxt);
        if(adapter.getCount()>0) {
            emptyRouteTxt.setVisibility(View.INVISIBLE);
        }
        else {
            emptyRouteTxt.setVisibility(View.VISIBLE);
        }
    }

    private class routeArrayAdapter extends ArrayAdapter<Route> {
        public routeArrayAdapter() {
            super(route_menu.this, R.layout.route_listview, allRoutes);//now we use DB instead of DataPack
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.route_listview, parent, false);
            }
            Route currentRoute = allRoutes.get(position);
            TextView routeName = (TextView)itemView.findViewById(R.id.routeName);
            routeName.setText(currentRoute.getName());
            TextView cityDistance = (TextView)itemView.findViewById(R.id.cityDistance);
            cityDistance.setText(getResources().getString(R.string.city_distance_route_menu) + String.valueOf(currentRoute.getCityDistance())+getResources().getString(R.string.km));
            TextView highwayDistance = (TextView)itemView.findViewById(R.id.highwayDistance);
            highwayDistance.setText(getResources().getString(R.string.highway_distance_route_menu) + String.valueOf(currentRoute.getHighwayDistance())+getResources().getString(R.string.km));
            TextView totalDistance = (TextView)itemView.findViewById(R.id.totalDistance);
            totalDistance.setText(getResources().getString(R.string.total_distance_route_menu) + String.valueOf(currentRoute.getTotalDistance())+getResources().getString(R.string.km));

            return itemView;
        }
    }

    private void setupAddRouteButton() {
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab_add_route);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = add_route_page.makeIntent(route_menu.this);
                intent.putExtra("transMode",transMode);
                intent.putExtra("carID", carID);
                intent.putExtra("mode","add");
                startActivityForResult(intent, ADD_EDIT_ROUTE_CODE);
            }
        });
    }

    private void setupItemClick() {
        ListView list = (ListView) findViewById(R.id.list_route);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the Car object selected in the previous page
                if(transMode.equals("car")) {
                    selectedCar = getCarFromIntent();
                }
                else {
                    selectedCar = null;
                }
                //create a Journey object with that Car and Route, and record the date when Journey is created
                //Journey constructor will do the calculations
                selectedRoute = allRoutes.get(position);

                //setup picking date dialog
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(route_menu.this, route_menu.this,
                        year, month, day);
                dateDialog.setTitle(getString(R.string.select_date));
                dateDialog.show();
            }
        });
    }

    private void setupItemLongClick() {
        ListView list = (ListView) findViewById(R.id.list_route);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Route selectedRoute = allRoutes.get(position);

                Intent intent = add_route_page.makeIntent(route_menu.this);
                intent.putExtra(ROUTE_ID,selectedRoute.getRouteID());
                intent.putExtra("mode","edit");
                startActivityForResult(intent, ADD_EDIT_ROUTE_CODE);
                return true;
            }
        });
    }

    private Car getCarFromIntent() {
        Car car = DB.RC_getCarByID(carID);
        return car;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ADD_EDIT_ROUTE_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    //populate route list here

                    allRoutes = DB.RR_getAllRoutes();//read from DB again everytime come back
                    populateRouteList();
                }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        String dateOfJourney = dateFormat.format(date); //get date of journey


        Journey journey = new Journey(transMode, selectedCar, selectedRoute, dateOfJourney);


        double totalEmission = journey.getTotalEmission(unit); //calculate emission with journey object

        DB.RJ_addJourney(selectedCar,selectedRoute,transMode,dateOfJourney,1); //add finished journey to db


        DecimalFormat decimalFormat = new DecimalFormat("#.##"); //limit emission to 2 decimal places
        strToDisplay= String.valueOf(decimalFormat.format(totalEmission) + " "+ getString(unitRID));//this is to set to the amount of CO2 emitted in the trip
        //display emissions in dialog
        feedbackDialog fd = new feedbackDialog();
        fd.setStrToDisplay(strToDisplay);
        fd.setIsEmissions(true);
        fd.setIsTips(false);
        fd.show(getSupportFragmentManager(),"emission dialog");

        //increment journeys entered today count
        SharedPreferences sharedPreferences = thisContext.getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int numJourneysEnteredToday = sharedPreferences.getInt("numJourneysEnteredToday", 0);
        numJourneysEnteredToday++;
        Log.i("ROUTEMENU", "" + numJourneysEnteredToday);
        editor.putInt("numJourneysEnteredToday", numJourneysEnteredToday);
        editor.commit();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, route_menu.class);
    }
}