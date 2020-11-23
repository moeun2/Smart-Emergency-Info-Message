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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterImageLocation extends RecyclerView.Adapter<AdapterImageLocation.AdapterImageLocationViewHolder> {

    private View view;
    protected Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Integer> imgIdList;
    private Integer imgId;

    public AdapterImageLocation(ArrayList<Integer> imgIdList) {
        this.imgIdList = imgIdList;
    }

    @NonNull
    @Override
    public AdapterImageLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_location_circle_list, parent, false);
        imgId = 0;

        return new AdapterImageLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterImageLocationViewHolder holder, int position) {
        holder.circleImageView.setImageResource(imgIdList.get(position));
        imgId = imgIdList.get(position);

        holder.checkImageViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return imgIdList.size();
    }

    public class AdapterImageLocationViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView circleImageView;
        private ImageView checkImageViwe;
        public AdapterImageLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.img_location_item_hashtag_list);
            this.checkImageViwe = (ImageView) itemView.findViewById(R.id.check_img__location_iem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkImageViwe.setVisibility(View.VISIBLE);

                }
            });
        }



    }
}
