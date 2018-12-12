package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;
import com.example.carbon_footprint_calculation.majorproject_partone.R;

import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Car;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Journey;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Route;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class edit_journey_page extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
        //TODO: the edit journey UI became very messy. Need redesign

    private static final String TAG = "edit_journey_page";
    private Button edit_car_btn;
    private megaDataPackHelper DB;
    private Journey journeyToEdit;
    private DataPack data;
    private String transMode;
    private String[] transModes;
    private RadioGroup radioGroup;
    private RadioButton car_selection;
    private RadioButton bus_selection;
    private RadioButton walk_selection;
    private RadioButton train_selection;
    private Car editedCar;
    private Route editedRoute;
    private String editedDate;
    final private String TRANS_MODE_CAR = "car";
    final private String TRANS_MODE_WALK = "walk/bike";
    final private String TRANS_MODE_BUS = "bus";
    final private String TRANS_MODE_SKYTRAIN = "skytrain";
    final private int EDIT_CAR = 0;
    final private int EDIT_ROUTE = 1;
    private int iconID;
    private ImageView iconFrame;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey_page);

        toolbar = (Toolbar) findViewById(R.id.edit_journey_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        TextView title = (TextView) findViewById(R.id.edit_journey_toolbar_title);
        title.setText(R.string.edit_journey_intro);

        data = DataPack.getData();
        iconFrame = (ImageView)findViewById(R.id.trans_icon);
        edit_car_btn = (Button)findViewById(R.id.btn_edit_car);
        DB = new megaDataPackHelper(this);
        DB.close();

        Intent intent = getIntent();

        journeyToEdit = DB.RJ_getJourneyByID(intent.getLongExtra("journeyID",0));
        data.setEditJourney(journeyToEdit);
        transMode = journeyToEdit.getTransMode();
        editedDate = journeyToEdit.getJourneyDate();
        editedRoute = journeyToEdit.getRoute();
        editedCar = journeyToEdit.getCar();

        transModes = getResources().getStringArray(R.array.transMode);
        setTextViews();
        setRadioGroup();
        preSelectRadioButton();
        setEditDateButton();
        setEditCarButton();
        setEditRouteButton();
    }

    //actionbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_confirm:
                if(editedCar==null&&transMode.equals(TRANS_MODE_CAR)){
                    Toast.makeText(edit_journey_page.this,R.string.toast_car_missing,Toast.LENGTH_SHORT).show();
                    return false;
                }
                switch (transMode) {
                    case TRANS_MODE_CAR:
                        Log.i(TAG,""+DB.RJ_editJourney(transMode, journeyToEdit.getJourneyID(), editedCar, editedRoute, editedDate));
                        break;
                    case TRANS_MODE_BUS:
                        Log.i(TAG,""+DB.RJ_editJourney(transMode, journeyToEdit.getJourneyID(), null, editedRoute, editedDate));
                        break;
                    case TRANS_MODE_WALK:
                        Log.i(TAG,""+DB.RJ_editJourney(transMode, journeyToEdit.getJourneyID(), null, editedRoute, editedDate));
                        break;
                    case TRANS_MODE_SKYTRAIN:
                        Log.i(TAG,""+DB.RJ_editJourney(transMode, journeyToEdit.getJourneyID(), null, editedRoute, editedDate));
                        break;
                    default:
                        Log.e(TAG,"mode string error");
                        return false;
                }
                finish();
                break;
            case R.id.action_delete:
                Log.e(TAG,"deletion is a "+DB.RJ_deleteJourney(journeyToEdit.getJourneyID()));
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

    private void setEditDateButton() {
        //setup picking date dialog

        Button editDate = (Button)findViewById(R.id.btn_edit_date);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String date = editedDate;
                String[] dateArr = date.split("-");
                int year = Integer.parseInt(dateArr[0]);
                int month = Integer.parseInt(dateArr[1]) - 1;
                int day = Integer.parseInt(dateArr[2]);


                DatePickerDialog dateDialog = new DatePickerDialog(edit_journey_page.this,edit_journey_page.this,
                        year, month, day);
                dateDialog.setTitle(getString(R.string.select_date));
                dateDialog.show();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        editedDate = dateFormat.format(date);
        setTextViews();
    }

    private void setEditRouteButton() {
        Button editRoute = (Button)findViewById(R.id.btn_edit_route);
        editRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = add_route_page.makeIntent(edit_journey_page.this);
                intent.putExtra("mode", "edit_journey");
                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED)
            return;
        switch(requestCode) {
            case EDIT_CAR:
                editedCar = this.data.getEditJourney().getCar();
                break;
            case EDIT_ROUTE:
                editedRoute = this.data.getEditJourney().getRoute();
                Log.i(TAG,""+this.data.getEditJourney().getRoute().getTotalDistance());
                break;
        }
        setTextViews();
    }
    private void setEditCarButton() {
        Button editCar = (Button)findViewById(R.id.btn_edit_car);
        editCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = add_vehicle_page.makeIntent(edit_journey_page.this);
                intent.putExtra("mode", "edit_journey");
                startActivityForResult(intent,0);
            }
        });
    }

    private void preSelectRadioButton() {
        switch (transMode) {
            case TRANS_MODE_CAR:
                radioGroup.check(car_selection.getId());
                break;
            case TRANS_MODE_BUS:
                radioGroup.check(bus_selection.getId());
                edit_car_btn.setVisibility(View.INVISIBLE);
                break;
            case TRANS_MODE_WALK:
                radioGroup.check(walk_selection.getId());
                edit_car_btn.setVisibility(View.INVISIBLE);
                break;
            case TRANS_MODE_SKYTRAIN:
                radioGroup.check(train_selection.getId());
                edit_car_btn.setVisibility(View.INVISIBLE);
                break;
            default:
                Log.e(TAG,"mode string error");
        }
    }
    private void setTextViews() {
        TextView date = (TextView)findViewById(R.id.txt_edit_date);
        date.setText(editedDate);
        TextView car = (TextView)findViewById(R.id.txt_edit_car);
        switch (transMode){
            case TRANS_MODE_CAR:
                if (journeyToEdit.getCar()!=null) {
                    car.setText(journeyToEdit.getCar().getNickname() + "\n" + journeyToEdit.getCar().getSpecStr());
                    iconFrame.setImageResource(journeyToEdit.getCar().getIconID());
                }
                else{
                    iconFrame.setImageResource(R.mipmap.vehicleicon);
                    car.setText(R.string.empteyCarData);
                }
                break;
            case TRANS_MODE_WALK:
                car.setText(transModes[1]);
                iconFrame.setImageResource(R.mipmap.ic_bike);
                break;
            case TRANS_MODE_BUS:
                car.setText(transModes[2]);
                iconFrame.setImageResource(R.mipmap.ic_bus);
                break;
            case TRANS_MODE_SKYTRAIN:
                car.setText(transModes[3]);
                iconFrame.setImageResource(R.mipmap.ic_train);
                break;
            default:
                Log.e(TAG,"mode string error");
        }

        TextView route = (TextView)findViewById(R.id.txt_edit_route);
        route.setText(journeyToEdit.getRoute().getRouteDescription());
    }

    private void setRadioGroup() {
        radioGroup = (RadioGroup)findViewById(R.id.rdgroup_transMode);

        //create the radio buttons:

        car_selection = new RadioButton(this);
        car_selection.setText(transModes[0]);
        car_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_car_btn.setVisibility(View.VISIBLE);
                transMode = TRANS_MODE_CAR;
                setTextViews();
            }
        });
        radioGroup.addView(car_selection);


        walk_selection = new RadioButton(this);
        walk_selection.setText(transModes[1]);
        walk_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_car_btn.setVisibility(View.INVISIBLE);
                transMode = TRANS_MODE_WALK;
                iconFrame.setImageResource(R.mipmap.ic_bike);
                setTextViews();
            }
        });
        radioGroup.addView(walk_selection);


        bus_selection = new RadioButton(this);
        bus_selection.setText(transModes[2]);
        bus_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_car_btn.setVisibility(View.INVISIBLE);
                transMode =TRANS_MODE_BUS;
                iconFrame.setImageResource(R.mipmap.ic_bus);
                setTextViews();
            }
        });
        radioGroup.addView(bus_selection);

        train_selection = new RadioButton(this);
        train_selection.setText(transModes[3]);
        train_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_car_btn.setVisibility(View.INVISIBLE);
                transMode = TRANS_MODE_SKYTRAIN;
                iconFrame.setImageResource(R.mipmap.ic_train);
                setTextViews();
            }
        });
        radioGroup.addView(train_selection);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, edit_journey_page.class);
    }
}
