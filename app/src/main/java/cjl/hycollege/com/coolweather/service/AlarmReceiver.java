package cjl.hycollege.com.coolweather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cjl.hycollege.com.coolweather.constant.keyUtils;

/**
 * Created by xiaolong on 2017/7/12.
 */

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, AutoUpdateService.class);
        SharedPreferences getprefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean state = getprefs.getBoolean(keyUtils.SAVASTATE, true);
        if (state){
            context.startService(i);
        }

    }
}
