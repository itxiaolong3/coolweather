package cjl.hycollege.com.coolweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cjl.hycollege.com.coolweather.R;
import cjl.hycollege.com.coolweather.constant.keyUtils;
import cjl.hycollege.com.coolweather.gson.Forecast;
import cjl.hycollege.com.coolweather.gson.Weather;
import cjl.hycollege.com.coolweather.selfview.PickerView;
import cjl.hycollege.com.coolweather.selfview.ToggleViews;
import cjl.hycollege.com.coolweather.service.AutoUpdateService;
import cjl.hycollege.com.coolweather.util.HttpUtil;
import cjl.hycollege.com.coolweather.util.JsonUtilty;
import cjl.hycollege.com.coolweather.util.LogUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    int img[] = new int[]{
            R.drawable.ic100, R.drawable.ic101
            , R.drawable.ic102, R.drawable.ic103
            , R.drawable.ic104
            , R.drawable.ic202, R.drawable.ic205
            , R.drawable.ic208, R.drawable.ic300
            , R.drawable.ic301, R.drawable.ic302
            , R.drawable.ic303, R.drawable.ic304
            , R.drawable.ic305, R.drawable.ic306
            , R.drawable.ic307, R.drawable.ic308
            , R.drawable.ic309, R.drawable.ic310
            , R.drawable.ic311, R.drawable.ic400
            , R.drawable.ic401, R.drawable.ic402
            , R.drawable.ic403, R.drawable.ic404
            , R.drawable.ic501, R.drawable.ic999

    };

    //背景图片
    private ImageView bingPicImg, cond_img;
    //展示天气信息
    private ScrollView weathearLayout;

    //城市、更新时间、程度、天气情况
    private TextView titleCity, titleUpdateTime, degreeText, weatherInfoText;

    //预报信息
    private LinearLayout forecastLayout;

    //AQI、pm2.5、舒适度、洗车、运动
    private TextView aqiText, pm25Text, comfortText, carWashText, sportText;
    //下拉刷新
    public SwipeRefreshLayout swipe_refresh;

    //加入碎片
    public DrawerLayout drawerLayout;
    private Button nav_bt, settingbt, trueBt, falseBt;
    ;
    //记录天气id
    private String mWeatherId;
    //弹出设置对话框的view
    private View view;
    private LinearLayout LinePrike;
    private PickerView pickerView;
    //获取默认的频率
    private int getSelectPostion;
    //定时刷新界面
    private Handler mhandler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
            String weatherString = prefs.getString(keyUtils.WEATHER, null);
            if (weatherString != null) {
                Weather weather = JsonUtilty.handlWeatherResponse(weatherString);
                if (weather != null) {
                    showWeatherInfo(weather);
                    //Log.d("TAG","5000自动更新");
                }
                mhandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initView();
        mhandler.postDelayed(runnable, 500);
        //开启服务
        Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
        startService(intent);

    }

    private void initView() {
        weathearLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        //添加天气图标
        cond_img = (ImageView) findViewById(R.id.img_cond_icon);
        //下拉刷新
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeResources(R.color.colorPrimary);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString(keyUtils.WEATHER, null);
        //final String weather_Id;
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = JsonUtilty.handlWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            Log.d("TAG", "有缓存");
            showWeatherInfo(weather);
            //requestWeather(mWeatherId,"刚刚刷新",true);
        } else {
            //没有缓存就查找服务器信息
            mWeatherId = getIntent().getStringExtra("weather_id");
            //请求数据时先把ScrollView隐藏
            weathearLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId, "刚刚刷新", true);
        }

        //添加背景图片
        bingPicImg = (ImageView) findViewById(R.id.bing_bg_img);
        String bingpic = prefs.getString(keyUtils.BING_PIC, null);
        if (bingpic != null) {
            Glide.with(this).load(bingpic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        //下拉刷新

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Log.d("TAG", "刷新传入的id=" + weather_Id);
                //String getWeatherid = prefs.getString("weatherid", null);
                requestWeather(mWeatherId, "已刷新", true);
                //Toast.makeText(WeatherActivity.this, "更新完毕", Toast.LENGTH_SHORT).show();
            }
        });
        //加入手动切换城市
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_bt = (Button) findViewById(R.id.nav_button);
        nav_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        settingbt = (Button) findViewById(R.id.setting_button);
        settingbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingDalong();
            }
        });
    }

    //显示设置对话框
    private void showSettingDalong() {
        //保存配置
        final SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(WeatherActivity.this).edit();

        //自定义设置弹出框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        view = View.inflate(this, R.layout.setupdate, null);
        dialog.setView(view);
        dialog.show();
        LinePrike = (LinearLayout) view.findViewById(R.id.Rela_Picke_view);
        pickerView = (PickerView) view.findViewById(R.id.picke_view);
        List<String> data = new ArrayList<String>();
        for (int i = 1; i < 8; i++) {
            data.add("0" + i);
        }
        pickerView.setData(data);
        getSelectPostion = pickerView.getmCurrentSelected(data) + 3;
        //选择2-8
        final SharedPreferences getprefs = PreferenceManager.getDefaultSharedPreferences(this);
        int getsavaRate = getprefs.getInt(keyUtils.SAVARATE, 4);
        //自动滚动到已设置的频率
        pickerView.setSelected(getsavaRate);
        LogUtil.d("TAG", "获取默认频率为：" + getsavaRate);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                LogUtil.d("TAG", "设置频率为：" + text);
                getSelectPostion = Integer.parseInt(text);
                LogUtil.d("TAG", "再次默认频率为：" + getSelectPostion);
                //保存设置频率
                editor.putInt(keyUtils.SAVARATE, getSelectPostion);
                //editor.apply();
            }
        });

        ToggleViews toggleView = (ToggleViews) view.findViewById(R.id.bt_toggleview);
        toggleView.setOnSwitchStateUpdateLinstener(new ToggleViews.OnSwitchStateUpdateLinster() {
            @Override
            public void onStateUpdate(boolean state) {
                editor.putBoolean(keyUtils.SAVASTATE, state);
                settingRote(state);
                LogUtil.d("TAG", "保存好的状态=" + state);
                Toast.makeText(getApplicationContext(), "设置为：" + (state ? "开" : "关"), Toast.LENGTH_SHORT).show();
                //editor.apply();
            }
        });
        trueBt = (Button) view.findViewById(R.id.true_bt);
        falseBt = (Button) view.findViewById(R.id.false_bt);
        falseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        trueBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.apply();
                //服务是否再次开启
                if (getprefs.getBoolean(keyUtils.SAVASTATE, false)) {
                    Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
                    startService(intent);
                }
                dialog.dismiss();
            }
        });

        //默认是自动打开自动更新的
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean getPrfState = prefs.getBoolean(keyUtils.SAVASTATE, true);
        LogUtil.d("TAG", "当前状态=" + getPrfState);
        toggleView.setSwichState(getPrfState);
        settingRote(getPrfState);
    }

    //如果设置了自动更新则显示设置更新频率，否则不显示
    private void settingRote(boolean state) {
        if (state) {
            LinePrike.setVisibility(View.VISIBLE);
        } else {
            LinePrike.setVisibility(View.GONE);
        }
    }

    //加载必应中的每一日背景图片
    private void loadBingPic() {
        String requestBingPic = keyUtils.BGUrl;
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString(keyUtils.BING_PIC, bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息
     *
     * @param weatherId 天气id
     */
    public void requestWeather(String weatherId, final String showTostText, final boolean ifshowTost) {
        //key是自己在 和风天气网申请的
        Log.d("TAG", "requestWeather获取的weatherId=" + weatherId);
        String weatherUrl = keyUtils.weatherUrl(weatherId);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = JsonUtilty.handlWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString(keyUtils.WEATHER, responseText);
                            //获取天气id
                            mWeatherId = weather.basic.weatherId;
                            editor.apply();
                            showWeatherInfo(weather);
                            if (ifshowTost) {
                                Toast.makeText(WeatherActivity.this, showTostText, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败！",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipe_refresh.setRefreshing(false);

                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败！",
                                Toast.LENGTH_SHORT).show();
                        swipe_refresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 显示天气信息
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        //LogUtil.d("TAG", "获取城市名称" + cityName);
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        //Log.d("WeatherActivity", "获取更新时间" + updateTime);
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText("更新于" + updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        //获取图标id
        //String getCondIconId = weather.now.more.pic_code;
        int getCondIcomId = Integer.parseInt(weather.now.more.pic_code);
        LogUtil.d("TAG", "get到的图标id=" + getCondIcomId);
        getCondIcon(getCondIcomId,cond_img);

        forecastLayout.removeAllViews();
        //预告布局嵌套有子view在里面，所以需要获取子view对象再继续获取控件
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dataText = (TextView) view.findViewById(R.id.data_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            ImageView condIcon= (ImageView) view.findViewById(R.id.img_cond_forecast_icon);
            //Log.d("TAG","预报日期="+forecast.date);
            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max + "℃");
            minText.setText(forecast.temperature.min + "℃");
            getCondIcon(Integer.parseInt(forecast.more.condCode),condIcon);
            forecastLayout.addView(view);

        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        // Log.d("TAG", "舒适度=" + weather.suggestion.comfort);
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        if (weather != null && "ok".equals(weather.status)) {
            weathearLayout.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(WeatherActivity.this, "获取天气信息失败！",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void getCondIcon(int getCondIcomId,ImageView condview) {
        if (getCondIcomId >= 100 && getCondIcomId < 200) {
            condview.setImageResource(img[getCondIcomId - 100]);
        } else if (getCondIcomId >= 200 && getCondIcomId < 300) {
            switch (getCondIcomId) {
                case 202:
                    condview.setImageResource(R.drawable.ic202);
                    break;
                case 205:
                    condview.setImageResource(R.drawable.ic205);
                    break;
                case 208:
                    condview.setImageResource(R.drawable.ic208);
                    break;
            }
        } else if (getCondIcomId >= 300 && getCondIcomId < 312) {
            condview.setImageResource(img[getCondIcomId - 292]);
        } else if (getCondIcomId == 501) {
            condview.setImageResource(R.drawable.ic501);
        } else {
            condview.setImageResource(R.drawable.ic999);
        }
    }

    @Override
    protected void onDestroy() {
        mhandler.removeCallbacks(runnable); //停止刷新
        super.onDestroy();
    }
}
