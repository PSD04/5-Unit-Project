package com.example.myapp2.activities;



import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.myapp2.BackgroundSoundService;
import com.example.myapp2.GameView;
import com.example.myapp2.LowBatteryReceiver;

/**
 * The activity which runs the game
 */
public class StartGameActivity extends Activity {
    private GameView gameView;      //the GameView object which runs the game
    private boolean isMusicMute;
    LowBatteryReceiver lowBatteryReceiver = new LowBatteryReceiver(); // the low battery BroadCastReceiver


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.myapp2", Context.MODE_PRIVATE);
        this.isMusicMute = prefs.getBoolean("isMusicMute",false);
        gameView = new GameView(this);      //start a new game
        setContentView(gameView);       //focus on it


    }

    @Override
    protected void onPause() {
        gameView.pause();       //pause the game
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //set the low battery receiver:
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(lowBatteryReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stop the low battery receiver:
        unregisterReceiver(lowBatteryReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isServiceRunning(BackgroundSoundService.class) && !isMusicMute){
            startService(new Intent(StartGameActivity.this, BackgroundSoundService.class));
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if(isServiceRunning(BackgroundSoundService.class)){
            stopService(new Intent(
                    StartGameActivity.this, BackgroundSoundService.class));
        }
    }

    @Override
    public void onBackPressed() {
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}