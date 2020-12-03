package com.passta.a2ndproj;

public class CoronaVO {

    String date;
    String decide_cnt;
    String clear_cnt;
    String exam_cnt;
    String death_cnt;

    public CoronaVO(String decide_cnt, String clear_cnt, String exam_cnt, String death_cnt,String date) {
        this.decide_cnt = decide_cnt;
        this.clear_cnt = clear_cnt;
        this.exam_cnt = exam_cnt;
        this.death_cnt = death_cnt;
        this.date = date;
    }

    public String getDecide_cnt() {
        return decide_cnt;
    }

    public void setDecide_cnt(String decide_cnt) {
        this.decide_cnt = decide_cnt;
    }

    public String getClear_cnt() {
        return clear_cnt;
    }

    public void setClear_cnt(String clear_cnt) {
        this.clear_cnt = clear_cnt;
    }

    public String getExam_cnt() {
        return exam_cnt;
    }

    public void setExam_cnt(String exam_cnt) {
        this.exam_cnt = exam_cnt;
    }

    public String getDeath_cnt() {
        return death_cnt;
    }

    public void setDeath_cnt(String death_cnt) {
        this.death_cnt = death_cnt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
