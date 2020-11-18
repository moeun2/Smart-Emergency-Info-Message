package com.passta.a2ndproj.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HashtagUpRecyclerViewAdapter extends RecyclerView.Adapter<HashtagUpRecyclerViewAdapter.HashtagUpRecyclerViewHolder> {

    private View view;
    protected Context context;
    private LayoutInflater layoutInflater;
    private MainActivity mainActivity;

    public HashtagUpRecyclerViewAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public HashtagUpRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_hashtag_circle_list, parent, false);

        return new HashtagUpRecyclerViewHolder(view, mainActivity);
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull HashtagUpRecyclerViewHolder viewHolder, int position) {
        viewHolder.name.setText("#" + mainActivity.hashtagUpDataList.get(position).getHashtagText());
        viewHolder.circleImageView.setImageResource(mainActivity.hashtagUpDataList.get(position).getCircleImageViewId());// 클릭 돼 있는 경우

        // 클릭 돼 있는 경우
        if (mainActivity.hashtagUpDataList.get(position).isClicked()) {
            Typeface typeface = context.getResources().getFont(R.font.nanumsquareeb);
            viewHolder.name.setTextColor(Color.parseColor(context.getString(R.color.twitterBlue)));
            viewHolder.name.setTypeface(typeface);
        } else {
            Typeface typeface = context.getResources().getFont(R.font.nanumsquarer);
            viewHolder.name.setTextColor(Color.parseColor(context.getString(R.color.black)));
            viewHolder.name.setTypeface(typeface);
        }
    }

    @Override
    public int getItemCount() {
        return mainActivity.hashtagUpDataList.size();
    }

    public static class HashtagUpRecyclerViewHolder extends RecyclerView.ViewHolder{

        protected TextView name;
        protected CircleImageView circleImageView;
        protected MainActivity mainActivity;

        public HashtagUpRecyclerViewHolder(@NonNull View itemView,MainActivity mainActivity) {
            super(itemView);
            this.mainActivity = mainActivity;
            this.name = (TextView) itemView.findViewById(R.id.name_item_hashtag_list);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.circle_item_hashtag_list);

            //각각 해시태크 아이템 마다 클릭 리스너
            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint({"ResourceAsColor", "ResourceType"})
                @Override
                public void onClick(View view) {

                    String hashtagText = mainActivity.hashtagUpDataList.get(getAdapterPosition()).getHashtagText().replaceAll("\n", "");

                    // 클릭 돼 있는 경우
                    if (mainActivity.hashtagUpDataList.get(getAdapterPosition()).isClicked()) {

                        //꺼주기(글자색 바꾸기)
                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquarer);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.black)));
                        name.setTypeface(typeface);
                        mainActivity.hashtagUpDataList.get(getAdapterPosition()).setClicked(false);
                    }


                    // 클릭 안돼져있는 경우우
                    else {
                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquareeb);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.twitterBlue)));
                        name.setTypeface(typeface);
                        mainActivity.hashtagUpDataList.get(getAdapterPosition()).setClicked(true);
                    }

                }
            });
        }
    }
}
