package com.beemo.moodsupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReciever extends BroadcastReceiver {
    public NotificationReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MoodActivity.class);
        context.startService(intent1);
    }
}