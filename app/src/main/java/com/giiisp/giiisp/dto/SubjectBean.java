package com.giiisp.giiisp.dto;

import java.util.List;

public class SubjectBean extends BaseBean {
    private List<SubjectVO> majors;

    public List<SubjectVO> getMajors() {
        return majors;
    }

    public void setMajors(List<SubjectVO> majors) {
        this.majors = majors;
    }
}
