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

public class HashtagRecyclerViewAdapter extends RecyclerView.Adapter<HashtagRecyclerViewAdapter.HasgtagCircleViewHolder>{

    private View view;
    private Context context;
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

        return new HasgtagCircleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HasgtagCircleViewHolder viewHolder, int position) {
        viewHolder.name.setText("# " + arrayList.get(position).getHashtagText() );
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HasgtagCircleViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;

        public HasgtagCircleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name_item_hashtag_list);
        }
    }
}