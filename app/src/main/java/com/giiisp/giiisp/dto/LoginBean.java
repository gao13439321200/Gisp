package com.giiisp.giiisp.dto;

public class LoginBean extends BaseBean {
    private String id;
    private String isvip;
    private String emailauthen;

    public String getIsvip() {
        return isvip;
    }

    public void setIsvip(String isvip) {
        this.isvip = isvip;
    }

    public String getEmailauthen() {
        return emailauthen;
    }

    public void setEmailauthen(String emailauthen) {
        this.emailauthen = emailauthen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
