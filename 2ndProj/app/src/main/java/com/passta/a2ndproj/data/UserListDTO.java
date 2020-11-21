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

    public UserListDTO(@NonNull String tag, String location_si, String location_gu) {
        this.tag = tag;
        this.location_si = location_si;
        this.location_gu = location_gu;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
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
