package com.warfarin_app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Coming on 8/9/15.
 */
public class WarfarinDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "warfarin.db";

    public WarfarinDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        Log.d("app", "dbhelper create database");
        db.execSQL(PatientEntry.SQL_CREATE_ENTRIES);
        db.execSQL(ExamEntry.SQL_CREATE_ENTRIES);
        db.execSQL(LogEntry.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.d("app", "dbhelper upgrade database");
        Log.d("app", PatientEntry.SQL_DELETE_ENTRIES);
        db.execSQL(PatientEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ExamEntry.SQL_DELETE_ENTRIES);
        db.execSQL(LogEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
