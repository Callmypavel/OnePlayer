package com.example.peacemaker.oneplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 请叫我保尔 on 2015/10/7.
 */
public class InstallBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("接收到变化", intent.getAction());
        // ((MainActivity)context).openApk(context,"");
    }
}
