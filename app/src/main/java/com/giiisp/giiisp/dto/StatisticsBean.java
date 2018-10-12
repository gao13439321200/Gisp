package com.giiisp.giiisp.dto;

import java.util.List;

public class StatisticsBean extends BaseBean {
    private List<String> x;
    private List<String> download;
    private List<String> collect;
    private List<String> time;
    private String ziji;
    private String bieren;

    public String getZiji() {
        return ziji;
    }

    public void setZiji(String ziji) {
        this.ziji = ziji;
    }

    public String getBieren() {
        return bieren;
    }

    public void setBieren(String bieren) {
        this.bieren = bieren;
    }

    public List<String> getX() {
        return x;
    }

    public void setX(List<String> x) {
        this.x = x;
    }

    public List<String> getDownload() {
        return download;
    }

    public void setDownload(List<String> download) {
        this.download = download;
    }

    public List<String> getCollect() {
        return collect;
    }

    public void setCollect(List<String> collect) {
        this.collect = collect;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}
