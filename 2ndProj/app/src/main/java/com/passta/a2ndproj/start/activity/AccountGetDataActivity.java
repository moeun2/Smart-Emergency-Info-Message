package com.passta.a2ndproj.start.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//4
public class AccountGetDataActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_get_data);

        setStatusBar();



    }
    @Override
    public void onBackPressed() {
        finish();
//        Intent intent = new Intent(getApplicationContext(), TransferActivity.class);
//        startActivity(intent);
    }
    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#FAEBD7"));

    }
    class BtnOnClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // 일단은 intent로 넘기는 걸로 하고, 서버 되면 바꿔놓기


            switch (view.getId()) {

            }

        }
    }



}