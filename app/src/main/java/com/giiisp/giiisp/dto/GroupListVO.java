package com.giiisp.giiisp.dto;

public class GroupListVO {


    /**
     * id : id
     * title : 标题
     * detail : 介绍
     * createtime : 创建时间
     * status : 1在团队2退出团组
     */

    private String id;
    private String title;
    private String detail;
    private String createtime;
    private String status;

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
