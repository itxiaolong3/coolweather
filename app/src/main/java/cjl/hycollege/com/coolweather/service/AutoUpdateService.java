package cjl.hycollege.com.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import java.io.IOException;

import cjl.hycollege.com.coolweather.constant.keyUtils;
import cjl.hycollege.com.coolweather.gson.Weather;
import cjl.hycollege.com.coolweather.util.HttpUtil;
import cjl.hycollege.com.coolweather.util.JsonUtilty;
import cjl.hycollege.com.coolweather.util.LogUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
                updateBingPic();
            }
        }).start();
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
        int savaRate = spref.getInt(keyUtils.SAVARATE, 1);
        LogUtil.d("TAG", "服务中的定时器设置值改为：" + savaRate);
        //间隔触发器
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anhour = savaRate * 60 * 60 * 1000;//savaRate小时
        long triggerTime = SystemClock.elapsedRealtime() + anhour;//触发时间
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        LogUtil.d("TAG", "服务调用成功");
        return super.onStartCommand(intent, flags, startId);
    }

    /*
     **
     * 更新天气
     */
    private void updateWeather() {
        LogUtil.d("TAG", "服务里的更新天气");
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
        String weather_String = spref.getString(keyUtils.WEATHER, null);
        if (weather_String != null) {
            Weather weather = JsonUtilty.handlWeatherResponse(weather_String);
            String weatherId = weather.basic.weatherId;
            //请求天气链接
            String weatherUrl = keyUtils.weatherUrl(weatherId);

            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseWeather_String = response.body().string();
                    Weather weather1 = JsonUtilty.handlWeatherResponse(responseWeather_String);
                    if (weather1 != null && "ok".equals(weather1.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString(keyUtils.WEATHER, responseWeather_String);
                        editor.apply();
                    }
                }
            });
        }


    }

    //加载必应中的每一日背景图片
    private void updateBingPic() {
        String requestBingPic = keyUtils.BGUrl;
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString(keyUtils.BING_PIC, bingPic);
                editor.apply();
            }
        });
    }

}
