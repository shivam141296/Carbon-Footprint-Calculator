package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Journey;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Receives the alarm and then sends a notification to the user
 */

public class notification_receiver extends BroadcastReceiver {
    public static final int MONTH_AND_A_HALF_DAYS = 45;
    private megaDataPackHelper DB;
    @Override
    public void onReceive(Context context, Intent intent) {
        DB = new megaDataPackHelper(context);
        DB.close();
        NotificationCompat.Builder mBuilder;
        int numJourneys = 0;
        List<Journey> journeys = DB.RJ_getAllJourney();
        List<Utility> utilities = DB.UB_getAllUtilities();
        List<Utility> gasUtilities = new ArrayList<>();
        List<Utility> electricityUtilities = new ArrayList<>();
        for(Utility utility : utilities) {
            switch (utility.getBillType()) {
                case "Natural Gas":
                    gasUtilities.add(utility);
                    break;
                case "Electricity":
                    electricityUtilities.add(utility);
                    break;
            }
        }
        boolean hasRecentGasBill = hasRecentBill(gasUtilities);
        boolean hasRecentElectricityBill = hasRecentBill(electricityUtilities);

        for(Journey journey : journeys) {
            try {
                if(isToday(journey.getJourneyDate())) {
                    numJourneys++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(numJourneys == 0) {
            mBuilder = makeNotificationBuilderWithGivenTextAndTitle(context.getString(R.string.notification_no_journey),
                    context.getString(R.string.notification_title_no_journey),
                    context);
            Intent resultIntent = new Intent(context, base_activity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            base_activity.setIsJourneyNotification(true);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
        } else if(!hasRecentGasBill){
            Intent resultIntent = new Intent(context, add_utility_page.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent.putExtra("mode", "add");
            base_activity.setIsUtilityNotification(true);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(add_utility_page.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder = makeNotificationBuilderWithGivenTextAndTitle(context.getString(R.string.notification_no_utility),
                    context.getString(R.string.notification_title_no_gas_utility),
                    context);
            mBuilder.setContentIntent(resultPendingIntent);
        } else if(!hasRecentElectricityBill) {
            Intent resultIntent = new Intent(context, add_utility_page.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent.putExtra("mode", "add");
            base_activity.setIsUtilityNotification(true);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(add_utility_page.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder = makeNotificationBuilderWithGivenTextAndTitle(context.getString(R.string.notification_no_utility),
                    context.getString(R.string.notification_title_no_electricity_utility),
                    context);
            mBuilder.setContentIntent(resultPendingIntent);
        } else {
            mBuilder = makeNotificationBuilderWithGivenTextAndTitle(context.getString(R.string.notification_general),
                    context.getString(R.string.notification_title_general, numJourneys),
                    context);
            Intent resultIntent = new Intent(context, base_activity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            base_activity.setIsJourneyNotification(true);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
        }
        int notifyId = 1;
        NotificationManager notifier =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notifier.notify(notifyId, mBuilder.build());
    }

    private boolean hasRecentBill(List<Utility> list) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -MONTH_AND_A_HALF_DAYS);
        Date start = calendar.getTime();
        String endDate = dateFormat.format(end);
        String startDate = dateFormat.format(start);
        if(list.size() == 0) {
            return false;
        } else {
            for(Utility utility : list) {
                boolean utilityStartDateInRange = false;
                boolean utilityEndDateInRange = false;
                try {
                    utilityStartDateInRange = isWithinRange(startDate, endDate, utility.getBillStartDate());
                    utilityEndDateInRange = isWithinRange(startDate, endDate, utility.getBillEndDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(utilityEndDateInRange || utilityStartDateInRange) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isToday(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date check = dateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        String temp = dateFormat.format(today);
        today = dateFormat.parse(temp);
        return check.equals(today);
    }

    private boolean isWithinRange(String start, String end, String checkDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(checkDate);
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);
        return !date.before(startDate) && !date.after(endDate);
    }

    private NotificationCompat.Builder makeNotificationBuilderWithGivenTextAndTitle(String text, String title, Context context) {
        return new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true);
    }
}
