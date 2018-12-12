package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.carbon_footprint_calculation.majorproject_partone.R;

import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;
import com.example.carbon_footprint_calculation.majorproject_partone.extra.pickDateDialog;
import com.example.carbon_footprint_calculation.majorproject_partone.data.vehicleDataHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Journey;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Utility;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class main_menu extends android.support.v4.app.Fragment {
    private PieChart chart;
    private megaDataPackHelper DB;
    private vehicleDataHelper VDBHelper;
    private SQLiteDatabase mDB;
    static boolean isJourneyAdded;
    private List<Journey> allJourneys;
    private List<Utility> allUtilities;
    private String TAG_MAIN_MENU ="MAIN MENU" ;
    private int TRIP_WARN_NUM = 5;
    private BottomNavigation bottomBar;
    private View v;
    private Toolbar toolbar;
    private DataPack data = DataPack.getData();
    private String unit;
    private int unitRID;
    final private String TRANS_MODE_CAR = "car";
    final private String TRANS_MODE_WALK = "walk/bike";
    final private String TRANS_MODE_BUS = "bus";
    final private String TRANS_MODE_SKYTRAIN = "skytrain";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_main_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();

        toolbar = (Toolbar) v.findViewById(R.id.main_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView title = (TextView) v.findViewById(R.id.main_toolbar_title);
        title.setText(R.string.app_name);

        setUnit();

        chart = (PieChart) v.findViewById(R.id.chart);

        //Only the main_menu needs to load VDBHelper
        VDBHelper = new vehicleDataHelper(getActivity());
        DB = new megaDataPackHelper(getActivity());
        mDB = DB.getWritableDatabase();
        mDB.close();
        VDBHelper.close();
        DB.close();

        allJourneys = DB.RJ_getAllJourney();
        allUtilities = DB.UB_getAllUtilities();
        setupPieChart();
    }

    private void setUnit() {
        unit = data.getUnit_setting();

        if (unit.equals("Kg")){
            unitRID = R.string.kg;
        }
        else {
            unitRID = R.string.tree_day;
        }
    }

    //actionbar methods
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mainmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.more_graphs:
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                pickDateDialog dialog = new pickDateDialog();
                dialog.setCallBack(onDateSetListener);
                dialog.show(manager,"PickDate");
                return true;
            case R.id.optimize_route:
                Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage("carbonfootprint.majorproject_parttwo.vehicular_route_optimization");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
                return true;
            case R.id.action_settings:
                Intent setting = setting_page.makeIntent(getActivity());
                startActivity(setting);
                return true;
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void closeDB() {
        DB.close();
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, main_menu.class);
    }

    private void addToList(List<String> list,List<Double> values, String name, Double value){
        boolean alreadyHas = false;
        int index=0;
        int size = list.size();
        for (int i = 0; i <size;i++){
            String string = list.get(i);
            if (name.equals(string)){
                alreadyHas = true;
                index = i;
            }
        }
        if (!alreadyHas){
            list.add(name);
            values.add(value);
        }
        else {
            Double temp = values.get(index) + value;
            values.set(index,temp);
        }
    }

    private void setupPieChart() {
        //populate a list of pie entries

        List<PieEntry> pieEntries = new ArrayList<>();

        float sumBusEmission = 0;
        float sumSkytrainEmission = 0;

        List<String> cars = new ArrayList<>();
        List<Double> carEmissions = new ArrayList<>();

        for (Journey journey : allJourneys) {
            String transMode = journey.getTransMode();
            switch (transMode) {
                case TRANS_MODE_CAR:
                    addToList(cars,carEmissions,journey.getCar().getNickname(),journey.getTotalEmission(unit));
                    break;
                case TRANS_MODE_BUS:
                    sumBusEmission += journey.getTotalEmission(unit).floatValue();
                    break;
                case TRANS_MODE_SKYTRAIN:
                    sumSkytrainEmission += journey.getTotalEmission(unit).floatValue();
                    break;
            }
        }
        int size = cars.size();
        for (int i = 0; i<size;i++){
            pieEntries.add(new PieEntry(carEmissions.get(i).floatValue(),cars.get(i)));
        }
        if (sumBusEmission != 0) {
            pieEntries.add(new PieEntry(sumBusEmission, getString(R.string.bus)));
        }
        if (sumSkytrainEmission != 0) {
            pieEntries.add(new PieEntry(sumSkytrainEmission, getString(R.string.skytrain)));
        }

        int utilityListSize = allUtilities.size();
        for (int i = 0; i <utilityListSize;i++){
            String utilityType = allUtilities.get(i).getBillType();
            Utility tempUtility = allUtilities.get(i);
            String[] billType = getResources().getStringArray(R.array.bill_type);
            Float utitlityEmission = tempUtility.getEmissionPerPerson(unit).floatValue()*tempUtility.getDays();
            String label;
            switch(utilityType){
                case "Natural Gas":
                    utilityType = billType[1];
                    break;
                case "Electricity":
                    utilityType = billType[0];
                    break;
            }
            pieEntries.add(new PieEntry(utitlityEmission,utilityType));
        }
        PieDataSet dataSet;

        dataSet= new PieDataSet(pieEntries,getString(R.string.chart_label)+"("+getString(unitRID)+")");
        dataSet.setValueTextSize(10);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);

        //Get the chart:

        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        data.setUnit_setting(getPrefs.getString("carbon_unit","Kg"));
        setUnit();
        setupPieChart();

    }

    public DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            Date date = calendar.getTime();
            String dateToDisplay = dateFormat.format(date);
            Intent intent = more_graphs.makeIntentWithData(getActivity(), dateToDisplay, 0, null);
            startActivity(intent);
        }
    };


}