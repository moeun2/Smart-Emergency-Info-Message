package com.passta.a2ndproj.start.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Page4Activity extends AppCompatActivity implements View.OnClickListener{
    private TextView next;
    private TextView back;
    private TextView bank_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4);

        setStatusBar();
        InitializeView();
        SetListener();
    }


    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#6bc7ee"));//색 지정

    }
    public void InitializeView()
    {

        next = (TextView)findViewById(R.id.next);
        back = (TextView)findViewById(R.id.back);
        bank_data = (TextView)findViewById(R.id.bank_data);


    }

    public void SetListener(){
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        bank_data.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.next:
                goToNextActivity(new MainActivity());
                break;
            case R.id.back:
                goToNextActivity(new Page3Activity());
                break;
            case R.id.bank_data:
                Intent intent = new Intent(getApplicationContext(), SearchBankActivity.class);
//                    intent.putExtra("setting", "login");
                startActivity(intent);

                break;
        }
    }
    private void goToNextActivity(Activity activity) {
        finish();
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);
    }
}