package com.giiisp.giiisp.dto;

public class FansVO {


    /**
     * id : 用户ID
     * name : 时间开始时间 秒
     * avatar : 头像
     * organization : 所属机构
     * isfollow : 1已关注 2为关注
     */

    private String id;
    private String name;
    private String avatar;
    private String organization;
    private String isfollow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(String isfollow) {
        this.isfollow = isfollow;
    }
}
