package cjl.hycollege.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaolong on 2017/7/9.
 */

public class Forecast {

    public String date;

    @SerializedName("cond")
    public More more;

    @SerializedName("tmp")
    public Temperature temperature;
    public class More {
        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature {
        @SerializedName("max")
        public String max;
        @SerializedName("min")
        public String min;
    }
}
