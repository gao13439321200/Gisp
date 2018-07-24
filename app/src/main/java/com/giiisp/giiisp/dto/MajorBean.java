package com.giiisp.giiisp.dto;

import java.util.List;

public class MajorBean extends BaseBean {
    private List<MajorVO> majors;

    public List<MajorVO> getMajors() {
        return majors;
    }

    public void setMajors(List<MajorVO> majors) {
        this.majors = majors;
    }
}
