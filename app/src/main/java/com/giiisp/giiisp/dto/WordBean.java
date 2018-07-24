package com.giiisp.giiisp.dto;

import java.util.List;

public class WordBean extends BaseBean {
    private List<WordVO> alist;

    public List<WordVO> getAlist() {
        return alist;
    }

    public void setAlist(List<WordVO> alist) {
        this.alist = alist;
    }
}
