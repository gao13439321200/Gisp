package com.giiisp.giiisp.dto;

public class PaperEventVO {
    private String type;
    private String time;
    private String x;
    private String y;
    private String timgid;

    public String getTimgid() {
        return timgid;
    }

    public void setTimgid(String timgid) {
        this.timgid = timgid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}
