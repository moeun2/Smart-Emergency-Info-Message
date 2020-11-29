package com.passta.a2ndproj;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.FilterDAO;
import com.passta.a2ndproj.data.FilterDTO;
import com.passta.a2ndproj.data.MsgDAO;
import com.passta.a2ndproj.data.MsgDTO;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.data.UserSettingDAO;
import com.passta.a2ndproj.data.UserSettingDTO;
import com.passta.a2ndproj.main.Adapter.HashtagDownRecyclerViewAdapter;
import com.passta.a2ndproj.main.Adapter.HashtagUpRecyclerViewAdapter;
import com.passta.a2ndproj.main.DataVO.Hashtag_VO;
import com.passta.a2ndproj.main.DataVO.MsgCategoryPoint_VO;
import com.passta.a2ndproj.main.DataVO.Msg_VO;
import com.passta.a2ndproj.main.Adapter.OneDayMsgRecyclerViewAdapter;
import com.passta.a2ndproj.main.DataVO.OneDayMsg_VO;
import com.passta.a2ndproj.main.Seekbar;
import com.passta.a2ndproj.network.RetrofitClient;
import com.passta.a2ndproj.network.ServiceApi;
import com.passta.a2ndproj.notification.AlarmSettingActivity;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.warkiz.widget.IndicatorSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public List<MsgDTO> msgDTOList;
    public UserSettingDTO userSetting;
    public String insertedLocationName;
    public String insertedLocation_si;
    public String insertedLocation_gu;
    private ServiceApi serviceApi;
    private ImageView refreshButton;


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
        refreshButton = findViewById(R.id.refresh_main_activity);
        seekbar1 = (IndicatorSeekBar) findViewById(R.id.seekbar1_main_activity);
        seekbar2 = (IndicatorSeekBar) findViewById(R.id.seekbar2_main_activity);
        seekbar3 = (IndicatorSeekBar) findViewById(R.id.seekbar3_main_activity);
        seekbar4 = (IndicatorSeekBar) findViewById(R.id.seekbar4_main_activity);
        seekbar5 = (IndicatorSeekBar) findViewById(R.id.seekbar5_main_activity);
        filterList = new ArrayList<>();
        userList = new ArrayList<>();

        ImageView setting_ = (ImageView) findViewById(R.id.icon_setting);
        setting_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
                startActivity(intent);

            }
        });

        //이미 등록된 경우
        @SuppressLint("WrongThread") String savedToken = FirebaseInstanceId.getInstance().getId();
        Log.e("savedToken", savedToken);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.containsKey("noti")){
                boolean noti = extras.getBoolean("noti");
                if(noti)
                {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        }

        //db생성
        db = AppDatabase.getInstance(this);

        //Firebase에 토큰 등록시
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.e("new Token : ", newToken);
                        subcribeTopic("testTopic");//topic 에 등록
                    }
                });

        new FilterDatabaseAsyncTask(db.filterDAO()).execute();
    }

    public void subcribeTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        Log.e("Main ", "subcribe Topic : " + topic);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void start() {

        //seekbar 너무 길어져서 클래스로 뺌
        Seekbar seekbar = new Seekbar(this);
        seekbar.setSeekbar();

        //데이타set
        setData();

        oneDayMsgRecyclerViewAdapter = new OneDayMsgRecyclerViewAdapter(oneDayMsgDataList, this);
        msgRecyclerView.setAdapter(oneDayMsgRecyclerViewAdapter);
        msgRecyclerView.setItemAnimator(null);

        hashtagDownRecyclerViewAdapter = new HashtagDownRecyclerViewAdapter(this);
        hashtagDownRecyclerView.setAdapter(hashtagDownRecyclerViewAdapter);

        hashtagUpRecyclerViewAdapter = new HashtagUpRecyclerViewAdapter(this);
        hashtagUpRecyclerView.setAdapter(hashtagUpRecyclerViewAdapter);

        //새로 고침
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            insertedLocationName = data.getStringExtra("tag");
            String location = data.getStringExtra("location");
            int imgNumber = data.getIntExtra("imgNumber", 0);
            Log.d("모은", insertedLocationName + " " + location + Integer.toString(imgNumber));
            insertedLocation_si = location.split(" ")[0];
            insertedLocation_gu = location.split(" ")[1];

            UserListDTO lst = new UserListDTO(insertedLocationName, insertedLocation_si, insertedLocation_gu, imgNumber, true);
            AppDatabase db = AppDatabase.getInstance(this);

            new UserListDatabaseInsertAsyncTask(db.userListDAO(), lst).execute();
            userList.add(lst);
            int size = userList.size() - 1;
            hashtagUpDataList.add(new Hashtag_VO(userList.get(size).tag, userList.get(size).img_number, true, location));
            hashtagUpRecyclerViewAdapter.notifyDataSetChanged();

            //위치에 따른 msg 추가
            getMsgData(insertedLocation_si, insertedLocation_gu);
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
        hashtagUpDataList.add(new Hashtag_VO("내 국민\n은행 계좌", R.drawable.bank_kb_gray_logo, false));

        for (int i = 0; i < userList.size(); i++) {
            hashtagUpDataList.add(new Hashtag_VO(userList.get(i).tag, userList.get(i).img_number, userList.get(i).isHashtagChecked, userList.get(i).getLocation_si() + " " + userList.get(i).getLocation_gu()));
        }

        msgDataList = new ArrayList<>();

        for (int i = 0; i < msgDTOList.size(); i++) {
            msgDataList.add(new Msg_VO(msgDTOList.get(i).getId(), msgDTOList.get(i).getDay(), msgDTOList.get(i).getTime(), msgDTOList.get(i).getMsgText(), msgDTOList.get(i).getSenderLocation(),
                    this, new MsgCategoryPoint_VO(msgDTOList.get(i).getRouteCatePoint(), msgDTOList.get(i).getUpbreakCatePoint(), msgDTOList.get(i).getSafetyCatePoint(),
                    msgDTOList.get(i).getDisasterCatePoint(), msgDTOList.get(i).getEconomyCatePoint())));
        }

        sortTotalMsgDataList();

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
        oneDayMsgDataList = new ArrayList<>();

        //필터 데이터 분류
        classifyMsgData();
        createOneDayMsgDataList();
    }

    //해쉬태그에 따라 문자데이터 분류하는 메소드
    public void classifyMsgData() {
        classifiedMsgDataList = new ArrayList<>();

        LoopP:
        for (int i = 0; i < msgDataList.size(); i++) {

            //중대본 무조건 추가
            if(msgDataList.get(i).getSenderLocation().trim().equals("중대본 전체")){
                classifiedMsgDataList.add(msgDataList.get(i));
                continue LoopP;
            }

            if (!hashtagDownDataList.get(msgDataList.get(i).getCategroyIndex()).isClicked())
                continue;
            if (!hashtagDownDataList.get(msgDataList.get(i).getLevel() + 4).isClicked())
                continue;

            // 해시태그 구 가 전체일 때가 있는지 부터 검사.
            for (int j = 2; j < hashtagUpDataList.size(); j++){
                String hashtagSi = hashtagUpDataList.get(j).getLocation().split(" ")[0];
                String hastagGu = hashtagUpDataList.get(j).getLocation().split(" ")[1];

                //해쉬태그 구가 전체이고 해시태그 시와 발신 지역의 시가 같은 경우.
                if(hastagGu.equals("전체") && (hashtagSi.equals(msgDataList.get(i).getSenderLocation().split(" ")[0])) && hashtagUpDataList.get(j).isClicked()){
                    // 해시태그 구 가 전체일때는 같은 시 는 다들어온다.
                    classifiedMsgDataList.add(msgDataList.get(i));
                    continue LoopP;
                }
            }


            LoopC:
            for (int j = 2; j < hashtagUpDataList.size(); j++) {
                // 1. 위치가 아예 같거나 2. 시는 같고 구 가 전체 이거나
                String hashtagSi = hashtagUpDataList.get(j).getLocation().split(" ")[0];

                // 1. 해시 태그와 발신 지역의 시,구가 아예 같은 경우 2. 해시 태그 시가 같고 발신 지역이 전체 인 경우
                if ((msgDataList.get(i).getSenderLocation().equals(hashtagUpDataList.get(j).getLocation()) || (
                        msgDataList.get(i).getSenderLocation().split(" ")[0].equals(hashtagSi) &&
                                msgDataList.get(i).getSenderLocation().split(" ")[1].equals("전체")
                )) && hashtagUpDataList.get(j).isClicked()) {
                    //해당 location의 해쉬태그가 클릭돼있다면
                    classifiedMsgDataList.add(msgDataList.get(i));
                    break LoopC;
                }
            }
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
        classifyMsgData();
        createOneDayMsgDataList();
    }

    public Integer calculateUpHashtagClickedNumber() {
        int result = 0;
        for (int i = 0; i < hashtagUpDataList.size(); i++) {
            if (hashtagUpDataList.get(i).isClicked())
                result++;
        }
        return result;
    }

    public ArrayList<String> returnDayString(String sendingTime) {
        String dayString = sendingTime.split(" ")[0];
        String timeString = sendingTime.split(" ")[1];

        dayString = dayString.substring(0, dayString.indexOf("/")) + "년 " +
                dayString.substring(dayString.indexOf("/") + 1, dayString.lastIndexOf("/")) + "월 " + dayString.substring(dayString.lastIndexOf("/") + 1) + "일";

        ArrayList<String> list = new ArrayList<>();
        list.add(dayString);
        list.add(timeString);
        return list;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortTotalMsgDataList() {
        //전체 데이터 시간순 배열
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        msgDataList.sort(new Comparator<Msg_VO>() {
            @Override
            public int compare(Msg_VO t, Msg_VO t1) {
                try {
                    return dateFormat.parse(t1.getDay() + " " + t1.getTime()).compareTo(dateFormat.parse(t.getDay() + " " + t1.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    public void getMsgData(String location_si, String location_gu) {
        int limit = 0;
        if (location_gu.equals("전체")) {
            limit = 30;
        } else
            limit = 15;
        serviceApi = RetrofitClient.getClient().create(ServiceApi.class);//내 서버 연결

        Call<ResponseBody> selectMsg = serviceApi.selectMsg(location_si, location_gu, limit);
        selectMsg.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = null;
                JSONArray jsonArray = null;
                boolean isTotalGu = false;

                if (location_gu.equals("전체"))
                    isTotalGu = true;

                try {
                    result = response.body().string();
                    Log.d("test",result);

                    int temp = 0;
                    boolean hasSameLocation_si = false;

                    // 이미 똑같은 시 의 문자 정보가 저장돼 있는 경우에는 ~~시 전체 의 문자를 한번더 넣어주지 않기 위해 검사
                    for (int i = 0; i < userList.size(); i++) {
                        if (location_si.equals(userList.get(i).getLocation_si())) {
                            temp++;
                            if (temp > 1) {
                                hasSameLocation_si = true;
                                break;
                            }
                        }
                    }

                    JSONObject jObject = new JSONObject(result);
                    jsonArray = (JSONArray) jObject.get("msg");

                    LoopP:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        ArrayList<String> tempDay = new ArrayList<>();
                        tempDay = returnDayString(obj.getString("msg_sendingTime"));

                        // 이미 똑같은 ~~시 문자가 저장돼있는경우 ~~시 전체의 문자의 경우는 continue 시켜서 add안함.
                        if (hasSameLocation_si) {
                            if (obj.getString("msg_gusi").equals("전체"))
                                continue;
                        }

                        //구가 전체 일 경우 같은 시 의 문자가 이미 저장돼어있다면 전체 반목문 컨티뉴
                        if (isTotalGu) {
                            LoopC:
                            for (int j = 0; j < userList.size() - 1; j++) {
                                if (obj.getString("msg_gusi").equals(userList.get(j).getLocation_gu())) {
                                    continue LoopP;
                                }
                            }
                        }

                        //같은 시의 전체 가 이미 저장돼있고, 같은 시가 들어올 경우 continue * -1 인 이유는 가장 최근에 등록된게 ~~시 전체인 경우를 제외하기 위함.
                        for (int j = 0; j < userList.size()-1; j++) {
                            if (obj.getString("msg_sido").equals(userList.get(j).getLocation_si())&&
                                    userList.get(j).getLocation_gu().equals("전체")) {
                                continue LoopP;
                            }
                        }

                        msgDataList.add(new Msg_VO(obj.getInt("msg_id"), tempDay.get(0), tempDay.get(1), obj.getString("msg_content").trim(),
                                obj.getString("msg_sido") + " " + obj.getString("msg_gusi"), MainActivity.this, new MsgCategoryPoint_VO(obj.getDouble("co_route"), obj.getDouble("co_outbreak_quarantine"), obj.getDouble("co_safetyTips"),
                                obj.getDouble("disaster_weather"), obj.getDouble("economy_finance"))));

                        Msg_VO tempMsgVO = msgDataList.get(msgDataList.size() - 1);

                        //데베에 저장
                        new MsgListDatabaseInsertAsyncTask(db.MsgDAO(), new MsgDTO(tempMsgVO.getDay(), tempMsgVO.getTime(), tempMsgVO.getMsgText(), tempMsgVO.getSenderLocation(),
                                tempMsgVO.getLevel(), tempMsgVO.getCircleImageViewId(), obj.getDouble("co_route"), obj.getDouble("co_outbreak_quarantine"), obj.getDouble("co_safetyTips"),
                                obj.getDouble("disaster_weather"), obj.getDouble("economy_finance"), tempMsgVO.getTotalMsgPoint(), tempMsgVO.getCategroyIndex())).execute();
                    }

                    sortTotalMsgDataList();

                    //필터 데이터 분류
                    classifyMsgData();
                    createOneDayMsgDataList();
                    oneDayMsgRecyclerViewAdapter.notifyDataSetChanged();

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
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
                userListDAO.insert(new UserListDTO("모은", "서울특별시", "광진구", R.drawable.home, true));
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
            new MsgDataAsyncTask(db.MsgDAO()).execute();
        }
    }

    public class MsgDataAsyncTask extends AsyncTask<MsgDAO, Void, Void> {

        private MsgDAO msgDAO;
        private Boolean isRefresh;

        public MsgDataAsyncTask(MsgDAO msgDAO) {
            this.msgDAO = msgDAO;
            this.isRefresh = isRefresh;
        }

        @Override
        protected Void doInBackground(MsgDAO... msgDAOS) {

            msgDTOList = msgDAO.loadMsgList();
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

    public class MsgListDatabaseInsertAsyncTask extends AsyncTask<MsgDTO, Void, Void> {

        private MsgDAO msgDAO;
        private MsgDTO msgDTO;

        public MsgListDatabaseInsertAsyncTask(MsgDAO msgDAO, MsgDTO msgDTo) {
            this.msgDAO = msgDAO;
            this.msgDTO = msgDTo;
        }

        @Override
        protected Void doInBackground(MsgDTO... msgDTOS) {
            msgDAO.insert(msgDTO);
            msgDTOList = msgDAO.loadMsgList();
            return null;
        }
    }

}