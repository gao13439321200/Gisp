package com.giiisp.giiisp.dto;

public class MyAnswerVO {


    /**
     * username : 提问者名字
     * avatar : 提问者头像
     * createtime : 提问时间
     * qid : 问题ID
     * quiz : 问题内容
     * qrecord : 问题录音
     * qtime : 问题录音时长
     * firstquiz :  是否是首问  1是2否
     * hasanswer : 是否回答 1是2否
     * ausername : 回答者姓名
     * answer : 回答内容
     * arecord : 回答录音
     * imgurl : 图片地址
     * atime : 回答录音时长
     */

    private String username;
    private String avatar;
    private String createtime;
    private String qid;
    private String quiz;
    private String qrecord;
    private String qtime;
    private String firstquiz;
    private String hasanswer;
    private String ausername;
    private String answer;
    private String arecord;
    private String imgurl;
    private String atime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getQrecord() {
        return qrecord;
    }

    public void setQrecord(String qrecord) {
        this.qrecord = qrecord;
    }

    public String getQtime() {
        return qtime;
    }

    public void setQtime(String qtime) {
        this.qtime = qtime;
    }

    public String getFirstquiz() {
        return firstquiz;
    }

    public void setFirstquiz(String firstquiz) {
        this.firstquiz = firstquiz;
    }

    public String getHasanswer() {
        return hasanswer;
    }

    public void setHasanswer(String hasanswer) {
        this.hasanswer = hasanswer;
    }

    public String getAusername() {
        return ausername;
    }

    public void setAusername(String ausername) {
        this.ausername = ausername;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getArecord() {
        return arecord;
    }

    public void setArecord(String arecord) {
        this.arecord = arecord;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }
}
