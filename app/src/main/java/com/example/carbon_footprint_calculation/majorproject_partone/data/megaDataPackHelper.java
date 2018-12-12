package com.example.carbon_footprint_calculation.majorproject_partone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.carbon_footprint_calculation.majorproject_partone.model.Car;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Journey;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Route;
import com.example.carbon_footprint_calculation.majorproject_partone.model.Utility;

import java.util.ArrayList;
import java.util.List;

public class megaDataPackHelper extends SQLiteOpenHelper{
    public static final String DB_PATH = "data/data/com.example.carbon_footprint_calculation.majorproject_partone/databases/";
    public static final String DB_NAME = "megaDataPack.sqlite";
    private static final String TAG = "megaDataPackHelper";

    // all tables in the DataBase
    public static final String DB_VehicleData = "VehicleData";//predefined table with all vehicle data from the csv file
    public static final String DB_registeredCars = "registeredCars";
    public static final String DB_registeredRoutes = "registeredRoutes";
    public static final String DB_registeredJourneys = "registeredJourneys";
    public static final String DB_utility = "utilityBills";
    public static final String DB_tips = "tips";

    public static final String VD_KEY_ROWID = "_id";
    public static final String VD_MAKE = "make";
    public static final String VD_MODEL = "model";
    public static final String VD_YEAR = "year";
    public static final String VD_CITY = "city08";
    public static final String VD_HIGHWAY = "highway08";
    public static final String VD_DRIVE = "drive";
    public static final String VD_DISPL = "displ";
    public static final String VD_TRANSMISSION = "trany";
    public static final String VD_VEHICLE_CLASS = "vClass";
    public static final String VD_FUEL_TYPE = "fuelType";
    private static final String[] VD_ALL_KEYS = new String[]{VD_KEY_ROWID, VD_MAKE, VD_MODEL, VD_YEAR, VD_CITY, VD_HIGHWAY, VD_DRIVE, VD_DISPL, VD_TRANSMISSION, VD_VEHICLE_CLASS, VD_FUEL_TYPE};
    // DB column number for vehicleData
    public static final int VD_COL_ROWID = 0;
    public static final int VD_COL_MAKE = 1;
    public static final int VD_COL_MODEL = 2;
    public static final int VD_COL_YEAR = 3;
    public static final int VD_COL_CITY = 4;
    public static final int VD_COL_HIGHWAY = 5;
    public static final int VD_COL_DRIVE = 6;
    public static final int VD_COL_DISPL = 7;
    public static final int VD_COL_TRANSMISSION = 8;
    public static final int VD_COL_VEHICLE_CLASS = 9;
    public static final int VD_COL_FUEL_TYPE = 10;

    public static final String RC_KEY_ROWID = "_id";
    public static final String RC_MAKE = "make";
    public static final String RC_MODEL = "model";
    public static final String RC_YEAR = "year";
    public static final String RC_CITY = "cityFuelUsage";
    public static final String RC_HIGHWAY = "highwayFuelUsage";
    public static final String RC_DRIVE = "drive";
    public static final String RC_DISPL = "displ";
    public static final String RC_TRANSMISSION = "transmission";
    public static final String RC_VEHICLE_CLASS = "vehicleClass";
    public static final String RC_FUEL_TYPE = "fuelType";
    public static final String RC_NICKNAME = "nickName";
    public static final String RC_ICON_ID = "carIcon";
    public static final String[] RC_ALL_KEYS = new String[]{RC_KEY_ROWID,RC_MAKE,RC_MODEL,RC_YEAR,
            RC_CITY,RC_HIGHWAY,RC_DRIVE,RC_DISPL,RC_TRANSMISSION,RC_VEHICLE_CLASS,RC_FUEL_TYPE,RC_NICKNAME,RC_ICON_ID};
    // DB column number for registeredCars
    public static final int RC_COL_KEY_ROWID = 0;
    public static final int RC_COL_MAKE = 1;
    public static final int RC_COL_MODEL = 2;
    public static final int RC_COL_YEAR = 3;
    public static final int RC_COL_CITY = 4;
    public static final int RC_COL_HIGHWAY = 5;
    public static final int RC_COL_DRIVE = 6;
    public static final int RC_COL_DISPL = 7;
    public static final int RC_COL_TRANSMISSION = 8;
    public static final int RC_COL_VEHICLE_CLASS = 9;
    public static final int RC_COL_FUEL_TYPE = 10;
    public static final int RC_COL_NICKNAME = 11;
    public static final int RC_COL_ICON = 12;

    private static final String DATABASE_CREATE_TABLE_RC =
            "create table if not exists " + DB_registeredCars
                    + " (" + RC_KEY_ROWID + " integer primary key autoincrement, "
                    + RC_MAKE + " text, "
                    + RC_MODEL + " text, "
                    + RC_YEAR + " integer, "
                    + RC_CITY + " real, "
                    + RC_HIGHWAY + " real, "
                    + RC_DRIVE + " text, "
                    + RC_DISPL + " text, "
                    + RC_TRANSMISSION + " text, "
                    + RC_VEHICLE_CLASS + " text, "
                    + RC_FUEL_TYPE + " text, "
                    + RC_NICKNAME + " text, "
                    + RC_ICON_ID + " integer "
                    + ");";
    public static final String RR_KEY_ROWID = "_id";
    public static final String RR_CITY_DIST = "cityDistance";
    public static final String RR_HIGHWAY_DIST = "highwayDistance";
    public static final String RR_TOTAL_DIST = "totalDistance";
    public static final String RR_ROUTE_NAME = "routeName";

    public static final String[] RR_ALL_KEYS = new String[] {
            RR_KEY_ROWID,
            RR_CITY_DIST,
            RR_HIGHWAY_DIST,
            RR_TOTAL_DIST,
            RR_ROUTE_NAME
    };
    //DB column numbers for registeredRoutes
    public static final int RR_COL_KEY_ROWID = 0;
    public static final int RR_COL_CITY_DIST = 1;
    public static final int RR_COL_HIGHWAY_DIST = 2;
    public static final int RR_COL_TOTAL_DIST = 3;
    public static final int RR_COL_ROUTE_NAME = 4;
    private static final String DATABASE_CREATE_TABLE_RR =
            "create table if not exists " + DB_registeredRoutes
                    + " (" + RR_KEY_ROWID + " integer primary key autoincrement, "
                    + RR_CITY_DIST + " real not null, "
                    + RR_HIGHWAY_DIST + " real not null, "
                    + RR_TOTAL_DIST + " real not null, "
                    + RR_ROUTE_NAME + " text not null "
                    + ");";
    // registeredJourney section (RJ)
    //DB fields for registeredJourneys
    public static final String RJ_KEY_ROWID = "_id";
    public static final String RJ_MODE = "mode";
    public static final String RJ_MAKE = "make";
    public static final String RJ_MODEL = "model";
    public static final String RJ_YEAR = "year";
    public static final String RJ_CITY = "city08";
    public static final String RJ_HIGHWAY = "highway08";
    public static final String RJ_DRIVE = "drive";
    public static final String RJ_DISPL = "displ";
    public static final String RJ_TRANSMISSION = "trany";
    public static final String RJ_VEHICLE_CLASS = "vClass";
    public static final String RJ_FUEL_TYPE = "fuelType";
    public static final String RJ_CAR_NAME = "carNickname";
    public static final String RJ_CITY_DIST = "cityDistance";
    public static final String RJ_HIGHWAY_DIST = "highwayDistance";
    public static final String RJ_TOTAL_DIST = "totalDistance";
    public static final String RJ_ROUTE_NAME = "routeName";
    public static final String RJ_EMISSION = "totalEmission";
    public static final String RJ_DATE = "date";
    public static final String RJ_CAR_ICON = "carIcon";
    public static final String RJ_ROUTE_SAVE = "route_save";

    public static final String[] RJ_ALL_KEYS = new String[]{
            RJ_KEY_ROWID,
            RJ_MODE,
            RJ_MAKE,
            RJ_MODEL,
            RJ_YEAR,
            RJ_CITY,
            RJ_HIGHWAY,
            RJ_DRIVE,
            RJ_DISPL,
            RJ_TRANSMISSION,
            RJ_VEHICLE_CLASS,
            RJ_FUEL_TYPE,
            RJ_CAR_NAME,
            RJ_CITY_DIST,
            RJ_HIGHWAY_DIST,
            RJ_TOTAL_DIST,
            RJ_ROUTE_NAME,
            RJ_EMISSION,
            RJ_DATE,
            RJ_CAR_ICON,
            RJ_ROUTE_SAVE
    };

    //DB column numbers for registeredJourneys
    public static final int RJ_COL_KEY_ROWID = 0;
    public static final int RJ_COL_MODE = 1;
    public static final int RJ_COL_MAKE = 2;
    public static final int RJ_COL_MODEL = 3;
    public static final int RJ_COL_YEAR = 4;
    public static final int RJ_COL_CITY = 5;
    public static final int RJ_COL_HIGHWAY = 6;
    public static final int RJ_COL_DRIVE = 7;
    public static final int RJ_COL_DISPL = 8;
    public static final int RJ_COL_TRANSMISSION = 9;
    public static final int RJ_COL_VEHICLE_CLASS = 10;
    public static final int RJ_COL_FUEL_TYPE = 11;
    public static final int RJ_COL_CAR_NAME = 12;
    public static final int RJ_COL_CITY_DIST = 13;
    public static final int RJ_COL_HIGHWAY_DIST = 14;
    public static final int RJ_COL_TOTAL_DIST = 15;
    public static final int RJ_COL_ROUTE_NAME = 16;
    public static final int RJ_COL_EMISSION = 17;
    public static final int RJ_COL_DATE = 18;
    public static final int RJ_COL_CAR_ICON = 19;
    public static final int RJ_COL_ROUTE_SAVE = 20;

    private static final String DATABASE_CREATE_TABLE_RJ =
            "create table if not exists " + DB_registeredJourneys
                    + " (" + RJ_KEY_ROWID + " integer primary key autoincrement, "
                    + RJ_MODE + " text not null, "
                    + RJ_MAKE + " text, "
                    + RJ_MODEL + " text, "
                    + RJ_YEAR + " integer, "
                    + RJ_CITY + " real, "
                    + RJ_HIGHWAY + " real, "
                    + RJ_DRIVE + " text, "
                    + RJ_DISPL + " real, "
                    + RJ_TRANSMISSION + " text, "
                    + RJ_VEHICLE_CLASS + " text, "
                    + RJ_FUEL_TYPE + " text, "
                    + RJ_CAR_NAME + " text, "
                    + RJ_CITY_DIST + " real not null, "
                    + RJ_HIGHWAY_DIST + " real not null, "
                    + RJ_TOTAL_DIST + " real not null, "
                    + RJ_ROUTE_NAME + " text not null, "
                    + RJ_EMISSION + " real not null, "
                    + RJ_DATE + " text not null, "
                    + RJ_CAR_ICON + " integer, "
                    + RJ_ROUTE_SAVE + " integer "
                    + ");";


    public static final String UB_KEY_ROWID = "_id";
    public static final String UB_BILL_TYPE = "billType";
    public static final String UB_BILL_AMOUNT = "amount";
    public static final String UB_DATE_START = "start_date";
    public static final String UB_DATE_END = "end_date";
    public static final String UB_TOTAL_EMISSION = "totalEmission";
    public static final String UB_NUM_PEOPLE = "numOfPeople";
    public static final String UB_FAIR_SHARE = "fairShareOfEmission";
    public static final String UB_DAYS = "days";

    public static final String[] UB_ALL_KEYS = new String[]{
            UB_KEY_ROWID,
            UB_BILL_TYPE,
            UB_BILL_AMOUNT,
            UB_DATE_START,
            UB_DATE_END,
            UB_TOTAL_EMISSION,
            UB_NUM_PEOPLE,
            UB_FAIR_SHARE,
            UB_DAYS,
    };
    //DB column numbers for utilityBills
    public static final int UB_COL_KEY_ROWID = 0;
    public static final int UB_COL_BILL_TYPE = 1;
    public static final int UB_COL_BILL_AMOUNT = 2;
    public static final int UB_COL_START_DATE = 3;
    public static final int UB_COL_END_DATE = 4;
    public static final int UB_COL_TOTAL_EMISSION = 5;
    public static final int UB_COL_NUMBER_PEOPLE = 6;
    public static final int UB_COL_FAIR_SHARE = 7;
    public static final int UB_COL_DAYS = 8;



    private static final String DATABASE_CREATE_TABLE_UB =
            "create table if not exists " + DB_utility
                    + " (" + UB_KEY_ROWID + " integer primary key autoincrement, "
                    + UB_BILL_TYPE + " text not null, "
                    + UB_BILL_AMOUNT + " real not null, "
                    + UB_DATE_START + " text not null, "
                    + UB_DATE_END + " text not null, "
                    + UB_TOTAL_EMISSION + " real not null, "
                    + UB_NUM_PEOPLE + " integer not null, "
                    + UB_FAIR_SHARE + " real not null, "
                    + UB_DAYS + " numeric not null "
                    + ");";


    public static final String TP_KEY_ROWID = "_id";
    public static final String TP_TIP_INDEX = "tips_index";


    public static final int TP_COL_KEY_ROWID = 0;
    public static final int TP_COL_TIP_INDEX = 1;
    private static final String DATABASE_CREATE_TABLE_TP =
            "create table if not exists " + DB_tips
                    + "(" + TP_KEY_ROWID +" integer primary key autoincrement, "
                    + TP_TIP_INDEX + " integer not null "
                    + ");";
    public static final String[] TP_ALL_KEYS = new String[]{
            TP_KEY_ROWID,
            TP_TIP_INDEX
    };

    //-----------------Route methods--------------------------------------
    public Route RR_getRouteByID(long ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ DB_registeredRoutes +" where " +RR_KEY_ROWID+" = "+ ID,null);

        Route newRoute;
        Double city = 0.0;
        Double highway = 0.0;
        String name = null;
        c.moveToFirst();
        while (!c.isAfterLast()){
            city = c.getDouble(RR_COL_CITY_DIST);
            highway = c.getDouble(RR_COL_HIGHWAY_DIST);
            name = c.getString(RR_COL_ROUTE_NAME);
            c.move(1);
        }
        newRoute = new Route(ID,name,city,highway);
        c.close();
        db.close();
        return newRoute;
    }
    public List<Route> RR_getAllRoutes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = null;
        Cursor c = 	db.query(true, DB_registeredRoutes, RR_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        List<Route> routeCombo = new ArrayList<>();
        while (!c.isAfterLast()){
            long coolID = c.getLong(RR_COL_KEY_ROWID);
            Double city = c.getDouble(RR_COL_CITY_DIST);
            Double highway = c.getDouble(RR_COL_HIGHWAY_DIST);
            String name = c.getString(RR_COL_ROUTE_NAME);
            Route newRoute = new Route(coolID,name,city,highway);
            routeCombo.add(newRoute);
            c.move(1);
        }
        c.close();
        db.close();
        return routeCombo;
    }
    public boolean RR_deleteRoute(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = RR_KEY_ROWID + "=" + rowId;
        return db.delete(DB_registeredRoutes, where, null) != 0;
    }
    public long RR_addNewRoute(String name,Double city,Double highway) {
        SQLiteDatabase db = this.getWritableDatabase();
        Double total = city + highway;
        ContentValues initialValues = new ContentValues();
        initialValues.put(RR_CITY_DIST, city);
        initialValues.put(RR_HIGHWAY_DIST, highway);
        initialValues.put(RR_TOTAL_DIST, total);
        initialValues.put(RR_ROUTE_NAME, name);

        return db.insert(DB_registeredRoutes, null, initialValues);
    }

    public boolean RR_editRoute(long rowId, Double city, Double highway, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Double total = city + highway;
        String where = RR_KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(RR_CITY_DIST, city);
        newValues.put(RR_HIGHWAY_DIST, highway);
        newValues.put(RR_TOTAL_DIST, total);
        newValues.put(RR_ROUTE_NAME, name);
        // Insert it into the database.

        return db.update(DB_registeredRoutes, newValues, where, null) != 0;
    }

//----------------------------------------------------------------------------------------------------

//--------------------------------Vehicle data methods------------------------------------------------
    public String[] VD_getAllMakes(){
        db = this.getReadableDatabase();
        String where;
        Cursor c = 	db.rawQuery("SELECT distinct "+ VD_MAKE + " FROM "+DB_VehicleData+" order by make asc",null);
        if (c != null) {
            c.moveToFirst();
        }

        String[] allMakes = new String[c.getCount()];
        int i = 0;
        while (!c.isAfterLast()){
            String aMake = c.getString(0);
            allMakes[i] = aMake;
            i++;
            c.move(1);
        }
        c.close();
        db.close();
        return allMakes;
    }
    public String[] VD_getAllModels(String make){
        db = this.getReadableDatabase();
        String where = " where make = '"+make+"'";
        Cursor c = 	db.rawQuery("SELECT distinct "+ VD_MODEL + " FROM "+DB_VehicleData + where+" order by make asc",null);
        if (c != null) {
            c.moveToFirst();
        }

        String[] allModels = new String[c.getCount()];
        int i = 0;
        while (!c.isAfterLast()){
            String aModel = c.getString(0);
            allModels[i] = aModel;
            i++;
            c.move(1);
        }
        c.close();
        db.close();
        return allModels;
    }
    public String[] VD_getAllYears(String make, String model){
        db = this.getReadableDatabase();
        String where = " where make = '"+make+"' and model = '"+ model+"'";
        Cursor c = 	db.rawQuery("SELECT distinct "+ VD_YEAR + " FROM "+DB_VehicleData+ where+" order by model asc",null);
        if (c != null) {
            c.moveToFirst();
        }
        String[] allYears = new String[c.getCount()];
        int i = 0;
        while (!c.isAfterLast()){
            String aYear = c.getString(0);
            allYears[i] = aYear;
            i++;
            c.move(1);
        }
        c.close();
        db.close();
        return allYears;
    }
    public List<Car> VD_getAllSpecsByMMY(String make, String model, String year){
        db = this.getReadableDatabase();
        String where = " where make = '"+make+"' and model = '"+ model+"'" + "and year = '"+year+"'";
        Cursor c = 	db.rawQuery("SELECT distinct "
                +VD_FUEL_TYPE+", "
                +VD_DRIVE+", "
                +VD_TRANSMISSION+", "
                +VD_VEHICLE_CLASS +", "
                +VD_DISPL+", "
                +VD_CITY+", "
                +VD_HIGHWAY+ " "
                + "FROM "+DB_VehicleData+ where,null);
        if (c != null) {
            c.moveToFirst();
        }
        List<Car> allCars = new ArrayList<>();
        int i = 0;
        while (!c.isAfterLast()){
            String fuelType = checkNULL(c.getString(0))+" fuel";
            String drive = checkNULL(c.getString(1));
            String trans = checkNULL(c.getString(2));
            String vClass = checkNULL(c.getString(3));
            String disp =checkNULL(c.getString(4));
            Double cityFuelUse = c.getDouble(5);
            Double highwayFuelUse = c.getDouble(6);

            Car newCar = new Car(make, model, year, fuelType, drive, trans, vClass, disp, cityFuelUse, highwayFuelUse);
            allCars.add(newCar);
            i++;
            c.move(1);
        }
        c.close();
        db.close();
        return allCars;
    }
    private String checkNULL(String toCheck){
        if (toCheck.length()==0){
            return "N/A";
        }
        else return toCheck;
    }
//-------------------------------------------------------------------------------------------------
    //------------------------Car methods-------------------------------------------------

    public Car RC_getCarByID(long carID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ DB_registeredCars +" where " +RC_KEY_ROWID+" = "+ carID,null);
        c.moveToFirst();
        String make = null;
        String model = null;
        String drive = null;
        String displ = null;
        Double city = 0.0;
        Double highway = 0.0;
        String year = null;
        String transmission = null;
        String vClass = null;
        String fuelType = null;
        String nickName = null;
        int carIcon = -1;
        while (!c.isAfterLast()){
            make = c.getString(RC_COL_MAKE);
            model = c.getString(RC_COL_MODEL);
            drive = c.getString(RC_COL_DRIVE);
            displ = c.getString(RC_COL_DISPL);
            city = c.getDouble(RC_COL_CITY);
            highway = c.getDouble(RC_COL_HIGHWAY);
            year = c.getString(RC_COL_YEAR);
            transmission = c.getString(RC_COL_TRANSMISSION);
            vClass = c.getString(RC_COL_VEHICLE_CLASS);
            fuelType = c.getString(RC_COL_FUEL_TYPE);
            nickName =c.getString(RC_COL_NICKNAME);
            carIcon = c.getInt(RC_COL_ICON);

            c.move(1);
        }
        c.close();
        Car newCar =  new Car(carID,nickName,make,model,year,fuelType,drive,transmission,vClass,displ,city,highway);
        newCar.setIconID(carIcon);
        return newCar;
    }

    public List<Car> RC_getAllCars() {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = null;
        Cursor c = 	db.query(true, DB_registeredCars, RC_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        List<Car> allCars = new ArrayList<>();
        while (!c.isAfterLast()){
            long carID = c.getLong(RC_COL_KEY_ROWID);
            String make = c.getString(RC_COL_MAKE);
            String model = c.getString(RC_COL_MODEL);
            String drive = c.getString(RC_COL_DRIVE);
            String displ = c.getString(RC_COL_DISPL);
            Double city = c.getDouble(RC_COL_CITY);
            Double highway = c.getDouble(RC_COL_HIGHWAY);
            String year = c.getString(RC_COL_YEAR);
            String transmission = c.getString(RC_COL_TRANSMISSION);
            String vClass = c.getString(RC_COL_VEHICLE_CLASS);
            String fuelType = c.getString(RC_COL_FUEL_TYPE);
            String nickName = c.getString(RC_COL_NICKNAME);
            int carIcon = c.getInt(RC_COL_ICON);

            Car newCar = new Car(carID, nickName, make, model, year,fuelType, drive,  transmission,vClass, displ, city, highway);
            newCar.setIconID(carIcon);
            allCars.add(newCar);
            c.move(1);
        }
        c.close();
        db.close();
        return allCars;
    }

    public long RC_addCar(Car newCar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();

        initialValues.put(RC_MAKE, newCar.getMake());
        initialValues.put(RC_MODEL, newCar.getModel());
        initialValues.put(RC_YEAR, newCar.getYear());
        initialValues.put(RC_FUEL_TYPE,newCar.getFuelType());
        initialValues.put(RC_DRIVE,newCar.getDrive());
        initialValues.put(RC_TRANSMISSION,newCar.getTransmission());
        initialValues.put(RC_VEHICLE_CLASS,newCar.getvClass());
        initialValues.put(RC_DISPL,newCar.getDisp());
        initialValues.put(RC_NICKNAME,newCar.getNickname());
        initialValues.put(RC_CITY,newCar.getCityKmPerGallon());
        initialValues.put(RC_HIGHWAY,newCar.getHighwayKmPerGallon());
        initialValues.put(RC_ICON_ID, newCar.getIconID());

        return db.insert(DB_registeredCars, null, initialValues);
    }


    public boolean RC_editCar(long vehicleID, Car newCar) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = RC_KEY_ROWID + "=" + vehicleID;
        ContentValues editedValues = new ContentValues();

        editedValues.put(RC_MAKE, newCar.getMake());
        editedValues.put(RC_MODEL, newCar.getModel());
        editedValues.put(RC_YEAR, newCar.getYear());
        editedValues.put(RC_FUEL_TYPE,newCar.getFuelType());
        editedValues.put(RC_DRIVE,newCar.getDrive());
        editedValues.put(RC_TRANSMISSION,newCar.getTransmission());
        editedValues.put(RC_VEHICLE_CLASS,newCar.getvClass());
        editedValues.put(RC_DISPL,newCar.getDisp());
        editedValues.put(RC_NICKNAME,newCar.getNickname());
        editedValues.put(RC_CITY,newCar.getCityKmPerGallon());
        editedValues.put(RC_HIGHWAY,newCar.getHighwayKmPerGallon());
        editedValues.put(RC_ICON_ID, newCar.getIconID());

        return db.update(DB_registeredCars,editedValues,where,null)!=0;
    }

    public boolean RC_delCar(long carID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = RC_KEY_ROWID + "=" + carID;

        return db.delete(DB_registeredCars,where,null) !=0;
    }
    //---------------------Journey methods-------------------------------


    public long RJ_addJourney(Car car,Route route,String mode, String date, int save ){//save is 0 for false 1 for true
        SQLiteDatabase db = this.getWritableDatabase();
        Double totalEmission = 0.0;

        ContentValues initialValues = new ContentValues();
        initialValues.put(RJ_MODE, mode);
        if (mode.equals("car")) {
            initialValues.put(RJ_MAKE, car.getMake());
            initialValues.put(RJ_MODEL, car.getModel());
            initialValues.put(RJ_YEAR, car.getYear());
            initialValues.put(RJ_CITY, car.getCityKmPerGallon());
            initialValues.put(RJ_HIGHWAY, car.getHighwayKmPerGallon());
            initialValues.put(RJ_DRIVE, car.getDrive());
            initialValues.put(RJ_DISPL, car.getDisp());
            initialValues.put(RJ_TRANSMISSION, car.getTransmission());
            initialValues.put(RJ_VEHICLE_CLASS, car.getvClass());
            initialValues.put(RJ_FUEL_TYPE, car.getFuelType());
            initialValues.put(RJ_CAR_NAME, car.getNickname());
            initialValues.put(RJ_CAR_ICON, car.getIconID());

            Double totalFuelUsage = route.getCityDistance()/car.getCityKmPerGallon()+route.getHighwayDistance()/car.getHighwayKmPerGallon();
            totalEmission = totalFuelUsage*car.getKgPerGallon();
        }
        else if (mode.equals("walk")||mode.equals("bike")){
            totalEmission = 0.0;// walk or bike has no carbon footprint? Or find data for this and fix it
        }
        else if (mode.equals("bus")){
            totalEmission = route.getTotalDistance()*0.086;  //emission for bus
        }
        else if (mode.equals("skytrain")){
            totalEmission = route.getTotalDistance()*0.003; //emission for skytrain
        }

        initialValues.put(RJ_ROUTE_SAVE,save);
        initialValues.put(RJ_CITY_DIST, route.getCityDistance());
        initialValues.put(RJ_HIGHWAY_DIST, route.getHighwayDistance());
        initialValues.put(RJ_TOTAL_DIST, route.getTotalDistance());
        initialValues.put(RJ_ROUTE_NAME, route.getName());
        initialValues.put(RJ_EMISSION, totalEmission);
        initialValues.put(RJ_DATE, date);

        return db.insert(DB_registeredJourneys,null,initialValues);
    }
    public boolean RJ_editJourney(String mode, long journeyID, Car car,Route route, String date ){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = RJ_KEY_ROWID + "=" + journeyID;

        ContentValues editedValues = new ContentValues();
        Double totalEmission = 0.0;
        if (mode.equals("car")) {

            editedValues.put(RJ_CAR_NAME, car.getNickname());
            editedValues.put(RJ_CITY, car.getCityKmPerGallon());
            editedValues.put(RJ_HIGHWAY,car.getHighwayKmPerGallon());
            editedValues.put(RJ_DRIVE, car.getDrive());
            editedValues.put(RJ_DISPL, car.getDisp());
            editedValues.put(RJ_MAKE, car.getMake());
            editedValues.put(RJ_MODEL, car.getModel());
            editedValues.put(RJ_YEAR, car.getYear());
            editedValues.put(RJ_VEHICLE_CLASS, car.getvClass());
            editedValues.put(RJ_TRANSMISSION, car.getTransmission());
            editedValues.put(RJ_FUEL_TYPE, car.getFuelType());
            Double totalFuelUsage = route.getCityDistance()/car.getCityKmPerGallon()+route.getHighwayDistance()/car.getHighwayKmPerGallon();
            totalEmission = totalFuelUsage*car.getKgPerGallon();
            editedValues.put(RJ_CAR_ICON,car.getIconID());
        }
        else if (mode.equals("walk/bike")){
            totalEmission = 0.0;// walk or bike has no carbon footprint? Or find data for this and fix it
        }
        else if (mode.equals("bus")){
            totalEmission = route.getTotalDistance()*0.086;  //emission for bus
        }
        else if (mode.equals("skytrain")){
            totalEmission = route.getTotalDistance()*0.003; //emission for skytrain

        }
        editedValues.put(RJ_EMISSION, totalEmission);
        editedValues.put(RJ_DATE, date);
        editedValues.put(RJ_MODE,mode);
        editedValues.put(RJ_TOTAL_DIST, route.getTotalDistance());
        editedValues.put(RJ_ROUTE_NAME, route.getName());
        editedValues.put(RJ_CITY_DIST, route.getCityDistance());
        editedValues.put(RJ_HIGHWAY_DIST, route.getHighwayDistance());

        Log.i("megaData",route.getHighwayDistance()+" - highway");
        Log.i("megaData",route.getTotalDistance()+" - total");

        return db.update(DB_registeredJourneys,editedValues,where,null)!= 0;
    }

    public boolean RJ_deleteJourney(long journeyID){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = RJ_KEY_ROWID + "=" + journeyID;

        return db.delete(DB_registeredJourneys,where,null) !=0;
    }

    public List<Journey> RJ_getAllJourney(){
        SQLiteDatabase db = this.getReadableDatabase();
        String where = null;
        Car car;
        Cursor c = db.query(true, DB_registeredJourneys, RJ_ALL_KEYS, where, null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();
        }
        List<Journey> journeyCombo = new ArrayList<>();
        while (!c.isAfterLast()){
            long journeyID = c.getLong(RJ_COL_KEY_ROWID);
            String mode = c.getString(RJ_COL_MODE);
            if(mode.equals("car")) {
                String make = c.getString(RJ_COL_MAKE);
                String model = c.getString(RJ_COL_MODEL);
                String drive = c.getString(RJ_COL_DRIVE);
                String displ = c.getString(RJ_COL_DISPL);
                Double city = c.getDouble(RJ_COL_CITY);
                Double highway = c.getDouble(RJ_COL_HIGHWAY);
                String year = c.getString(RJ_COL_YEAR);
                String transmission = c.getString(RJ_COL_TRANSMISSION);
                String vClass = c.getString(RJ_COL_VEHICLE_CLASS);
                String fuelType = c.getString(RJ_COL_FUEL_TYPE);
                String nickName = c.getString(RJ_COL_CAR_NAME);
                car = new Car(false, journeyID, nickName, make, model, year, fuelType, drive, transmission, vClass, displ, city, highway);
                int carICon = c.getInt(RJ_COL_CAR_ICON);
                car.setIconID(carICon);
            }
            else {
                car = null;
            }
            Double cityDist = c.getDouble(RJ_COL_CITY_DIST);
            Double highwayDist = c.getDouble(RJ_COL_HIGHWAY_DIST);
            String routeName = c.getString(RJ_COL_ROUTE_NAME);
            String date = c.getString(RJ_COL_DATE);
            int route_save = c.getInt(RJ_COL_ROUTE_SAVE);
            Route route = new Route(routeName, cityDist, highwayDist);


            Journey newJourney = new Journey(journeyID,mode, car, route, date,route_save);
            journeyCombo.add(newJourney);
            c.move(1);
        }
        c.close();
        db.close();
        return journeyCombo;
    }

    public Journey RJ_getJourneyByID(long journeyID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ DB_registeredJourneys +" where " +RJ_KEY_ROWID+" = "+ journeyID,null);
        c.moveToFirst();

        Journey journey = null;

        while (!c.isAfterLast()){
            String mode = c.getString(RJ_COL_MODE);
            Car car = null;
            if(mode.equals("car")) {
                String make = c.getString(RJ_COL_MAKE);
                String model = c.getString(RJ_COL_MODEL);
                String drive = c.getString(RJ_COL_DRIVE);
                String displ = c.getString(RJ_COL_DISPL);
                Double city = c.getDouble(RJ_COL_CITY);
                Double highway = c.getDouble(RJ_COL_HIGHWAY);
                String year = c.getString(RJ_COL_YEAR);
                String transmission = c.getString(RJ_COL_TRANSMISSION);
                String vClass = c.getString(RJ_COL_VEHICLE_CLASS);
                String fuelType = c.getString(RJ_COL_FUEL_TYPE);
                String nickName = c.getString(RJ_COL_CAR_NAME);
                car = new Car(nickName, make, model, year, fuelType, drive, transmission, vClass, displ, city, highway);
                int carICon = c.getInt(RJ_COL_CAR_ICON);
                car.setIconID(carICon);
            }
            Double cityDist = c.getDouble(RJ_COL_CITY_DIST);
            Double highwayDist = c.getDouble(RJ_COL_HIGHWAY_DIST);
            String routeName = c.getString(RJ_COL_ROUTE_NAME);
            String date = c.getString(RJ_COL_DATE);
            int route_save = c.getInt(RJ_COL_ROUTE_SAVE);
            Route route = new Route(routeName, cityDist, highwayDist);

            journey = new Journey(journeyID,mode,car, route, date,route_save);
            c.move(1);
        }
        c.close();
        return journey;
    }

    public List<Journey> RJ_getJourneyByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ DB_registeredJourneys +" where " +RJ_DATE+" = "+ "'"+date+"'",null);
        c.moveToFirst();
        Log.i("DB", "date is "+date);

        List<Journey> journeysOnThatDate = new ArrayList<>();
        while (!c.isAfterLast()){
            String mode = c.getString(RJ_COL_MODE);
            Log.i("DB", "mode?? "+mode);
            Car car = null;
            long journeyID = c.getLong(RJ_COL_KEY_ROWID);
            if(mode.equals("car")) {
                String make = c.getString(RJ_COL_MAKE);
                String model = c.getString(RJ_COL_MODEL);
                String drive = c.getString(RJ_COL_DRIVE);
                String displ = c.getString(RJ_COL_DISPL);
                Double city = c.getDouble(RJ_COL_CITY);
                Double highway = c.getDouble(RJ_COL_HIGHWAY);
                String year = c.getString(RJ_COL_YEAR);
                String transmission = c.getString(RJ_COL_TRANSMISSION);
                String vClass = c.getString(RJ_COL_VEHICLE_CLASS);
                String fuelType = c.getString(RJ_COL_FUEL_TYPE);
                String nickName = c.getString(RJ_COL_CAR_NAME);
                car = new Car(nickName, make, model, year, fuelType, drive, transmission, vClass, displ, city, highway);
                int carICon = c.getInt(RJ_COL_CAR_ICON);
                car.setIconID(carICon);
            }
            Double cityDist = c.getDouble(RJ_COL_CITY_DIST);
            Double highwayDist = c.getDouble(RJ_COL_HIGHWAY_DIST);
            String routeName = c.getString(RJ_COL_ROUTE_NAME);
            int route_save = c.getInt(RJ_COL_ROUTE_SAVE);
            Route route = new Route(routeName, cityDist, highwayDist);

            Journey journey = new Journey(journeyID,mode,car, route, date,route_save);
            journeysOnThatDate.add(journey);
            c.move(1);
        }
        c.close();
        return journeysOnThatDate;

    }

    //--------------------------------------------------------------------------------

    //---------------------------Utility methods--------------------------------------
    public boolean UB_isEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ DB_utility,null);
        boolean isEmpty = c.getCount()==0;
        c.close();
        db.close();
        return isEmpty;
    }

    public Utility UB_getUtilityByID(long utilityID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ DB_utility +" where " +UB_KEY_ROWID+" = "+ utilityID,null);
        c.moveToFirst();
        String type = null;
        Double amount = 0.0;
        String startDate = null;
        String endDate = null;
        long days = 0;
        int people = 0;
        c.moveToFirst();
        while (!c.isAfterLast()){
            type = c.getString(UB_COL_BILL_TYPE);
            amount = c.getDouble(UB_COL_BILL_AMOUNT);
            startDate = c.getString(UB_COL_START_DATE);
            endDate = c.getString(UB_COL_END_DATE);
            people = c.getInt(UB_COL_NUMBER_PEOPLE);
            days = c.getLong(UB_COL_DAYS);
            c.move(1);
        }
        c.close();
        return new Utility(utilityID, type, startDate,endDate, amount, people,days);
    }

    public Utility UB_getLatestUtility() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = 	db.query(true, DB_utility, UB_ALL_KEYS,
                null, null, null, null, null, null);
        c.moveToLast();
        long id = c.getLong(UB_COL_KEY_ROWID);
        String type = c.getString(UB_COL_BILL_TYPE);
        Double billAmount = c.getDouble(UB_COL_BILL_AMOUNT);
        String start = c.getString(UB_COL_START_DATE);
        String end = c.getString(UB_COL_END_DATE);
        int people = c.getInt(UB_COL_NUMBER_PEOPLE);
        long days = c.getLong(UB_COL_DAYS);
        return new Utility(id, type, start,end,billAmount, people,days);

    }


    public List<Utility> UB_getAllUtilities() {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = null;
        Cursor c = 	db.query(true, DB_utility, UB_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        List<Utility> utilityCombo = new ArrayList<>();
        while (!c.isAfterLast()){
            long utilityID = c.getLong(UB_COL_KEY_ROWID);
            String type = c.getString(UB_COL_BILL_TYPE);
            Double billAmount = c.getDouble(UB_COL_BILL_AMOUNT);
            String startDate = c.getString(UB_COL_START_DATE);
            String endDate = c.getString(UB_COL_END_DATE);
            int people = c.getInt(UB_COL_NUMBER_PEOPLE);
            long days = c.getLong(UB_COL_DAYS);
            Utility newUtility = new Utility(utilityID, type,startDate,endDate, billAmount,people,days);
            utilityCombo.add(newUtility);
            c.move(1);
        }
        c.close();
        db.close();
        return utilityCombo;
    }

    //add utility to database
    public long UB_addNewUtility(Utility newUtility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(UB_BILL_TYPE, newUtility.getBillType());
        initialValues.put(UB_BILL_AMOUNT, newUtility.getBillAmount());
        initialValues.put(UB_DATE_START, newUtility.getBillStartDate());
        initialValues.put(UB_DATE_END,newUtility.getBillEndDate());
        initialValues.put(UB_NUM_PEOPLE, newUtility.getNumPeople());
        initialValues.put(UB_TOTAL_EMISSION,newUtility.getTotalEmission("Kg"));
        initialValues.put(UB_FAIR_SHARE,newUtility.getEmissionPerPerson("Kg"));
        initialValues.put(UB_DAYS,newUtility.getDays());
        return db.insert(DB_utility, null, initialValues);
    }

    public boolean UB_editUtility(long utilityID, Utility newUtility) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = UB_KEY_ROWID + "=" + utilityID;
        ContentValues editedValues = new ContentValues();
        editedValues.put(UB_BILL_TYPE, newUtility.getBillType());
        editedValues.put(UB_BILL_AMOUNT, newUtility.getBillAmount());
        editedValues.put(UB_DATE_START, newUtility.getBillStartDate());
        editedValues.put(UB_DATE_END,newUtility.getBillEndDate());
        editedValues.put(UB_NUM_PEOPLE, newUtility.getNumPeople());
        editedValues.put(UB_TOTAL_EMISSION,newUtility.getTotalEmission("Kg"));
        editedValues.put(UB_FAIR_SHARE,newUtility.getEmissionPerPerson("Kg"));
        editedValues.put(UB_DAYS,newUtility.getDays());
        return db.update(DB_utility,editedValues,where,null)!=0;
    }

    public boolean UB_deleteUtility(long utilityID){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = UB_KEY_ROWID + "=" + utilityID;

        return db.delete(DB_utility,where,null) !=0;
    }
    public Utility UB_getLatestEndDateBill(){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = " where "+UB_DATE_END+" = (select max("+ UB_DATE_END+") from "+DB_utility+")";
        Cursor c = db.rawQuery("select * from "+DB_utility+where,null);
        if (c != null) {
            c.moveToFirst();
        }
        long utilityID = 0;
        String type = null;
        Double billAmount = 0.0;
        String startDate = null;
        String endDate = null;
        long days = 0;
        int people = 0;
        while (!c.isAfterLast()){
            utilityID = c.getLong(UB_COL_KEY_ROWID);
            type = c.getString(UB_COL_BILL_TYPE);
            billAmount = c.getDouble(UB_COL_BILL_AMOUNT);
            startDate = c.getString(UB_COL_START_DATE);
            endDate = c.getString(UB_COL_END_DATE);
            people = c.getInt(UB_COL_NUMBER_PEOPLE);
            days = c.getLong(UB_COL_DAYS);
            c.move(1);
        }
        Utility newUtility = new Utility(utilityID, type,startDate,endDate, billAmount,people,days);
        c.close();
        db.close();
        return newUtility;
    }

    public List<Utility> UB_getAllUtilitiesSortedByEndDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + DB_utility + " order by end_date desc", null);
        c.moveToFirst();
        List<Utility> utilities = new ArrayList<>();
        while (!c.isAfterLast()){
            long utilityID = c.getLong(UB_COL_KEY_ROWID);
            String type = c.getString(UB_COL_BILL_TYPE);
            Double billAmount = c.getDouble(UB_COL_BILL_AMOUNT);
            String startDate = c.getString(UB_COL_START_DATE);
            String endDate = c.getString(UB_COL_END_DATE);
            int people = c.getInt(UB_COL_NUMBER_PEOPLE);
            long days = c.getLong(UB_COL_DAYS);
            Utility newUtility = new Utility(utilityID, type,startDate,endDate, billAmount,people,days);
            utilities.add(newUtility);
            c.move(1);
        }
        c.close();
        db.close();
        return utilities;
    }


    //-------------------------------------------------------------------------------------
    //-----------------------tips methods----------------------------------------------
    public boolean TP_checkEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ DB_tips,null);
        boolean isEmpty = c.getCount()==0;
        c.close();
        db.close();
        return isEmpty;
    }
    public ArrayList<Integer> TP_getAllIndex(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ DB_tips,null);
        ArrayList<Integer> intArray = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()){
            int index = c.getInt(TP_COL_TIP_INDEX);
            intArray.add(index);
            c.move(1);
        }
        c.close();
        db.close();
        return intArray;
    }

    public long TP_addNewTip(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(TP_TIP_INDEX, index);
        return db.insert(DB_tips, null, initialValues);
    }

    public boolean TP_deleteEarliestTip(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = TP_KEY_ROWID + "=" + (id - 7);
        return db.delete(DB_tips, where, null) != 0;
    }

    //--------------------------------------------------------------------------------


    public static final int DATABASE_VERSION = 12;//if you change the table design, increment this number by 1 plz
    private SQLiteDatabase db;


    public megaDataPackHelper(Context context){
        super(context,DB_NAME,null,DATABASE_VERSION);

        this.getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE_RC);
        db.execSQL(DATABASE_CREATE_TABLE_RR);
        db.execSQL(DATABASE_CREATE_TABLE_RJ);
        db.execSQL(DATABASE_CREATE_TABLE_UB);
        db.execSQL(DATABASE_CREATE_TABLE_TP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading application's database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data!");

        db.execSQL("DROP TABLE IF EXISTS "+ DB_registeredCars);
        db.execSQL("DROP TABLE IF EXISTS "+ DB_registeredJourneys);
        db.execSQL("DROP TABLE IF EXISTS "+ DB_registeredRoutes);
        db.execSQL("DROP TABLE IF EXISTS "+ DB_utility);
        db.execSQL("DROP TABLE IF EXISTS "+ DB_tips);
        onCreate(db);
    }



}