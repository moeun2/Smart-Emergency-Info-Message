package com.passta.a2ndproj.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Filter")
public class FilterDTO {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idx")
    public int idx;

    @ColumnInfo(name = "filter_1")
    public int filter_1;
    @ColumnInfo(name = "filter_2")
    public int filter_2;
    @ColumnInfo(name = "filter_3")
    public int filter_3;
    @ColumnInfo(name = "filter_4")
    public int filter_4;
    @ColumnInfo(name = "filter_5")
    public int filter_5;

    public FilterDTO(@NonNull int idx,  int filter_1, int filter_2, int filter_3, int filter_4, int filter_5) {
        this.idx = idx;
        this.filter_1 = filter_1;
        this.filter_2 = filter_2;
        this.filter_3 = filter_3;
        this.filter_4 = filter_4;
        this.filter_5 = filter_5;
    }

    public int getFilter_1() {
        return filter_1;
    }

    public void setFilter_1(int filter_1) {
        this.filter_1 = filter_1;
    }

    public int getFilter_2() {
        return filter_2;
    }

    public void setFilter_2(int filter_2) {
        this.filter_2 = filter_2;
    }

    public int getFilter_3() {
        return filter_3;
    }

    public void setFilter_3(int filter_3) {
        this.filter_3 = filter_3;
    }

    public int getFilter_4() {
        return filter_4;
    }

    public void setFilter_4(int filter_4) {
        this.filter_4 = filter_4;
    }

    public int getFilter_5() {
        return filter_5;
    }

    public void setFilter_5(int filter_5) {
        this.filter_5 = filter_5;
    }
}
