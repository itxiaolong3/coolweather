package cjl.hycollege.com.account;

/**
 * Created by xiaolong on 2017/6/20.
 */

public class Mybean {
    public String time;
    public String money;
    public String des;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "Mybean{" +
                "time='" + time + '\'' +
                ", money='" + money + '\'' +
                ", des='" + des + '\'' +
                '}';
    }
}
