package com.passta.a2ndproj.main;

public class Msg_VO {
    private String time;
    private String msgText;
    private String senderLocation;
    private int circleImageViewId;

    public Msg_VO(String time, String msgText, String senderLocation, int circleImageViewId) {
        this.time = time;
        this.msgText = msgText;
        this.senderLocation = senderLocation;
        this.circleImageViewId = circleImageViewId;
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
}
