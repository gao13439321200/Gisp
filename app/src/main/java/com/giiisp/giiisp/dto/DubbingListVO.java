package com.giiisp.giiisp.dto;

public class DubbingListVO {

    /**
     * id : 论文ID
     * title : 论文标题
     * cnfinish : 中文是否完成 1是 2否
     * enfinish : 英文文是否完成 1是 2否
     * cnper : 中文完成比例
     * enper : 英文完成比例
     * cnstatus : 中文发布状态 1发布 2未发布
     * enstatus : 英文发布状态 1发布 2未发布
     */

    private String id;
    private String title;
    private String cnfinish;
    private String enfinish;
    private String cnper;
    private String enper;
    private String cnstatus;
    private String enstatus;
    private String version;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCnfinish() {
        return cnfinish;
    }

    public void setCnfinish(String cnfinish) {
        this.cnfinish = cnfinish;
    }

    public String getEnfinish() {
        return enfinish;
    }

    public void setEnfinish(String enfinish) {
        this.enfinish = enfinish;
    }

    public String getCnper() {
        return cnper;
    }

    public void setCnper(String cnper) {
        this.cnper = cnper;
    }

    public String getEnper() {
        return enper;
    }

    public void setEnper(String enper) {
        this.enper = enper;
    }

    public String getCnstatus() {
        return cnstatus;
    }

    public void setCnstatus(String cnstatus) {
        this.cnstatus = cnstatus;
    }

    public String getEnstatus() {
        return enstatus;
    }

    public void setEnstatus(String enstatus) {
        this.enstatus = enstatus;
    }
}
