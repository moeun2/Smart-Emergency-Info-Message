package com.passta.a2ndproj.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.passta.a2ndproj.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmSettingActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private SwitchCompat level1;
    private SwitchCompat level2;
    private SwitchCompat level3;
    private SwitchCompat switch_basic;

    private SwitchCompat audio_notification;
    private SwitchCompat viberation_notification;

    private boolean allowsReciving;

    private boolean isCheckedLevel1;
    private boolean isCheckedLevel2;
    private boolean isCheckedLevel3;

    private boolean isCheckedAudioNotification;
    private boolean isCheckedVibrationNotification;

    private LabeledSwitch labeledSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        setStatusBar();
        initializeNotificationSwitch();
        InitializeView();
        initializeSwitch();

        switch_basic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(), "switch_basic 체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();
                pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
                editor = pref.edit();
                allowsReciving = isChecked;
                editor.putBoolean("allowsReciving", isChecked);
                editor.apply();

                initializeSwitch();
            }

        });

        level1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub
                if (allowsReciving) {
                    Toast.makeText(getApplicationContext(), "level1 체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();
                    pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
                    editor = pref.edit();
                    isCheckedLevel1 = isChecked;
                    editor.putBoolean("isCheckedLevel1", isChecked);
                    editor.apply();
                }
            }

        });
        level2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub
                if (allowsReciving) {
                    Toast.makeText(getApplicationContext(), "level2 체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();
                    pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
                    editor = pref.edit();
                    isCheckedLevel2 = isChecked;
                    editor.putBoolean("isCheckedLevel2", isChecked);
                    editor.apply();
                }


            }

        });
        level3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub
                if (allowsReciving) {
                    Toast.makeText(getApplicationContext(), "level3 체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();
                    pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
                    editor = pref.edit();
                    isCheckedLevel3 = isChecked;
                    editor.putBoolean("isCheckedLevel3", isChecked);
                    editor.apply();
                }


            }

        });


        audio_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(), "audio_notification 체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();
                pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
                editor = pref.edit();
                isCheckedAudioNotification = isChecked;
                editor.putBoolean("isCheckedAudioNotification", isChecked);
                editor.apply();

                initializeSwitch();
            }

        });

        viberation_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(), "viberation_notification 체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();
                pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
                editor = pref.edit();
                isCheckedVibrationNotification = isChecked;
                editor.putBoolean("isCheckedVibrationNotification", isChecked);
                editor.apply();

                initializeSwitch();
            }

        });

    }
    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));//색 지정

    }
    public void InitializeView() {
        level1 = (SwitchCompat) findViewById(R.id.level1);
        level2 = (SwitchCompat) findViewById(R.id.level2);
        level3 = (SwitchCompat) findViewById(R.id.level3);
        switch_basic = (SwitchCompat) findViewById(R.id.switch1);

        audio_notification = (SwitchCompat) findViewById(R.id.audio_notification);

        viberation_notification = (SwitchCompat) findViewById(R.id.viberation_notification);
    }

    public void initializeNotificationSwitch() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(new Runnable() {
            @Override
            public void run() {
                pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
                isCheckedAudioNotification = pref.getBoolean("isCheckedAudioNotification", true);
                isCheckedVibrationNotification = pref.getBoolean("isCheckedVibrationNotification", true);
            }
        });
        es.submit(new Runnable() {

            @Override
            public void run() {
                audio_notification.setChecked(isCheckedAudioNotification);

                viberation_notification.setChecked(isCheckedVibrationNotification);
            }
        });

        es.shutdown();


    }

    public void initializeSwitch() {
        pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
        allowsReciving = pref.getBoolean("allowsReciving", true);
        isCheckedLevel1 = pref.getBoolean("isCheckedLevel1", true);
        isCheckedLevel2 = pref.getBoolean("isCheckedLevel2", true);
        isCheckedLevel3 = pref.getBoolean("isCheckedLevel3", true);

        changeSwitch();

    }

    public void changeSwitch() {
        if (allowsReciving) {
            switch_basic.setChecked(true);


            if (isCheckedLevel1)
                level1.setChecked(true);
            else
                level1.setChecked(false);

            if (isCheckedLevel2)
                level2.setChecked(true);
            else
                level2.setChecked(false);

            if (isCheckedLevel3)
                level3.setChecked(true);
            else
                level3.setChecked(false);
        } else {
            switch_basic.setChecked(false);
            level1.setChecked(false);
            level2.setChecked(false);
            level3.setChecked(false);
        }
    }


    //재난문자 수신 알림 설정 on/off
    // 1. Shared Preference 초기화
//        pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
//        editor = pref.edit();
//        boolean allowsReciving = pref.getBoolean("allowsReciving", true);


    //관심도 알림 설정 (level 1/2/3 on/off)
}


