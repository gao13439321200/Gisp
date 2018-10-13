package com.giiisp.giiisp.dto;

import java.util.List;

public class HeBean extends BaseBean {
    private String isfollow;
    private HeVO userinfo;
    private List<HePaperTitleVO> papertitle;
    private List<HePaperTitleVO> spapertitle;
    private List<HeEduListVO> edulist;

    public String getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(String isfollow) {
        this.isfollow = isfollow;
    }

    public HeVO getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(HeVO userinfo) {
        this.userinfo = userinfo;
    }

    public List<HePaperTitleVO> getPapertitle() {
        return papertitle;
    }

    public void setPapertitle(List<HePaperTitleVO> papertitle) {
        this.papertitle = papertitle;
    }

    public List<HePaperTitleVO> getSpapertitle() {
        return spapertitle;
    }

    public void setSpapertitle(List<HePaperTitleVO> spapertitle) {
        this.spapertitle = spapertitle;
    }

    public List<HeEduListVO> getEdulist() {
        return edulist;
    }

    public void setEdulist(List<HeEduListVO> edulist) {
        this.edulist = edulist;
    }
}
