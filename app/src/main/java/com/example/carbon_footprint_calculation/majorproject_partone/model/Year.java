package com.example.carbon_footprint_calculation.majorproject_partone.model;

import java.util.ArrayList;
import java.util.List;


public class Year {
    private String year;
    private List<VehicleSpec> specs;
    private int numSpec = 0;

    public Year(String year){
        this.year = year;
        specs = new ArrayList<>();
    }


    public void addSpec(Double highway, Double city, String drive, String transmission, String vClass, Double engDisp, String fuelType){
        VehicleSpec newSpec = new VehicleSpec(highway,city,drive,transmission,vClass,engDisp,fuelType);
        specs.add(newSpec);
        numSpec++;
    }

    public void addSpec(Double highway, Double city, String drive, String transmission, String vClass, String engDisp,String fuelType){
        VehicleSpec newSpec = new VehicleSpec(highway,city,drive,transmission,vClass,engDisp,fuelType);
        specs.add(newSpec);
        numSpec++;
    }

    public String getYearName() {
        return year;
    }
    public VehicleSpec getSpecByIndex(int index){
        return specs.get(index);
    }

    //get all specs string description
    public String[] getAllSpecs(){
        String[] allSpecs = new String[numSpec];
        for (int i = 0; i <numSpec; i++){
            allSpecs[i] = specs.get(i).getSpecStr();
        }
        return allSpecs;
    }
}
