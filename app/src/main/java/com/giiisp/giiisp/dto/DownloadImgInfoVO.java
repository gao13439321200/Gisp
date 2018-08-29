package com.giiisp.giiisp.dto;

public class DownloadImgInfoVO {

    private String  picid  = "";
    private String  picurl  = "";
    private String  psize  = "";
    private String  rid  = "";
    private String  rurl  = "";
    private String  rsize   = "";

    public String getPsize() {
        return psize;
    }

    public void setPsize(String psize) {
        this.psize = psize;
    }

    public String getRsize() {
        return rsize;
    }

    public void setRsize(String rsize) {
        this.rsize = rsize;
    }

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRurl() {
        return rurl;
    }

    public void setRurl(String rurl) {
        this.rurl = rurl;
    }
}
