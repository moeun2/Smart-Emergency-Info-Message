package com.passta.a2ndproj.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.ArrayList;

public class OneDayMsgRecyclerViewAdapter extends RecyclerView.Adapter<OneDayMsgRecyclerViewAdapter.MsgDayViewHolder> {

    private View view;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<OneDayMsg_VO> arrayList;
    public ArrayList<MsgRecyclerViewAdapter> msgRecyclerViewAdapterArrayList;
    public MainActivity mainActivity;

    public OneDayMsgRecyclerViewAdapter(ArrayList<OneDayMsg_VO> arrayList, MainActivity mainActivity) {
        this.arrayList = arrayList;
        this.msgRecyclerViewAdapterArrayList = new ArrayList<>();
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MsgDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_one_day_msg_main_list, parent, false);

        return new MsgDayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgDayViewHolder holder, int position) {

        boolean hasSame = false;
        //어댑터의 사이즈가 0일경우 그냥 add
        if (msgRecyclerViewAdapterArrayList.size() == 0) {
            msgRecyclerViewAdapterArrayList.add(new MsgRecyclerViewAdapter(arrayList.get(position).getMsgArrayList()));
            holder.recyclerView.setAdapter(msgRecyclerViewAdapterArrayList.get(msgRecyclerViewAdapterArrayList.size() - 1));
        }
        //어댑터의 사이즈가 0이 아닐 경우 if문으로 검사.
        else {
            for (int i = 0; i < msgRecyclerViewAdapterArrayList.size(); i++) {
                if (arrayList.get(position).getDay().equals(msgRecyclerViewAdapterArrayList.get(i).adapterId)) {
                    hasSame = true;
                    msgRecyclerViewAdapterArrayList.get(i).setArrayList(arrayList.get(position).getMsgArrayList());
                    //msgRecyclerViewAdapterArrayList.get(i).updateMsgAdapter(arrayList.get(position).getMsgArrayList());
                    holder.recyclerView.setAdapter(msgRecyclerViewAdapterArrayList.get(i));

                    break;
                }
            }

            if (!hasSame) {
                msgRecyclerViewAdapterArrayList.add(new MsgRecyclerViewAdapter(arrayList.get(position).getMsgArrayList()));
                holder.recyclerView.setAdapter(msgRecyclerViewAdapterArrayList.get(msgRecyclerViewAdapterArrayList.size() - 1));
            }
        }
        //msgRecyclerViewAdapter = new MsgRecyclerViewAdapter(arrayList.get(position).getMsgArrayList());
        holder.msgDayText.setText(arrayList.get(position).getDay());
        //holder.recyclerView.setAdapter(msgRecyclerViewAdapterArrayList.get(msgRecyclerViewAdapterArrayList.size() - 1));
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
            msgDayText = (TextView) itemView.findViewById(R.id.one_day_item_msg_day_list);
            recyclerView = itemView.findViewById(R.id.recyclerview_msg_one_day_list);
            //recyclerView.setItemAnimator(null);
        }
    }

    public void updateAdapater(ArrayList<OneDayMsg_VO> newArrayList) {

        //자식 어댑터부터 갱신
        for (int i = 0; i < msgRecyclerViewAdapterArrayList.size(); i++) {
            for (int j = 0; j < newArrayList.size(); j++) {

                if (msgRecyclerViewAdapterArrayList.get(i).adapterId.equals(newArrayList.get(j).getDay())) {
                    msgRecyclerViewAdapterArrayList.get(i).updateMsgAdapter(
                            newArrayList.get(j).getMsgArrayList()
                    );
                    break;
                }
            }
        }

        OneDayMsgDiffentCallback diffentCallback = new OneDayMsgDiffentCallback(arrayList, newArrayList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffentCallback);

        arrayList.clear();
        arrayList.addAll(newArrayList);
        diffResult.dispatchUpdatesTo(OneDayMsgRecyclerViewAdapter.this);



    }
}
