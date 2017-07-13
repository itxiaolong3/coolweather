package cjl.hycollege.com.coolweather.constant;

/**
 * Created by xiaolong on 2017/7/13.
 */

public class keyUtils {
    /**
     * 保存天气信息的key
     */
    public static String WEATHER = "weather";

    /**
     * 加载必应中的每一日背景图片的地址
     */
    public static String BGUrl = "http://guolin.tech/api/bing_pic";

    /**
     * 保存背景图片key
     */
    public static String BING_PIC = "bing_pic";

    /**
     * 保存更新频率key
     */
    public static String SAVARATE = "savaRate";

    /**
     * 是否后台更新的状态key
     */
    public static String SAVASTATE="savaState";

    /**
     * @param weatherId 传入的天气id
     * @return 返回"http://guolin.tech/api/weather?cityid="+weatherId+"&key=282e4f2f4b7f49de8f11a2d959150d32";
     */
    public static String weatherUrl(String weatherId) {
        return "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=282e4f2f4b7f49de8f11a2d959150d32";
    }
}
