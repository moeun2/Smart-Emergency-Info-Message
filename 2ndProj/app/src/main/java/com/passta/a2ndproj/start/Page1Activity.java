package com.passta.a2ndproj.start;

import androidx.appcompat.app.AppCompatActivity;
import com.passta.a2ndproj.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Page1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Page2Activity.class);
                startActivity(intent);
            }
        });

    }
}