package com.giiisp.giiisp.dto;

public class BaseBean {
    /**
     * statusCode : 1
     * message : 登录成功
     * id : 8vgf54-7xc87f-897sdr-cvwcc2
     */

    private int statusCode;
    private String message;
    private String rid;
    private String rurl;

    public String getRurl() {
        return rurl;
    }

    public void setRurl(String rurl) {
        this.rurl = rurl;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
