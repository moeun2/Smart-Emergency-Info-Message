package com.passta.a2ndproj.start.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.passta.a2ndproj.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_location extends RecyclerView.Adapter<Adapter_location.ViewHolder>{
    private ArrayList<String> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView location;


        public ViewHolder(View view) {
            super(view);
            this.location = (TextView) view.findViewById(R.id.id_locationitem);

        }
    }

    public Adapter_location(ArrayList<String> list) {
        this.mList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_location_list, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {

        viewholder.location.setText(mList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }


}
