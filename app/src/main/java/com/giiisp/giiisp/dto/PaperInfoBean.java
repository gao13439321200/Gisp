package com.giiisp.giiisp.dto;

import java.io.Serializable;
import java.util.List;

public class PaperInfoBean extends BaseBean implements Serializable {
    private String digest;
    private String id;
    private String version;
    private String cnstatus;
    private String enstatus;
    private String two;
    private String twoid;
    private String three;
    private String threeid;
    private String four;
    private String fourid;
    private String isfollow;
    private String downloadnum;
    private List<PaperInfoVO> imglist;

    public String getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(String isfollow) {
        this.isfollow = isfollow;
    }

    public String getDownloadnum() {
        return downloadnum;
    }

    public void setDownloadnum(String downloadnum) {
        this.downloadnum = downloadnum;
    }

    public String getTwoid() {
        return twoid;
    }

    public void setTwoid(String twoid) {
        this.twoid = twoid;
    }

    public String getThreeid() {
        return threeid;
    }

    public void setThreeid(String threeid) {
        this.threeid = threeid;
    }

    public String getFourid() {
        return fourid;
    }

    public void setFourid(String fourid) {
        this.fourid = fourid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCnstatus() {
        return cnstatus;
    }

    public void setCnstatus(String cnstatus) {
        this.cnstatus = cnstatus;
    }

    public String getEnstatus() {
        return enstatus;
    }

    public void setEnstatus(String enstatus) {
        this.enstatus = enstatus;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public List<PaperInfoVO> getImglist() {
        return imglist;
    }

    public void setImglist(List<PaperInfoVO> imglist) {
        this.imglist = imglist;
    }
}
