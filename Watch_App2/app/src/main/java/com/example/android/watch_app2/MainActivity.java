package com.example.android.watch_app2;

import android.os.Bundle;
import android.os.Message;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

//TODO: Make a new class for actions using the buttons and call the class onCreate

public class MainActivity extends WearableActivity implements OnClickListener {

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

    public int seconds = 0;
    public int minutes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    public void onClickStartButton(View v){
        String startString = "Start Clicked";
        Toast.makeText(this, startString, Toast.LENGTH_LONG).show();
    }

    public void onClickStopButton (View v){
        String stopString = "Stop Clicked";
        Toast.makeText(this, stopString, Toast.LENGTH_LONG).show();

    }
    public void onClickResetButton (View v){
        String resetString = "Reset Clicked";
        Toast.makeText(this, resetString, Toast.LENGTH_LONG).show();

    }

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
                    mTimerText.setText(timeCurrent);
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
                    mTimerText.setText(timeStop);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        /*
        Manages Buttons being clicked. Disables Stop when Start clicked and vice versa
         */
        if(mStartButton == v) {
            mStartButton.setEnabled(false);
            mStopButton.setEnabled(true);
            mHandler.sendEmptyMessage(MSG_START_TIMER);
        }else
        if(mStopButton == v){
            mStopButton.setEnabled(false);
            mStartButton.setEnabled(true);
            mHandler.sendEmptyMessage(MSG_STOP_TIMER);
        }else if (mResetButton == v){
            mStartButton.setEnabled(true);
            mStopButton.setEnabled(true);
            mHandler.sendEmptyMessage(MSG_RESET_TIMER);
        }
    }
}
