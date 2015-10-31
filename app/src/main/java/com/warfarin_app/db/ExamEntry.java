package com.warfarin_app.db;

import android.provider.BaseColumns;
/**
 * Created by Coming on 8/9/15.
 */
public class ExamEntry implements BaseColumns{

    public static final String TABLE_NAME = "Exam";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_WEEK = "week";
    public static final String COLUMN_NAME_MARFARIN = "marfarin";
    public static final String COLUMN_NAME_PT = "pt";
    public static final String COLUMN_NAME_INR = "inr";


    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INT_TYPE = " INTEGER";
    private static final String DATETIME_TYPE = " DATETIME";


    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExamEntry.TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
//                    COLUMN_NAME_DATE + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DATE + DATETIME_TYPE + COMMA_SEP +
                    COLUMN_NAME_WEEK + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_MARFARIN + REAL_TYPE + COMMA_SEP +
                    COLUMN_NAME_PT + REAL_TYPE + COMMA_SEP +
                    COLUMN_NAME_INR + REAL_TYPE +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExamEntry.TABLE_NAME;


}
