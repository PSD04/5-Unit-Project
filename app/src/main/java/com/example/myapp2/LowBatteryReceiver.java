package com.example.myapp2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * The LowBatterReceiver notifies us when the battery is low.
 */
public class LowBatteryReceiver extends BroadcastReceiver {
    /**
     * Handles the low battery event
     * @param context - The context of the event
     * @param intent - the intent from which we came from
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Battery Low!", Toast.LENGTH_SHORT).show();
    }

}
