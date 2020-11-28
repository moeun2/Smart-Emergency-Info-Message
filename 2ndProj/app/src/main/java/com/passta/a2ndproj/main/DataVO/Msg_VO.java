package com.passta.a2ndproj.main.DataVO;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.FilterDTO;

public class Msg_VO {
    private int id;
    private String day;
    private String time;
    private String msgText;
    private String senderLocation;
    private int level;
    private int circleImageViewId;
    private MsgCategoryPoint_VO msgCategoryPoint;
    //index 0. 동선 1. 발생방역 2. 안전수칙 3.재난날씨 4. 경제금융
    private int categroyIndex;
    private double totalMsgPoint;
    private MainActivity mainActivity;
    private int filter1;
    private int filter2;
    private int filter3;
    private int filter4;
    private int filter5;
    private boolean isFirstStep;

    public Msg_VO(int id, String day, String time, String msgText, String senderLocation, MainActivity mainActivity, MsgCategoryPoint_VO msgCategoryPoint) {

        this.isFirstStep = false;
        this.id = id;
        this.day = day;
        this.time = time;
        this.msgText = msgText;
        this.senderLocation = senderLocation;
        this.mainActivity = mainActivity;
        this.msgCategoryPoint = new MsgCategoryPoint_VO(msgCategoryPoint.getCoronaRoute(),msgCategoryPoint.getCoronaUpbreak(),msgCategoryPoint.getCoronaSafetyRule(),
                msgCategoryPoint.getDisaster(),msgCategoryPoint.getEconomy());
        setCategroy();
        calculateTotalMsgPointAndLevel();
    }

    public Msg_VO(int id, String day, String time, String msgText, String senderLocation, MsgCategoryPoint_VO msgCategoryPoint, int filter1, int filter2 , int filter3 , int filter4 , int filter5) {

        this.isFirstStep = true;
        this.id = id;
        this.day = day;
        this.time = time;
        this.msgText = msgText;
        this.senderLocation = senderLocation;
        this.msgCategoryPoint = new MsgCategoryPoint_VO(msgCategoryPoint.getCoronaRoute(),msgCategoryPoint.getCoronaUpbreak(),msgCategoryPoint.getCoronaSafetyRule(),
                msgCategoryPoint.getDisaster(),msgCategoryPoint.getEconomy());

        this.filter1 = filter1;
        this.filter2 = filter2;
        this.filter3 = filter3;
        this.filter2 = filter4;
        this.filter3 = filter5;

        setCategroy();
        calculateTotalMsgPointAndLevel();
    }

    public void calculateTotalMsgPointAndLevel(){
        if(!isFirstStep){
        FilterDTO filterDTO = mainActivity.filterList.get(0);

        totalMsgPoint = (returnWeight(filterDTO.filter_1) * msgCategoryPoint.getCoronaRoute()) + (returnWeight(filterDTO.filter_2) * msgCategoryPoint.getCoronaUpbreak()) +
                (returnWeight(filterDTO.filter_3) * msgCategoryPoint.getCoronaSafetyRule()) + (returnWeight(filterDTO.filter_4) * msgCategoryPoint.getDisaster()) +
                (returnWeight(filterDTO.filter_5) * msgCategoryPoint.getEconomy());
        }else{
            totalMsgPoint = (returnWeight(filter1) * msgCategoryPoint.getCoronaRoute()) + (returnWeight(filter2) * msgCategoryPoint.getCoronaUpbreak()) +
                    (returnWeight(filter3) * msgCategoryPoint.getCoronaSafetyRule()) + (returnWeight(filter4) * msgCategoryPoint.getDisaster()) +
                    (returnWeight(filter5) * msgCategoryPoint.getEconomy());
        }


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

    public void setCategroy(){
        double[] cate = new double[]{msgCategoryPoint.getCoronaRoute(),msgCategoryPoint.getCoronaUpbreak(),msgCategoryPoint.getCoronaSafetyRule(),
                msgCategoryPoint.getDisaster(),msgCategoryPoint.getEconomy()
        };
        int maxIndex = 0;
        for(int i=0;i<5;i++){
            if(cate[maxIndex]<cate[i]){
                maxIndex = i;
            }
        }

        this.categroyIndex = maxIndex;
    }

    public int getCategroyIndex() {
        return categroyIndex;
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
