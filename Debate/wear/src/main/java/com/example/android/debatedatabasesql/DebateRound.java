package com.example.android.debatedatabasesql;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DebateRound {

    //fields
    private String watchID;
    private String date;
    private String timestamp;
    private int roundID;
    private String heartbeat;
    private String acc;
    private String gyro;

    //constructors
    public DebateRound() {}

    public DebateRound (String watchID, String date, String timestamp, int roundID, String heartbeat, String acc, String gyro) {
        this.watchID = watchID;
        this.date = date;
        this.timestamp = timestamp;
        this.roundID = roundID;
        this.heartbeat = heartbeat;
        this.acc = acc;
        this.gyro = gyro;
    }

    //properties

    public void setWatchID(String id) {
        this.watchID = id;
    }

    public String getWatchID() {
        return this.watchID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setTimestamp(String time) {
        this.timestamp = time;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setRoundID(int id) {
        this.roundID = id;
    }

    public int getRoundID() {
        return this.roundID;
    }

    public void setHeartbeat(JSONObject data) {

        this.heartbeat = data.toString();
    }

    public JSONObject getHeartbeat() {
        try{
        JSONObject obj =  new JSONObject(this.heartbeat);
            return obj;
        } catch (JSONException e){
            Log.e("tag", "Error parsing JSONObject", e);
            return null;
        }
    }

    public void setAcc(JSONObject data) {
        this.acc = data.toString();
    }

    public JSONObject getAcc() {
        try{
            JSONObject obj =  new JSONObject(this.acc);
            return obj;
        } catch (JSONException e){
            Log.e("tag", "Error parsing JSONObject", e);
            return null;
        }
    }

    public void setGyro(JSONObject data) {
        this.gyro = data.toString();
    }

    public JSONObject getGyro() {
        try{
            JSONObject obj =  new JSONObject(this.gyro);
            return obj;
        } catch (JSONException e){
            Log.e("tag", "Error parsing JSONObject", e);
            return null;
        }
    }
}
