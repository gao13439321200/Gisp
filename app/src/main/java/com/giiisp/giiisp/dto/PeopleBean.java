package com.giiisp.giiisp.dto;

import java.util.List;

public class PeopleBean extends BaseBean {
    private List<PeopleVO> ulist;

    public List<PeopleVO> getUlist() {
        return ulist;
    }

    public void setUlist(List<PeopleVO> ulist) {
        this.ulist = ulist;
    }
}
