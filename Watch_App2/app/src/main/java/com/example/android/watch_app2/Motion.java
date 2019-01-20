package com.example.android.watch_app2;
/*
In this class we implement motion sensors (accelerometer, gyroscope, bpm)
and collect data in the form of a spreadsheet for the time elapsed
 */

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class Motion extends Activity implements SensorEventListener {

    public float acc_x;
    public float acc_y;
    public float acc_z;

    @Override
    public void onSensorChanged(SensorEvent event) {
        acc_x = event.values[0];
        acc_y = event.values[1];
        acc_z = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
