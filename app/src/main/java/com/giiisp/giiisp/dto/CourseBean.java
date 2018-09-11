package com.giiisp.giiisp.dto;

import java.util.List;

public class CourseBean extends BaseBean {
    private List<CourseVO> list;

    public List<CourseVO> getList() {
        return list;
    }

    public void setList(List<CourseVO> list) {
        this.list = list;
    }
}
