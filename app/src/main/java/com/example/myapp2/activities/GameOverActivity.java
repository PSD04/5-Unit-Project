package com.example.myapp2.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapp2.LowBatteryReceiver;
import com.example.myapp2.R;

public class GameOverActivity extends Activity {
    Button gotoMenuButton;
    Button gotoUpgradesButton;
    Button replayButton;
    TextView coinCounter;
    TextView scoreCounter;
    LowBatteryReceiver lowBatteryReceiver = new LowBatteryReceiver(); // the low battery BroadCastReceiver
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.myapp2", Context.MODE_PRIVATE);

        boolean isMute = prefs.getBoolean("isMute",false);

        MediaPlayer gameOverMediaPlayer = MediaPlayer.create(this, R.raw.game_over_sound);
        MediaPlayer buttonMediaPlayer = MediaPlayer.create(this,R.raw.button_click_sound);


        if(!isMute){
            gameOverMediaPlayer.start();
        }


        gotoMenuButton = findViewById(R.id.gameOverGotoMenu);
        gotoUpgradesButton=findViewById(R.id.gameOverGotoUpgrades);
        replayButton = findViewById(R.id.gameOverReplay);
        coinCounter = findViewById(R.id.coinCounter);
        scoreCounter = findViewById(R.id.score);

        coinCounter.setText(String.valueOf(prefs.getInt("coinCounter",0)));
        scoreCounter.setText(String.valueOf(prefs.getInt("lastScore",0)));


        replayButton.setBackground(getResources().getDrawable(R.drawable.pressed_replay_button));
        gotoMenuButton.setBackground(getResources().getDrawable(R.drawable.pressed_goto_menu_button));
        gotoUpgradesButton.setBackground(getResources().getDrawable(R.drawable.pressed_goto_upgrades_button));


        replayButton.setOnClickListener(v -> {
            if(!isMute){
                buttonMediaPlayer.start();

            }            startActivity(new Intent(GameOverActivity.this, StartGameActivity.class));
            finish();
        });

        gotoMenuButton.setOnClickListener(v -> {
            if(!isMute){
                buttonMediaPlayer.start();

            }
            startActivity(new Intent(GameOverActivity.this, MenuActivity.class));
            finish();
        });

        gotoUpgradesButton.setOnClickListener(v -> {
            if(!isMute){
                buttonMediaPlayer.start();
            }
            startActivity(new Intent(GameOverActivity.this, UpgradeActivity.class));
            finish();
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
    public void onBackPressed() {
    }
}