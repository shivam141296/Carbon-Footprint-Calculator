package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;
import com.example.carbon_footprint_calculation.majorproject_partone.R;

import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Car;

import java.util.List;

public class vehicle_menu extends AppCompatActivity {
    private DataPack data = DataPack.getData();
    private ListView VehicleList;
    private List<Car> allCars;
    private megaDataPackHelper DB;
    private long carID;
    private String TRANS_MODE_CAR = "car";
    private String TRANS_MODE_WALK = "walk/bike";
    private String TRANS_MODE_BUS = "bus";
    private String TRANS_MODE_SKYTRAIN = "skytrain";
    FloatingActionButton fab_car;
    FloatingActionButton fab_bike;
    FloatingActionButton fab_skytrain;
    FloatingActionButton fab_bus;
    FloatingActionButton fab_add;
    Animation fab_open;
    Animation fab_close;
    Animation fab_clockwise;
    Animation fab_anticlockwise;
    boolean isClicked = false;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_menu);

        DB = new megaDataPackHelper(this);
        DB.close();

        allCars = DB.RC_getAllCars();

        toolbar = (Toolbar) findViewById(R.id.vehicle_menu_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        TextView title = (TextView) findViewById(R.id.main_toolbar_title);
        title.setText(R.string.transportation_menu);

        setupAddVehicleButton();
        populateVehicleList();
        setupItemClick();

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
    private void populateVehicleList() {
        ArrayAdapter<Car> adapter = new vehicleArrayAdapter();
        VehicleList = (ListView)findViewById(R.id.list_vehicle);
        VehicleList.setAdapter(adapter);

        //Display message if list is empty
        TextView emptyVehicleTxt = (TextView) findViewById(R.id.emptyVehicleTxt);
        if(adapter.getCount()>0) {
            emptyVehicleTxt.setVisibility(View.INVISIBLE);
        }
        else {
            emptyVehicleTxt.setVisibility(View.VISIBLE);
        }
    }



    private class vehicleArrayAdapter extends ArrayAdapter<Car>{
        public vehicleArrayAdapter(){
            super(vehicle_menu.this,R.layout.vehicle_listview,allCars);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.vehicle_listview,parent,false);
            }

            Car currentCar = allCars.get(position);
            ImageView carIconFrame = (ImageView)itemView.findViewById(R.id.carIcon);
            carIconFrame.setImageResource(currentCar.getIconID());

            TextView carNickName = (TextView)itemView.findViewById(R.id.carNickname);
            carNickName.setText(currentCar.getNickname());
            TextView CarSpec = (TextView)itemView.findViewById(R.id.carSpec);
            CarSpec.setText(currentCar.getSpecStr());
            TextView carMoreSpec = (TextView)itemView.findViewById(R.id.carSpec2);
            carMoreSpec.setText(currentCar.getMoreSpecStr());
            return itemView;
        }
    }

    private void setupAddVehicleButton() {
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add_vehicle);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = add_vehicle_page.makeIntent(vehicle_menu.this);
                intent.putExtra("mode","add");
                startActivityForResult(intent,0);
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_CANCELED){
            return;
        }
        allCars = DB.RC_getAllCars();
        populateVehicleList();
    }



    private void setupItemClick() {

        VehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = route_menu.makeIntent(vehicle_menu.this);
                carID = allCars.get(position).getCarID();
                // pass index of vehicle clicked to route menu
                intent.putExtra("carID",allCars.get(position).getCarID());
                intent.putExtra("transMode", TRANS_MODE_CAR);
                startActivity(intent);
            }
        });
        VehicleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = add_vehicle_page.makeIntent(vehicle_menu.this);
                carID = allCars.get(position).getCarID();
                editIntent.putExtra("mode","edit");
                editIntent.putExtra("carID",carID);//TODO: now passing long carID, find some way to pre-set the spinners
                startActivityForResult(editIntent,1);
                return true;
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, vehicle_menu.class);
    }
}
