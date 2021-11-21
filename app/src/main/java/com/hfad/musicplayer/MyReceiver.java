package com.hfad.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int action = intent.getIntExtra("action", 0);
        Intent serviceIntent = new Intent(context, MyService.class);
        serviceIntent.putExtra("service_action", action);
        context.startService(serviceIntent);
    }
}
