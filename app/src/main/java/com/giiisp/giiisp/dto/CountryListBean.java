package com.giiisp.giiisp.dto;

import java.util.List;

public class CountryListBean extends BaseBean {
    private List<CountryVO> list;

    public List<CountryVO> getList() {
        return list;
    }

    public void setList(List<CountryVO> list) {
        this.list = list;
    }
}
