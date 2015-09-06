package com.warfarin_app.db;

import android.provider.BaseColumns;

/**
 * Created by Coming on 9/6/15.
 */
public class LogEntry implements BaseColumns {
    public static final String TABLE_NAME = "Log";
    public static final String COLUMN_NAME_MSG = "msg";
    public static final String COLUMN_NAME_DATE = "date";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LogEntry.TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_MSG + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DATE + INTEGER_TYPE +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PatientEntry.TABLE_NAME;
}
