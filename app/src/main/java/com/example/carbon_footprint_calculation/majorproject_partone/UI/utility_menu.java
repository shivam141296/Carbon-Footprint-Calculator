package com.example.carbon_footprint_calculation.majorproject_partone.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.data.DataPack;
import com.example.carbon_footprint_calculation.majorproject_partone.extra.feedbackDialog;

import com.example.carbon_footprint_calculation.majorproject_partone.data.megaDataPackHelper;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;

public class utility_menu extends android.support.v4.app.Fragment {
    private ListView utilityList;
    private String strToDisplay;
    private long utilityID;
    private megaDataPackHelper DB;
    private List<Utility> allUtilities;
    public static Activity utilityMenu;
    public static final int ADD_UTILITY_CODE = 0;
    public static final int EDIT_UTILITY_CODE = 1;
    public static final String UTILITY_ID = "com.example.ottot.carbontracker.UI.add_utility_page - DB use ID";
    private View v;
    private Toolbar toolbar;
    private String BILL_TYPE_ELECTRICITY = "Electricity";
    private String BILL_TYPE_NATURAL_GAS = "Natural Gas";
    private DataPack data = DataPack.getData();
    private String unit;
    private int unitRID;
    private int unitPrePersonRID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_utility_menu, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        v = getView();

        unit = data.getUnit_setting();

        if (unit.equals("Kg")){
            unitRID = R.string.kg;
            unitPrePersonRID = R.string.kg_per_person;
        }
        else {
            unitRID = R.string.tree_day;
            unitPrePersonRID = R.string.tree_day_per_person;
        }

        toolbar = (Toolbar) v.findViewById(R.id.utility_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        TextView title = (TextView) v.findViewById(R.id.main_toolbar_title);
        title.setText(R.string.utility_menu);

        utilityMenu = getActivity(); //created to call finish on this object in other activity
        //setup DB
        DB = new megaDataPackHelper(utilityMenu);

        allUtilities = DB.UB_getAllUtilities();

        //setup buttons, click listeners and refresh list
        setupAddUtilityButton();
        populateUtilityList();
        setupItemClick();
    }

    @Override
    public void onDestroy() {
        //close DB on destroy
        super.onDestroy();
        DB.close();
    }


    private void populateUtilityList() {
        //use arrayadapter to populate listview with saved utilities
        ArrayAdapter<Utility> adapter = new utilityArrayAdapter();
        utilityList = (ListView)v.findViewById(R.id.list_utility);
        utilityList.setAdapter(adapter);

        //display message if list is empty
        TextView emptyUtilityTxt = (TextView) v.findViewById(R.id.emptyUtilityTxt);
        if(adapter.getCount()>0) {
            emptyUtilityTxt.setVisibility(View.INVISIBLE);
        }
        else {
            emptyUtilityTxt.setVisibility(View.VISIBLE);
        }
    }
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private class utilityArrayAdapter extends ArrayAdapter<Utility> {
        public utilityArrayAdapter() {
            super(utilityMenu, R.layout.utility_listview, allUtilities);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = utilityMenu.getLayoutInflater().inflate(R.layout.utility_listview, parent, false);
            }

            Utility currentUtility = allUtilities.get(position);
            TextView utilityType = (TextView)itemView.findViewById(R.id.utilityType);
            if (currentUtility.getBillType().equals(BILL_TYPE_ELECTRICITY)){
                utilityType.setText(getString(R.string.electricity));
            }
            else if (currentUtility.getBillType().equals(BILL_TYPE_NATURAL_GAS)){
                utilityType.setText(getString(R.string.natrualGas));
            }
            TextView billAmount = (TextView)itemView.findViewById(R.id.billAmount);
            if (currentUtility.getBillType().equals(BILL_TYPE_ELECTRICITY)){
                billAmount.setText(String.valueOf(currentUtility.getBillAmount()) + getString(R.string.kwh));
            }
            else {
                billAmount.setText(String.valueOf(currentUtility.getBillAmount()) + getString(R.string.gj));
            }
            TextView dailyEmission = (TextView) itemView.findViewById(R.id.dailyEmission);
            dailyEmission.setText(decimalFormat.format(currentUtility.getEmissionPerPerson(unit)) +" "+ getString(unitPrePersonRID));
            TextView startDate = (TextView) itemView.findViewById(R.id.txt_billDateFrom);
            startDate.setText(getString(R.string.from) + String.valueOf(currentUtility.getBillStartDate()) );
            TextView endDate = (TextView) itemView.findViewById(R.id.txt_billDateEnd);
            endDate.setText(" " + getString(R.string.to) + currentUtility.getBillEndDate());
            return itemView;
        }
    }

    private void setupAddUtilityButton() {
        FloatingActionButton btn = (FloatingActionButton) v.findViewById(R.id.fab_add_utility);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = add_utility_page.makeIntent(utilityMenu);
                intent.putExtra("mode","add");
                startActivityForResult(intent,ADD_UTILITY_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_CANCELED){
            return;
        }
        switch (requestCode) {
            case ADD_UTILITY_CODE:
                String tips[] = getResources().getStringArray(R.array.tips);
                feedbackDialog fd = new feedbackDialog();
                fd.setIsTips(true);
                fd.setIsEmissions(false);
                fd.setIsUtiltities(true);
                Utility utility = DB.UB_getLatestUtility();
                DecimalFormat decimalFormat = new DecimalFormat("#.####");
                String utilityEmission = getString(R.string.utility_tip_p1) +
                        decimalFormat.format(utility.getEmissionPerPerson(unit)) +
                        getString(unitPrePersonRID)+". ";
                ArrayList<Integer> recentTips = DB.TP_getAllIndex();
                Random rand = new Random();
                int index = (Math.abs(rand.nextInt() % 8) + 8);
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
                            index = (Math.abs(rand.nextInt() % 8) + 8);
                        }
                    }
                }
                long id = DB.TP_addNewTip(index);
                if (recentTips.size() == 7) {
                    DB.TP_deleteEarliestTip(id);
                }
                fd.setStrToDisplay(utilityEmission + tips[index]);
                fd.show(getFragmentManager(), "tips dialog");
                break;
            case EDIT_UTILITY_CODE:
                break;

        }

        allUtilities = DB.UB_getAllUtilities();
        populateUtilityList();
    }

    private void setupItemClick() {
        utilityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = add_utility_page.makeIntent(utilityMenu);
                utilityID = allUtilities.get(position).getUtilityID();
                editIntent.putExtra("mode","edit");
                editIntent.putExtra("utilityID", utilityID);
                startActivityForResult(editIntent,1);
            }
        });

        utilityList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });
    }



    public static Intent makeIntent(Context context) {
        return new Intent(context, utility_menu.class);
    }

}
