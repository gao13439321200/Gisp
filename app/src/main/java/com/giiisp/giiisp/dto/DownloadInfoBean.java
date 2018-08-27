package com.giiisp.giiisp.dto;

import java.util.List;

public class DownloadInfoBean extends BaseBean {


    /**
     * id : fsdfasd44112ads
     * title :
     * version : 2完整 3摘要 4精华
     * language : 1中文 2英文
     * type : 1论文 2综述
     * showimg : 展示图地址
     * downtime : 2018-05-05
     */

    private String id;
    private String title;
    private String version;
    private String language;
    private String type;
    private String showimg;
    private String downtime;
    private List<DownloadImgInfoVO> pics;

    public List<DownloadImgInfoVO> getPics() {
        return pics;
    }

    public void setPics(List<DownloadImgInfoVO> pics) {
        this.pics = pics;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShowimg() {
        return showimg;
    }

    public void setShowimg(String showimg) {
        this.showimg = showimg;
    }

    public String getDowntime() {
        return downtime;
    }

    public void setDowntime(String downtime) {
        this.downtime = downtime;
    }
}
