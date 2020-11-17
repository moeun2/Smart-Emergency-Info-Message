package com.passta.a2ndproj.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Page3Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);

        InitializeView();
        SetListener();
    }
    public void InitializeView()
    {

        next = (TextView)findViewById(R.id.next2);

    }

    public void SetListener(){
        next.setOnClickListener(this);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.next2:
                Intent intent = new Intent(getApplicationContext(),Page4Activity.class);
                startActivity(intent);
                break;
        }
    }
}