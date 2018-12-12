package com.example.carbon_footprint_calculation.majorproject_partone.model;


public class VehicleSpec {
    private Double city;
    private Double highway;
    private String drive;
    private String transmission;
    private String vClass;
    private Double engDisp;
    private boolean engDispNA;
    private String fuelType;

    public VehicleSpec(Double highway,Double city, String drive,String transmission, String vClass,Double engDisp,String fuel) {
        engDispNA = false;
        this.engDisp = engDisp;
        this.vClass = vClass;
        this.transmission = transmission;
        this.drive = drive;
        this.highway = highway;
        this.city = city;
        fuelType = fuel;
    }

    public VehicleSpec(Double highway,Double city, String drive,String transmission, String vClass,String engDisp,String fuel) {
        engDispNA = true;
        this.vClass = vClass;
        this.transmission = transmission;
        this.drive = drive;
        this.highway = highway;
        this.city = city;
        fuelType = fuel;
    }

    // get car spec string
    public String getSpecStr(){
        String specs;
        if (engDispNA){
            specs = fuelType+",\n"+drive+",\n"+ transmission+",\n"+vClass+",\nN/A";
        }
        else
            specs = fuelType+",\n"+drive+",\n"+ transmission+",\n"+vClass+",\n"+engDisp;
        return specs;
    }
    public Double getCity() {
        return city;
    }

    public Double getHighway() {
        return highway;
    }

    public String getDrive() {
        return drive;
    }

    public String getTransmission() {
        return transmission;
    }

    public Double getEngDisp() {
        return engDisp;
    }

    public String getvClass() {
        return vClass;
    }

    public String getFuelType() {
        return fuelType;
    }
}
