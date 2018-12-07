package com.giiisp.giiisp.dto;

public class DubbingVO {

    /**
     * pid : 论文ID
     * pcid : 图片ID
     * url : 图片地址
     * language : 1中文 2英文
     * rid : 录音ID
     */

    private String pid;
    private String pcid;
    private String url;
    private String language;
    private String rid;
    private String rurl;

    public String getRurl() {
        return rurl;
    }

    public void setRurl(String rurl) {
        this.rurl = rurl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPcid() {
        return pcid;
    }

    public void setPcid(String pcid) {
        this.pcid = pcid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
