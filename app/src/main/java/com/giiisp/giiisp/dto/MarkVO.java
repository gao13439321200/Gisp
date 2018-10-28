package com.giiisp.giiisp.dto;

public class MarkVO {


    /**
     * id : ID
     * cname : 中文名称
     * ename : 英文名称
     */

    private String id;
    private String cname;
    private String ename;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
