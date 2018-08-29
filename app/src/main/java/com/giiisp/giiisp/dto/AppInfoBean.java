package com.giiisp.giiisp.dto;

public class AppInfoBean extends BaseBean {
    private String isupdate;
    private AppInfoVO AppInfo;

    public AppInfoVO getAppInfo() {
        return AppInfo;
    }

    public void setAppInfo(AppInfoVO appInfo) {
        AppInfo = appInfo;
    }

    public String getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(String isupdate) {
        this.isupdate = isupdate;
    }

}
