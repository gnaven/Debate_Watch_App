package com.example.android.debatedatabasesql;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    private EditText mWatchID;
    private EditText mHeartbeat;
    private EditText mAccelerometer;
    private EditText mGyroscope;
    private Button mLoad;
    private Button mAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = new DatabaseHandler(this, null, null, 1).getWritableDatabase();
        mTextView = (TextView) findViewById(R.id.text);
        mWatchID = (EditText) findViewById(R.id.watchID);
        mHeartbeat = (EditText) findViewById(R.id.heartbeat);
        mAccelerometer = (EditText) findViewById(R.id.acclerometer);
        mGyroscope = (EditText) findViewById(R.id.gyroscope);
        mLoad = (Button) findViewById(R.id.btnload);
        mAdd = (Button) findViewById(R.id.btnadd);
        DatabaseHandler dbHandler = new DatabaseHandler(this, null, null, 1);
        dbHandler.deleteHandlerByID(7);


        // Enables Always-on
        setAmbientEnabled();
    }

    public void loadDatabase(View view) {
        DatabaseHandler dbHandler = new DatabaseHandler(this, null, null, 1);
        Log.d("DebateApp", "Database Loading Starting");
        Log.d("DebateApp", "-- Start --");
        Log.d("DebateApp", dbHandler.loadHandler());
        Log.d("DebateApp", "-- End --");
    }

    public void addData(View view) {
        DatabaseHandler dbHandler = new DatabaseHandler(this, null, null, 1);
        String watchID = mWatchID.getText().toString();
        int rID = 1;
        String hbeat = mHeartbeat.getText().toString();
        String acc = mAccelerometer.getText().toString();
        String gyro = mGyroscope.getText().toString();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat hrs = new SimpleDateFormat("HH:mm:ss:SSS");
        SimpleDateFormat dateForm = new SimpleDateFormat("MM/dd/yyyy");
        Date d = new Date();
        String tStamp = hrs.format(cal.getTime());
        String currDate = dateForm.format(d);
        System.out.print(currDate);

        DebateRound debate = new DebateRound(watchID,currDate, tStamp, rID, hbeat, acc, gyro);
        dbHandler.addHandler(debate);
        Log.d("DebateApp", "-- Successfully added data --");
    }


}
