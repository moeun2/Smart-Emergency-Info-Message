package com.passta.a2ndproj.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        return mainActivity.hashtagDownDataList.size();
    }

    public static class HasgtagCircleViewHolder extends RecyclerView.ViewHolder {

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
                        mainActivity.hashtagDownDataList.get(getAdapterPosition()).setClicked(false);

                        if (hashtagText.equals("관심도1단계") || hashtagText.equals("관심도2단계") || hashtagText.equals("관심도3단계")) {
                            deleteInterestLevelItem(hashtagText);
                        }
                    }


                    // 클릭 안돼져있는 경우우
                    else {
                        Typeface typeface = itemView.getContext().getResources().getFont(R.font.nanumsquareeb);
                        name.setTextColor(Color.parseColor(itemView.getContext().getString(R.color.twitterBlue)));
                        name.setTypeface(typeface);
                        mainActivity.hashtagDownDataList.get(getAdapterPosition()).setClicked(true);

                        if (hashtagText.equals("관심도1단계") || hashtagText.equals("관심도2단계") || hashtagText.equals("관심도3단계")) {
                            addInterestLevelItem(hashtagText);
                        }
                    }


                }
            });


        }

        //레벨에 따른 삭제 메소드
        public void deleteInterestLevelItem(String hashtagText) {

            int level = returnLevel(hashtagText);

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void addInterestLevelItem(String hashtagText) {

            int level = returnLevel(hashtagText);
            //ArrayList<OneDayMsg_VO> newOneDayArrayList = new ArrayList<> (mainActivity.oneDayMsgDataList);
            ArrayList<OneDayMsg_VO> newOneDayArrayList = new ArrayList<> (mainActivity.oneDayMsgDataList.size());

            for(int i=0;i<mainActivity.oneDayMsgDataList.size();i++){
                ArrayList<Msg_VO> tempList = new ArrayList<>();

                for(int j=0;j<mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().size();j++){

                    Msg_VO oldVo = mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().get(j);
                    Msg_VO tempVo = new Msg_VO(oldVo.getId(),oldVo.getDay(),oldVo.getTime(),oldVo.getMsgText(),oldVo.getSenderLocation(),oldVo.getLevel());
                    tempList.add(tempVo);
                }

                newOneDayArrayList.add(new OneDayMsg_VO(mainActivity.oneDayMsgDataList.get(i).getDay(),tempList));
            }

            for (int totalDataNume = 0; totalDataNume < mainActivity.msgDataList.size(); totalDataNume++) {

                boolean hasDayList = false;

                // 클릭한 레벨에서
                if (level == mainActivity.msgDataList.get(totalDataNume).getLevel()) {

                    //그 날짜의 oneDayMsgDataList 가 이미 있는지 부터 검사.
                    for (int dayListNum = 0; dayListNum < newOneDayArrayList.size(); dayListNum++) {

                        // 그 day list가 이미 있다면 -> 시간에 따른 정렬 필요
                        if ((mainActivity.msgDataList.get(totalDataNume).getDay()).equals(newOneDayArrayList.get(dayListNum).getDay())) {
                            newOneDayArrayList.get(dayListNum).getMsgArrayList().add(mainActivity.msgDataList.get(totalDataNume));
                            hasDayList = true;
                            break;
                        }
                    }

                    // 그 dayList가 없을 경우, 만들어주면서 들어간다. -> 시간에 따른 정렬 필요.
                    if (!hasDayList) {
                        ArrayList<Msg_VO> newList = new ArrayList<>();
                        newList.add(mainActivity.msgDataList.get(totalDataNume));
                        newOneDayArrayList.add(new OneDayMsg_VO(mainActivity.msgDataList.get(totalDataNume).getDay(), newList));
                    }
                }
            }

            newOneDayArrayList = mainActivity.sortByDay(newOneDayArrayList);
            newOneDayArrayList = mainActivity.sortByTime(newOneDayArrayList);

            mainActivity.oneDayMsgRecyclerViewAdapter.updateAdapater(newOneDayArrayList);


        }

        public int returnLevel(String hashtagText) {
            int level = 0;

            switch (hashtagText) {
                case "관심도1단계":
                    level = 1;
                    break;
                case "관심도2단계":
                    level = 2;
                    break;
                case "관심도3단계":
                    level = 3;
                    break;
            }
            return level;
        }

    }
}