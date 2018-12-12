package com.example.carbon_footprint_calculation.majorproject_partone.model;


public class Car {//TODO: Car class has to be changed that it takes actual string for data instead of index of VehicleSpec
    private long carID;
    private String disp;
    private String vClass;
    private String transmission;
    private String drive;
    private String nickname;
    private String make;
    private String model;
    private String year;
    private VehicleSpec carSpec;
    private Double KgPerGallon;//kg of CO2 per gallon
    private Double cityKmPerGallon; //km per gallon of gas used in city
    private Double highwayKmPerGallon; //km per gallon of gas used in highway
    private String fuelType;
    private int iconID;


    public Car(boolean b, long ID, String nickName, String make, String model,//for getting from DB
               String year, String fuelType, String drive, String transmission, String vClass, String displ, Double city, Double highway) {
        this.carID = ID;
        this.nickname = nickName;
        this.make = make;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        calculateKGPG(fuelType);
        this.drive = drive;
        this.transmission = transmission;
        this.vClass = vClass;
        this.disp = displ;
        cityKmPerGallon = city; //doesn't need to convert to KPG
        highwayKmPerGallon = highway; //doesn't need to convert to KPG
    }
    public Car(long ID, String nickname, String Make, String Model, String Year, String fuelType, String drive, String Trans, String vClass, String disp, double city, double highway){
        this.carID = ID;
        this.nickname = nickname;
        this.make = Make;
        this.model = Model;
        this.year = Year;
        this.fuelType = fuelType;
        calculateKGPG(fuelType);
        this.drive = drive;
        this.transmission = Trans;
        this.vClass = vClass;
        this.disp = disp;
        cityKmPerGallon = city*1.609; //convert to KPG
        highwayKmPerGallon = highway*1.609; //convert to KPG
    }
    public Car( String nickname, String Make, String Model, String Year, String fuelType, String drive, String Trans, String vClass, String disp, double city,double highway){
        this.carID = -1;
        this.nickname = nickname;
        this.make = Make;
        this.model = Model;
        this.year = Year;
        this.fuelType = fuelType;
        calculateKGPG(fuelType);
        this.drive = drive;
        this.transmission = Trans;
        this.vClass = vClass;
        this.disp = disp;
        cityKmPerGallon = city; //convert to KPG
        highwayKmPerGallon = highway; //convert to KPG
    }
    public Car(String Make, String Model, String Year, String fuelType, String drive, String Trans, String vClass, String disp, double city,double highway){
        this.carID = -1;
        this.make = Make;
        this.model = Model;
        this.year = Year;
        this.fuelType = fuelType;
        calculateKGPG(fuelType);
        this.drive = drive;
        this.transmission = Trans;
        this.vClass = vClass;
        this.disp = disp;
        cityKmPerGallon = city*1.609; //convert to KPG
        highwayKmPerGallon = highway*1.609; //convert to KPG
    }

    //calculate emission per gallon for different fuel types
    public void calculateKGPG(String fuelType){
        if (fuelType.equals("Electricity fuel")){
            KgPerGallon = 0.0;
        }
        else if (fuelType.equals("Diesel fuel")){
            KgPerGallon = 10.16;
        }
        else {
            KgPerGallon = 8.89;
        }
    }

    public void setCar(long ID, String nickname, String Make, String Model, String Year, String fuelType, String drive, String Trans, String vClass, String disp, double city,double highway){
        this.carID = ID;
        this.nickname = nickname;
        this.make = Make;
        this.model = Model;
        this.year = Year;
        this.fuelType = fuelType;
        calculateKGPG(fuelType);
        this.drive = drive;
        this.transmission = Trans;
        this.vClass = vClass;
        this.disp = disp;
        cityKmPerGallon = city*1.609; //convert to KPG
        highwayKmPerGallon = highway*1.609; //convert to KPG
    }


    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getNickname() {
        return nickname;
    }


    public String getSpecStr() {
        String specStr = make+"\n"+model+"\n"+year;
        return specStr;
    }
    public String getMoreSpecStr(){
        String specStr = fuelType+",\n"+drive+",\n"+transmission+",\n"+vClass+",\n"+disp;
        return specStr;
    }

    public Double getKgPerGallon() {
        return KgPerGallon;
    }

    public Double getHighwayKmPerGallon() {
        return highwayKmPerGallon;
    }

    public Double getCityKmPerGallon() {
        return cityKmPerGallon;
    }
    public long getCarID() {
        return carID;
    }

    public String getDisp() {
        return disp;
    }

    public String getvClass() {
        return vClass;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getDrive() {
        return drive;
    }

    public String getYear() {
        return year;
    }

    public VehicleSpec getCarSpec() {
        return carSpec;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getIconID() {
        return iconID;
    }
}
