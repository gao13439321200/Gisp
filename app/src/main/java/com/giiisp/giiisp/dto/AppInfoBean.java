package com.giiisp.giiisp.dto;

public class AppInfoBean extends BaseBean {
    /**
     * {
     * "packageName": "diyiban",  //名称
     * "apkUrl": "url" ，  //安装包地址
     * "versionCode": "1.0",  //版本号
     * "versionName": "1.0",  //版本名称
     * "detailDesc": "描述",  //版本描述
     * "apkSize": "100M",  //安装包大小
     * “isupdate”:”1”//1强制更新 2 不强制
     * }
     */
    private String packageName;
    private String apkUrl;
    private String versionCode;
    private String versionName;
    private String detailDesc;
    private String apkSize;
    private String isupdate;

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

    public String getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(String isupdate) {
        this.isupdate = isupdate;
    }
}
