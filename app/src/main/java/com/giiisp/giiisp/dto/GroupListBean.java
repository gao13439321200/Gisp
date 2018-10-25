package com.giiisp.giiisp.dto;

import java.util.List;

public class GroupListBean extends BaseBean {
    private List<GroupListVO> glist;

    public List<GroupListVO> getGlist() {
        return glist;
    }

    public void setGlist(List<GroupListVO> glist) {
        this.glist = glist;
    }
}
