package com.example.myapp2.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp2.LowBatteryReceiver;
import com.example.myapp2.R;

import java.util.concurrent.atomic.AtomicInteger;

public class UpgradeActivity extends Activity {
    Button upgradeHealth;
    Button upgradeSpeed;
    Button upgradeCastSpeed;
    Button upgradeBackButton;

    TextView coinCounter;

    ProgressBar healthProgress;
    ProgressBar speedProgress;
    ProgressBar castSpeedProgress;




    LowBatteryReceiver lowBatteryReceiver = new LowBatteryReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.myapp2", Context.MODE_PRIVATE);

        final MediaPlayer mediaPlayer =MediaPlayer.create(this,R.raw.button_click_sound);

        upgradeBackButton = findViewById(R.id.upgradeBackButton);

        upgradeHealth = findViewById(R.id.upgradeHealth);
        upgradeSpeed = findViewById(R.id.upgradeSpeed);
        upgradeCastSpeed = findViewById(R.id.upgradeCastSpeed);

        coinCounter = findViewById(R.id.coins);

        healthProgress = findViewById(R.id.healthProgress);
        speedProgress = findViewById(R.id.speedProgress);
        castSpeedProgress = findViewById(R.id.castSpeedProgress);

        boolean isMute = prefs.getBoolean("isMute",false);
        AtomicInteger coinCount = new AtomicInteger(prefs.getInt("coinCounter", 0));
        AtomicInteger healthLevel = new AtomicInteger(prefs.getInt("healthLevel", 3));
        AtomicInteger speedLevel = new AtomicInteger(prefs.getInt("speedLevel",3));
        AtomicInteger castSpeedLevel = new AtomicInteger(prefs.getInt("castSpeedLevel",1));

        coinCounter.setText(String.valueOf(coinCount.get()));

        upgradeHealth.setText(String.valueOf(healthLevel.get() *2));
        upgradeSpeed.setText(String.valueOf(speedLevel.get()*4));
        upgradeCastSpeed.setText(String.valueOf(castSpeedLevel.get()*20));

        healthProgress.setProgress(healthLevel.get());
        speedProgress.setProgress(speedLevel.get());
        castSpeedProgress.setProgress(castSpeedLevel.get());


        upgradeHealth.setOnClickListener(v -> {
            if(coinCount.get() >= (healthLevel.get() *2)){
                if(healthLevel.get() == 15){
                    Toast.makeText(this,"Health is maxed out!",Toast.LENGTH_SHORT).show();
                }
                else{
                    coinCount.addAndGet(-(int) (healthLevel.get() * 2));
                    healthLevel.getAndIncrement();
                    prefs.edit().putInt("healthLevel", healthLevel.get()).apply();
                    prefs.edit().putInt("coinCounter", coinCount.get()).apply();
                    healthProgress.setProgress(healthLevel.get());
                    if(!isMute){
                        mediaPlayer.start();
                    }
                }
                coinCounter.setText(String.valueOf(coinCount.get()));
                upgradeHealth.setText(String.valueOf(healthLevel.get() *2));
            }
            else{
                Toast.makeText(this,"Not enough coins!",Toast.LENGTH_SHORT).show();
            }
        });

        upgradeSpeed.setOnClickListener(v -> {
            if(coinCount.get() >=speedLevel.get() *4){
                if(speedLevel.get() == 9){
                    Toast.makeText(this,"Speed is maxed out!",Toast.LENGTH_SHORT).show();
                }
                else{
                    coinCount.addAndGet(-(int) (speedLevel.get() * 4));
                    speedLevel.getAndIncrement();
                    prefs.edit().putInt("speedLevel", speedLevel.get()).apply();
                    prefs.edit().putInt("coinCounter", coinCount.get()).apply();
                    speedProgress.setProgress(speedLevel.get());
                    if(!isMute){
                        mediaPlayer.start();
                    }
                }
                coinCounter.setText(String.valueOf(coinCount.get()));
                upgradeSpeed.setText(String.valueOf(speedLevel.get()*4));
            }
            else{
                Toast.makeText(this,"Not enough coins!",Toast.LENGTH_SHORT).show();
            }
        });

        upgradeCastSpeed.setOnClickListener(v -> {
            if(coinCount.get() >=castSpeedLevel.get() * 20){
                if(castSpeedLevel.get() == 5){
                    Toast.makeText(this,"Cast speed is maxed out!",Toast.LENGTH_SHORT).show();
                }
                else{
                    coinCount.addAndGet(-(int) (castSpeedLevel.get() * 20));
                    castSpeedLevel.getAndIncrement();
                    prefs.edit().putInt("castSpeedLevel", castSpeedLevel.get()).apply();
                    prefs.edit().putInt("coinCounter", coinCount.get()).apply();
                    castSpeedProgress.setProgress(castSpeedLevel.get());
                    if(!isMute){
                        mediaPlayer.start();
                    }
                }
                coinCounter.setText(String.valueOf(coinCount.get()));
                upgradeCastSpeed.setText(String.valueOf(castSpeedLevel.get() *20));
            }
            else{
                Toast.makeText(this,"Not enough coins!",Toast.LENGTH_SHORT).show();
            }
        });

        upgradeBackButton.setOnClickListener(v -> {
            upgradeBackButton.setBackground(getResources().getDrawable(
                    R.drawable.pressed_back_button));
            if(!isMute){
                mediaPlayer.start();
            }
            startActivity(new Intent(UpgradeActivity.this, MenuActivity.class));
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