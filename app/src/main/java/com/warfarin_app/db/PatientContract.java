package com.warfarin_app.db;


import android.provider.BaseColumns;

/**
 * Created by Coming on 8/9/15.
 */
public class PatientContract {
    public PatientContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PatientEntry implements BaseColumns {
        public static final String TABLE_NAME = "Patient";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_BIRTHDAY = "birthday";

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";

        private static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + PatientEntry.TABLE_NAME + " (" +
                        PatientEntry._ID + " INTEGER PRIMARY KEY," +
                        PatientEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        PatientEntry.COLUMN_NAME_GENDER + INTEGER_TYPE + COMMA_SEP +
                        PatientEntry.COLUMN_NAME_BIRTHDAY + TEXT_TYPE + COMMA_SEP +

                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + PatientEntry.TABLE_NAME;


    }



}
