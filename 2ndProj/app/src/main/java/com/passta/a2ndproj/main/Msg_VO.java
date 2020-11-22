package com.passta.a2ndproj.main;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.FilterDTO;

import java.util.ArrayList;

public class Msg_VO {
    private int id;
    private String day;
    private String time;
    private String msgText;
    private String senderLocation;
    private int level;
    private int circleImageViewId;
    //1. 동선 2. 발생방역 3. 안전수칙 4.재난상황 5. 경제금융
    private MsgCategoryPoint_VO msgCategoryPoint;
    private double totalMsgPoint;
    private MainActivity mainActivity;


    public Msg_VO(int id, String day, String time, String msgText, String senderLocation, MainActivity mainActivity, MsgCategoryPoint_VO msgCategoryPoint) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.msgText = msgText;
        this.senderLocation = senderLocation;
        this.mainActivity = mainActivity;
        this.msgCategoryPoint = new MsgCategoryPoint_VO(msgCategoryPoint.getCoronaRoute(),msgCategoryPoint.getCoronaUpbreak(),msgCategoryPoint.getCoronaSafetyRule(),
                msgCategoryPoint.getDisaster(),msgCategoryPoint.getEconomy());

        calculateTotalMsgPointAndLevel();
    }

    public void calculateTotalMsgPointAndLevel(){
        FilterDTO filterDTO = mainActivity.filterList.get(0);

        totalMsgPoint = (returnWeight(filterDTO.filter_1) * msgCategoryPoint.getCoronaRoute()) + (returnWeight(filterDTO.filter_2) * msgCategoryPoint.getCoronaUpbreak()) +
                (returnWeight(filterDTO.filter_3) * msgCategoryPoint.getCoronaSafetyRule()) + (returnWeight(filterDTO.filter_4) * msgCategoryPoint.getDisaster()) +
                (returnWeight(filterDTO.filter_5) * msgCategoryPoint.getEconomy());

        if(totalMsgPoint < 400)
            level = 1;
        else if(totalMsgPoint>= 400 && totalMsgPoint<650)
            level = 2;
        else
            level = 3;

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

    public double returnWeight(int input){
        switch (input){
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 5;
            case 4:
                return 9;
        }
        return 0;
    }

    public double getTotalMsgPoint() {
        return totalMsgPoint;
    }

    public MsgCategoryPoint_VO getMsgCategoryPoint() {
        return msgCategoryPoint;
    }

    public void setMsgCategoryPoint(MsgCategoryPoint_VO msgCategoryPoint) {
        this.msgCategoryPoint = msgCategoryPoint;
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
