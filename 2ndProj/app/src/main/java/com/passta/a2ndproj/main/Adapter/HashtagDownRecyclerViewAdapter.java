package com.passta.a2ndproj.main.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.UserSettingDAO;
import com.passta.a2ndproj.data.UserSettingDTO;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HashtagDownRecyclerViewAdapter extends RecyclerView.Adapter<HashtagDownRecyclerViewAdapter.HasgtagCircleViewHolder> {

    private View view;
    protected Context context;
    private LayoutInflater layoutInflater;
    private TextView name;
    private MainActivity mainActivity;

    public HashtagDownRecyclerViewAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public HasgtagCircleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_hashtag_circle_list, parent, false);

        return new HasgtagCircleViewHolder(view, mainActivity);
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull HasgtagCircleViewHolder viewHolder, int position) {
        viewHolder.name.setText("#" + mainActivity.hashtagDownDataList.get(position).getHashtagText());
        viewHolder.circleImageView.setImageResource(mainActivity.hashtagDownDataList.get(position).getCircleImageViewId());// 클릭 돼 있는 경우

        // 클릭 돼 있는 경우
        if (mainActivity.hashtagDownDataList.get(position).isClicked()) {
            Typeface typeface = ResourcesCompat.getFont(context, R.font.nanumsquareb);
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
        return mainActivity.hashtagDownDataList.size();
    }

    public class HasgtagCircleViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected CircleImageView circleImageView;
        protected MainActivity mainActivity;

        public HasgtagCircleViewHolder(@NonNull View itemView, MainActivity mainActivity) {
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

                    String hashtagText = mainActivity.hashtagDownDataList.get(getAdapterPosition()).getHashtagText().replaceAll("\n", "");

                    // 클릭 돼 있는 경우
                    if (mainActivity.hashtagDownDataList.get(getAdapterPosition()).isClicked()) {

                        //꺼주기(글자색 바꾸기)
                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquarer);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.black)));
                        name.setTypeface(typeface);

                        if (hashtagText.equals("관심도1단계") || hashtagText.equals("관심도2단계") || hashtagText.equals("관심도3단계")) {
                            deleteInterestLevelItem(hashtagText);
                        }
                        else if(hashtagText.equals("(코로나)동선") || hashtagText.equals("(코로나)발생/방역") || hashtagText.equals("(코로나)안전수칙") || hashtagText.equals("재난/날씨") || hashtagText.equals("경제/금융")){
                            deleteCategoryItem(returnCategoryIndex(hashtagText));
                        }
                    }

                    // 클릭 안돼져있는 경우우김김진진우우
                    else {
                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquareeb);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.twitterBlue)));
                        name.setTypeface(typeface);

                        if (hashtagText.equals("관심도1단계") || hashtagText.equals("관심도2단계") || hashtagText.equals("관심도3단계")) {
                            addInterestLevelItem(hashtagText);
                        }
                        else if(hashtagText.equals("(코로나)동선") || hashtagText.equals("(코로나)발생/방역") || hashtagText.equals("(코로나)안전수칙") || hashtagText.equals("재난/날씨") || hashtagText.equals("경제/금융")){
                            addCategryItem(returnCategoryIndex(hashtagText));
                        }
                    }
                }
            });


        }

        //레벨에 따른 삭제 메소드
        public void deleteInterestLevelItem(String hashtagText) {

            int level = returnLevel(hashtagText);
            setUserSettingInterestLevel(level,false);
            updateHashtagSetting();
            //관심도에 따른 삭제
            for (int i = 0; i < mainActivity.oneDayMsgDataList.size(); i++) {

                int size = mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().size();

                for (int j = 0; j < size; j++) {

                    if (mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().get(j).getLevel() == level) {

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

        //카테고리에 따른 삭제 메소드
        public void deleteCategoryItem(int categoryIndex) {

            setUserSettingCategory(categoryIndex,false);
            updateHashtagSetting();

            //카테고리에 따른 삭제
            for (int i = 0; i < mainActivity.oneDayMsgDataList.size(); i++) {

                int size = mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().size();

                for (int j = 0; j < size; j++) {

                    if (mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().get(j).getCategroyIndex() == categoryIndex) {
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

        // 레벨에 따른 추가, 추가할때는 전체 데이터에서 다시 가지고오면서 해야한다.
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void addInterestLevelItem(String hashtagText) {

            int level = returnLevel(hashtagText);

            setUserSettingInterestLevel(level,true);
            updateHashtagSetting();
            mainActivity.classifyMsgData();
            mainActivity.createOneDayMsgDataList();

            mainActivity.oneDayMsgRecyclerViewAdapter.notifyDataSetChanged();
        }

        // 카테고리에 따른 추가 , 추가할때는 전체 데이터에서 다시 가지고오면서 해야한다.
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void addCategryItem(int categoryIndex) {

            setUserSettingCategory(categoryIndex,true);
            updateHashtagSetting();
            mainActivity.classifyMsgData();
            mainActivity.createOneDayMsgDataList();

            mainActivity.oneDayMsgRecyclerViewAdapter.notifyDataSetChanged();
        }

        public void setUserSettingInterestLevel(int level,boolean input){
            switch (level){
                case 1:
                    mainActivity.userSetting.setIs_clicked_lv1_hashtag(input);
                    mainActivity.hashtagDownDataList.get(5).setClicked(input);
                    break;
                case 2:
                    mainActivity.userSetting.setIs_clicked_lv2_hashtag(input);
                    mainActivity.hashtagDownDataList.get(6).setClicked(input);
                    break;
                case 3:
                    mainActivity.userSetting.setIs_clicked_lv3_hashtag(input);
                    mainActivity.hashtagDownDataList.get(7).setClicked(input);
                    break;
            }
        }

        public void setUserSettingCategory(int index,boolean input){
            switch (index){
                case 0:
                    mainActivity.userSetting.setIs_clicked_corona_route_hashtag(input);
                    mainActivity.hashtagDownDataList.get(0).setClicked(input);
                    break;
                case 1:
                    mainActivity.userSetting.setIs_clicked_corona_upbreak_hashtag(input);
                    mainActivity.hashtagDownDataList.get(1).setClicked(input);
                    break;
                case 2:
                    mainActivity.userSetting.setIs_clicked_corona_safety_hashtag(input);
                    mainActivity.hashtagDownDataList.get(2).setClicked(input);
                    break;
                case 3:
                    mainActivity.userSetting.setIs_clicked_corona_disaster_hashtag(input);
                    mainActivity.hashtagDownDataList.get(3).setClicked(input);
                    break;
                case 4:
                    mainActivity.userSetting.setIs_clicked_economy_hashtag(input);
                    mainActivity.hashtagDownDataList.get(4).setClicked(input);
                    break;
            }
        }


        public int returnLevel(String hashtagText) {
            switch (hashtagText) {
                case "관심도1단계":
                    return 1;
                case "관심도2단계":
                    return 2;
                case "관심도3단계":
                    return 3;
            }
            return 0;
        }

        public int returnCategoryIndex(String hashtagText){
            switch (hashtagText){
                case "(코로나)동선":
                    return 0;
                case "(코로나)발생/방역":
                    return 1;
                case "(코로나)안전수칙":
                    return 2;
                case "재난/날씨":
                    return 3;
                case "경제/금융":
                    return 4;
            }
            return 0;
        }
    }

    public void updateHashtagSetting(){
        UserSettingDTO temp = new UserSettingDTO(mainActivity.userSetting.is_clicked_corona_route_hashtag,mainActivity.userSetting.is_clicked_corona_upbreak_hashtag,
                mainActivity.userSetting.is_clicked_corona_safety_hashtag,mainActivity.userSetting.is_clicked_corona_disaster_hashtag,mainActivity.userSetting.is_clicked_economy_hashtag,
                mainActivity.userSetting.is_clicked_lv1_hashtag,mainActivity.userSetting.is_clicked_lv2_hashtag,mainActivity.userSetting.is_clicked_lv3_hashtag);
        new UpdateUserSettingDatabaseAsyncTask(mainActivity.db.userSettingDAO(),temp).execute();
    }

    public class UpdateUserSettingDatabaseAsyncTask extends AsyncTask<UserSettingDTO, Void,Void> {

        private UserSettingDAO userSettingDAO;
        private UserSettingDTO userSettingDTO;

        public UpdateUserSettingDatabaseAsyncTask(UserSettingDAO userSettingDAO, UserSettingDTO userSettingDTO) {
            this.userSettingDAO = userSettingDAO;
            this.userSettingDTO = userSettingDTO;
        }

        @Override
        protected Void doInBackground(UserSettingDTO... userSettingDTOS) {
            userSettingDAO.update(userSettingDTO);
            return null;
        }
    }

}