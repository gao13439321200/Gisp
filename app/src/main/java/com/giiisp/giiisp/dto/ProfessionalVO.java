package com.giiisp.giiisp.dto;

public class ProfessionalVO {


    /**
     * id : ID
     * cid : 大学所在的国家ID
     * cname : 中文名
     * ename : 英文名
     * abbreviation : 简称
     */

    private String id;
    private String cid;
    private String cname;
    private String ename;
    private String abbreviation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
