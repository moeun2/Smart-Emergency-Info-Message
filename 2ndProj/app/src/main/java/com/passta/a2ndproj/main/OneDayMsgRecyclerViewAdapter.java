package com.passta.a2ndproj.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.passta.a2ndproj.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OneDayMsgRecyclerViewAdapter extends RecyclerView.Adapter<OneDayMsgRecyclerViewAdapter.MsgDayViewHolder> {

    private View view;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<OneDayMsg_VO> arrayList;

    public OneDayMsgRecyclerViewAdapter(ArrayList<OneDayMsg_VO> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MsgDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_one_day_msg_main_list,parent,false);

        return new MsgDayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgDayViewHolder holder, int position) {
        holder.msgDayText.setText(arrayList.get(position).getDay());
        holder.recyclerView.setAdapter(new MsgRecyclerViewAdapter(arrayList.get(position).getArrayList()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class MsgDayViewHolder extends RecyclerView.ViewHolder {

        protected TextView msgDayText;
        protected RecyclerView recyclerView;

        public MsgDayViewHolder(@NonNull View itemView) {
            super(itemView);
            msgDayText = (TextView)itemView.findViewById(R.id.one_day_item_msg_day_list);
            recyclerView = itemView.findViewById(R.id.recyclerview_msg_one_day_list);
        }
    }
}
