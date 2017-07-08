package cjl.hycollege.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省
 * Created by xiaolong on 2017/7/8.
 */

public class Province extends DataSupport{
    private int id;//省id
    private int provinceCode;//省代号
    private String provinceName;//省名字

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
