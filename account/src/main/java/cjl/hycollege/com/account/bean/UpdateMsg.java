package cjl.hycollege.com.account.bean;

/**
 * Created by Administrator on 2017/6/18.
 */

public class UpdateMsg {
    public String downUrl;
    public String updateDes;
    public String versionCode;
    public String versionName;

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getUpdateDes() {
        return updateDes;
    }

    public void setUpdateDes(String updateDes) {
        this.updateDes = updateDes;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return "UpdateMsg{" +
                "downUrl='" + downUrl + '\'' +
                ", updateDes='" + updateDes + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                '}';
    }
}
