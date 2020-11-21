package com.passta.a2ndproj.start.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.UserListDTO;

import java.util.List;

public class Adapter_page2 extends RecyclerView.Adapter<Adapter_page2.ViewHolder>{

    private List<UserListDTO> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView location;
        protected TextView tag;


        public ViewHolder(View view) {
            super(view);
            this.location = (TextView) view.findViewById(R.id.location);
            this.tag =(TextView) view.findViewById(R.id.tag);


        }
    }

    public Adapter_page2(List<UserListDTO> list) {
        this.mList = list;
    }


    @NonNull
    @Override
    public Adapter_page2.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_page2_list, viewGroup, false);

        Adapter_page2.ViewHolder viewHolder = new Adapter_page2.ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Adapter_page2.ViewHolder viewholder, int position) {

        viewholder.location.setText(mList.get(position).getLocation_si() +" "+ mList.get(position).getLocation_gu());
        viewholder.tag.setText(mList.get(position).getTag());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
