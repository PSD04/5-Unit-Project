package com.example.myapp2.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapp2.LowBatteryReceiver;
import com.example.myapp2.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class MenuActivity extends Activity {
    Button startButton;
    Button upgradesButton;
    Button audioButton;
    Button musicButton;

    TextView coinView;
    TextView highscoreView;
    LowBatteryReceiver lowBatteryReceiver = new LowBatteryReceiver();

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.myapp2", Context.MODE_PRIVATE);

        final MediaPlayer mediaPlayer =MediaPlayer.create(this,R.raw.button_click_sound);

        AtomicBoolean isMute = new AtomicBoolean(prefs.getBoolean("isMute", false));
        AtomicBoolean isMusicMute = new AtomicBoolean(prefs.getBoolean("isMusicMute", false));

        coinView = findViewById(R.id.coins);
        coinView.setText(String.valueOf(prefs.getInt("coinCounter",0)));

        highscoreView = findViewById(R.id.highscore);
        highscoreView.setText(String.valueOf(prefs.getInt("highscore",0)));


        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            if(!isMute.get()){
                mediaPlayer.start();
            }
            startButton.setBackground(getResources().getDrawable(
                    R.drawable.pressed_button_background));
            startActivity(new Intent(MenuActivity.this, StartGameActivity.class));
            finish();
        });


        upgradesButton = findViewById(R.id.upgradesButton);
        upgradesButton.setOnClickListener(v -> {
            if(!isMute.get()){
                mediaPlayer.start();
            }
            upgradesButton.setBackground(getResources().getDrawable(
                    R.drawable.pressed_button_background));
            startActivity(new Intent(MenuActivity.this, UpgradeActivity.class));
            finish();
        });


        audioButton = findViewById(R.id.audioButton);
        if(isMute.get()){
            audioButton.setBackground(getResources().getDrawable(R.drawable.audio_off));
        }else{
            audioButton.setBackground(getResources().getDrawable(R.drawable.audio_on));
        }
        audioButton.setOnClickListener(v -> {
            mediaPlayer.start();
            if(isMute.get()){
                prefs.edit().putBoolean("isMute",false).apply();
                isMute.set(false);
                audioButton.setBackground(getResources().getDrawable(R.drawable.audio_on));
            }else{
                prefs.edit().putBoolean("isMute",true).apply();
                isMute.set(true);
                audioButton.setBackground(getResources().getDrawable(R.drawable.audio_off));
            }
        });


        musicButton = findViewById(R.id.musicButton);
        if(isMusicMute.get()){
            musicButton.setBackground(getResources().getDrawable(R.drawable.music_off));
        }else{
            musicButton.setBackground(getResources().getDrawable(R.drawable.music_on));
        }
        musicButton.setOnClickListener(v -> {
            mediaPlayer.start();
            if(isMusicMute.get()){
                prefs.edit().putBoolean("isMusicMute",false).apply();
                isMusicMute.set(false);
                musicButton.setBackground(getResources().getDrawable(R.drawable.music_on));
            }else{
                prefs.edit().putBoolean("isMusicMute",true).apply();
                isMusicMute.set(true);
                musicButton.setBackground(getResources().getDrawable(R.drawable.music_off));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(lowBatteryReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(lowBatteryReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setMessage("Do you really want to exit the game?");
        exitDialog.setTitle("Exit");
        exitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MenuActivity.super.onBackPressed();
            }
        });
        exitDialog.setNegativeButton("No",null);
        exitDialog.create().show();
    }
}