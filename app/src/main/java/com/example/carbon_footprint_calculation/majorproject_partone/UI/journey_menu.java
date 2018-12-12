package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;
import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.data.vehicleDataHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.extra.feedbackDialog;
import com.example.carbon_footprint_calculation.majorproject_partone.extra.pickDateDialog;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Journey;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Utility;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class journey_menu extends android.support.v4.app.Fragment {
    private DataPack data = DataPack.getData();
    private ListView table;
    private PieChart chart;
    private TextView tableEntry1;
    private TextView tableEntry2;
    private megaDataPackHelper DB;
    private vehicleDataHelper VDBHelper;
    private SQLiteDatabase mDB;
    private static boolean isJourneyAdded;
    private List<Journey> allJourneys;
    private List<Utility> allUtilities;
    private String TAG_MAIN_MENU ="JOURNEY MENU" ;
    private int TRIP_WARN_NUM = 5;
    private Activity journeyMenu;
    FloatingActionButton fab_car;
    FloatingActionButton fab_bike;
    FloatingActionButton fab_skytrain;
    FloatingActionButton fab_bus;
    FloatingActionButton fab_add;
    Animation fab_open;
    Animation fab_close;
    Animation fab_clockwise;
    Animation fab_anticlockwise;
    final private String TRANS_MODE_CAR = "car";
    final private String TRANS_MODE_WALK = "walk/bike";
    final private String TRANS_MODE_BUS = "bus";
    final private String TRANS_MODE_SKYTRAIN = "skytrain";
    boolean isClicked = false;
    private View v;
    Toolbar toolbar;
    String unit;
    int unitRID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_journey_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        getActivity().invalidateOptionsMenu();
        unit = data.getUnit_setting();

        if (unit.equals("Kg")){
            unitRID = R.string.kg;
        }
        else {
            unitRID = R.string.tree_day;
        }

        toolbar = (Toolbar) v.findViewById(R.id.journey_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView title = (TextView) v.findViewById(R.id.main_toolbar_title);
        title.setText(R.string.journey_menu_title);

        journeyMenu=getActivity();
        table = (ListView) v.findViewById(R.id.list_journey);
        fab_add = (FloatingActionButton) v.findViewById(R.id.fab_add_journey);
        fab_car = (FloatingActionButton) v.findViewById(R.id.fab_car);
        fab_bike = (FloatingActionButton) v.findViewById(R.id.fab_bike);
        fab_skytrain = (FloatingActionButton) v.findViewById(R.id.fab_skytrain);
        fab_bus = (FloatingActionButton) v.findViewById(R.id.fab_bus);
        fab_open = AnimationUtils.loadAnimation(journeyMenu,R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(journeyMenu,R.anim.fab_close);
        fab_close.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                fab_car.clearAnimation();
                fab_bike.clearAnimation();
                fab_skytrain.clearAnimation();
                fab_bus.clearAnimation();

                fab_car.setVisibility(View.GONE);
                fab_bike.setVisibility(View.GONE);
                fab_skytrain.setVisibility(View.GONE);
                fab_bus.setVisibility(View.GONE);
                isClicked = false;
            }

        });

        fab_clockwise = AnimationUtils.loadAnimation(journeyMenu,R.anim.fab_rotate);
        fab_anticlockwise = AnimationUtils.loadAnimation(journeyMenu, R.anim.fab_anticlockwise);

        if(isClicked==false) {
            fab_car.setVisibility(View.GONE);
            fab_bike.setVisibility(View.GONE);
            fab_skytrain.setVisibility(View.GONE);
            fab_bus.setVisibility(View.GONE);
        }

        setupAddJourneyButton();

        //setup DB
        DB = new megaDataPackHelper(getActivity());
        DB.close();

        allJourneys = DB.RJ_getAllJourney();
        allUtilities = DB.UB_getAllUtilities();
        setupTable();
        setupEditJourney();
        setupAddVehicleButton();

    }




    private void setupEditJourney() {
        table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editJourney = edit_journey_page.makeIntent(getActivity());
                editJourney.putExtra("journeyID",allJourneys.get(position).getJourneyID());
                startActivity(editJourney);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void closeDB() {
        DB.close();
    }


    private void setupAddJourneyButton() {
        FloatingActionButton btn = (FloatingActionButton) v.findViewById(R.id.fab_add_journey);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = vehicle_menu.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume() {//TODO:Can change to using snake bar
        super.onResume();
        allJourneys = DB.RJ_getAllJourney();
        allUtilities = DB.UB_getAllUtilities();
        if (isJourneyAdded) {
            String tips[] = getResources().getStringArray(R.array.tips);
            feedbackDialog fd = new feedbackDialog();
            fd.setIsTips(true);
            fd.setIsEmissions(false);
            Journey latestJourney = allJourneys.get(allJourneys.size() - 1);
            String date = latestJourney.getJourneyDate();
            int trips = 0;
            double maxEmission = 0;
            for (Journey journey : allJourneys) {
                if (journey.getJourneyDate().equals(date)) {
                    trips++;
                }
                if (journey != latestJourney) {
                    if (journey.getTotalEmission(unit) > maxEmission) {
                        maxEmission = journey.getTotalEmission(unit);
                    }
                }
            }
            double emissions = latestJourney.getTotalEmission(unit);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String finalMessageToDisplay = "";
            boolean isTripOrMaxExceed = false;
            if (trips != 1) {
                if (latestJourney.getTotalEmission(unit) >= maxEmission) {
                    String emissionMaxMessage = getString(R.string.previous_emission_max) + decimalFormat.format(maxEmission) +getString(unitRID)+ getString(R.string.tip_report_p2);
                    finalMessageToDisplay += emissionMaxMessage;
                    isTripOrMaxExceed = true;
                }
            } else if (trips > TRIP_WARN_NUM) {
                String tripsMessage = getString(R.string.trips_message, trips);
                finalMessageToDisplay += tripsMessage;
                isTripOrMaxExceed = true;
            }
            if (!isTripOrMaxExceed) {
                String emissionMessage = getString(R.string.tip_report) + decimalFormat.format(emissions) + getString(unitRID) + getString(R.string.tip_report_p2);
                finalMessageToDisplay += emissionMessage;
            }
            ArrayList<Integer> recentTips = DB.TP_getAllIndex();
            Random rand = new Random();
            int index = Math.abs(rand.nextInt() % 8);
            if (!DB.TP_checkEmpty()) {
                boolean isUniqueIndex = false;
                while (!isUniqueIndex) {
                    int count = 0;
                    for (int recent : recentTips) {
                        if (recent == index) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        isUniqueIndex = true;
                    } else {
                        index = Math.abs(rand.nextInt() % 8);
                    }
                }
            }
            long id = DB.TP_addNewTip(index);
            if (recentTips.size() == 7) {
                DB.TP_deleteEarliestTip(id);
            }
            fd.setStrToDisplay(finalMessageToDisplay + tips[index]);
            fd.show(getFragmentManager(), "tips dialog");
            setIsJourneyAdded(false);
        }
        setupTable();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, journey_menu.class);
    }


    private void setupTable() {
        ArrayAdapter<Journey> adapter = new journey_menu.journeyAdapter();
        table = (ListView)v.findViewById(R.id.list_journey);
        table.setAdapter(adapter);
        TextView emptyJourneyTxt = (TextView) v.findViewById(R.id.emptyJourneyTxt);
        if(adapter.getCount()>0) {
            emptyJourneyTxt.setVisibility(View.INVISIBLE);
        }
        else {
            emptyJourneyTxt.setVisibility(View.VISIBLE);
        }

    }
    private class journeyAdapter extends ArrayAdapter<Journey>{
        public journeyAdapter(){
            super(getActivity(),R.layout.journey_listview, allJourneys);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            //set all text for journey_listview
            if (itemView == null){
                itemView = getActivity().getLayoutInflater().inflate(R.layout.journey_listview,parent,false);
            }
            Journey currentJourney = allJourneys.get(position);
            TextView date = (TextView)itemView.findViewById(R.id.table_date);
            TextView route = (TextView)itemView.findViewById(R.id.table_route_name);
            ImageView vehicle = (ImageView)itemView.findViewById(R.id.carIcon);
            TextView distance = (TextView)itemView.findViewById(R.id.table_distance);
            TextView emission = (TextView)itemView.findViewById(R.id.table_emission);
            //reformat date to something easier to read
            Date oldDate = null;
            try {
                oldDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentJourney.getJourneyDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Format formatter = new SimpleDateFormat("MMM d, yyyy"); //just month and day
            String newDate = formatter.format(oldDate);
            date.setText(newDate);

            route.setText(currentJourney.getRoute().getName());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            distance.setText(decimalFormat.format(currentJourney.getRoute().getTotalDistance())+getResources().getString(R.string.km));

            String transMode = currentJourney.getTransMode();
            int iconID;
                switch (transMode){
                    case TRANS_MODE_BUS:
                        iconID = R.mipmap.ic_bus;
                        break;
                    case TRANS_MODE_SKYTRAIN:
                        iconID = R.mipmap.ic_train;
                        break;
                    case TRANS_MODE_WALK:
                        iconID = R.mipmap.ic_bike;
                        break;
                    case TRANS_MODE_CAR:
                        iconID = currentJourney.getCar().getIconID();
                        break;
                    default:
                        iconID = R.mipmap.ic_launcher;
                        break;
                }

            vehicle.setImageResource(iconID);

            Log.i(TAG_MAIN_MENU,"emission: "+currentJourney.getTotalEmission(unit));
            emission.setText(decimalFormat.format(currentJourney.getTotalEmission(unit))+"\n"+getString(unitRID));
            return itemView;
        }
    }

    private void openFAB(){
        fab_add.startAnimation(fab_clockwise);
        fab_car.startAnimation(fab_open);
        fab_bike.startAnimation(fab_open);
        fab_skytrain.startAnimation(fab_open);
        fab_bus.startAnimation(fab_open);

        fab_car.setVisibility(View.VISIBLE);
        fab_bike.setVisibility(View.VISIBLE);
        fab_skytrain.setVisibility(View.VISIBLE);
        fab_bus.setVisibility(View.VISIBLE);
        isClicked=true;
    }
    private void closeFab() {
        fab_add.startAnimation(fab_anticlockwise);
        fab_car.startAnimation(fab_close);
        fab_bike.startAnimation(fab_close);
        fab_skytrain.startAnimation(fab_close);
        fab_bus.startAnimation(fab_close);
    }

    private void setupAddVehicleButton() {

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClicked){
                    closeFab();
                }else{
                    openFAB();

                    fab_car.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = vehicle_menu.makeIntent(journeyMenu);
                            intent.putExtra("mode","add");
                            intent.putExtra("transMode", TRANS_MODE_CAR);
                            closeFab();
                            startActivityForResult(intent,0);//request code 0 for add
                        }
                    });
                    //send directly to add route page for transit methods
                    fab_bike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = route_menu.makeIntent(journeyMenu);
                            intent.putExtra("mode","add");
                            intent.putExtra("transMode", TRANS_MODE_WALK);
                            closeFab();
                            startActivityForResult(intent,0);//request code 0 for add
                        }
                    });

                    fab_skytrain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = route_menu.makeIntent(journeyMenu);
                            intent.putExtra("mode","add");
                            intent.putExtra("transMode", TRANS_MODE_SKYTRAIN);
                            closeFab();
                            startActivityForResult(intent,0);//request code 0 for add
                        }
                    });

                    fab_bus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = route_menu.makeIntent(journeyMenu);
                            intent.putExtra("mode","add");
                            intent.putExtra("transMode", TRANS_MODE_BUS);
                            closeFab();
                            startActivityForResult(intent,0);//request code 0 for add
                        }
                    });

                }
            }
        });
    }



    public static void setIsJourneyAdded(boolean journeyAdded) {
        isJourneyAdded = journeyAdded;
    }


}
