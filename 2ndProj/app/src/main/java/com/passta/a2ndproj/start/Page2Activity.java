package com.passta.a2ndproj.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Page2Activity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Page2";
    private TextView add_location;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        InitializeView();
        SetListener();
    }
    public void InitializeView()
    {
        add_location = (TextView)findViewById(R.id.add_location);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        next = (TextView)findViewById(R.id.next);

    }

    public void SetListener(){
        add_location.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.next:
                Intent intent = new Intent(getApplicationContext(),Page3Activity.class);
                startActivity(intent);
                break;
            case R.id.add_location:
                intent = new Intent(getApplicationContext(), Dialogue_add_location.class);
                startActivity(intent);
                break;
        }
    }
}