package com.giiisp.giiisp.dto;

import java.io.Serializable;

public class EditInfoVo implements Serializable{


    /**
     * id : ID
     * ucname : 大学中文名字
     * uename : 大学英文名字
     * mcname : 专业中文名字
     * mename : 专业英文名字
     * ecname : 学历中文名字
     * eename : 学历英文名字
     * timestart : 开始时间
     * timeend : 结束时间
     * edubackground : 学位
     */

    private String id;
    private String unid;
    private String umid;
    private String eid;
    private String cid;
    private String ucname;
    private String uename;
    private String mcname;
    private String mename;
    private String ecname;
    private String eename;
    private String timestart;
    private String timeend;
    private String edubackground;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUnid() {
        return unid;
    }

    public void setUnid(String unid) {
        this.unid = unid;
    }

    public String getUmid() {
        return umid;
    }

    public void setUmid(String umid) {
        this.umid = umid;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUcname() {
        return ucname;
    }

    public void setUcname(String ucname) {
        this.ucname = ucname;
    }

    public String getUename() {
        return uename;
    }

    public void setUename(String uename) {
        this.uename = uename;
    }

    public String getMcname() {
        return mcname;
    }

    public void setMcname(String mcname) {
        this.mcname = mcname;
    }

    public String getMename() {
        return mename;
    }

    public void setMename(String mename) {
        this.mename = mename;
    }

    public String getEcname() {
        return ecname;
    }

    public void setEcname(String ecname) {
        this.ecname = ecname;
    }

    public String getEename() {
        return eename;
    }

    public void setEename(String eename) {
        this.eename = eename;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getEdubackground() {
        return edubackground;
    }

    public void setEdubackground(String edubackground) {
        this.edubackground = edubackground;
    }
}
