package com.passta.a2ndproj.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.passta.a2ndproj.R;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HashtagRecyclerViewAdapter extends RecyclerView.Adapter<HashtagRecyclerViewAdapter.HasgtagCircleViewHolder>{

    private View view;
    protected Context context;
    private LayoutInflater layoutInflater;
    private TextView name;
    private ArrayList<Hashtag_VO> arrayList;

    public HashtagRecyclerViewAdapter(ArrayList<Hashtag_VO> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HasgtagCircleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_hashtag_circle_list,parent,false);

        return new HasgtagCircleViewHolder(view,arrayList);
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull HasgtagCircleViewHolder viewHolder, int position) {
        viewHolder.name.setText("#" + arrayList.get(position).getHashtagText() );
        viewHolder.circleImageView.setImageResource(arrayList.get(position).getCircleImageViewId());// 클릭 돼 있는 경우

        // 클릭 돼 있는 경우
        if(arrayList.get(position).isClicked()){
            Typeface typeface = context.getResources().getFont(R.font.nanumsquareeb);
            viewHolder.name.setTextColor(Color.parseColor(context.getString(R.color.twitterBlue)));
            viewHolder.name.setTypeface(typeface);
        }
        else{
            Typeface typeface = context.getResources().getFont(R.font.nanumsquarer);
            viewHolder.name.setTextColor(Color.parseColor(context.getString(R.color.black)));
            viewHolder.name.setTypeface(typeface);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HasgtagCircleViewHolder extends RecyclerView.ViewHolder {

        private ArrayList<Hashtag_VO> arrayList;
        protected TextView name;
        protected CircleImageView circleImageView;

        public HasgtagCircleViewHolder(@NonNull View itemView,ArrayList<Hashtag_VO> arrayList) {
            super(itemView);
            this.arrayList = arrayList;
            this.name = (TextView) itemView.findViewById(R.id.name_item_hashtag_list);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.circle_item_hashtag_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint({"ResourceAsColor", "ResourceType"})
                @Override
                public void onClick(View view) {

                    // 클릭 돼 있는 경우
                    if(arrayList.get(getAdapterPosition()).isClicked()){
                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquarer);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.black)));
                        name.setTypeface(typeface);
                        arrayList.get(getAdapterPosition()).setClicked(false);

                    }

                    else{
                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquareeb);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.twitterBlue)));
                        name.setTypeface(typeface);
                        arrayList.get(getAdapterPosition()).setClicked(true);
                    }
                }
            });
        }
    }
}