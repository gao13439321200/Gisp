package com.giiisp.giiisp.dto;

public class MsgNewVO {


    /**
     * id : 消息ID
     * content : 消息内容
     * type : 消息类型  2注册消息 3邀请加入团组消息
     * createtime : 时间
     * status : 状态 1未读 2已读 3删除
     * username : 发送者姓名
     * userid : 发送者ID
     * userphoto : 发送者头像
     */

    private String id;
    private String content;
    private String type;
    private String createtime;
    private String status;
    private String username;
    private String userid;
    private String userphoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }
}
