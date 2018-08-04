package com.giiisp.giiisp.dto;

import java.util.List;

public class PaperInfoBean extends BaseBean {
    private String digest;
    private List<PaperInfoVO> imglist;

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
