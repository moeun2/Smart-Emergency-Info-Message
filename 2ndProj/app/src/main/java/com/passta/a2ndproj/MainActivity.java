package com.passta.a2ndproj;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.FilterDAO;
import com.passta.a2ndproj.data.FilterDTO;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.data.UserSettingDAO;
import com.passta.a2ndproj.data.UserSettingDTO;
import com.passta.a2ndproj.main.HashtagDownRecyclerViewAdapter;
import com.passta.a2ndproj.main.HashtagUpRecyclerViewAdapter;
import com.passta.a2ndproj.main.Hashtag_VO;
import com.passta.a2ndproj.main.MsgCategoryPoint_VO;
import com.passta.a2ndproj.main.Msg_VO;
import com.passta.a2ndproj.main.OneDayMsgRecyclerViewAdapter;
import com.passta.a2ndproj.main.OneDayMsg_VO;
import com.passta.a2ndproj.main.Seekbar;
import com.passta.a2ndproj.start.activity.Page2Activity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView hashtagDownRecyclerView;
    private RecyclerView hashtagUpRecyclerView;
    private RecyclerView msgRecyclerView;
    public ArrayList<Hashtag_VO> hashtagDownDataList;
    public ArrayList<Hashtag_VO> hashtagUpDataList;
    public ArrayList<Msg_VO> msgDataList;
    public ArrayList<Msg_VO> classifiedMsgDataList;
    public ArrayList<OneDayMsg_VO> oneDayMsgDataList;
    public HashtagDownRecyclerViewAdapter hashtagDownRecyclerViewAdapter;
    public HashtagUpRecyclerViewAdapter hashtagUpRecyclerViewAdapter;
    public OneDayMsgRecyclerViewAdapter oneDayMsgRecyclerViewAdapter;
    public SlidingUpPanelLayout slidingUpPanelLayout;
    public IndicatorSeekBar seekbar1;
    public IndicatorSeekBar seekbar2;
    public IndicatorSeekBar seekbar3;
    public IndicatorSeekBar seekbar4;
    public IndicatorSeekBar seekbar5;
    public AppDatabase db;
    public List<FilterDTO> filterList;
    public List<UserListDTO> userList;
    public UserSettingDTO userSetting;
    public String insertedLocationName;
    public String insertedLocation_si;
    public String insertedLocation_gu;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();

        hashtagDownRecyclerView = findViewById(R.id.recyclerview_down_main_hashtag);
        hashtagUpRecyclerView = findViewById(R.id.recyclerview_up_main_hashtag);
        msgRecyclerView = findViewById(R.id.recyclerview_main_msg);
        slidingUpPanelLayout = findViewById(R.id.sliding_panel_main_activity);
        seekbar1 = (IndicatorSeekBar) findViewById(R.id.seekbar1_main_activity);
        seekbar2 = (IndicatorSeekBar) findViewById(R.id.seekbar2_main_activity);
        seekbar3 = (IndicatorSeekBar) findViewById(R.id.seekbar3_main_activity);
        seekbar4 = (IndicatorSeekBar) findViewById(R.id.seekbar4_main_activity);
        seekbar5 = (IndicatorSeekBar) findViewById(R.id.seekbar5_main_activity);
        filterList = new ArrayList<>();
        userList = new ArrayList<>();

        //db생성
        db = AppDatabase.getInstance(this);
        new FilterDatabaseAsyncTask(db.filterDAO()).execute();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void start() {

        //seekbar 너무 길어져서 클래스로 뺌
        Seekbar seekbar = new Seekbar(this);
        seekbar.setSeekbar();

        //예시데이터
        setData();

        oneDayMsgRecyclerViewAdapter = new OneDayMsgRecyclerViewAdapter(oneDayMsgDataList, this);
        msgRecyclerView.setAdapter(oneDayMsgRecyclerViewAdapter);
        msgRecyclerView.setItemAnimator(null);

        hashtagDownRecyclerViewAdapter = new HashtagDownRecyclerViewAdapter(this);
        hashtagDownRecyclerView.setAdapter(hashtagDownRecyclerViewAdapter);

        hashtagUpRecyclerViewAdapter = new HashtagUpRecyclerViewAdapter(this);
        hashtagUpRecyclerView.setAdapter(hashtagUpRecyclerViewAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            insertedLocationName = data.getStringExtra("tag");
            String location = data.getStringExtra("location");
            int imgNumber = data.getIntExtra("imgNumber",0);
            Log.d("모은", insertedLocationName + " " + location + Integer.toString(imgNumber));
            insertedLocation_si = location.split(" ")[0];
            insertedLocation_gu = location.split(" ")[1];

            UserListDTO lst = new UserListDTO(insertedLocationName, insertedLocation_si, insertedLocation_gu,imgNumber);
            AppDatabase db = AppDatabase.getInstance(this);

            new UserListDatabaseInsertAsyncTask(db.userListDAO(), lst).execute();
            userList.add(lst);
            int size = userList.size() - 1;
            hashtagUpDataList.add(new Hashtag_VO(userList.get(size).tag, userList.get(size).img_number, true));
            hashtagUpRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));//색 지정
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setData() {

        // 해시태크 원 가데이터
        hashtagUpDataList = new ArrayList<>();

        hashtagUpDataList.add(new Hashtag_VO("내 장소\n추가하기", R.drawable.plus2, false));
        for(int i=0;i<userList.size();i++){
            hashtagUpDataList.add(new Hashtag_VO(userList.get(i).tag, userList.get(i).img_number, true));
        }

        hashtagDownDataList = new ArrayList<>();
        hashtagDownDataList.add(new Hashtag_VO("(코로나)\n동선", R.drawable.coronavirus, userSetting.is_clicked_corona_route_hashtag));
        hashtagDownDataList.add(new Hashtag_VO("(코로나)\n발생/방역", R.drawable.prevention, userSetting.is_clicked_corona_upbreak_hashtag));
        hashtagDownDataList.add(new Hashtag_VO("(코로나)\n안전수칙", R.drawable.mask_man, userSetting.is_clicked_corona_safety_hashtag));
        hashtagDownDataList.add(new Hashtag_VO("재난/날씨", R.drawable.disaster, userSetting.is_clicked_corona_disaster_hashtag));
        hashtagDownDataList.add(new Hashtag_VO("경제/금융", R.drawable.economy, userSetting.is_clicked_economy_hashtag));
        hashtagDownDataList.add(new Hashtag_VO("관심도\n1단계", R.drawable.level1, userSetting.is_clicked_lv1_hashtag));
        hashtagDownDataList.add(new Hashtag_VO("관심도\n2단계", R.drawable.level2, userSetting.is_clicked_lv2_hashtag));
        hashtagDownDataList.add(new Hashtag_VO("관심도\n3단계", R.drawable.level3, userSetting.is_clicked_lv3_hashtag));

        // 데이터 흐름 1. 위치에 따른 재난문자  2. 해쉬태크,필터에 따른 재난문자
        msgDataList = new ArrayList<>();
        oneDayMsgDataList = new ArrayList<>();

        msgDataList.add(new Msg_VO(1, "2020년 11월 6일", "21:30:23", "해외 유입 확진자가 증가 추세로 해외 입국이 예정되어 있는 가족 및" +
                " 외국인근로자가 있을 경우 반드시 완도군보건의료원로 신고 바랍니다", "[완도군청]", this, new MsgCategoryPoint_VO(70, 20, 10, 0, 0)));
        msgDataList.add(new Msg_VO(2, "2020년 11월 6일", "11:29:23", "367~369번 확진자 발생. 거주지 등 방역 완료. 코로나19 관련 안내 홈페이지" +
                " 참고바랍니다.", "[성북군청]", this, new MsgCategoryPoint_VO(10, 20, 70, 0, 0)));
        msgDataList.add(new Msg_VO(12, "2020년 11월 6일", "11:31:23", "367~369번 확진자 발생. 거주지 등 방역 완료. 코로나19 관련 안내 홈페이지" +
                " 참고바랍니다.", "[성북군청]", this, new MsgCategoryPoint_VO(70, 20, 10, 0, 0)));
        msgDataList.add(new Msg_VO(3, "2020년 11월 5일", "11:30:23", "11.8일 2명, 11.9일 4명 확진자 추가 발생." +
                " 상세내용 추후 시홈페이지에 공개예정입니다. corona.seongnam.go.kr", "[성남시청]", this, new MsgCategoryPoint_VO(10, 20, 70, 0, 0)));
        msgDataList.add(new Msg_VO(4, "2020년 11월 5일", "11:39:23", "해외 유입 확진자가 증가 추세로 해외 입국이 예정되어 있는 가족 및" +
                " 외국인근로자가 있을 경우 반드시 완도군보건의료원로 신고 바랍니다", "[완도군청]", this, new MsgCategoryPoint_VO(70, 20, 10, 0, 0)));
        msgDataList.add(new Msg_VO(5, "2020년 11월 5일", "03:39:10", "11.8일 2명, 11.9일 4명 확진자 추가 발생." +
                " 상세내용 추후 시홈페이지에 공개예정입니다. corona.seongnam.go.kr", "[성남시청]", this, new MsgCategoryPoint_VO(10, 70, 20, 0, 0)));
        msgDataList.add(new Msg_VO(6, "2020년 11월 4일", "21:30:23", "해외 유입 확진자가 증가 추세로 해외 입국이 예정되어 있는 가족 및" +
                " 외국인근로자가 있을 경우 반드시 완도군보건의료원로 신고 바랍니다", "[완도군청]", this, new MsgCategoryPoint_VO(10, 70, 20, 0, 0)));
        msgDataList.add(new Msg_VO(7, "2020년 11월 4일", "11:29:23", "해외 유입 확진자가 증가 추세로 해외 입국이 예정되어 있는 가족 및" +
                " 외국인근로자가 있을 경우 반드시 완도군보건의료원로 신고 바랍니다", "[완도군청]", this, new MsgCategoryPoint_VO(70, 20, 10, 0, 0)));
        msgDataList.add(new Msg_VO(8, "2020년 11월 3일", "03:39:10", "367~369번 확진자 발생. 거주지 등 방역 완료. 코로나19 관련 안내 홈페이지" +
                " 참고바랍니다.", "[성북군청]", this, new MsgCategoryPoint_VO(10, 20, 70, 0, 0)));
        msgDataList.add(new Msg_VO(9, "2020년 11월 3일", "03:39:10", "367~369번 확진자 발생. 거주지 등 방역 완료. 코로나19 관련 안내 홈페이지" +
                " 참고바랍니다.", "[성북군청]", this, new MsgCategoryPoint_VO(10, 70, 20, 0, 0)));
        msgDataList.add(new Msg_VO(10, "2020년 11월 3일", "21:30:23", "367~369번 확진자 발생. 거주지 등 방역 완료. 코로나19 관련 안내 홈페이지" +
                " 참고바랍니다.", "[성북군청]", this, new MsgCategoryPoint_VO(10, 20, 70, 0, 0)));
        msgDataList.add(new Msg_VO(11, "2020년 11월 3일", "03:39:10", "367~369번 확진자 발생. 거주지 등 방역 완료. 코로나19 관련 안내 홈페이지" +
                " 참고바랍니다.", "[성북군청]", this, new MsgCategoryPoint_VO(70, 20, 10, 0, 0)));

        //필터 데이터 분류
        classifyMsgData(true);
        createOneDayMsgDataList();
    }

    //해쉬태그에 따라 문자데이터 분류하는 메소드
    public void classifyMsgData(boolean isCheckingCategory) {
        classifiedMsgDataList = new ArrayList<>();
        for (int i = 0; i < msgDataList.size(); i++) {
            if (!hashtagDownDataList.get(msgDataList.get(i).getCategroyIndex()).isClicked() && isCheckingCategory)
                continue;
            if (!hashtagDownDataList.get(msgDataList.get(i).getLevel() + 4).isClicked())
                continue;

            classifiedMsgDataList.add(msgDataList.get(i));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createOneDayMsgDataList() {

        oneDayMsgDataList.clear();
        //day 에 따라 분류
        for (int i = 0; i < classifiedMsgDataList.size(); ) {

            // 마지막에서는 케이스 분류
            if (i == classifiedMsgDataList.size() - 1) {
                oneDayMsgDataList.add(new OneDayMsg_VO(classifiedMsgDataList.get(i).getDay(), new ArrayList<>(classifiedMsgDataList.subList(i, classifiedMsgDataList.size()))));
                break;
            }

            for (int j = i + 1; j <= classifiedMsgDataList.size(); j++) {

                if (j == classifiedMsgDataList.size()) {
                    oneDayMsgDataList.add(new OneDayMsg_VO(classifiedMsgDataList.get(i).getDay(), new ArrayList<>(classifiedMsgDataList.subList(i, j))));
                    i = j + 1;
                    break;
                }

                if (!classifiedMsgDataList.get(i).getDay().equals(classifiedMsgDataList.get(j).getDay())) {
                    oneDayMsgDataList.add(new OneDayMsg_VO(classifiedMsgDataList.get(i).getDay(), new ArrayList<>(classifiedMsgDataList.subList(i, j))));
                    i = j;
                    break;
                }
            }
        }

        //날짜,시간 순으로 배열
        oneDayMsgDataList = sortByDay(oneDayMsgDataList);
        oneDayMsgDataList = sortByTime(oneDayMsgDataList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMsgLevel() {

        //모든 문자에 대해 level을 다시 set해야함.
        for (int i = 0; i < msgDataList.size(); i++) {
            msgDataList.get(i).calculateTotalMsgPointAndLevel();
        }
        classifyMsgData(false);
        createOneDayMsgDataList();
    }

    public Integer calculateUpHashtagClickedNumber(){
        int result = 0;
        for(int i=0;i<hashtagUpDataList.size();i++){
            if(hashtagUpDataList.get(i).isClicked())
                result++;
        }
        return result;
    }

    //날짜순 정렬하는 메소드
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<OneDayMsg_VO> sortByDay(ArrayList<OneDayMsg_VO> oneDayMsgDataList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        oneDayMsgDataList.sort(new Comparator<OneDayMsg_VO>() {
            @Override
            public int compare(OneDayMsg_VO t, OneDayMsg_VO t1) {
                try {
                    return dateFormat.parse(t1.getDay()).compareTo(dateFormat.parse(t.getDay()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return oneDayMsgDataList;
    }

    //시간순으로 정렬하는 메소드
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<OneDayMsg_VO> sortByTime(ArrayList<OneDayMsg_VO> oneDayMsgDataList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        for (int i = 0; i < oneDayMsgDataList.size(); i++) {
            oneDayMsgDataList.get(i).getMsgArrayList().sort(new Comparator<Msg_VO>() {
                @Override
                public int compare(Msg_VO t, Msg_VO t1) {
                    try {
                        return dateFormat.parse(t1.getTime()).compareTo(dateFormat.parse(t.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        }

        return oneDayMsgDataList;
    }

    // 데이터 AsyncvTask
    public class FilterDatabaseAsyncTask extends AsyncTask<FilterDTO, Void, Void> {

        private FilterDAO filterDAO;

        FilterDatabaseAsyncTask(FilterDAO filterDAO) {
            this.filterDAO = filterDAO;
        }

        @Override
        protected Void doInBackground(FilterDTO... filterDTOS) {
            filterList = filterDAO.loadFilterList();
            if (filterList.size() == 0) {
                filterDAO.insert(new FilterDTO(0, 4, 4, 4, 4, 4));
                filterList = filterDAO.loadFilterList();
            }
            filterList = filterDAO.loadFilterList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //이 async 끝나고 유저 asyncTask 실행
            new UserDatabaseAsyncTask(db.userListDAO()).execute();
        }
    }

    public class UserDatabaseAsyncTask extends AsyncTask<UserListDTO, Void, Void> {

        private UserListDAO userListDAO;

        UserDatabaseAsyncTask(UserListDAO userListDAO) {
            this.userListDAO = userListDAO;
        }

        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {

            userList = userListDAO.loadUserList();
            if (userList.size() == 0) {
                userListDAO.insert(new UserListDTO("모은", "서울특별시", "광진구",R.drawable.home));
            }

            userList = userListDAO.loadUserList();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //이후 데이터 셋팅
            new UserSettingAsyncTask(db.userSettingDAO()).execute();
        }
    }

    public class UserSettingAsyncTask extends AsyncTask<UserSettingDTO, Void, Void> {

        private UserSettingDAO userSettingDAO;

        public UserSettingAsyncTask(UserSettingDAO userSettingDAO) {
            this.userSettingDAO = userSettingDAO;
        }

        @Override
        protected Void doInBackground(UserSettingDTO... userSettingDTOS) {
            userSetting = userSettingDAO.loadUserSetting();

            if (userSetting == null) {
                userSettingDAO.insert(new UserSettingDTO(true, true, true, false, false, true, true, true));
            }
            userSetting = userSettingDAO.loadUserSetting();

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            start();
        }
    }

    public class UserListDatabaseInsertAsyncTask extends AsyncTask<UserListDTO, Void, Void> {
        private UserListDAO userListDAO;
        private UserListDTO userListDTO;

        UserListDatabaseInsertAsyncTask(UserListDAO userListDAO, UserListDTO userListDTO) {
            this.userListDAO = userListDAO;
            this.userListDTO = userListDTO;
        }


        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {

            userListDAO.insert(userListDTO);
            List<UserListDTO> lst = userListDAO.loadUserList();
            userList = lst;
            return null;
        }
    }

}