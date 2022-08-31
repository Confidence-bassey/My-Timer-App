package com.confidenceb.exercisetimerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView tv_timerIcon;
    private Button startPausebtn, resetBtn;
    private  boolean isRunning;

    private CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS = 600000;  // equivalent to 10 minutes
    private long timeLeft = START_TIME_IN_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_timerIcon = findViewById(R.id.timerIcon);
        startPausebtn = findViewById(R.id.startTimer);
        resetBtn = findViewById(R.id.resetTimer);


        startPausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRunning){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        remaingTime();

      /*  long timerduration = TimeUnit.MINUTES.toMillis(1);

        new CountDownTimer(timerduration, 1000) {
            @Override
            public void onTick(long l) {
                String durationString = String.format(Locale.ENGLISH, "%02d : %02d", TimeUnit.MILLISECONDS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(1) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(1)));
                tv_timerIcon.setText(durationString);
            }

            @Override
            public void onFinish() {
                tv_timerIcon.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Time elapseted", Toast.LENGTH_LONG).show();
            }
        }.start();   */
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                remaingTime();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                startPausebtn.setText("Start Timer");
                startPausebtn.setVisibility(View.INVISIBLE);
                resetBtn.setVisibility(View.VISIBLE);
            }
        }.start();
        isRunning = true;
        startPausebtn.setText("Pause Timer");
        resetBtn.setVisibility(View.INVISIBLE);
    }

    private void remaingTime() {
        int minutes = (int) timeLeft/1000/60;
        int seconds = (int) timeLeft / 1000 % 60;
        String formatedTimeLeft = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        tv_timerIcon.setText(formatedTimeLeft);
    }

    private void resetTimer() {
        timeLeft = START_TIME_IN_MILLIS;
        remaingTime();
        resetBtn.setVisibility(View.INVISIBLE);
        startPausebtn.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isRunning = false;
        startPausebtn.setText("Start Timer");
        resetBtn.setVisibility(View.VISIBLE);
    }
}