package com.example.carbon_footprint_calculation.majorproject_partone.extra;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.carbon_footprint_calculation.majorproject_partone.R;
import com.example.carbon_footprint_calculation.majorproject_partone.UI.add_vehicle_page;



public class pickCarIconDialog extends DialogFragment {
    LayoutInflater inflater;
    View v;
    boolean selected;
    int iconIDToSave;


    DialogInterface.OnDismissListener listener;
    //icon resource-------------
    private int iconRowCount = 3;
    private int iconColCount = 2;
    private int icon1 = R.mipmap.ic_car1;
    private int icon2 = R.mipmap.ic_car2;
    private int icon3 = R.mipmap.ic_car3;
    private int icon4 = R.mipmap.ic_car4;
    private int icon5 = R.mipmap.ic_car5;
    private int icon6 = R.mipmap.ic_car6;
    private int[][] iconIDArray = {{icon1,icon2},{icon3,icon4},{icon5,icon6}};//add icon into the array to show more icons
    //--------------------------


    public void setListener(DialogInterface.OnDismissListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.pick_icon_dialog,null);

        setupGrid();

        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle(R.string.pick_icon_title)
                .setView(v)
                .setMessage(R.string.please_pick_icon)
                //.setPositiveButton(R.string.ok, listener) we don't need OK since user clicks an icon to dismiss
                .create();
    }

    private void setupGrid() {
        TableLayout imageGrid = (TableLayout) v.findViewById(R.id.image_table);
        for (int i =0;i<iconRowCount;i++) {
            TableRow newRow = new TableRow(getContext());
            newRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f)
            );
            imageGrid.addView(newRow);
            for (int j = 0; j < iconColCount; j++) {
                final ImageView newImageButton = new ImageView(getContext());
                newImageButton.setImageResource(iconIDArray[i][j]);
                newImageButton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f)
                );
                newImageButton.setPadding(0,0,0,0);
                newRow.addView(newImageButton);
                final int indexX = j;
                final int indexY = i;

                newImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iconIDToSave = iconIDArray[indexY][indexX];
                        add_vehicle_page.setCarIcon(iconIDToSave);
                        ImageView icon = (ImageView)getActivity().findViewById(R.id.vehicle_icon);
                        icon.setImageResource(iconIDToSave);
                        dismiss();
                    }

                });

            }
        }
    }



}
