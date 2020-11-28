package com.passta.a2ndproj.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserList")
public class UserListDTO {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "tag")
    public String tag;

    @ColumnInfo(name = "location_si")
    public String location_si;
    @ColumnInfo(name = "location_gu")
    public String location_gu;

    @ColumnInfo(name = "img_number")
    public Integer img_number;

    @ColumnInfo(name = "is_hastag_ckecked")
    public boolean isHashtagChecked;

    public UserListDTO(@NonNull String tag, String location_si, String location_gu,Integer img_number,boolean isHashtagChecked) {
        this.tag = tag;
        this.location_si = location_si;
        this.location_gu = location_gu;
        this.img_number = img_number;
        this.isHashtagChecked = isHashtagChecked;
    }

    public Integer getImg_number() {
        return img_number;
    }

    public void setImg_number(Integer img_number) {
        this.img_number = img_number;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }

    public boolean isHashtagChecked() {
        return isHashtagChecked;
    }

    public void setHashtagChecked(boolean hashtagChecked) {
        isHashtagChecked = hashtagChecked;
    }

    public String getLocation_si() {
        return location_si;
    }

    public void setLocation_si(String location_si) {
        this.location_si = location_si;
    }

    public String getLocation_gu() {
        return location_gu;
    }

    public void setLocation_gu(String location_gu) {
        this.location_gu = location_gu;
    }
}
