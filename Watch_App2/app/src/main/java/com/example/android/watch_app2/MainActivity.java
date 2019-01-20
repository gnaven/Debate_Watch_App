package com.example.android.watch_app2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//TODO: Make a new class for actions using the buttons and call the class onCreate

public class MainActivity extends WearableActivity implements OnClickListener,SensorEventListener {

    private TextView mRoundTextView;
    private TextView mTimerText;

    public Button mStartButton;
    public Button mStopButton;
    public Button mResetButton;

    public Button mPrevious;

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;
    final int MSG_RESET_TIMER = 3;

    Stopwatch timer = new Stopwatch();
    final int REFRESH_RATE = 100;
    public static float heartrate = 0.0f;
    // initializing accelerometer variables
    public static float acc_x;
    public static float acc_y;
    public static float acc_z;
    // initializing gyroscope variables
    public static float gyro_x;
    public static float gyro_y;
    public static float gyro_z;

    public int seconds = 0;
    public int minutes = 0;
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mGyroSensor;
    private Sensor mHeartRateSensor;

    JSONObject data;
    JSONArray roundArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // makes a new data file for storing data from the watch
        data = new JSONObject();
        try {
            data.put("watch id", 101);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        roundArr = new JSONArray();


        mTimerText = (TextView) findViewById(R.id.text_timer);

        mPrevious = (Button)findViewById(R.id.prev_button);

        mStartButton = (Button)findViewById(R.id.start_button);
        mStopButton = (Button)findViewById(R.id.stop_button);
        mResetButton = (Button)findViewById(R.id.reset_button);

        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
       // mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        boolean sensorRegistered = mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

//    public void onClickStartButton(View v){
//        String startString = "Start Clicked";
//        Toast.makeText(this, startString, Toast.LENGTH_LONG).show();
//    }
//
//    public void onClickStopButton (View v){
//        String stopString = "Stop Clicked";
//        Toast.makeText(this, stopString, Toast.LENGTH_LONG).show();
//
//    }
//    public void onClickResetButton (View v){
//        String resetString = "Reset Clicked";
//        Toast.makeText(this, resetString, Toast.LENGTH_LONG).show();
//
//    }

    public void onClickNextButton (View v){
        mRoundTextView = (TextView)findViewById(R.id.round_num_text);
        int currentRound = Integer.parseInt(mRoundTextView.getText().toString());
        int nextRound = currentRound +1;
        mRoundTextView.setText(Integer.toString(nextRound));
        mPrevious.setVisibility(View.VISIBLE);

        String nextString = "Next Clicked";
        Toast.makeText(this, nextString, Toast.LENGTH_LONG).show();

    }

    public void onClickPreviousButton (View v){
        /*
        For clicking the previous button, decreases number of round if clicked.
        Does not let the round number go beyond 1
         */
        mRoundTextView = (TextView)findViewById(R.id.round_num_text);
        int currentRound = Integer.parseInt(mRoundTextView.getText().toString());
        int nextRound = currentRound -1;

        if (nextRound<=1){
            mPrevious.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Round cannot be less than 1", Toast.LENGTH_LONG).show();
            mRoundTextView.setText(Integer.toString(nextRound));
        }else {
            mRoundTextView.setText(Integer.toString(nextRound));
        }

        String prevString = "Previous Clicked";
        Toast.makeText(this, prevString, Toast.LENGTH_LONG).show();

    }
    /*
    Handles clicks on buttons and uses the Stopwatch class to show
    time counts
     */
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_RESET_TIMER:
                    /*
                    activated when reset button is clicked.
                    calls reset in Stopwatch and cleans the interface
                     */
                    timer.reset();
                    mHandler.removeMessages(MSG_UPDATE_TIMER);
                    mTimerText.setText("00:00");
                    break;
                case MSG_START_TIMER:
                    /*
                    Starts the timer
                     */
                    timer.resume(); //start timer
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    /*
                    Updates the timer according to refresh rate as long as the stopwatch is running
                     */
                    String timeCurrent = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(timer.getElapsedTime()),
                            TimeUnit.MILLISECONDS.toSeconds(timer.getElapsedTime()) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                                            .toMinutes(timer.getElapsedTime()))
                    );
                    //mTimerText.setText(timeCurrent);
                    mTimerText.setText(""+timeCurrent);
                    try {
                        sensorArr = sensorDataInput(sensorArr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE); //text view is updated every second,
                    break;                                  //though the timer is still running
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    String timeStop = String.format("%02d:%02d ",
                            TimeUnit.MILLISECONDS.toMinutes(timer.getElapsedTime()),
                            TimeUnit.MILLISECONDS.toSeconds(timer.getElapsedTime()) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                                            .toMinutes(timer.getElapsedTime()))
                    );
                    mTimerText.setText(""+heartrate);
                    break;

                default:
                    break;
            }
        }
    };
    JSONArray sensorArr;
    JSONObject roundN;
    @Override
    public void onClick(View v) {
        /*
        Manages Buttons being clicked. Disables Stop when Start clicked and vice versa
         */
        if(mStartButton == v) {
            mStartButton.setEnabled(false);
            mStopButton.setEnabled(true);
            mHandler.sendEmptyMessage(MSG_START_TIMER);

            // making a new Round Object
            String roundNum = mRoundTextView.getText().toString();
            roundN = new JSONObject();
            try {
                roundN.put("round num", roundNum);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sensorArr = new JSONArray();

            startMeasure();
        }else
        if(mStopButton == v){
            mStopButton.setEnabled(false);
            mStartButton.setEnabled(true);
            mHandler.sendEmptyMessage(MSG_STOP_TIMER);
            try {
                roundN.put("sensor data", sensorArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            roundArr.put(roundN);

            try {
                data.put("round num", roundArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                fileClose();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stopMeasure();
        }else if (mResetButton == v){
            mStartButton.setEnabled(true);
            mStopButton.setEnabled(true);
            mHandler.sendEmptyMessage(MSG_RESET_TIMER);
        }
    }

    private void startMeasure() {
        boolean sensorAccRegistered = mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //boolean sensorGyroRegistered = mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
       // boolean sensorHrateRegistered = mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Log.d("Sensor Status:", " Acc Sensor registered: " + (sensorAccRegistered ? "yes" : "no"));
        //Log.d("Sensor Status:", " Gyro Sensor registered: " + (sensorGyroRegistered ? "yes" : "no"));
        //Log.d("Sensor Status:", " Heart Rate Sensor registered: " + (sensorHrateRegistered ? "yes" : "no"));

    }

    private void stopMeasure() {
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            acc_x = event.values[0];
            acc_y = event.values[1];
            acc_z = event.values[2];
        }
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            gyro_x = event.values[0];
            gyro_y = event.values[1];
            gyro_z = event.values[2];
        }
        if(event.sensor.getType() == Sensor.TYPE_HEART_RATE){
            heartrate = event.values[0];
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public JSONArray sensorDataInput (JSONArray sensorArray) throws JSONException {
    // inputs all the fields as a JSON object

        JSONObject sensorData = new JSONObject();
        sensorData.put("acc_x", acc_x);
        sensorData.put("acc_x", acc_y);
        sensorData.put("acc_x", acc_z);
        sensorData.put("gyro_x", gyro_x);
        sensorData.put("gyro_y", gyro_y);
        sensorData.put("gyro_z", gyro_z);

        sensorArray.put(sensorData);

        return sensorArray;

    }

    public void fileClose () throws IOException {
        String File_Name = "watch_data.JSON";
        File datafile = new File (this.getFilesDir(), File_Name);
        FileWriter fileWriter = new FileWriter(datafile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(data.toString());
        bw.close();

    }
}
