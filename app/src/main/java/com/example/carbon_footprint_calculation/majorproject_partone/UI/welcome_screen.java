package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antonionicolaspina.revealtextview.RevealTextView;
import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;

import java.util.Calendar;
import java.util.Date;

public class welcome_screen extends AppCompatActivity {
    private final int lasting = 4000;
    private DataPack data = DataPack.getData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNotificationAlarm();

        RevealTextView txt_title = (RevealTextView) findViewById(R.id.txt_title);
        setupTitle(txt_title);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        data.setUnit_setting(getPrefs.getString("carbon_unit","Kg"));
//        Toast.makeText(welcome_screen.this,data.getUnit_setting(),Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(welcome_screen.this,base_activity.class);
                welcome_screen.this.startActivity(mainIntent);
                welcome_screen.this.finish();
            }
        }, lasting);

    }



    private void setupTitle(RevealTextView txt) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "dt.ttf");
            txt.setTypeface(typeface);
    }

    private void setupNotificationAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Calendar today = Calendar.getInstance();
        Date notification = calendar.getTime();
        Date todayDate = today.getTime();

        if(todayDate.after(notification)) {
            Log.i("WELCOME", "" + calendar.getTimeInMillis());
            Log.i("WELCOME", "" + today.getTimeInMillis());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(this, notification_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }
}
