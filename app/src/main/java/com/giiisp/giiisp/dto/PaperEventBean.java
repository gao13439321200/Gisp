package com.giiisp.giiisp.dto;

import java.util.List;

public class PaperEventBean extends BaseBean {
    private List<PaperEventVO> list;

    public List<PaperEventVO> getList() {
        return list;
    }

    public void setList(List<PaperEventVO> list) {
        this.list = list;
    }
}
