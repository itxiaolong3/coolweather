package cjl.hycollege.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaolong on 2017/7/9.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
