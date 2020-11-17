package com.passta.a2ndproj.main;

import java.util.ArrayList;

public class OneDayMsg_VO {
    private String day;
    private ArrayList<Msg_VO> arrayList;

    public OneDayMsg_VO(String day, ArrayList<Msg_VO> arrayList) {
        this.day = day;
        this.arrayList = arrayList;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<Msg_VO> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Msg_VO> arrayList) {
        this.arrayList = arrayList;
    }
}
