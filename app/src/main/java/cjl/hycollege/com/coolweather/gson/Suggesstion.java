package cjl.hycollege.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaolong on 2017/7/9.
 */

public class Suggesstion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    //@SerializedName("sport")
    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
