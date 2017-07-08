package cjl.hycollege.com.coolweather.db;

import org.litepal.crud.DataSupport;

/** 市
 * Created by xiaolong on 2017/7/8.
 */

public class City extends DataSupport{
    private int id;
    private int cityCode;//城市代号
    private String cityName;//城市名称
    private int provinceId;//城市所属的省id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
