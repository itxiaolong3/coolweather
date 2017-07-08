package cjl.hycollege.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省
 * Created by xiaolong on 2017/7/8.
 */

public class Province extends DataSupport{
    private int id;//省id
    private String provinceCode;//省代号
    private int provinceName;//省名字

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(int provinceName) {
        this.provinceName = provinceName;
    }
}
