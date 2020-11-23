package com.passta.a2ndproj.start.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.start.dialogue.Dialogue_add_location;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterImageLocation extends RecyclerView.Adapter<AdapterImageLocation.AdapterImageLocationViewHolder> {

    private View view;
    protected Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Integer> locationList;
    public int selectedPosition = -1;

    public AdapterImageLocation(ArrayList<Integer> locationList) {
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public AdapterImageLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_location_circle_list, parent, false);

        return new AdapterImageLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterImageLocationViewHolder holder, int position) {

        //리사이클러뷰 업데이트에도 바뀌지않게하기위해
        holder.checkBox.setOnCheckedChangeListener(null);

        holder.circleImageView.setImageResource(locationList.get(position));
        holder.checkBox.setChecked(selectedPosition == position);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class AdapterImageLocationViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private CheckBox checkBox;

        public AdapterImageLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.img_location_item_hashtag_list);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_location_item_hashtag_list);
        }
    }
}
