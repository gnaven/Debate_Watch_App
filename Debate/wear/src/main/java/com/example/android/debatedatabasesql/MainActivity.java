package com.example.android.debatedatabasesql;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    static int tempRoundID = 1;
    static String tempTimeStamp;
    static String tempCurrentDate;

    static final String postingURL = "http://10.0.2.2:28017/dataPOST";
    static final String gettingURL = "gettingURL";

    RequestQueue requestQueue;
    static final int INTERNET_REQ = 23;
    static final String REQ_TAG = "VACTIVITY";

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

        //volley
        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

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

//    public void lastEntry(View view) {
//        DatabaseHandler dbHandler = new DatabaseHandler(this, null, null, 1);
//        Log.d("DebateApp", "Database Last Entry");
//        Log.d("DebateApp", "-- Start --");
//        Log.d("DebateApp", dbHandler.lastEntry());
//        Log.d("DebateApp", "-- End --");
//    }

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

        /////////
        tempCurrentDate = currDate;
        tempTimeStamp = tStamp;

        DebateRound debate = new DebateRound(watchID, currDate, tStamp, rID, hbeat, acc, gyro);
        dbHandler.addHandler(debate);
        Log.d("DebateApp", "-- Successfully added data --");
    }


    //Volley Methods
    public void GetJSONResponse(View v) {
        DatabaseHandler dbHandler = new DatabaseHandler(this, null, null, 1);
        String url = gettingURL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("String Response", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Server Response", "Error getting response");
            }
        });

        jsonObjectRequest.setTag(REQ_TAG);
        requestQueue.add(jsonObjectRequest);
    }

    public void PostJSONRequest(View v) {
        DatabaseHandler dbHandler = new DatabaseHandler(this, null, null, 1);
        JSONObject catchLastEntry = dbHandler.lastEntry();
        JSONObject json = new JSONObject();
        try {
            json.put("primary_key", catchLastEntry.getInt("primary_key"));
            json.put("WatchID", catchLastEntry.getString("WatchID"));
            json.put("RoundID", catchLastEntry.getString("RoundID"));
            json.put("Heartbeat", catchLastEntry.getString("Heartbeat"));
            json.put("Accelerometer", catchLastEntry.getString("Accelerometer"));
            json.put("Gyroscope", catchLastEntry.getString("Gyroscope"));
            json.put("TimeStamp", catchLastEntry.getString("TimeStamp"));
            json.put("Date", catchLastEntry.getString("Date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = postingURL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("String Response", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Server Response", "Error posting response");
            }

        });

        jsonObjectRequest.setTag(REQ_TAG);
        requestQueue.add(jsonObjectRequest);
    }


}
