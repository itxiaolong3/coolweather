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

    //穿衣建议
    public Drsg drsg;

    //禽流感
    public Flu flu;

    //旅游
    public Trav trav;

    //紫外线
    public Uv uv;

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
    public class Drsg{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class Flu{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }
    public class Trav{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class Uv{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }
}
