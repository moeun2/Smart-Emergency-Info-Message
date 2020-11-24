package com.passta.a2ndproj.start.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.network.NetworkActivity;
import com.passta.a2ndproj.network.NetworkStatus;
import com.passta.a2ndproj.network.RetrofitClient;
import com.passta.a2ndproj.network.ServiceApi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Page1Activity extends AppCompatActivity {
    NetworkStatus networkStatus;
    private ServiceApi serviceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        setStatusBar();
        abc();
        int networkSatusNum = networkStatus.getConnectivityStatus(getApplicationContext());
        if (networkSatusNum == networkStatus.TYPE_NOT_CONNECTED)
            goToNextActivity(new NetworkActivity());

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                goToNextActivity(new Page2Activity());
            }
        });
    }
    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));//색 지정

    }
    private void goToNextActivity(Activity activity) {
        finish();
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);
    }

    public void abc(){

        serviceApi = RetrofitClient.getClient().create(ServiceApi.class);//내 서버 연결

        Call<ResponseBody> selectMsg = serviceApi.selectMsg("서울특별시","광진구",10);
        selectMsg.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("test","ssss");
                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("test",result);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("test",t.toString());
            }
        });
    }
}