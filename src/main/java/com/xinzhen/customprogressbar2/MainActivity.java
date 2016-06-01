package com.xinzhen.customprogressbar2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xinzhen.customprogressbar2.customview.MyHoriztalProgressBar;
import com.xinzhen.customprogressbar2.customview.MyRoundProgressBar;
import com.xinzhen.customprogressbar2.customview.MyRoundProgressBar2;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MyHoriztalProgressBar progressBar1, progressBar2, progressBar3;
    private MyRoundProgressBar progressBar4, progressBar5;
    private MyRoundProgressBar2 progressBar6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar1 = (MyHoriztalProgressBar) findViewById(R.id.progressbar1);
        progressBar2 = (MyHoriztalProgressBar) findViewById(R.id.progressbar2);
        progressBar3 = (MyHoriztalProgressBar) findViewById(R.id.progressbar3);
        progressBar4 = (MyRoundProgressBar) findViewById(R.id.progressbar4);
        progressBar5 = (MyRoundProgressBar) findViewById(R.id.progressbar5);
        progressBar6 = (MyRoundProgressBar2) findViewById(R.id.progressbar6);
        new Timer().schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                progressBar1.setProgress(++i);
            }
        }, 0, 150);
        new Timer().schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                progressBar2.setProgress(++i);
            }
        }, 100, 180);
        new Timer().schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                progressBar3.setProgress(++i);
            }
        }, 0, 250);
        new Timer().schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                progressBar4.setProgress(++i);
            }
        }, 0, 150);
        new Timer().schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                progressBar5.setProgress(++i);
            }
        }, 0, 200);
        new Timer().schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                progressBar6.setProgress(++i);
            }
        }, 200, 200);
    }
}
