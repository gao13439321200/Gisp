package com.giiisp.giiisp.dto;

public class AppInfoVO {

    /**
     * packageName : 版本2
     * apkUrl : static/upload/app/android/88e2a95424404dbb904bcf1635ed32ba.apk
     * versionCode : 1.0.2
     * versionName : 版本2
     * detailDesc : 版本2
     * apkSize : .02MB
     */

    private String packageName;
    private String apkUrl;
    private String versionCode;
    private String versionName;
    private String detailDesc;
    private String apkSize;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
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

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }
}
