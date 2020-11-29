package com.passta.a2ndproj.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Msg")
public class MsgDTO {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "day")
    private String day;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "msg_text")
    private String msgText;

    @ColumnInfo(name = "location")
    private String senderLocation;

    @ColumnInfo(name = "level")
    private int level;

    @ColumnInfo(name = "img_id")
    private int circleImageViewId;

    @ColumnInfo(name = "route_point")
    private double routeCatePoint;

    @ColumnInfo(name = "upbreak_point")
    private double upbreakCatePoint;

    @ColumnInfo(name = "safety_point")
    private double safetyCatePoint;

    @ColumnInfo(name = "disaster_point")
    private double disasterCatePoint;

    @ColumnInfo(name = "economy_point")
    private double economyCatePoint;

    @ColumnInfo(name = "total_point")
    private double totalMsgPoint;

    @ColumnInfo(name = "category_index")
    private int categroyIndex;

    @ColumnInfo(name = "location_si")
    private String senderLocation_si;


    @ColumnInfo(name = "location_gu")
    private String senderLocation_gu;

    public MsgDTO( String day, String time, String msgText, String senderLocation, int level, int circleImageViewId,
                  double routeCatePoint, double upbreakCatePoint, double safetyCatePoint, double disasterCatePoint, double economyCatePoint,
                  double totalMsgPoint, int categroyIndex) {

        this.day = day;
        this.time = time;
        this.msgText = msgText;
        this.senderLocation = senderLocation;
        this.level = level;
        this.circleImageViewId = circleImageViewId;
        this.routeCatePoint = routeCatePoint;
        this.upbreakCatePoint = upbreakCatePoint;
        this.safetyCatePoint = safetyCatePoint;
        this.disasterCatePoint = disasterCatePoint;
        this.economyCatePoint = economyCatePoint;
        this.totalMsgPoint = totalMsgPoint;
        this.categroyIndex = categroyIndex;
        this.senderLocation_si = senderLocation.split(" ")[0];
        this.senderLocation_gu = senderLocation.split(" ")[1];
    }

    public String getSenderLocation_si() {
        return senderLocation_si;
    }

    public void setSenderLocation_si(String senderLocation_si) {
        this.senderLocation_si = senderLocation_si;
    }

    public String getSenderLocation_gu() {
        return senderLocation_gu;
    }

    public void setSenderLocation_gu(String senderLocation_gu) {
        this.senderLocation_gu = senderLocation_gu;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCircleImageViewId() {
        return circleImageViewId;
    }

    public void setCircleImageViewId(int circleImageViewId) {
        this.circleImageViewId = circleImageViewId;
    }

    public double getRouteCatePoint() {
        return routeCatePoint;
    }

    public void setRouteCatePoint(double routeCatePoint) {
        this.routeCatePoint = routeCatePoint;
    }

    public double getUpbreakCatePoint() {
        return upbreakCatePoint;
    }

    public void setUpbreakCatePoint(double upbreakCatePoint) {
        this.upbreakCatePoint = upbreakCatePoint;
    }

    public double getSafetyCatePoint() {
        return safetyCatePoint;
    }

    public void setSafetyCatePoint(double safetyCatePoint) {
        this.safetyCatePoint = safetyCatePoint;
    }

    public double getDisasterCatePoint() {
        return disasterCatePoint;
    }

    public void setDisasterCatePoint(double disasterCatePoint) {
        this.disasterCatePoint = disasterCatePoint;
    }

    public double getEconomyCatePoint() {
        return economyCatePoint;
    }

    public void setEconomyCatePoint(double economyCatePoint) {
        this.economyCatePoint = economyCatePoint;
    }

    public double getTotalMsgPoint() {
        return totalMsgPoint;
    }

    public void setTotalMsgPoint(double totalMsgPoint) {
        this.totalMsgPoint = totalMsgPoint;
    }

    public int getCategroyIndex() {
        return categroyIndex;
    }

    public void setCategroyIndex(int categroyIndex) {
        this.categroyIndex = categroyIndex;
    }
}
