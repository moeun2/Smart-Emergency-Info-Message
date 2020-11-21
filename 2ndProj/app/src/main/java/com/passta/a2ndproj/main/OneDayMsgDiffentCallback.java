package com.passta.a2ndproj.main;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class OneDayMsgDiffentCallback extends DiffUtil.Callback {

    private ArrayList<OneDayMsg_VO> oldArrayList;
    private ArrayList<OneDayMsg_VO> newArrayList;

    public OneDayMsgDiffentCallback(ArrayList<OneDayMsg_VO> oldArrayList, ArrayList<OneDayMsg_VO> newArrayList) {
        this.oldArrayList = oldArrayList;
        this.newArrayList = newArrayList;
    }

    @Override
    public int getOldListSize() {
        return oldArrayList.size();
    }

    @Override
    public int getNewListSize() {
        return newArrayList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        return (oldArrayList.get(oldItemPosition).getDay() == newArrayList.get(newItemPosition).getDay());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldArrayList.get(oldItemPosition).equals(newArrayList.get(newItemPosition))) ;
    }
}
