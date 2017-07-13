package cjl.hycollege.com.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       Intent i=new Intent(context,AutoUpdateService.class);
        context.startService(i);
    }
}
