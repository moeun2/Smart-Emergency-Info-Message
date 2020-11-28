package com.passta.a2ndproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.start.activity.Page1Activity;

import java.util.List;

public class IntroLoadingActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    public List<UserListDTO> userList;
    public AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //앱 최초 실행 확인
        sharedPreferences = getSharedPreferences("checkFirst", MODE_PRIVATE);
        boolean checkFirst = sharedPreferences.getBoolean("checkFirst", true);

        setStatusBar();
        setContentView(R.layout.activity_intro_loading);

        db = AppDatabase.getInstance(this);
        new UserDatabaseAsyncTask(db.userListDAO()).execute();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //앱 최초 실행 일 경우 or Msg 데이터 size가 0일경우 Start1Activity 로이동.
                if (checkFirst || userList.size() == 0) {
                    Intent intent = new Intent(getApplicationContext(), Page1Activity.class);
                    startActivity(intent); //다음화면으로 넘어감
                    finish();

                    //false로 만들어줌으로써 최초실행 이후에는 안들어오게끔
                    sharedPreferences.edit().putBoolean("checkFirst", false).apply();
                } else {
                    //앱 최초 실행이 아닐 경우 Main으로 로이동.
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent); //다음화면으로 넘어감
                    finish();

                }
//
//                //바로 페이지1액티로 넘어가는 코드드
//               Intent intent = new Intent(getApplicationContext(), Page1Activity.class);
//                startActivity(intent); //다음화면으로 넘어감
//                finish();

            }
        }, 3000); //3초 뒤에 Runner객체 실행하도록 함
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));//색 지정
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

}
