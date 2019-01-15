package com.example.android.debatedatabasesql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "debateDB.db";
    private static final String TABLE_NAME = "DebateRound";
    private static final String COLUMN_ID = "debateID";
    private static final String COLUMN_1 = "WatchID";
    private static final String COLUMN_2 = "Date";
    private static final String COLUMN_3 = "TimeStamp";
    private static final String COLUMN_4 = "RoundID";
    private static final String COLUMN_5 = "Heartbeat";
    private static final String COLUMN_6 = "Accelerometer";
    private static final String COLUMN_7 = "Gyroscope";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                "INTEGER PRIMARYKEY," + COLUMN_1 + "TEXT," + COLUMN_2 + "TEXT," +
                COLUMN_3 + "TEXT," + COLUMN_4 + "TEXT," + COLUMN_5 + "TEXT," +
                COLUMN_6 + "TEXT," + COLUMN_7 + "TEXT,)";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    public String loadHandler() {}
    public void addHandler(DebateRound debateRound) {}
    public DebateRound findHandler(String watchID) {}
    public boolean deleteHandler(int ID) {}
    public boolean updateHandler(int ID, String name) {}


}
