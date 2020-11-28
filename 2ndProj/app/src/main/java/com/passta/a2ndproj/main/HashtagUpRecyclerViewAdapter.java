package com.passta.a2ndproj.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.network.RetrofitClient;
import com.passta.a2ndproj.network.ServiceApi;
import com.passta.a2ndproj.start.dialogue.Dialogue_add_location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            Typeface typeface = ResourcesCompat.getFont(context, R.font.nanumsquareeb);
            viewHolder.name.setTextColor(Color.parseColor(context.getString(R.color.twitterBlue)));
            viewHolder.name.setTypeface(typeface);
        } else {
            Typeface typeface = ResourcesCompat.getFont(context, R.font.nanumsquarer);
            viewHolder.name.setTextColor(Color.parseColor(context.getString(R.color.black)));
            viewHolder.name.setTypeface(typeface);
        }
    }

    @Override
    public int getItemCount() {
        return mainActivity.hashtagUpDataList.size();
    }

    public class HashtagUpRecyclerViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected CircleImageView circleImageView;
        protected MainActivity mainActivity;

        @SuppressLint("ResourceType")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public HashtagUpRecyclerViewHolder(@NonNull View itemView, MainActivity mainActivity) {
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
                    if (getAdapterPosition() == 0) {
                        Intent intent = new Intent(mainActivity.getApplicationContext(), Dialogue_add_location.class);
                        intent.putExtra("type", "main");
                        mainActivity.startActivityForResult(intent, 1003);
                        return;
                    }

                    if(getAdapterPosition() == 1){
                        return;
                    }

                    String hashtagText = mainActivity.hashtagUpDataList.get(getAdapterPosition()).getHashtagText().replaceAll("\n", "");
                    String hashtagLocation = mainActivity.userList.get(getAdapterPosition()-2).getLocation_si() + " " +  mainActivity.userList.get(getAdapterPosition()-2).getLocation_gu();

                    // 클릭 돼 있는 경우
                    if (mainActivity.hashtagUpDataList.get(getAdapterPosition()).isClicked()) {

                        if (mainActivity.calculateUpHashtagClickedNumber() == 1) {
                            Toast.makeText(context, "수신 지역은 반드시 한개 이상 할당 돼 있어야 합니다.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        deleteLocationMsgItem(hashtagText,hashtagLocation,getAdapterPosition());

                        //꺼주기(글자색 바꾸기)
                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquarer);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.black)));
                        name.setTypeface(typeface);
                    }


                    // 클릭 안돼져있는 경우우
                    else {
                        addLocationItem(hashtagText,getAdapterPosition());

                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquareeb);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.twitterBlue)));
                        name.setTypeface(typeface);
                    }

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (getAdapterPosition() != 0) {
                        CheckDeleteLocation checkDeleteLocation = new CheckDeleteLocation(mainActivity, getAdapterPosition());
                    }

                    return true;
                }
            });
        }
    }

    // 장소에 따른 추가 , 추가할때는 전체 데이터에서 다시 가지고오면서 해야한다.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addLocationItem(String hashtagText,int position) {

        mainActivity.hashtagUpDataList.get(position).setClicked(true);
        new UpdateUserListDatabaseAsyncTask(mainActivity.db.userListDAO(),hashtagText,true).execute();
        mainActivity.classifyMsgData();
        mainActivity.createOneDayMsgDataList();

        mainActivity.oneDayMsgRecyclerViewAdapter.notifyDataSetChanged();
    }

    //장소에 따른 삭제 메소드
    public void deleteLocationMsgItem(String hashtagText,String location,int position) {

        mainActivity.hashtagUpDataList.get(position).setClicked(false);
        new UpdateUserListDatabaseAsyncTask(mainActivity.db.userListDAO(),hashtagText,false).execute();

        //장소에 따른 삭제
        for (int i = 0; i < mainActivity.oneDayMsgDataList.size(); i++) {

            int size = mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().size();

            for (int j = 0; j < size; j++) {

                if (mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().get(j).getSenderLocation().equals(location) ||
                        (mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().get(j).getSenderLocation().split(" ")[0].equals(
                                location.split(" ")[0]) && (mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().
                                        get(j).getSenderLocation().split(" ")[1].equals("전체"))
                        )) {
                    mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().remove(j);
                    j--;
                    size--;
                }
            }
        }

        //메세지 하나도 없을 경우 날짜 자체를 삭제
        int size = mainActivity.oneDayMsgDataList.size();
        for (int i = 0; i < size; i++) {
            if (mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().size() == 0) {
                mainActivity.oneDayMsgDataList.remove(i);
                size--;
                i--;
            }
        }

        mainActivity.oneDayMsgRecyclerViewAdapter.notifyDataSetChanged();
    }

    public class UpdateUserListDatabaseAsyncTask extends AsyncTask<UserListDTO, Void , Void>{

        private UserListDAO userListDAO;
        private String tag;
        private boolean isChecked;

        public UpdateUserListDatabaseAsyncTask(UserListDAO userListDAO, String tag, boolean isChecked) {
            this.userListDAO = userListDAO;
            this.tag = tag;
            this.isChecked = isChecked;
        }

        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {
            userListDAO.updateHastagChecked(isChecked,tag);
            return null;
        }
    }

}
