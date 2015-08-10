package com.warfarin_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.warfarin_app.Patient;

//import android.database.sqlite.SQLiteCursor;

/**
 * Created by Coming on 8/9/15.
 */
public class DbUtil {

    private static PatientDbHelper patientDbHelper;
    private static Context context;
    private static PatientDbHelper mDbHelper;

    public static void init(Context context)
    {
//        this.context = context;
        mDbHelper = new PatientDbHelper(context);
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

// Define a projection that specifies which columns from the database
// you will actually use after this query.
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

}
