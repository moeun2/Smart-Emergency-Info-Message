package com.passta.a2ndproj.start.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.FilterDAO;
import com.passta.a2ndproj.data.FilterDTO;
import com.passta.a2ndproj.data.MsgDAO;
import com.passta.a2ndproj.data.MsgDTO;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.main.MsgCategoryPoint_VO;
import com.passta.a2ndproj.main.Msg_VO;
import com.passta.a2ndproj.network.RetrofitClient;
import com.passta.a2ndproj.network.ServiceApi;
import com.warkiz.widget.IndicatorSeekBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Page3Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView next;
    private ImageView back;
    private IndicatorSeekBar seekbar1;
    private IndicatorSeekBar seekbar2;
    private IndicatorSeekBar seekbar3;
    private IndicatorSeekBar seekbar4;
    private IndicatorSeekBar seekbar5;
    private int seekbar1_progress;
    private int seekbar2_progress;
    private int seekbar3_progress;
    private int seekbar4_progress;
    private int seekbar5_progress;
    private List<FilterDTO> list;
    private ServiceApi serviceApi;
    public List<UserListDTO> userList;
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);

        //db생성
        db = AppDatabase.getInstance(this);
        new UserDatabaseAsyncTask(db.userListDAO()).execute();

        setStatusBar();
        InitializeView();
        SetListener();
    }
    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#ffffff"));//색 지정

    }
    public void InitializeView()
    {

        next = (TextView)findViewById(R.id.next_page3_activity);
        back = (ImageView) findViewById(R.id.back_page3_activity);
        seekbar1 = (IndicatorSeekBar)findViewById(R.id.seekbar1);
        seekbar2 = (IndicatorSeekBar)findViewById(R.id.seekbar2);
        seekbar3 = (IndicatorSeekBar)findViewById(R.id.seekbar3);
        seekbar4 = (IndicatorSeekBar)findViewById(R.id.seekbar4);
        seekbar5 = (IndicatorSeekBar)findViewById(R.id.seekbar5);

    }

    public void SetListener(){
        next.setOnClickListener(this);
        back.setOnClickListener(this);

    }


    //4,3,2,1
    public void onClick(View v){
        switch (v.getId()){
            case R.id.next_page3_activity:
                getSeekbarProgress();
                goToNextActivity(new Page4Activity());
                break;
            case R.id.back_page3_activity:
                goToNextActivity(new Page2Activity());
                break;
        }
    }
    private void goToNextActivity(Activity activity) {
        finish();
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);
    }
    private void getSeekbarProgress(){

         seekbar1_progress = Math.abs(seekbar1.getProgress() - 5);
         seekbar2_progress = Math.abs(seekbar2.getProgress() - 5);
         seekbar3_progress = Math.abs(seekbar3.getProgress() - 5);
         seekbar4_progress = Math.abs(seekbar4.getProgress() - 5);
         seekbar5_progress = Math.abs(seekbar5.getProgress() - 5);

        new DatabaseAsyncTask(db.filterDAO()).execute();

        for(int i=0;i<userList.size();i++){
            insertMsgData(userList.get(i).getLocation_si(),userList.get(i).getLocation_gu());
        }

    }

    public void insertMsgData(String location_si, String location_gu) {

        serviceApi = RetrofitClient.getClient().create(ServiceApi.class);//내 서버 연결

        Call<ResponseBody> selectMsg = serviceApi.selectMsg(location_si, location_gu, 10);
        selectMsg.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String result = null;
                JSONArray jsonArray = null;
                try {
                    result = response.body().string();
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

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        ArrayList<String> tempDay = new ArrayList<>();
                        tempDay = returnDayString(obj.getString("msg_sendingTime"));

                        // 이미 똑같은 ~~시 문자가 저장돼있는경우 ~~시 전체의 문자의 경우는 continue 시켜서 add안함.
                        if (hasSameLocation_si) {
                            if (obj.getString("msg_sendingArea").split(" ")[1].equals("전체"))
                                continue;
                        }

                        //msgVo 하나만들고
                        Msg_VO tempMsgVO = new Msg_VO(obj.getInt("msg_id"), tempDay.get(0), tempDay.get(1), obj.getString("msg_content").trim(),
                                obj.getString("msg_sendingArea"),new MsgCategoryPoint_VO(obj.getDouble("co_route"), obj.getDouble("co_outbreak_quarantine"), obj.getDouble("co_safetyTips"),
                                obj.getDouble("disaster_weather"), obj.getDouble("economy_finance")),seekbar1_progress,seekbar2_progress,seekbar3_progress,seekbar4_progress,seekbar5_progress);

                        //데베에 저장
                        new MsgListDatabaseInsertAsyncTask(db.MsgDAO(), new MsgDTO(tempMsgVO.getId(), tempMsgVO.getDay(), tempMsgVO.getTime(), tempMsgVO.getMsgText(),
                                tempMsgVO.getSenderLocation(), tempMsgVO.getLevel(), tempMsgVO.getCircleImageViewId(), obj.getDouble("co_route"), obj.getDouble("co_outbreak_quarantine"), obj.getDouble("co_safetyTips"),
                                obj.getDouble("disaster_weather"), obj.getDouble("economy_finance"), tempMsgVO.getTotalMsgPoint(), tempMsgVO.getCategroyIndex())).execute();
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
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


    public class DatabaseAsyncTask extends AsyncTask<FilterDTO,Void,Void> {

        private FilterDAO filterDAO;

        DatabaseAsyncTask(FilterDAO filterDAO)
        {
            this.filterDAO = filterDAO;
        }
        @Override
        protected Void doInBackground(FilterDTO... filterDTOS) {
            list = filterDAO.loadFilterList();
            if(list.size() == 0)
            {
                filterDAO.insert(new FilterDTO(0,seekbar1_progress,seekbar2_progress,seekbar3_progress,seekbar4_progress,seekbar5_progress));
                list = filterDAO.loadFilterList();
            }
            else{
                filterDAO.update(new FilterDTO(0,seekbar1_progress,seekbar2_progress,seekbar3_progress,seekbar4_progress,seekbar5_progress));
                for(int i=0;i<list.size();i++)
                {
                    Log.i("모은 filter db 확인", String.valueOf(list.get(i).getFilter_1()));
                }
            }
            return null;
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
            return null;
        }
    }



}