package com.example.carbon_footprint_calculation.majorproject_partone.extra;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.UI.base_activity;
import com.example.carbon_footprint_calculation.majorproject_partone.UI.journey_menu;
import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;

import java.util.ArrayList;
import java.util.Random;


public class feedbackDialog extends DialogFragment {
    public static final int NUM_UNIQUE_TIPS_PER_CATEGORY = 8;
    private megaDataPackHelper DB;
    LayoutInflater inflater;
    View v;
    String emissionText;

    static boolean isTips = false;
    static boolean isEmissions = false;
    static boolean isUtiltities = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DB = new megaDataPackHelper(getActivity());
        DB.close();
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.emissiondialog,null);
        TextView dialogText = (TextView)v.findViewById(R.id.text_feedback);
        dialogText.setText(emissionText);
        Button btn = (Button)v.findViewById(R.id.btn_nextTip);
        if(isTips) {
            setupNextTipButton(btn, dialogText);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //finish all activities and return to main menu
                        if(isUtiltities) {
                            isUtiltities = false;
                        } else {
                            Intent intent = base_activity.makeIntent(getContext());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            if(isEmissions) {
                                journey_menu.setIsJourneyAdded(true);
                            }
                            startActivity(intent);
                        }
                        break;
                }
            }

        };
        if(isEmissions) {
            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle(R.string.emissionTitle)
                    .setView(v)
                    .setMessage(R.string.emissionIntro)
                    .setPositiveButton(R.string.ok, listener)
                    .create();
        } else if(isTips){
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.tips)
                    .setView(v)
                    .setPositiveButton(R.string.ok, listener)
                    .create();
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error)
                .setView(v)
                .setPositiveButton(R.string.ok, listener)
                .create();
    }

    private void setupNextTipButton(Button button, final TextView tv) {
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tips[] = getResources().getStringArray(R.array.tips);
                if(isUtiltities) {
                    int index = getNextTipIndex(NUM_UNIQUE_TIPS_PER_CATEGORY);
                    tv.setText(tips[index]);
                } else {
                    int index = getNextTipIndex(0);
                    tv.setText(tips[index]);
                }
            }
        });
    }

    private int getNextTipIndex(int displacement) {
        ArrayList<Integer> recentTips = DB.TP_getAllIndex();
        Random rand = new Random();
        int index = (Math.abs(rand.nextInt() % NUM_UNIQUE_TIPS_PER_CATEGORY) + displacement);
        if(!DB.TP_checkEmpty()) {
            boolean isUniqueIndex = false;
            while(!isUniqueIndex) {
                int count = 0;
                for(int recent : recentTips) {
                    if(recent == index) {
                        count++;
                    }
                }
                if(count == 0) {
                    isUniqueIndex = true;
                } else {
                    index = (Math.abs(rand.nextInt() % NUM_UNIQUE_TIPS_PER_CATEGORY) + displacement);
                }
            }
        }
        long id = DB.TP_addNewTip(index);
        if(recentTips.size() == 7) {
            DB.TP_deleteEarliestTip(id);
        }
        return index;
    }

    public void setStrToDisplay(String str){
        emissionText = str;
    }

    public static void setIsEmissions(boolean bool) {
        isEmissions = bool;
    }


    public static void setIsTips(boolean bool) {
        isTips = bool;
    }

    public static void setIsUtiltities(boolean bool) {
        isUtiltities = bool;
    }
}
