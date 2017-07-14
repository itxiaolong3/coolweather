package cjl.hycollege.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaolong on 2017/7/9.
 */

public class AQI {
    @SerializedName("city")
    public AQICity city;

    public class AQICity{
        @SerializedName("aqi")
        public String aqi;

        @SerializedName("pm25")
        public String pm25;
        @SerializedName("qlty")
        public String qurity;
    }
}
