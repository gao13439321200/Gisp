package com.giiisp.giiisp.dto;

public class PaperQaVO {


    /**
     * id : asdfas4545d4f5s
     * content : 内容
     * firstquiz :  是否是首问
     * createtime : 时间
     * parentid : 父问题id
     * isrecord : 是否录音
     * record : 录音地址
     * timing : 录音时长
     */

    private String id;
    private String content;
    private String firstquiz;
    private String createtime;
    private String parentid;
    private String isrecord;
    private String record;
    private String timing;

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

    public String getFirstquiz() {
        return firstquiz;
    }

    public void setFirstquiz(String firstquiz) {
        this.firstquiz = firstquiz;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getIsrecord() {
        return isrecord;
    }

    public void setIsrecord(String isrecord) {
        this.isrecord = isrecord;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
