package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;
import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Journey;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Utility;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class more_graphs extends AppCompatActivity {
    public static final int FOUR_WEEKS = 28;
    public static final int ONE_YEAR = 365;
    public static final float AVERAGE_CANADIAN_CO2_EMISSIONS_PER_DAY = 40.22f;
    public static final float AVERAGE_CANADIAN_CO2_EMISSIONS_PER_30_DAYS = 1206.24f;
    public static final float AVERAGE_CANADIAN_CO2_EMISSIONS_PER_5_DAYS = 201.10f;
    public static final float TARGET_CANADIAN_CO2_EMISSIONS_PER_DAY = 28.15f;
    public static final float TARGET_CANADIAN_CO2_EMISSIONS_PER_30_DAYS = 844.37f;
    public static final float TARGET_CANADIAN_CO2_EMISSIONS_PER_5_DAYS = 140.77f;
    private megaDataPackHelper DB;
    private static final String DATE_KEY = "com.example.carbon_footprint_calculation.majorproject_partone.more_graphs - Pass date into activity";
    private static final String RANGE_KEY = "com.example.carbon_footprint_calculation.majorproject_partone.more_graphs - Pass range into activity";
    private static final String TYPE_KEY = "com.example.carbon_footprint_calculation.majorproject_partone.more_graphs - Pass graphType into activity";
    private String TAG_MORE_GRAPHS = "more graph page";
    private DataPack data = DataPack.getData();
    private String unit;
    private int unitRID;
    private int range;
    private String date;
    private String graphType;
    final private String TRANS_MODE_CAR = "car";
    final private String TRANS_MODE_WALK = "walk/bike";
    final private String TRANS_MODE_BUS = "bus";
    final private String TRANS_MODE_SKYTRAIN = "skytrain";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_graphs);

        unit = data.getUnit_setting();

        if (unit.equals("Kg")){
            unitRID = R.string.kg;
        }
        else {
            unitRID = R.string.tree_day;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.more_graphs_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        TextView title = (TextView) findViewById(R.id.more_graphs_toolbar_title);
        title.setText(R.string.more_graphs);

        DB = new megaDataPackHelper(this);
        DB.close();

        try {
            setupView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    private void setupView() throws ParseException {//set up charts, pie or bar
        Intent intent = getIntent();
        range = intent.getIntExtra(RANGE_KEY, 0);
        date = intent.getStringExtra(DATE_KEY);
        graphType = intent.getStringExtra(TYPE_KEY);
        String[] graphTypes = getResources().getStringArray(R.array.chart_type);
        TextView title = (TextView) findViewById(R.id.txt_moreGraphsTitle);
        if(range == 0){
            setupSwitchButton();
            title.setText(date);
        }else if(graphType.equals(graphTypes[0])){
            setupSwitchButton();
            title.setText(getString(R.string.date_range, range));
        }else{
            setupCombinedChart(range);
            title.setText(getString(R.string.date_range, range));
        }
    }

    private void setupSwitchButton() throws ParseException {
        final Switch switchView = (Switch) findViewById(R.id.graph_switch);
        switchView.setVisibility(View.VISIBLE);
        final TextView switchLabel = (TextView)findViewById(R.id.switchDescription);
        switchLabel.setVisibility(View.VISIBLE);
        final String offLabel = getString(R.string.groupTransMode);//code 0
        final String onLabel = getString(R.string.groupRoute);//code 1
        switchLabel.setText(offLabel);

        setupPieChart(0);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchLabel.setText(onLabel);
                    try {
                        setupPieChart(1);//1 == group by route Mode
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    switchLabel.setText(offLabel);
                    try {
                        setupPieChart(0);//0 == group by transMode
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    private void setupPieChart(int groupBy) throws ParseException {
        PieChart chart = (PieChart) findViewById(R.id.pie_moreGraphs);
        chart.setVisibility(View.VISIBLE);
        CombinedChart combinedChart = (CombinedChart) findViewById(R.id.combined_moreGraphs);
        combinedChart.setVisibility(View.INVISIBLE);
        List<Journey> journeys;
        List<Utility> utilities = DB.UB_getAllUtilitiesSortedByEndDate();

        Double sumBusEmission = 0.0;
        Double sumSkytrainEmission = 0.0;
        Double sumGasEmission = 0.0;
        Double sumElectricityEmission = 0.0;


        List<PieEntry> pieEntries = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        List<Double> emissions = new ArrayList<>();


        List<String> cars = new ArrayList<>();
        List<Double> carEmissions = new ArrayList<>();

        Double unsavedRoutesEmissions = 0.0;
//gather data for 1 day------------------------------------------------------
        if (range==0){//one day, just loop through all journeys and all utilities once
            journeys = DB.RJ_getJourneyByDate(date);

            for (Journey journey : journeys) {
                if (groupBy==0) {
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
                if (groupBy==1){
                    if (journey.getRoute_save()==0){
                        unsavedRoutesEmissions+=journey.getTotalEmission(unit);
                    }
                    else {
                        addToList(routes, emissions, journey.getRoute().getName(), journey.getTotalEmission(unit));
                    }
                }
            }

            for (Utility utility : utilities) {
                String billStartDate = utility.getBillStartDate();
                String billEndDate = utility.getBillEndDate();
                if (isWithinRange(billStartDate, billEndDate, date)) {
                    String billType = utility.getBillType();
                    switch (billType) {
                        case "Natural Gas":
                            sumGasEmission += utility.getEmissionPerPerson(unit);
                            break;
                        case "Electricity":
                            sumElectricityEmission += utility.getEmissionPerPerson(unit);
                            break;
                    }
                }
            }
            if (sumGasEmission==0.0&&sumElectricityEmission==0.0){//assume using a most recent bill if there is no bill on this day
                Date checkDate = dateFormat.parse(date);
                boolean isLatestValidEndDate = false;
                Utility validUtility = null;
                int index = 0;
                while(!isLatestValidEndDate && index != utilities.size()) {
                    Utility currentUtility = utilities.get(index);
                    if(isBefore(currentUtility.getBillEndDate(), checkDate)) {
                        isLatestValidEndDate = true;
                        validUtility = currentUtility;
                    }
                    index++;
                }
                if(validUtility != null) {
                    String billType = validUtility.getBillType();
                    Float emission = validUtility.getEmissionPerPerson(unit).floatValue();
                    pieEntries.add(new PieEntry(emission, billType));
                }
            }

        }
//-------------------------------------------------------------------------------
        //gather data for 28/365 days---------------------------------------
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date endDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, (range * -1));
            Date startCheckDate = calendar.getTime();

            journeys = DB.RJ_getAllJourney();


            while (startCheckDate.before(endDate)) {
                for (Journey journey : journeys) {
                    if (isOnDate(journey.getJourneyDate(), startCheckDate)) {
                        if (groupBy == 0) {
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
                        if (groupBy == 1) {
                            if (journey.getRoute_save() == 0) {
                                unsavedRoutesEmissions += journey.getTotalEmission(unit);
                            } else {
                                addToList(routes,emissions,journey.getRoute().getName(),journey.getTotalEmission(unit));
                            }
                        }
                    }
                }
                for (Utility utility : utilities) {
                    String billStartDate = utility.getBillStartDate();
                    String billEndDate = utility.getBillEndDate();
                    if (isWithinRange(billStartDate, billEndDate, startCheckDate)) {
                        String billType = utility.getBillType();
                        switch (billType) {
                            case "Natural Gas":
                                sumGasEmission += utility.getEmissionPerPerson(unit);
                                break;
                            case "Electricity":
                                sumElectricityEmission += utility.getEmissionPerPerson(unit);
                                break;
                        }
                    }
                }
                calendar.add(Calendar.DATE, 1);
                startCheckDate = calendar.getTime();
            }
        }
        //---------------gather data end----------------------------------------------------
        //---------------put in data into the chart-----------------------------------------
        if (sumGasEmission != 0.0) {
            pieEntries.add(new PieEntry(sumGasEmission.floatValue(), getString(R.string.natrualGas)));
        }
        if (sumElectricityEmission != 0.0) {
            pieEntries.add(new PieEntry(sumElectricityEmission.floatValue(), getString(R.string.electricity)));
        }

        if (groupBy == 0) {
            int size = cars.size();
            for (int i = 0; i < size; i++) {
                pieEntries.add(new PieEntry(carEmissions.get(i).floatValue(), cars.get(i)));
            }
            if (sumBusEmission != 0.0) {
                pieEntries.add(new PieEntry(sumBusEmission.floatValue(), getString(R.string.bus)));
            }
            if (sumSkytrainEmission != 0.0) {
                pieEntries.add(new PieEntry(sumSkytrainEmission.floatValue(), getString(R.string.skytrain)));
            }
        }

        if (groupBy==1){
            int size = routes.size();
            for (int i = 0; i<size;i++){
                pieEntries.add(new PieEntry(emissions.get(i).floatValue(),routes.get(i)));
            }
            if (unsavedRoutesEmissions!=0.0) {
                pieEntries.add(new PieEntry(unsavedRoutesEmissions.floatValue(), getString(R.string.chart_label_other)));
            }
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,getString(R.string.chart_label)+" ("+getString(unitRID)+")");

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(10);
        PieData data = new PieData(dataSet);

        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
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

    private void setupCombinedChart(int range) throws ParseException {
        Switch switchView = (Switch) findViewById(R.id.graph_switch);
        switchView.setVisibility(View.INVISIBLE);
        final TextView switchLabel = (TextView)findViewById(R.id.switchDescription);
        switchLabel.setVisibility(View.INVISIBLE);
        PieChart pieChart = (PieChart) findViewById(R.id.pie_moreGraphs);
        pieChart.setVisibility(View.INVISIBLE);
        CombinedChart chart = (CombinedChart) findViewById(R.id.combined_moreGraphs);
        chart.setVisibility(View.VISIBLE);
        chart.setDrawValueAboveBar(true);

        List<Journey> journeys = DB.RJ_getAllJourney();
        List<Utility> utilities = DB.UB_getAllUtilitiesSortedByEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, (range * -1));
        Date startDate = calendar.getTime();

        //load in data here into chart
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Entry> lineEntriesAverage = new ArrayList<>();
        ArrayList<Entry> lineEntriesTarget = new ArrayList<>();

        int index = 0;
        int count = 0;
        int barCount= 0;
        Date startCheckDate = startDate;
        float totalCarEmission = 0;
        float totalBusEmission = 0;
        float totalSkytrainEmission = 0;
        float totalGasEmission = 0;
        float totalElectricityEmission = 0;
        while (startCheckDate.before(endDate)) {//loop through everyday in the past 28/365 days
            float[] monthList = new float[5];
            float currentCarEmission = 0;
            float currentBusEmission = 0;
            float currentSkytrainEmission = 0;
            float currentGasEmission = 0;
            float currentElectricityEmission = 0;
            boolean isAnyWithinRange = false;
            for (Journey journey : journeys) {//check if there is any journey on this day
                if (isOnDate(journey.getJourneyDate(), startCheckDate)) {
                    switch (journey.getTransMode()) {
                        case "car":
                            currentCarEmission += journey.getTotalEmission(unit).floatValue();
                            break;
                        case "bus":
                            currentBusEmission += journey.getTotalEmission(unit).floatValue();
                            break;
                        case "skytrain":
                            currentSkytrainEmission += journey.getTotalEmission(unit).floatValue();
                            break;
                    }
                }
            }
            for (Utility utility : utilities) {//check if this day is within range of any utility bills
                if (isWithinRange(utility.getBillStartDate(), utility.getBillEndDate(), startCheckDate)) {
                    switch (utility.getBillType()) {
                        case "Natural Gas":
                            currentGasEmission += utility.getEmissionPerPerson(unit).floatValue();
                            break;
                        case "Electricity":
                            currentElectricityEmission += utility.getEmissionPerPerson(unit).floatValue();
                            break;
                    }
                    isAnyWithinRange = true;
                }
            }
            if(!isAnyWithinRange) {
                boolean isLatestValidEndDate = false;
                Utility validUtility = null;
                int utilityIndex = 0;
                while(!isLatestValidEndDate && utilityIndex != utilities.size()) {
                    Utility currentUtility = utilities.get(utilityIndex);
                    if(isBefore(currentUtility.getBillEndDate(), startCheckDate)) {
                        isLatestValidEndDate = true;
                        validUtility = currentUtility;
                    }
                    utilityIndex++;
                }
                if(validUtility != null) {
                    switch (validUtility.getBillType()) {
                        case "Natural Gas":
                            currentGasEmission += validUtility.getEmissionPerPerson(unit).floatValue();
                            break;
                        case "Electricity":
                            currentElectricityEmission += validUtility.getEmissionPerPerson(unit).floatValue();
                            break;
                    }
                }
            }
            if (range==FOUR_WEEKS) {//for 28 days there would be one bar for every day
                monthList[0] = currentCarEmission;
                monthList[1] = currentBusEmission;
                monthList[2] = currentSkytrainEmission;
                monthList[3] = currentGasEmission;
                monthList[4] = currentElectricityEmission;
                barEntries.add(index, new BarEntry(index, monthList));
                if (unit.equals("Kg")){
                    lineEntriesAverage.add(index, new Entry(index, AVERAGE_CANADIAN_CO2_EMISSIONS_PER_DAY));
                    lineEntriesTarget.add(index, new Entry(index, TARGET_CANADIAN_CO2_EMISSIONS_PER_DAY));
                }
                else {
                    lineEntriesAverage.add(index, new Entry(index, (AVERAGE_CANADIAN_CO2_EMISSIONS_PER_DAY/0.05965f)));
                    lineEntriesTarget.add(index, new Entry(index, (TARGET_CANADIAN_CO2_EMISSIONS_PER_DAY/0.05965f)));
                }
            }
            else if (range == ONE_YEAR){//for a year there would be 13 bars
                totalCarEmission += currentCarEmission;
                totalBusEmission += currentBusEmission;
                totalSkytrainEmission += currentSkytrainEmission;
                totalGasEmission += currentGasEmission;
                totalElectricityEmission += currentElectricityEmission;
                if (count==30){//this means 365/count bars will be on the ONE_YEAR graph. Yeah there will be remainder that won't be shown on graph
                    float[] yearList = new float[5];
                    yearList[0] = totalCarEmission;
                    yearList[1] = totalBusEmission;
                    yearList[2] = totalSkytrainEmission;
                    yearList[3] = totalGasEmission;
                    yearList[4] = totalElectricityEmission;
                    barEntries.add(barCount, new BarEntry(barCount, yearList));
                    if (unit.equals("Kg")){
                        lineEntriesAverage.add(barCount, new Entry(barCount, AVERAGE_CANADIAN_CO2_EMISSIONS_PER_30_DAYS));
                        lineEntriesTarget.add(barCount, new Entry(barCount, TARGET_CANADIAN_CO2_EMISSIONS_PER_30_DAYS));
                    }
                    else {
                        lineEntriesAverage.add(barCount, new Entry(barCount, (AVERAGE_CANADIAN_CO2_EMISSIONS_PER_30_DAYS/0.05965f)));
                        lineEntriesTarget.add(barCount, new Entry(barCount, (TARGET_CANADIAN_CO2_EMISSIONS_PER_30_DAYS/0.05965f)));
                    }
                    barCount++;
                    count=0;
                    totalCarEmission = 0;
                    totalBusEmission = 0;
                    totalSkytrainEmission = 0;
                    totalGasEmission = 0;
                    totalElectricityEmission = 0;
                }
                //might add an else to also show the remainder
                else if (index>360){
                    float[] yearList = new float[5];
                    yearList[0] = totalCarEmission;
                    yearList[1] = totalBusEmission;
                    yearList[2] = totalSkytrainEmission;
                    yearList[3] = totalGasEmission;
                    yearList[4] = totalElectricityEmission;
                    barEntries.add(barCount, new BarEntry(barCount, yearList));
                    if (unit.equals("Kg")){
                        lineEntriesAverage.add(barCount, new Entry(barCount, AVERAGE_CANADIAN_CO2_EMISSIONS_PER_5_DAYS));
                        lineEntriesTarget.add(barCount, new Entry(barCount, TARGET_CANADIAN_CO2_EMISSIONS_PER_5_DAYS));
                    }
                    else {
                        lineEntriesAverage.add(barCount, new Entry(barCount, (AVERAGE_CANADIAN_CO2_EMISSIONS_PER_5_DAYS/0.05965f)));
                        lineEntriesTarget.add(barCount, new Entry(barCount, (TARGET_CANADIAN_CO2_EMISSIONS_PER_5_DAYS/0.05965f)));
                    }
                    count=0;
                    totalCarEmission = 0;
                    totalBusEmission = 0;
                    totalSkytrainEmission = 0;
                    totalGasEmission = 0;
                    totalElectricityEmission = 0;
                }
            }
            count++;
            index++;
            calendar.add(Calendar.DATE, 1);
            startCheckDate = calendar.getTime();//increment check date by 1
        }

        //setup datasets
        BarDataSet dataSet = new BarDataSet(barEntries, getString(R.string.chart_label)+"("+getString(unitRID)+")");
        LineDataSet lineDataSetAverage = new LineDataSet(lineEntriesAverage, getString(R.string.average));
        LineDataSet lineDataSetTarget = new LineDataSet(lineEntriesTarget, getString(R.string.target));

        //set colors
        lineDataSetAverage.setColor(Color.rgb(240, 238, 70));
        lineDataSetAverage.setCircleColor(Color.rgb(240, 238, 70));
        lineDataSetAverage.setFillColor(Color.rgb(240, 238, 70));
        lineDataSetTarget.setColor(Color.rgb(0, 100, 0));
        lineDataSetTarget.setCircleColor(Color.rgb(0, 100, 0));
        lineDataSetTarget.setFillColor(Color.rgb(0, 100, 0));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //set labels
        String[] labels = getResources().getStringArray(R.array.barChart_labels);
        dataSet.setStackLabels(labels);

        BarData data = new BarData(dataSet);
        LineData lineData = new LineData(lineDataSetAverage, lineDataSetTarget);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedData.setData(data);
        chart.setData(combinedData);
        chart.invalidate();
    }





    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private boolean isWithinRange(String start, String end, Date checkDate) throws ParseException {
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);
        return !checkDate.before(startDate) && !checkDate.after(endDate);
    }

    private boolean isWithinRange(String start, String end, String checkDate) throws ParseException {
        Date date = dateFormat.parse(checkDate);
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);
        return !date.before(startDate) && !date.after(endDate);
    }

    private boolean isBefore(String startDateTocheck, Date endDate) throws ParseException {
        Date dateF = dateFormat.parse(startDateTocheck);
        return dateF.before(endDate);
    }
    private boolean isOnDate(String date, Date check) throws ParseException {
        String checkInStr = dateFormat.format(check);
        return date.equals(checkInStr);
    }

    public static Intent makeIntentWithData(Context context, String date, int range, String type) {
        Intent intent = new Intent(context, more_graphs.class);
        intent.putExtra(DATE_KEY, date);
        intent.putExtra(RANGE_KEY, range);
        intent.putExtra(TYPE_KEY,type);
        return intent;
    }
}
