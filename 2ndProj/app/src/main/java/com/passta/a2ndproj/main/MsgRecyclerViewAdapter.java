package com.passta.a2ndproj.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.passta.a2ndproj.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MsgRecyclerViewAdapter extends RecyclerView.Adapter<MsgRecyclerViewAdapter.MsgViewHolder>  {

    private View view;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Msg_VO> arrayList;

    public MsgRecyclerViewAdapter(ArrayList<Msg_VO> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MsgRecyclerViewAdapter.MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_msg_main_list,parent,false);

        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgRecyclerViewAdapter.MsgViewHolder holder, int position) {

        String time = returnTimeWithKorean(arrayList.get(position).getTime());

        holder.msgText.setText(arrayList.get(position).getMsgText());
        holder.time.setText(time.substring(0,time.indexOf("분") + 1));
        holder.senderLocation.setText(arrayList.get(position).getSenderLocation());
        holder.circleImageView.setImageResource(arrayList.get(position).getCircleImageViewId());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public String returnTimeWithKorean(String time) {

        int hour = Integer.parseInt(time.substring(0, 2));
        int min = Integer.parseInt(time.substring(3, 5));
        int sec = Integer.parseInt(time.substring(6, 8));

        if (hour == 0)
            return "오전 12시 " + min + "분 " + sec + "초";

        if (hour == 12)
            return "오후 12시 " + min + "분 " + sec + "초";

        if (hour > 12) {
            hour = hour - 12;
            return "오후 " + hour + "시 " + min + "분 " + sec + "초";
        }

        return "오전 " + hour + "시 " + min + "분 " + sec + "초";
    }

    public static class MsgViewHolder extends RecyclerView.ViewHolder {

        protected TextView msgText;
        protected TextView senderLocation;
        protected TextView time;
        protected ImageView circleImageView;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            this.msgText = (TextView) itemView.findViewById(R.id.msg_item_msg_list);
            this.senderLocation = (TextView) itemView.findViewById(R.id.sender_locattion_item_msg_list);
            this.time = (TextView) itemView.findViewById(R.id.time_item_msg_list);
            this.circleImageView = (ImageView) itemView.findViewById(R.id.color_item_msg_list);

        }
    }
}
