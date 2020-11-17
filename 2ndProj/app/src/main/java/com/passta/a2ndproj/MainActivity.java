package com.passta.a2ndproj;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.main.HashtagRecyclerViewAdapter;
import com.passta.a2ndproj.main.Hashtag_VO;
import com.passta.a2ndproj.main.Msg_VO;
import com.passta.a2ndproj.main.OneDayMsgRecyclerViewAdapter;
import com.passta.a2ndproj.main.OneDayMsg_VO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView hashtagRecyclerView;
    private RecyclerView msgRecyclerView;
    private ArrayList<Hashtag_VO> hashtagDataList;
    private ArrayList<Msg_VO> msgDataList;
    private ArrayList<OneDayMsg_VO> oneDayMsgDataList;
    private HashtagRecyclerViewAdapter hashtagRecyclerViewAdapter;
    private OneDayMsgRecyclerViewAdapter oneDayMsgRecyclerViewAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();

        //예시데이터
        setData();

        hashtagRecyclerView = findViewById(R.id.recyclerview_main_hashtag);
        msgRecyclerView = findViewById(R.id.recyclerview_main_msg);

        hashtagRecyclerViewAdapter = new HashtagRecyclerViewAdapter(hashtagDataList);
        hashtagRecyclerView.setAdapter(hashtagRecyclerViewAdapter);

        oneDayMsgRecyclerViewAdapter = new OneDayMsgRecyclerViewAdapter(oneDayMsgDataList);
        msgRecyclerView.setAdapter(oneDayMsgRecyclerViewAdapter);

    }

    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));//색 지정

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setData() {

        // 해시태크 원 가데이터
        hashtagDataList = new ArrayList<>();

        hashtagDataList.add(new Hashtag_VO("우리 집",R.drawable.home));
        hashtagDataList.add(new Hashtag_VO("학교",R.drawable.school));
        hashtagDataList.add(new Hashtag_VO("회사",R.drawable.company));
        hashtagDataList.add(new Hashtag_VO("(코로나)\n동선",R.drawable.coronavirus));
        hashtagDataList.add(new Hashtag_VO("(코로나)\n발생,방역",R.drawable.prevention));
        hashtagDataList.add(new Hashtag_VO("(코로나)\n안전수칙",R.drawable.mask_man));
        hashtagDataList.add(new Hashtag_VO("경제,금융",R.drawable.economy));
        hashtagDataList.add(new Hashtag_VO("재난,날씨",R.drawable.disaster));

        oneDayMsgDataList = new ArrayList<>();

        for (int j = 0; j < 3; j++) {
            msgDataList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {

                if (i == 0) {
                    msgDataList.add(new Msg_VO("오후 8시 23분", "해외 유입 확진자가 증가 추세로 해외 입국이 예정되어 있는 가족 및" +
                            " 외국인근로자가 있을 경우 반드시 완도군보건의료원로 신고 바랍니다", "[완도군청]", R.drawable.level1));
                } else if (i == 1) {
                    msgDataList.add(new Msg_VO("오후 8시 23분", "해외 유입 확진자가 증가 추세로 해외 입국이 예정되어 있는 가족 및" +
                            " 외국인근로자가 있을 경우 반드시 완도군보건의료원로 신고 바랍니다", "[완도군청]", R.drawable.level2));
                } else {
                    msgDataList.add(new Msg_VO("오후 8시 23분", "해외 유입 확진자가 증가 추세로 해외 입국이 예정되어 있는 가족 및" +
                            " 외국인근로자가 있을 경우 반드시 완도군보건의료원로 신고 바랍니다", "[완도군청]", R.drawable.level3));
                }

            }
            oneDayMsgDataList.add(new OneDayMsg_VO("2020년 11월 2일", msgDataList));
        }

    }
}