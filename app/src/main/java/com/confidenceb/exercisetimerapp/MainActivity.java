package com.confidenceb.exercisetimerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView tv_timerIcon;
    private EditText setTimeEdtxt;
    private Button startPausebtn, resetBtn, setTimeBtn;
    private  boolean isRunning;

    private CountDownTimer countDownTimer;
    private long START_TIME_IN_MILLIS;  // equivalent to 10 minutes
    private long timeLeft = START_TIME_IN_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_timerIcon = findViewById(R.id.timerIcon);
        startPausebtn = findViewById(R.id.startTimer);
        resetBtn = findViewById(R.id.resetTimer);
        setTimeBtn = findViewById(R.id.SetTimer);
        setTimeEdtxt = findViewById(R.id.enterMinutes);


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

        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterMinutes = setTimeEdtxt.getText().toString();
                if(enterMinutes.length() == 0){
                    Toast.makeText(MainActivity.this, "Enter minutes to set timer", Toast.LENGTH_LONG).show();
                    return;
                }
                long enteredMinutesinMillis = Long.parseLong(enterMinutes)*60000;
                if(enteredMinutesinMillis == 0){
                    Toast.makeText(MainActivity.this, "No minutes entered", Toast.LENGTH_LONG).show();
                    return;
                }
                setTimeMinutes(enteredMinutesinMillis);
                setTimeEdtxt.setText("");
            }
        });

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

    private void setTimeMinutes(long milliSeconds) {
        START_TIME_IN_MILLIS = milliSeconds;
        resetTimer();
    }

    private void startTimer() {
        startService(new Intent(this, MyServiceClass.class));
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
        stopService(new Intent(this, MyServiceClass.class));
        timeLeft = START_TIME_IN_MILLIS;
        remaingTime();
        resetBtn.setVisibility(View.INVISIBLE);
        startPausebtn.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        stopService(new Intent(this, MyServiceClass.class));
        countDownTimer.cancel();
        isRunning = false;
        startPausebtn.setText("Start Timer");
        resetBtn.setVisibility(View.VISIBLE);
    }
}