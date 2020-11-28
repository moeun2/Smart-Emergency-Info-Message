package com.passta.a2ndproj.start.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.CardVO;
import com.passta.a2ndproj.start.adapter.GridAdapter_p;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SearchBankActivity extends AppCompatActivity {
    private GridView gridView;
    private GridAdapter_p adapter;
    private ArrayList<CardVO> cards = new ArrayList<>();
    private ImageView back;
    private TextView next;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bank);
        setStatusBar();
        cards.add(new CardVO("NH농협", R.drawable.bank_nh_logo));
        cards.add(new CardVO("우리", R.drawable.bank_uri_logo));
        cards.add(new CardVO("신한", R.drawable.bank_sinhan_logo));
        cards.add(new CardVO("KB국민", R.drawable.bank_kb_logo));
        cards.add(new CardVO("하나", R.drawable.bank_hana_logo));
        cards.add(new CardVO("씨티", R.drawable.bank_city_logo));
        cards.add(new CardVO("IBK기업", R.drawable.bank_ibk_logo));
        cards.add(new CardVO("케이뱅크", R.drawable.bank_kbank_logo));
        cards.add(new CardVO("카카오뱅크", R.drawable.bank_kakaobank_logo));

        back = findViewById(R.id.back_search_bank_activity);
        next = findViewById(R.id.next_search_bank_activity);

        adapter = new GridAdapter_p(this, cards);
        gridView = (GridView) findViewById(R.id.cardGrid_p);
        gridView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchBankActivity.this, "등록할 계좌의 은행을 선택해주세요.", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onStart() {

        super.onStart();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), AccountNumberActivity.class);
                intent.putExtra("cardName", cards.get(i).getName());
                startActivity(intent);

            }
        });


    }


    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#ffffff"));//색 지정

    }
}