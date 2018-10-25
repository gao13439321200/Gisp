package com.giiisp.giiisp.dto;

public class GroupMemberInfo {


    /**
     * userid : 成员ID
     * username : 姓名
     * userphoto : 头像
     * createtime : 创建时间
     * job : 职务
     * detail : 介绍
     * status : 1在团队2退出团组
     */

    private String userid;
    private String username;
    private String userphoto;
    private String createtime;
    private String job;
    private String detail;
    private String status;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
