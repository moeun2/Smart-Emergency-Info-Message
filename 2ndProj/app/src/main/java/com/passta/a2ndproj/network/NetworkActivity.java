package com.passta.a2ndproj.network;


import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.passta.a2ndproj.R;


public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_network);
        //setStatusBar();

        ImageView gifImageView = findViewById(R.id.wifi_gif);
        Glide.with(this).asGif()
                .load(R.drawable.network_wifi)
                .into(gifImageView);


    }


}


