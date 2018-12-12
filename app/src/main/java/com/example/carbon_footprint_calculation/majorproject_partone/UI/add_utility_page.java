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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carbon_footprint_calculation.majorproject_partone.R;

import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class add_utility_page extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Utility newUtility;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int idOfSelected;
    private String billTypeMessage;
    private double billAmount;
    private int numPeople;
    private EditText bill_amount_input;
    private EditText num_people_input;
    private Intent IntentMode;
    private String mode;
    private String billStart;
    private String billEnd;
    private boolean startOrEnd;
    private TextView from;
    private TextView to;
    private boolean fromSelected = false;
    private boolean toSelected = false;
    private long interval;
    private long utilityID;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String TAG_ADD_UTILITY_PAGE = "add_utility_page";
    private static megaDataPackHelper DB;
    private Context thisContext = this;
    private String[] billTypes;
    private String BILL_TYPE_ELECTRICITY = "Electricity";
    private String BILL_TYPE_NATURAL_GAS = "Natural Gas";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_utility_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_utility_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        TextView title = (TextView) findViewById(R.id.add_utility_toolbar_title);
        title.setText(R.string.new_utility);

        //-------setup Database------
        DB = new megaDataPackHelper(this);
        //---------------------------
        IntentMode = getIntent();
        mode = IntentMode.getStringExtra("mode");
        setResult(RESULT_CANCELED);

        billTypes = getResources().getStringArray(R.array.bill_type);

        Button billStartDate = (Button) findViewById(R.id.btn_billDateFrom);
        Button billEndDate = (Button) findViewById(R.id.btn_billDateTo);
        radioGroup = (RadioGroup) findViewById(R.id.radio_billType);
        bill_amount_input= (EditText) findViewById(R.id.edit_utilityAmount);
        num_people_input= (EditText) findViewById(R.id.edit_utilityPeople);

        if(mode.equals("edit")){
            utilityID = IntentMode.getLongExtra("utilityID", 0);
            newUtility=DB.UB_getUtilityByID(utilityID);
            interval = newUtility.getDays();
            bill_amount_input.setText(""+newUtility.getBillAmount());
            num_people_input.setText(""+newUtility.getNumPeople());
            from = (TextView) findViewById(R.id.txt_billDateFrom);
            from.setText(newUtility.getBillStartDate());
            to = (TextView) findViewById(R.id.txt_billDateTo);
            to.setText(newUtility.getBillEndDate());
            changeTitle(title);
            fromSelected = true;
            toSelected = true;
            try {
                preSetDates();
            } catch (ParseException e) {
                Log.e(TAG_ADD_UTILITY_PAGE,"parsing date error");
            }
        }
        setupRadioButton();

        from = (TextView) findViewById(R.id.txt_billDateFrom);
        to = (TextView) findViewById(R.id.txt_billDateTo);
        billStart=from.getText().toString();
        billEnd=to.getText().toString();

        setupDateButton(billStartDate);
        setupDateButton(billEndDate);
    }

    //actionbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mode.equals("edit")){
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
                    //increment utitilies entered today count
                    SharedPreferences sharedPreferences = thisContext.getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    int numUtilitiesEnteredToday = sharedPreferences.getInt("numUtilitiesEnteredToday", 0);
                    numUtilitiesEnteredToday++;
                    editor.putInt("numUtilitiesEnteredToday", numUtilitiesEnteredToday);
                    editor.commit();


                    String amount = bill_amount_input.getText().toString();
                    String people= num_people_input.getText().toString();

                    if(radioGroup.getCheckedRadioButtonId() == -1){
                        Toast.makeText(add_utility_page.this,R.string.toast_type_missing,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }else if(billStart.startsWith("x")){
                        Toast.makeText(add_utility_page.this,R.string.toast_startDate_missing,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }else if(billEnd.startsWith("x")){
                        Toast.makeText(add_utility_page.this,R.string.toast_endDate_missing,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    } else if(amount.length() == 0){
                        Toast.makeText(add_utility_page.this,R.string.toast_billAmount_missing,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }else if(amount.equals(".")){
                        Toast.makeText(add_utility_page.this, R.string.invalidBillAmount,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else if(people.length() == 0){
                        Toast.makeText(add_utility_page.this,R.string.toast_number_ppl_missing,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else if(people.equals("0")){
                        Toast.makeText(add_utility_page.this, R.string.invalidNumPeople,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    idOfSelected = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(idOfSelected);

                    if (radioButton.getText().toString().equals(billTypes[0])){
                        billTypeMessage = BILL_TYPE_ELECTRICITY;
                    }
                    else {
                        billTypeMessage = BILL_TYPE_NATURAL_GAS;
                    }


                    billAmount = Double.parseDouble(amount);
                    numPeople = Integer.parseInt(people);
                    billStart = from.getText().toString();
                    billEnd = to.getText().toString();


                    if (toSelected&&fromSelected){
                        long msDiff = endMilli - startMilli;
                        interval = TimeUnit.MILLISECONDS.toDays(msDiff)+1;
                        Log.i(TAG_ADD_UTILITY_PAGE,"interval "+interval);
                    }
                    if (interval <=0){
                        Toast.makeText(add_utility_page.this,R.string.toast_error_date,Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    setResult(RESULT_OK);
                    if(mode.equals("edit")){
                        utilityID = IntentMode.getLongExtra("utilityID",0);
                        newUtility.setBillType(billTypeMessage);
                        newUtility.setBillStartDate(billStart);
                        newUtility.setBillEndDate(billEnd);
                        newUtility.setBillAmount(billAmount);
                        newUtility.setNumPeople(numPeople);
                        newUtility.setDays(interval);
                        Log.i(TAG_ADD_UTILITY_PAGE,""+DB.UB_editUtility(utilityID,newUtility));
                        finish();

                    }
                    else{
                        newUtility = new Utility(billTypeMessage,billStart,billEnd,billAmount, numPeople, interval);
                        DB.UB_addNewUtility(newUtility);
                        finish();
                    }
                    break;
                case R.id.action_delete:
                    DB.UB_deleteUtility(utilityID);
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

    private void changeTitle(TextView title) {
        title.setText(R.string.edit_utility_title);
    }


    private void setupRadioButton() {
        RadioGroup group = (RadioGroup) findViewById(R.id.radio_billType);

        //create the buttons:
        RadioButton button1 = new RadioButton(this);
        button1.setText(billTypes[0]);//
        final TextView amountText = (TextView)findViewById(R.id.txt_utilityAmount);
        button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amountText.setText(R.string.amount_used_in_Kwh);
                }
            });
        group.addView(button1);
        RadioButton button2 = new RadioButton(this);
        button2.setText(billTypes[1]);//
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountText.setText(R.string.amount_used_in_gj);
            }
        });
        group.addView(button2);
        if (mode.equals("edit")){
            if (newUtility.getBillType().equals(billTypes[0])){
                group.check(button1.getId());
            }
            else
                group.check(button2.getId());
        }

    }
    private void preSetDates() throws ParseException {

        Date startD = dateFormat.parse(newUtility.getBillStartDate());
        startMilli = startD.getTime();

        Date endD = dateFormat.parse(newUtility.getBillEndDate());
        endMilli = endD.getTime();
    }

    private void setupDateButton(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                String check = button.getText().toString();
                if(check.equals(getString(R.string.from))) {
                    startOrEnd = true;
                } else if(check.equals(getString(R.string.to))){
                    startOrEnd = false;
                }


                if (mode.equals("edit")){//preset the calendar to the date on the bill
                    String date;
                    if (startOrEnd) {
                        date = newUtility.getBillStartDate();
                    }
                    else {
                        date = newUtility.getBillEndDate();
                    }
                    String[] dateArr = date.split("-");
                    year = Integer.parseInt(dateArr[0]);
                    month = Integer.parseInt(dateArr[1]) - 1;
                    day = Integer.parseInt(dateArr[2]);
                }
                else if (mode.equals("add")){//if mode add, preset the calendars to today or previously entered date
                    String date = null;
                    if (startOrEnd && !billStart.startsWith("x")) {
                        date = billStart;
                        String[] dateArr = date.split("-");
                        year = Integer.parseInt(dateArr[0]);
                        month = Integer.parseInt(dateArr[1]) - 1;
                        day = Integer.parseInt(dateArr[2]);
                    }
                    else if (!startOrEnd && !billEnd.startsWith("x")){
                        date = billEnd;
                        String[] dateArr = date.split("-");
                        year = Integer.parseInt(dateArr[0]);
                        month = Integer.parseInt(dateArr[1]) - 1;
                        day = Integer.parseInt(dateArr[2]);
                    }
                }

                DatePickerDialog dateDialog = new DatePickerDialog(add_utility_page.this,add_utility_page.this,
                        year,month,day);
                if (startOrEnd) {
                    dateDialog.setTitle(R.string.select_from);
                }
                else
                    dateDialog.setTitle(R.string.selet_to);

                dateDialog.show();
            }
        });

    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, add_utility_page.class);
    }

    private Long startMilli;
    private Long endMilli;
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        String dateOfBill = dateFormat.format(date);

        if(startOrEnd) {
            TextView from = (TextView) findViewById(R.id.txt_billDateFrom);
            from.setText(dateOfBill);
            billStart = dateOfBill;
            startMilli = calendar.getTimeInMillis();
            fromSelected = true;
        }
        else {
            TextView to = (TextView) findViewById(R.id.txt_billDateTo);
            to.setText(dateOfBill);
            billEnd = dateOfBill;
            endMilli = calendar.getTimeInMillis();
            toSelected = true;
        }
    }

}
