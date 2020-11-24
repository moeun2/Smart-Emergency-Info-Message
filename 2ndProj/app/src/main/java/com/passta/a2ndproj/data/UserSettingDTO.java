package com.passta.a2ndproj.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserSetting")
public class UserSettingDTO {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "index")
    public int index;

    @ColumnInfo(name = "is_clicked_corona_route_hashtag")
    public boolean is_clicked_corona_route_hashtag;

    @ColumnInfo(name = "is_clicked_corona_upbreak_hashtag")
    public boolean is_clicked_corona_upbreak_hashtag;

    @ColumnInfo(name = "is_clicked_corona_safety_hashtag")
    public boolean is_clicked_corona_safety_hashtag;

    @ColumnInfo(name = "is_clicked_disaster_hashtag")
    public boolean is_clicked_corona_disaster_hashtag;

    @ColumnInfo(name = "is_clicked_economy_hashtag")
    public boolean is_clicked_economy_hashtag;

    @ColumnInfo(name = "is_clicked_lv1_hashtag")
    public boolean is_clicked_lv1_hashtag;

    @ColumnInfo(name = "is_clicked_lv2_hashtag")
    public boolean is_clicked_lv2_hashtag;

    @ColumnInfo(name = "is_clicked_lv3_hashtag")
    public boolean is_clicked_lv3_hashtag;

    public UserSettingDTO(boolean is_clicked_corona_route_hashtag, boolean is_clicked_corona_upbreak_hashtag, boolean is_clicked_corona_safety_hashtag, boolean is_clicked_corona_disaster_hashtag, boolean is_clicked_economy_hashtag, boolean is_clicked_lv1_hashtag, boolean is_clicked_lv2_hashtag, boolean is_clicked_lv3_hashtag) {
        index=0;
        this.is_clicked_corona_route_hashtag = is_clicked_corona_route_hashtag;
        this.is_clicked_corona_upbreak_hashtag = is_clicked_corona_upbreak_hashtag;
        this.is_clicked_corona_safety_hashtag = is_clicked_corona_safety_hashtag;
        this.is_clicked_corona_disaster_hashtag = is_clicked_corona_disaster_hashtag;
        this.is_clicked_economy_hashtag = is_clicked_economy_hashtag;
        this.is_clicked_lv1_hashtag = is_clicked_lv1_hashtag;
        this.is_clicked_lv2_hashtag = is_clicked_lv2_hashtag;
        this.is_clicked_lv3_hashtag = is_clicked_lv3_hashtag;
    }

    public boolean isIs_clicked_corona_route_hashtag() {
        return is_clicked_corona_route_hashtag;
    }

    public void setIs_clicked_corona_route_hashtag(boolean is_clicked_corona_route_hashtag) {
        this.is_clicked_corona_route_hashtag = is_clicked_corona_route_hashtag;
    }

    public boolean isIs_clicked_corona_upbreak_hashtag() {
        return is_clicked_corona_upbreak_hashtag;
    }

    public void setIs_clicked_corona_upbreak_hashtag(boolean is_clicked_corona_upbreak_hashtag) {
        this.is_clicked_corona_upbreak_hashtag = is_clicked_corona_upbreak_hashtag;
    }

    public boolean isIs_clicked_corona_safety_hashtag() {
        return is_clicked_corona_safety_hashtag;
    }

    public void setIs_clicked_corona_safety_hashtag(boolean is_clicked_corona_safety_hashtag) {
        this.is_clicked_corona_safety_hashtag = is_clicked_corona_safety_hashtag;
    }

    public boolean isIs_clicked_corona_disaster_hashtag() {
        return is_clicked_corona_disaster_hashtag;
    }

    public void setIs_clicked_corona_disaster_hashtag(boolean is_clicked_corona_disaster_hashtag) {
        this.is_clicked_corona_disaster_hashtag = is_clicked_corona_disaster_hashtag;
    }

    public boolean isIs_clicked_economy_hashtag() {
        return is_clicked_economy_hashtag;
    }

    public void setIs_clicked_economy_hashtag(boolean is_clicked_economy_hashtag) {
        this.is_clicked_economy_hashtag = is_clicked_economy_hashtag;
    }

    public boolean isIs_clicked_lv1_hashtag() {
        return is_clicked_lv1_hashtag;
    }

    public void setIs_clicked_lv1_hashtag(boolean is_clicked_lv1_hashtag) {
        this.is_clicked_lv1_hashtag = is_clicked_lv1_hashtag;
    }

    public boolean isIs_clicked_lv2_hashtag() {
        return is_clicked_lv2_hashtag;
    }

    public void setIs_clicked_lv2_hashtag(boolean is_clicked_lv2_hashtag) {
        this.is_clicked_lv2_hashtag = is_clicked_lv2_hashtag;
    }

    public boolean isIs_clicked_lv3_hashtag() {
        return is_clicked_lv3_hashtag;
    }

    public void setIs_clicked_lv3_hashtag(boolean is_clicked_lv3_hashtag) {
        this.is_clicked_lv3_hashtag = is_clicked_lv3_hashtag;
    }
}
