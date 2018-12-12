package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;
import com.example.carbon_footprint_calculation.majorproject_partone.R;

import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.extra.pickCarIconDialog;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Car;

import java.util.List;

public class add_vehicle_page extends AppCompatActivity{
    private DataPack data;
    private long vehicleID; //this is the id is DB
    private Spinner MakeSelect;
    private Spinner ModelSelect;
    private Spinner YearSelect;
    private Spinner SpecSelect;
    private String SelectedMake;
    private String SelectedModel;
    private String SelectedYear;
    private String SelectedSpec;
    private String TAG_ADD_VEHICLE_PAGE = "add_vehicle_page";
    private Intent IntentMode;
    private String mode;
    private EditText CarNameInput;
    private Car carToEdit;
    private boolean toPreselect;
    private long carID;
    private megaDataPackHelper DB;
    private Car SelectedCar;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_page);

        //--------------setup Database--------------
        DB = new megaDataPackHelper(this);
        data = DataPack.getData();
        //------------------------------------------
        carIconToSave = R.mipmap.vehicleicon;

        toolbar = (Toolbar) findViewById(R.id.add_vehicle_menu_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        TextView title = (TextView) findViewById(R.id.add_vehicle_menu_toolbar_title);
        title.setText(R.string.transportation);

        IntentMode = getIntent();
        mode = IntentMode.getStringExtra("mode");
        setResult(RESULT_CANCELED);


        CarNameInput =  (EditText)findViewById(R.id.edit_vehicleNickname);
        MakeSelect = (Spinner)findViewById(R.id.spinner_make);
        ModelSelect = (Spinner)findViewById(R.id.spinner_model);
        YearSelect = (Spinner)findViewById(R.id.spinner_year);
        SpecSelect = (Spinner)findViewById(R.id.spinner_spec);

        if (mode.equals("edit")){
            carID = IntentMode.getLongExtra("carID",0);
            carToEdit = DB.RC_getCarByID(carID);
            carIconToSave = carToEdit.getIconID();
            CarNameInput.setText(carToEdit.getNickname());
            changeTitle(title);

        }
        if (mode.equals("edit_journey")){
            carToEdit = data.getEditJourney().getCar();
            if (carToEdit!=null){
                CarNameInput.setText(carToEdit.getNickname());
                carIconToSave = carToEdit.getIconID();
            }
            changeTitle2(title);
        }
        setupEditIcon();
        populateSpinners();

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
                String nickName = CarNameInput.getText().toString();
                if (nickName.equals("")){
                    Toast.makeText(add_vehicle_page.this,R.string.emptyNickname,Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (carIconToSave==R.mipmap.vehicleicon){
                    Toast.makeText(add_vehicle_page.this,R.string.please_pick_icon,Toast.LENGTH_SHORT).show();
                    return false;
                }

                SelectedCar.setIconID(carIconToSave);
                if (mode.equals("edit")){
                    long carID = IntentMode.getLongExtra("carID",0);
                    SelectedCar.setNickname(nickName);

                    Log.i(TAG_ADD_VEHICLE_PAGE,""+DB.RC_editCar(carID,SelectedCar));
                }
                else if(mode.equals("edit_journey")){
                    data.getEditJourney().setCar(SelectedCar);
                    data.getEditJourney().getCar().setNickname(nickName);
                }
                else{
                    SelectedCar.setNickname(nickName);
                    DB.RC_addCar(SelectedCar);
                }

                setResult(RESULT_OK);
                finish();
                break;
            case R.id.action_delete:
                DB.RC_delCar(carID);
                setResult(RESULT_OK);
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

    private void changeTitle(TextView title) {
        title.setText(R.string.edit_existing_car);
    }

    private void changeTitle2(TextView title) {
        title.setText(R.string.edit_car_in_journey);
    }

    private void populateSpinners() {
        toPreselect = false;
        if (mode.equals("edit")){
            toPreselect = true;
        }

        // populate the spinners with VehicleData
        final String[] allMakes = DB.VD_getAllMakes();
        final ArrayAdapter<String> MakeAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,allMakes);
        MakeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        MakeSelect.setAdapter(MakeAdapter);
        MakeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String tempMake = allMakes[position];
                final String[] allModels = DB.VD_getAllModels(tempMake);
                if (mode.equals("edit") && toPreselect && carToEdit!=null){
                    int spinnerPosition = MakeAdapter.getPosition(carToEdit.getMake());
                    MakeSelect.setSelection(spinnerPosition);
                }
                SelectedMake = allMakes[position];
                final ArrayAdapter<String> ModelAdapter = new ArrayAdapter<String>(add_vehicle_page.this,R.layout.support_simple_spinner_dropdown_item,allModels);
                ModelSelect.setAdapter(ModelAdapter);
                ModelSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String tempModel = allModels[position];
                        final String[] allYears = DB.VD_getAllYears(tempMake,tempModel);
                        final ArrayAdapter<String> YearAdapter = new ArrayAdapter<String>(add_vehicle_page.this,R.layout.support_simple_spinner_dropdown_item,allYears);
                        if (mode.equals("edit") && toPreselect&& carToEdit!=null){
                            int spinnerPosition = ModelAdapter.getPosition(carToEdit.getModel());
                            ModelSelect.setSelection(spinnerPosition);
                        }
                        SelectedModel = allModels[position];
                        YearSelect.setAdapter(YearAdapter);
                        YearSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (mode.equals("edit") && toPreselect&& carToEdit!=null){
                                    int spinnerPosition = YearAdapter.getPosition(carToEdit.getYear());
                                    YearSelect.setSelection(spinnerPosition);
                                }
                                SelectedYear = allYears[position];
                                final String tempYear = allYears[position];
                                final List<Car> allCars = DB.VD_getAllSpecsByMMY(tempMake,tempModel,tempYear);//MMY == Make, Model, Year
                                final String[] allSpecs = getAllSpecStr(allCars);
                                final ArrayAdapter<String> SpecAdapter = new ArrayAdapter<String>(add_vehicle_page.this,R.layout.support_simple_spinner_dropdown_item,allSpecs);
                                SpecSelect.setAdapter(SpecAdapter);
                                SpecSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (mode.equals("edit") && toPreselect&& carToEdit!=null){
                                            int spinnerPosition = SpecAdapter.getPosition(carToEdit.getMoreSpecStr());
                                            SpecSelect.setSelection(spinnerPosition);
                                            toPreselect = false;
                                        }

                                        SelectedCar = allCars.get(position);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String[] getAllSpecStr(List<Car> allCars) {
        int i = allCars.size();
        String[] specStr = new String[i];
        for (int j = 0;j<i;j++){
            specStr[j]= allCars.get(j).getMoreSpecStr();
        }
        return specStr;
    }

    private void setupEditIcon(){
        ImageView iconFrame = (ImageView)findViewById(R.id.vehicle_icon);
        iconFrame.setImageResource(carIconToSave);
        iconFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCarIconDialog fd = new pickCarIconDialog();

                fd.show(getSupportFragmentManager(),"pick car icon");
            }
        });
    }



    static private int carIconToSave;
    public static void setCarIcon(int ID){
       carIconToSave = ID;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, add_vehicle_page.class);
    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        ImageView icon = (ImageView)findViewById(R.id.vehicle_icon);
//        icon.setImageResource(carIconToSave);
//    }
}
