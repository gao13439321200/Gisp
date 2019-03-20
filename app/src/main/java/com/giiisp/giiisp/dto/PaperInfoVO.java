package com.giiisp.giiisp.dto;

public class PaperInfoVO {
    private String id;
    private String url;
    private String type;
    private int isCnFollow;
    private int isEnFollow;

    public int getIsCnFollow() {
        return isCnFollow;
    }

    public void setIsCnFollow(int isCnFollow) {
        this.isCnFollow = isCnFollow;
    }

    public int getIsEnFollow() {
        return isEnFollow;
    }

    public void setIsEnFollow(int isEnFollow) {
        this.isEnFollow = isEnFollow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
