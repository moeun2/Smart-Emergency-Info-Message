package com.passta.a2ndproj.main;

import java.util.ArrayList;

public class OneDayMsg_VO {
    private String day;
    private ArrayList<Msg_VO> msgArrayList;

    public OneDayMsg_VO(String day, ArrayList<Msg_VO> arrayList) {
        this.day = day;
        this.msgArrayList = arrayList;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<Msg_VO> getMsgArrayList() {
        return msgArrayList;
    }

    public void setMsgArrayList(ArrayList<Msg_VO> msgArrayList) {
        this.msgArrayList = msgArrayList;
    }
}
