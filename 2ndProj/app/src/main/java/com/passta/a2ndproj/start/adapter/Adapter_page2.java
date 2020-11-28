package com.passta.a2ndproj.start.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.main.CheckDeleteLocation;
import com.passta.a2ndproj.start.activity.Page2Activity;
import com.passta.a2ndproj.start.dialogue.CheckDeleteLocationInPage2;

import java.util.List;

public class Adapter_page2 extends RecyclerView.Adapter<Adapter_page2.ViewHolder>{

//    private List<UserListDTO> mList;
    private Page2Activity page2Activity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView location;
        protected TextView tag;
        protected Button deleteButton;

        public ViewHolder(View view) {
            super(view);
            this.location = (TextView) view.findViewById(R.id.location);
            this.tag =(TextView) view.findViewById(R.id.tag);
            this.deleteButton = (Button)view.findViewById(R.id.delete_button_page2_item);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckDeleteLocationInPage2 checkDeleteLocationInPage2 = new CheckDeleteLocationInPage2(page2Activity,getAdapterPosition(),
                            page2Activity.list.get(getAdapterPosition()).getTag());
                }
            });

        }
    }

    public Adapter_page2(Page2Activity page2Activity) {
//        this.mList = list;
        this.page2Activity = page2Activity;
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

        viewholder.location.setText(page2Activity.list.get(position).getLocation_si() +" "+ page2Activity.list.get(position).getLocation_gu());
        viewholder.tag.setText(page2Activity.list.get(position).getTag());
    }

    @Override
    public int getItemCount() {
        return (null != page2Activity.list ? page2Activity.list.size() : 0);
    }

}
