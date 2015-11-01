package com.warfarin_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.warfarin_app.data.ExamData;
import com.warfarin_app.data.LogData;
import com.warfarin_app.data.Patient;
import com.warfarin_app.util.DateUtil;

import java.util.ArrayList;

//import android.database.sqlite.SQLiteCursor;

/**
 * Created by Coming on 8/9/15.
 */
public class DbUtil {

    private static Context context;
    private static WarfarinDbHelper mDbHelper;

    public static void init(Context c)
    {
        context = c;
        mDbHelper = new WarfarinDbHelper(context);
//        if (!checkDataBase())
//        {
//            Log.d("app", "create database");
//            createDb();
//        }
    }

    public static boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {

            checkDB = SQLiteDatabase.openDatabase(WarfarinDbHelper.DATABASE_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (Exception e) {
            // database doesn't exist yet.
        }

        return checkDB != null;
    }

    public static void deleteDb()
    {
        context.deleteDatabase("warfarin.db");
    }

    private static void createDb()
    {
//        mDbHelper.onCreate();
    }

    public static void savePatient(Patient patient)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PatientEntry.COLUMN_NAME_NAME, patient.getName());
        values.put(PatientEntry.COLUMN_NAME_BIRTHDAY, patient.getBirthday());
        values.put(PatientEntry.COLUMN_NAME_IS_WARFARIN, patient.getIsWarfarin() ? 1 : 0);
        values.put(PatientEntry.COLUMN_NAME_GENDER, patient.getGender() ? 1 : 0);
        values.put(PatientEntry.COLUMN_NAME_DOCTOR, patient.getDoctor());
        values.put(PatientEntry.COLUMN_NAME_BLUEDEV_NAME, patient.getBlueDevName());
        values.put(PatientEntry.COLUMN_NAME_BLUEDEV_ADDRESS, patient.getBlueDevAddress());

// Insert the new row, returning the primary key value of the new row
        long newRowId = -1;
        if (patient.getId() != 1) {

            newRowId = db.insert(
                    PatientEntry.TABLE_NAME,
                    null,
                    values);

            patient.setId(newRowId);
        }
        else
        {

            String selection = PatientEntry._ID + " = " + patient.getId();

            db.update(
                    PatientEntry.TABLE_NAME,
                    values,
                    selection,
                    null);

        }

    }

    public static boolean loadPatient(Patient patient)
    {
        int result;
        boolean gender;
        boolean isMarfarin;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                PatientEntry._ID,
                PatientEntry.COLUMN_NAME_NAME,
                PatientEntry.COLUMN_NAME_GENDER,
                PatientEntry.COLUMN_NAME_BIRTHDAY,
                PatientEntry.COLUMN_NAME_DOCTOR,
                PatientEntry.COLUMN_NAME_IS_WARFARIN,
                PatientEntry.COLUMN_NAME_BLUEDEV_NAME,
                PatientEntry.COLUMN_NAME_BLUEDEV_ADDRESS

        };

        String[] where = {
                PatientEntry._ID,

        };


        String sortOrder =
                PatientEntry._ID + " DESC";

        Cursor cursor = db.query(
                PatientEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                PatientEntry._ID + " = 1",                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        if (cursor.getCount() < 1)
        {
            return false;
        }


        cursor.moveToFirst();

        patient.setId(cursor.getLong(
                cursor.getColumnIndexOrThrow(PatientEntry._ID)
        ));


        patient.setName(cursor.getString(cursor.getColumnIndexOrThrow(PatientEntry.COLUMN_NAME_NAME)));
        patient.setGender(cursor.getInt(cursor.getColumnIndexOrThrow(PatientEntry.COLUMN_NAME_GENDER)) == 1);
        patient.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(PatientEntry.COLUMN_NAME_BIRTHDAY)));
        patient.setDoctor(cursor.getString(cursor.getColumnIndexOrThrow(PatientEntry.COLUMN_NAME_DOCTOR)));
        patient.setIsWarfarin(cursor.getInt(cursor.getColumnIndexOrThrow(PatientEntry.COLUMN_NAME_IS_WARFARIN)) == 1);
        patient.setBlueDevName(cursor.getString(cursor.getColumnIndexOrThrow(PatientEntry.COLUMN_NAME_BLUEDEV_NAME)));
        patient.setBlueDevAddress(cursor.getString(cursor.getColumnIndexOrThrow(PatientEntry.COLUMN_NAME_BLUEDEV_ADDRESS)));

        return true;
    }

    public static void saveExamData(ExamData d)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ExamEntry.COLUMN_NAME_DATE, d.getDbDateTimeStr());
        values.put(ExamEntry.COLUMN_NAME_WEEK, DateUtil.getWeek(d.date));
        values.put(ExamEntry.COLUMN_NAME_PT, d.pt);
        values.put(ExamEntry.COLUMN_NAME_INR, d.inr);
        values.put(ExamEntry.COLUMN_NAME_MARFARIN, d.warfarin);
        d.id = db.insert(ExamEntry.TABLE_NAME, null, values);
    }

    public static void addLog(LogData l)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LogEntry.COLUMN_NAME_DATE, l.date);
        values.put(LogEntry.COLUMN_NAME_MSG, l.msg);

        db.insert(LogEntry.TABLE_NAME, null, values);
    }

    public static boolean loadLogHistory(ArrayList<LogData> list, int limitCount)
    {
        int result;
        boolean gender;
        boolean isMarfarin;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        String[] projection = {
                LogEntry._ID,
                LogEntry.COLUMN_NAME_DATE,
                LogEntry.COLUMN_NAME_MSG,
        };

        String[] where = {
                ExamEntry._ID,
        };


        String sortOrder =
                ExamEntry._ID + " DESC";
        String limit = "" + limitCount;


        // no limit
        if (limitCount == -1) {
            cursor =db.query(
                    LogEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,
                    null,                                       // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order,
            );
        }
        else
        {
            cursor =db.query(
                    LogEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,
                    null,                                       // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder,                                 // The sort order,
                    limit
            );
        }

        if (cursor.getCount() < 1)
        {
            return false;
        }


        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++)
        {
            LogData data = new LogData("");
            data.id = cursor.getLong(cursor.getColumnIndexOrThrow(LogEntry._ID));
            data.date = cursor.getLong(cursor.getColumnIndexOrThrow(LogEntry.COLUMN_NAME_DATE));
            data.msg = cursor.getString(cursor.getColumnIndexOrThrow(LogEntry.COLUMN_NAME_MSG));

            list.add(data);

            cursor.moveToNext();
        }

        return true;


    }

    public static boolean loadExamHistory(ArrayList<ExamData> list)
    {

        return loadExamHistoryWithLimit(list, -1, null);
    }

    public static boolean loadExamHistoryByWeekWithLimit(ArrayList<ExamData> list, int limitCount)
    {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        String[] projection = {
                "strftime('%Y', date) year ",
                ExamEntry.COLUMN_NAME_WEEK,
                "avg(" + ExamEntry.COLUMN_NAME_PT +") " + ExamEntry.COLUMN_NAME_PT ,
                "avg(" + ExamEntry.COLUMN_NAME_INR + ") " + ExamEntry.COLUMN_NAME_INR,
                "avg(" + ExamEntry.COLUMN_NAME_MARFARIN + ") " + ExamEntry.COLUMN_NAME_MARFARIN
        };


        String sortOrder = "strftime('%Y', date) DESC, " + ExamEntry.COLUMN_NAME_WEEK + " DESC";
        String orderBy = "strftime('%Y', date), " + ExamEntry.COLUMN_NAME_WEEK;
        String limit = "" + limitCount;


        // no limit
        if (limitCount == -1) {
            cursor =db.query(
                    ExamEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,
                    null,                                       // The values for the WHERE clause
                    orderBy,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order,
            );
        }
        else
        {
            cursor =db.query(
                    ExamEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,
                    null,                                       // The values for the WHERE clause
                    orderBy,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder,                                 // The sort order,
                    limit
            );
        }

        if (cursor.getCount() < 1)
        {
            return false;
        }


        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++)
        {
            ExamData ed = new ExamData();
            ed.pt = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_PT));
            ed.inr = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_INR));
            ed.warfarin = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_MARFARIN));
            ed.date = DateUtil.getTimeByWeek(
                    cursor.getString(cursor.getColumnIndexOrThrow("year")),
                    cursor.getInt(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_WEEK))
            );

            list.add(ed);

            cursor.moveToNext();
        }

        return true;
    }

    public static boolean loadExamHistoryByMonthWithLimit(ArrayList<ExamData> list, int limitCount)
    {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        String[] projection = {
                "strftime('%Y', " + ExamEntry.COLUMN_NAME_DATE + ") year",
                "strftime('%m', " + ExamEntry.COLUMN_NAME_DATE + ") month",
                "avg(" + ExamEntry.COLUMN_NAME_PT +") " + ExamEntry.COLUMN_NAME_PT ,
                "avg(" + ExamEntry.COLUMN_NAME_INR + ") " + ExamEntry.COLUMN_NAME_INR,
                "avg(" + ExamEntry.COLUMN_NAME_MARFARIN + ") " + ExamEntry.COLUMN_NAME_MARFARIN
        };


        String sortOrder =
                "strftime('%Y/%m', " + ExamEntry.COLUMN_NAME_DATE + ") DESC";
        String orderBy = "strftime('%Y/%m', " + ExamEntry.COLUMN_NAME_DATE + ")";
        String limit = "" + limitCount;


        // no limit
        if (limitCount == -1) {
            cursor =db.query(
                    ExamEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,
                    null,                                       // The values for the WHERE clause
                    orderBy,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order,
            );
        }
        else
        {
            cursor =db.query(
                    ExamEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,
                    null,                                       // The values for the WHERE clause
                    orderBy,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder,                                 // The sort order,
                    limit
            );
        }

        if (cursor.getCount() < 1)
        {
            return false;
        }


        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++)
        {
            ExamData ed = new ExamData();
            ed.pt = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_PT));
            ed.inr = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_INR));
            ed.warfarin = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_MARFARIN));

            ed.date = DateUtil.getTimeByYearMonth(
                    cursor.getString(cursor.getColumnIndexOrThrow("year")),
                    cursor.getString(cursor.getColumnIndexOrThrow("month")));

//            Log.d("app", "month: " + cursor.getString(cursor.getColumnIndexOrThrow("month")));
//            Log.d("app", ed.toString());
            list.add(ed);

            cursor.moveToNext();
        }

        return true;
    }

    public static boolean loadExamHistoryWithLimit(ArrayList<ExamData> list, int limitCount, String startDate)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        String[] projection = {
                ExamEntry._ID,
                ExamEntry.COLUMN_NAME_DATE,
                ExamEntry.COLUMN_NAME_WEEK,
                ExamEntry.COLUMN_NAME_PT,
                ExamEntry.COLUMN_NAME_INR,
                ExamEntry.COLUMN_NAME_MARFARIN
        };

        String sortOrder =
                ExamEntry._ID + " DESC";
        String limit = "" + limitCount;

        String whereClause = null;

        if (startDate != null)
        {
            Log.d("app", startDate.toString());
//            whereClause = "date("+ExamEntry.COLUMN_NAME_DATE+") " + " >= '2015/10/1'";
//            whereClause = "date("+ExamEntry.COLUMN_NAME_DATE+") " + " >= '2015/10/01'";
//            whereClause = ExamEntry.COLUMN_NAME_DATE + " >= date('2015/10/01 00:00:00')";
//            whereClause = "date("+ExamEntry.COLUMN_NAME_DATE+") " + " >= date('2012/10/01 00:00:00')";
//            whereClause = "date(date) >= date('2014/10/01 00:00:00')";
//            whereClause = "date >= '2014/10/01 00:00:00'"; // get
//            whereClause = "date >= date('2014/10/01 00:00:00')";
//            whereClause = "date(date) >= date('2015-10-01')";
            whereClause = "strftime('%Y/%m/%d', date) >= '" + startDate + "'";
        }


        // no limit
        if (limitCount == -1) {
            cursor = db.query(
                    ExamEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereClause,
                    null,                                       // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order,
            );

        }
        else
        {
            cursor =db.query(
                    ExamEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereClause,
                    null,                                       // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder,                                 // The sort order,
                    limit
            );
        }

        if (cursor.getCount() < 1)
        {
            return false;
        }


        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++)
        {
            ExamData ed = new ExamData();
            ed.id = cursor.getLong(cursor.getColumnIndexOrThrow(ExamEntry._ID));
            ed.pt = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_PT));
            ed.inr = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_INR));
            ed.warfarin = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_MARFARIN));
            ed.date = DateUtil.getTimeByString(cursor.getString(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_DATE)));
            list.add(ed);

            cursor.moveToNext();
        }

        return true;
    }

    public static void updateExam(ExamData d)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Log.d("app", "update Exam Data: " + d.toString());

        ContentValues values = new ContentValues();

//        values.put(ExamEntry.COLUMN_NAME_DATE, d.date);
//        values.put(ExamEntry.COLUMN_NAME_PT, d.pt);
//        values.put(ExamEntry.COLUMN_NAME_INR, d.inr);
        values.put(ExamEntry.COLUMN_NAME_MARFARIN, d.warfarin);

        db.update(ExamEntry.TABLE_NAME, values, ExamEntry._ID + "=" + d.id, null);
    }


    public static void insertExamHistorySample()
    {
        int year;
        int month;
        int day;
        int hour = 1;
        int min = 2;
        int sec = 3;
        int week;


        for(year=2014; year<2016; year++) {
            for (month = 1; month < 13; month++) {
                for (day = 1; day < 29; day++) {

                    ExamData ed = new ExamData();

                    ed.date = DateUtil.getTimeBy(year, month, day, hour, min, sec);
                    week = DateUtil.getWeek(ed.date);

//                    ed.pt = month;
//                    ed.inr = month+1;
//                    ed.warfarin = month+2;

                    ed.pt = week;
                    ed.inr = week+1;
                    ed.warfarin = week+2;

                    DbUtil.saveExamData(ed);
                }
            }
        }

        ArrayList<ExamData> list = new ArrayList<>();
//        loadExamHistoryByWeekWithLimit(list, 60);
//        loadExamHistoryByMonthWithLimit(list, 30);
    }

    public static void cleanDb()
    {
        cleanLogData();
        cleanExamData();
    }

    public static void cleanExamData()
    {
        Log.d("app", "clean cleanExamData");
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(ExamEntry.TABLE_NAME, null, null);
    }

    public static void cleanLogData()
    {
        Log.d("app", "clean cleanLogData");
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(LogEntry.TABLE_NAME, null, null);
    }
}
