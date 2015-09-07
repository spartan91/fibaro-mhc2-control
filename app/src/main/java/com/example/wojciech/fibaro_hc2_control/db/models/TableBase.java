package com.example.wojciech.fibaro_hc2_control.db.models;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Wojciech on 2015-02-05.
 */
public class TableBase {
    public static void onCreate(SQLiteDatabase database,String databaseCreate){
        Log.d("TableBase", databaseCreate);
        database.execSQL(databaseCreate);
    }
    public static void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion, String tableName){
        Log.w(tableName, "Upgrading database from version " + oldVersion + " to " + newVersion + ",which will destroy old data");
        database.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(database,tableName);
    }
}
