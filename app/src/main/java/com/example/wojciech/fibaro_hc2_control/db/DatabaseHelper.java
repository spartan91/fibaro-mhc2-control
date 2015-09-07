package com.example.wojciech.fibaro_hc2_control.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wojciech.fibaro_hc2_control.db.models.File;


/**
 * Created by Wojciech on 2015-07-17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "safecloud.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        File.onCreate(database, File.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        File.onUpgrade(database, oldVersion, newVersion, File.TABLE_CC_FILE);
    }
}
