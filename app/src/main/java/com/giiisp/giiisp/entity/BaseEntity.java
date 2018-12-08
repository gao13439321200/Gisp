package com.giiisp.giiisp.entity;

/**
 * Entity基类
 * Created by Thinkpad on 2017/5/3.
 */

public class BaseEntity {

    private int result;
    private String code;
    private int statusCode;
    private String info;
    private String uid;
    private String rid;
    private String rurl;
    private String firstUser ;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;



    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getNewUser() {
        return firstUser;
    }

    public void setNewUser(String newUser) {
        this.firstUser = newUser;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "result=" + result +
                ", code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", uid='" + uid + '\'' +
                ", firstUser='" + firstUser + '\'' +
                '}';
    }
}
