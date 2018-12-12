package com.example.carbon_footprint_calculation.majorproject_partone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;



public class vehicleDataHelper extends SQLiteOpenHelper {
    private SQLiteDatabase DB;
    public static final String DB_PATH = "data/data/com.example.carbon_footprint_calculation.majorproject_partone/databases/";
    public static final String DB_NAME = "megaDataPack.sqlite";
    private static final String TAG = "vehicleDataHelper";

    private final Context myContext;



    public vehicleDataHelper(Context context){
        super(context,DB_NAME,null, megaDataPackHelper.DATABASE_VERSION);
        this.myContext = context;

        Boolean ifExist = checkForDBFile();

        if (!ifExist){
            DB = this.getReadableDatabase();
            copyDataBase();
            Log.i("VDB","copying VDB");
        }
    }

    //check for database file
    private Boolean checkForDBFile() {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();

    }

    private void copyDataBase() {
        try{
            InputStream myInput = myContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream myOutPut = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer))>0){
                myOutPut.write(buffer,0,length);
            }
            myOutPut.flush();
            myOutPut.close();
            myInput.close();
        }catch (Exception e){

        }
    }

    @Override
    public synchronized void close(){
        if(DB != null){
            DB.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
