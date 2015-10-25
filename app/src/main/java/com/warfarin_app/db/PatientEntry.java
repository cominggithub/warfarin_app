package com.warfarin_app.db;

import android.provider.BaseColumns;

/**
 * Created by Coming on 8/9/15.
 */
public class PatientEntry implements BaseColumns {
    public static final String TABLE_NAME = "Patient";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_GENDER = "gender";
    public static final String COLUMN_NAME_BIRTHDAY = "birthday";
    public static final String COLUMN_NAME_IS_WARFARIN = "is_warfarin";
    public static final String COLUMN_NAME_DOCTOR = "doctor";
    public static final String COLUMN_NAME_BLUEDEV_NAME = "bluedev_name";
    public static final String COLUMN_NAME_BLUEDEV_ADDRESS = "bluedev_address";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PatientEntry.TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_GENDER + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_BIRTHDAY + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_IS_WARFARIN + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_DOCTOR + TEXT_TYPE  + COMMA_SEP +
                    COLUMN_NAME_BLUEDEV_NAME + TEXT_TYPE  + COMMA_SEP +
                    COLUMN_NAME_BLUEDEV_ADDRESS + TEXT_TYPE +

                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PatientEntry.TABLE_NAME;


}