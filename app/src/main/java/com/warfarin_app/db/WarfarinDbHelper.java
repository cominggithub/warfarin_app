package com.warfarin_app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.warfarin_app.data.ExamData;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        Log.d("app", PatientEntry.SQL_CREATE_ENTRIES);
        db.execSQL(PatientEntry.SQL_CREATE_ENTRIES);
        db.execSQL(ExamEntry.SQL_CREATE_ENTRIES);




    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.d("app", PatientEntry.SQL_DELETE_ENTRIES);
        db.execSQL(PatientEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ExamEntry.SQL_DELETE_ENTRIES);
        onCreate(db);




    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertExamHistorySample()
    {
        for(int i=0; i<10; i++)
        {
            String string_date = "2015/08/10";
            long milliseconds = 0;
            ExamData ed = new ExamData();
            SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");

            try{
                Date d = f.parse(string_date);
                milliseconds = d.getTime();
            }catch (Exception e)
            {

            }

            ed.date = milliseconds;
            ed.pt = 1+i*.1;
            ed.inr = 2+i*.2;
            ed.warfarin = 2+i*.3;

            DbUtil.saveExamData(ed);
        }
    }
}
