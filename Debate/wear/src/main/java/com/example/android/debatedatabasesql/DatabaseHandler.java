package com.example.android.debatedatabasesql;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


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
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "("
                + "COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_1 + " TEXT, "
                + COLUMN_2 + " TEXT, "
                + COLUMN_3 + " TEXT, "
                + COLUMN_4 + " INTEGER, "
                + COLUMN_5 + " TEXT,"
                + COLUMN_6 + " TEXT, "
                + COLUMN_7 + " TEXT);"
                ;
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public String loadHandler() {
        String result = "";
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            String result_2 = cursor.getString(2);
            String result_3 = cursor.getString(3);
            String result_4 = cursor.getString(4);
            String result_5 = cursor.getString(5);
            String result_6 = cursor.getString(6);
            String result_7 = cursor.getString(7);

            result += String.valueOf(result_0) + " " + result_1 + " " + result_2 + " " + result_3 +
                    " " + result_4 + " " + result_5 + " " + result_6 + " " + result_7 +
                    System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }

    public JSONObject lastEntry() {
        String query= "SELECT * FROM " + TABLE_NAME +" ORDER BY COLUMN_ID DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Log.d("cursor", DatabaseUtils.dumpCursorToString(cursor));
        Log.d("cursorCheck", Integer.toString(cursor.getColumnIndexOrThrow("COLUMN_ID")));
        int column_primaryKey = 0;
        String watchID = "", date = "", timestamp = "", roundID = "", heartbeat = "",
                accelerometer = "", gyroscope = "";
        if (cursor != null && cursor.moveToFirst()) {
            column_primaryKey = cursor.getInt(0);
            watchID = cursor.getString(1);
            date = cursor.getString(2);
            timestamp = cursor.getString(3);
            roundID = cursor.getString(4);
            heartbeat = cursor.getString(5);
            accelerometer = cursor.getString(6);
            gyroscope = cursor.getString(7);
        }
        cursor.close();

        JSONObject json = new JSONObject();
        try {
            json.put("primary_key", column_primaryKey);
            json.put("WatchID", watchID);
            json.put("RoundID", roundID);
            json.put("Heartbeat", heartbeat);
            json.put("Accelerometer", accelerometer);
            json.put("Gyroscope", gyroscope);
            json.put("TimeStamp", timestamp);
            json.put("Date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void addHandler(DebateRound debateRound) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_1, debateRound.getWatchID());
        values.put(COLUMN_2, debateRound.getDate());
        values.put(COLUMN_3, debateRound.getTimestamp());
        values.put(COLUMN_4, debateRound.getRoundID());
        values.put(COLUMN_5, debateRound.getHeartbeat());
        values.put(COLUMN_6, debateRound.getAcc());
        values.put(COLUMN_7, debateRound.getGyro());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //might need to change the return type
    public Cursor findHandlerByWatchID(String watchID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] strings = {watchID};
        return db.query(TABLE_NAME, null, "Column1==?", strings, null, null, null);

    }

    public Cursor findHandlerByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] strings = {date};
        return db.query(TABLE_NAME, null, "Column2==?", strings, null, null, null);


    }

    public boolean deleteHandlerByID(int ID) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + "COLUMN_ID = " + String.valueOf(ID);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DebateRound debate = new DebateRound();
        if (cursor.moveToFirst()) {
            String[] a = {String.valueOf(ID)};
            db.delete(TABLE_NAME, "COLUMN_ID" + "=?", a);
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public boolean updateHandler(int ID, String name) {
        return false;
    }

}
