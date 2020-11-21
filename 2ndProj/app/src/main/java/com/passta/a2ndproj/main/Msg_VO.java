package com.passta.a2ndproj.main;

import com.passta.a2ndproj.R;

public class Msg_VO {
    private int id;
    private String day;
    private String time;
    private String msgText;
    private String senderLocation;
    private int level;
    private int circleImageViewId;

    public Msg_VO(int id,String day,String time, String msgText, String senderLocation, int level) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.msgText = msgText;
        this.senderLocation = senderLocation;
        this.level = level;

        // level에 따른 이미지 값 다르게 주기
        switch (level){
            case 1 :
                this.circleImageViewId = R.drawable.level1;
                break;

            case 2 :
                this.circleImageViewId = R.drawable.level2;
                break;

            case 3 :
                this.circleImageViewId = R.drawable.level3;
                break;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getSenderLocation() {
        return senderLocation;
    }

    public void setSenderLocation(String senderLocation) {
        this.senderLocation = senderLocation;
    }

    public int getCircleImageViewId() {
        return circleImageViewId;
    }

    public void setCircleImageViewId(int circleImageViewId) {
        this.circleImageViewId = circleImageViewId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
