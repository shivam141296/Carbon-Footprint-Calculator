package com.example.carbon_footprint_calculation.majorproject_partone.extra;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.UI.more_graphs;

import java.util.Calendar;


public class pickDateDialog extends android.support.v4.app.DialogFragment {
    boolean pickDate = true;
    int range;
    String type;
    DatePickerDialog.OnDateSetListener ondateSet;

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.pick_date_dialog, null);

        final TextView txt = (TextView) v.findViewById(R.id.txt_chart_type);

        final RadioGroup group1 = (RadioGroup) v.findViewById(R.id.radio_pickChartType);
        String[] chartType = getResources().getStringArray(R.array.chart_type);
        //create the buttons:
        final String selection_pie = chartType[0];
        RadioButton btn_piechart = new RadioButton(getActivity());
        btn_piechart.setText(selection_pie);
        btn_piechart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pie chart
                type = selection_pie;
            }
        });
        group1.addView(btn_piechart);


        final String selection_bar = chartType[1];
        RadioButton btn_barchart = new RadioButton(getActivity());
        btn_barchart.setText(selection_bar);
        btn_barchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = selection_bar;
                //bar chart
            }
        });
        group1.addView(btn_barchart);



        final RadioGroup group = (RadioGroup) v.findViewById(R.id.radio_pickDate);
        final int numDays[] = getResources().getIntArray(R.array.num_days);
        for(int i = 0; i < numDays.length; i++) {
            RadioButton btn = new RadioButton(getActivity());
            group.addView(btn);
            if(numDays[i] == 1) {
                btn.setText(R.string.pick_a_date);
                btn.setChecked(true);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt.setVisibility(View.INVISIBLE);
                        pickDate = true;
                        group1.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                btn.setText(getString(R.string.date_range, numDays[i]));
                final int selection = numDays[i];
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt.setVisibility(View.VISIBLE);
                        group1.setVisibility(View.VISIBLE);
                        pickDate = false;
                        range = selection;
                    }
                });
            }
        }

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.show_emissions_for)
                .setView(v)
                .setPositiveButton(R.string.ok, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pickDate) {
                            Calendar cal = Calendar.getInstance();
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH);
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog dateDialog = new DatePickerDialog(getContext(), ondateSet,
                                    year, month, day);
                            dateDialog.show();
                            dialog.dismiss();
                        } else if (type == null) {
                            Toast.makeText(getActivity(), R.string.chartType_unselect,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = more_graphs.makeIntentWithData(getActivity(), null, range, type);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });

            }


        });
        return dialog;
    }

}

