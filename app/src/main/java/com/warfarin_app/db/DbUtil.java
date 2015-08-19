package com.warfarin_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.warfarin_app.Patient;
import com.warfarin_app.data.ExamData;

import java.util.ArrayList;

//import android.database.sqlite.SQLiteCursor;

/**
 * Created by Coming on 8/9/15.
 */
public class DbUtil {

    private static WarfarinDbHelper patientDbHelper;
    private static Context context;
    private static WarfarinDbHelper mDbHelper;

    public static void init(Context context)
    {
//        this.context = context;
        mDbHelper = new WarfarinDbHelper(context);
        if (!checkDb())
        {
            createDb();
        }
    }

    private static boolean checkDb()
    {

        return true;
    }

    private static void createDb()
    {
//        mDbHelper.onCreate();
    }

    public static void savePatient(Patient patient)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Log.d("app", "save patient");
// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PatientEntry.COLUMN_NAME_NAME, patient.getName());
        values.put(PatientEntry.COLUMN_NAME_BIRTHDAY, patient.getBirthday());
        values.put(PatientEntry.COLUMN_NAME_IS_WARFARIN, patient.getIsWarfarin() ? 1:0);
        values.put(PatientEntry.COLUMN_NAME_GENDER, patient.getGender() ? 1:0);
        values.put(PatientEntry.COLUMN_NAME_DOCTOR, patient.getDoctor());

// Insert the new row, returning the primary key value of the new row
        long newRowId = -1;
        if (patient.getId() != 1) {
            Log.d("app", "insert patient");
            newRowId = db.insert(
                    PatientEntry.TABLE_NAME,
                    null,
                    values);

            patient.setId(newRowId);
        }
        else
        {
            Log.d("app", "update patient");
            String selection = PatientEntry._ID + " = " + patient.getId();

            db.update(
                    PatientEntry.TABLE_NAME,
                    values,
                    selection,
                    null);

        }
        Log.d("app", "new id: " + newRowId);
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

        Log.d("app", "cursor count: " + cursor.getCount());

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

        return true;
    }

    public static void saveExamData(ExamData d)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Log.d("app", "save patient");

        ContentValues values = new ContentValues();
        values.put(ExamEntry.COLUMN_NAME_DATE, d.date);
        values.put(ExamEntry.COLUMN_NAME_PT, d.pt);
        values.put(ExamEntry.COLUMN_NAME_INR, d.inr);
        values.put(ExamEntry.COLUMN_NAME_MARFARIN, d.warfarin);

        Log.d("app", "insert exam data");
        d.id = db.insert(ExamEntry.TABLE_NAME, null, values);
    }

    public static boolean loadExamHistory(ArrayList<ExamData> list)
    {

        return loadExamHistoryWithLimit(list, -1);
    }

    public static boolean loadExamHistoryWithLimit(ArrayList<ExamData> list, int limitCount)
    {
        int result;
        boolean gender;
        boolean isMarfarin;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        String[] projection = {
                ExamEntry._ID,
                ExamEntry.COLUMN_NAME_DATE,
                ExamEntry.COLUMN_NAME_PT,
                ExamEntry.COLUMN_NAME_INR,
                ExamEntry.COLUMN_NAME_MARFARIN
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
                    ExamEntry.TABLE_NAME,  // The table to query
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
                    ExamEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,
                    null,                                       // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder,                                 // The sort order,
                    limit
            );

//            cursor =db.query(
//                    ExamEntry.TABLE_NAME,  // The table to query
//                    projection,                               // The columns to return
//                    null,
//                    null,                                       // The values for the WHERE clause
//                    null,                                     // don't group the rows
//                    null,                                     // don't filter by row groups
//                    sortOrder                                 // The sort order,
//            );
        }


        Log.d("app", "cursor count: " + cursor.getCount());

        if (cursor.getCount() < 1)
        {
            return false;
        }


        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++)
        {
            ExamData ed = new ExamData();
            ed.id = cursor.getLong(cursor.getColumnIndexOrThrow(ExamEntry._ID));
            ed.date = cursor.getLong(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_DATE));
            ed.pt = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_PT));
            ed.inr = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_INR));
            ed.warfarin = cursor.getDouble(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_MARFARIN));
            list.add(ed);

            cursor.moveToNext();
        }

        return true;
    }

    public static void updateExam(ExamData d)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Log.d("app", "save patient");

        ContentValues values = new ContentValues();

        values.put(ExamEntry.COLUMN_NAME_DATE, d.date);
        values.put(ExamEntry.COLUMN_NAME_PT, d.pt);
        values.put(ExamEntry.COLUMN_NAME_INR, d.inr);
        values.put(ExamEntry.COLUMN_NAME_MARFARIN, d.warfarin);

        Log.d("app", "update exam data");
//        db.insert(ExamEntry.TABLE_NAME, null, values);
        db.update(ExamEntry.TABLE_NAME, values, ExamEntry._ID + "=" + d.id, null);
    }
    public static void insertExamHistorySample()
    {
        Log.d("app", "insertExamHistorySample");
        for(int i=0; i<10; i++)
        {
            ExamData ed = new ExamData();

            ed.pt = 1+i*.1;
            ed.inr = 2+i*.2;
            ed.warfarin = 3+i*.3;

            DbUtil.saveExamData(ed);
        }
    }

    public static void cleanDb()
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(ExamEntry.TABLE_NAME, null, null);
    }
}
