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

import cjl.hycollege.com.coolweather.gson.Weather;
import cjl.hycollege.com.coolweather.util.HttpUtil;
import cjl.hycollege.com.coolweather.util.JsonUtilty;
import cjl.hycollege.com.coolweather.util.LogUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        //间隔触发器
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anhour = 8 * 60 * 60 * 1000;//8小时
        long triggerTime = SystemClock.elapsedRealtime() + anhour;//触发时间
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        LogUtil.d("TAG","启动服务onstartcommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 更新天气
     */
    private void updateWeather() {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
        String weather_String = spref.getString("weather", null);
        if (weather_String != null) {
            Weather weather = JsonUtilty.handlWeatherResponse(weather_String);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=282e4f2f4b7f49de8f11a2d959150d32";
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
                        editor.putString("weather", responseWeather_String);
                        editor.apply();
                    }
                }
            });
        }


    }

    //加载必应中的每一日背景图片
    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }
        });
    }
}
