package com.passta.a2ndproj.start.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.passta.a2ndproj.data.CardVO;

import java.util.ArrayList;

public class GridAdapter_p extends BaseAdapter {

    Context context;
    ArrayList<CardVO> cards = new ArrayList<>();
    boolean visible = false;

    public GridAdapter_p(Context context, ArrayList<CardVO> cards){
        this.context = context;
        this.cards = cards;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = new CardGridItem_p(context);

            ((CardGridItem_p)view).setData(cards.get(position),visible);

        }
        return view;
    }

    public void show(boolean visible){
        this.visible = visible;
        this.notifyDataSetChanged();
    }
}
