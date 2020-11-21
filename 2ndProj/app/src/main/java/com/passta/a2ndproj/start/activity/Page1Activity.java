package com.passta.a2ndproj.start.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.network.NetworkActivity;
import com.passta.a2ndproj.network.NetworkStatus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Page1Activity extends AppCompatActivity {
    NetworkStatus networkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        setStatusBar();

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
}