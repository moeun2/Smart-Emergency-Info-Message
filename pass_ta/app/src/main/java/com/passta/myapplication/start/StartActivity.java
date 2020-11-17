package com.passta.myapplication.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import com.passta.myapplication.R;

import me.relex.circleindicator.CircleIndicator3;

public class StartActivity extends AppCompatActivity {
    private CircleIndicator3 mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //상태바 없애기
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            final WindowInsetsController insetsController = getWindow().getInsetsController();
//            if (insetsController != null) {
//                insetsController.hide(WindowInsets.Type.statusBars());
//            }
//        } else {
//            getWindow().setFlags(
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN
//            );
//        }
        setContentView(R.layout.activity_start);

        ViewPager2 viewPager = findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        mIndicator = findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        mIndicator.createIndicators(4,0);


    }


}