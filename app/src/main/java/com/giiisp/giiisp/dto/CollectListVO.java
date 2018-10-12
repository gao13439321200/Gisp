package com.giiisp.giiisp.dto;

public class CollectListVO {


    /**
     * collectnum : 4
     * createtime : 2018-10-12 10:01:17
     * downloadnum : 0
     * id : 18a9d493-3de9-4dd2-aa4a-61a2c4744620
     * img : static/upload/attimg/5c3718fc5f1945e893578bc9e9480fcf.jpg
     * pid : 2a47ee54-6b77-4579-9e93-0c81a67dac8d
     * quiznum : 0
     * readnum : 164
     * title : 安卓视频
     * version : 2
     */

    private int collectnum;
    private String createtime;
    private int downloadnum;
    private String id;
    private String img;
    private String pid;
    private int quiznum;
    private int readnum;
    private String title;
    private String version;
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getCollectnum() {
        return collectnum;
    }

    public void setCollectnum(int collectnum) {
        this.collectnum = collectnum;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getDownloadnum() {
        return downloadnum;
    }

    public void setDownloadnum(int downloadnum) {
        this.downloadnum = downloadnum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getQuiznum() {
        return quiznum;
    }

    public void setQuiznum(int quiznum) {
        this.quiznum = quiznum;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
