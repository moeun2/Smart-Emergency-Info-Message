package com.passta.a2ndproj.main.Callback;

import androidx.recyclerview.widget.DiffUtil;

import com.passta.a2ndproj.main.DataVO.Msg_VO;

import java.util.ArrayList;

public class MsgDiffentCallback extends DiffUtil.Callback {

    private ArrayList<Msg_VO> oldArrayList;
    private ArrayList<Msg_VO> newArrayList;

    public MsgDiffentCallback(ArrayList<Msg_VO> oldArrayList, ArrayList<Msg_VO> newArrayList) {
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

        return oldArrayList.get(oldItemPosition).getId() == newArrayList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldArrayList.get(oldItemPosition).equals(newArrayList.get(newItemPosition));
    }
}
