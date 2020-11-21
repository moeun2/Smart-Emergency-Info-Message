package com.passta.a2ndproj.start.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.CardVO;
import com.passta.a2ndproj.start.adapter.GridAdapter_p;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SearchBankActivity extends AppCompatActivity {
    GridView gridView;
    GridAdapter_p adapter;
    ArrayList<CardVO> cards = new ArrayList<>();

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bank);

        cards.add(new CardVO("NH농협",R.drawable.bank_nh_logo));
        cards.add(new CardVO("우리",R.drawable.bank_uri_logo));
        cards.add(new CardVO("신한",R.drawable.bank_sinhan_logo));
        cards.add(new CardVO("KB국민",R.drawable.bank_kb_logo));
        cards.add(new CardVO("하나",R.drawable.bank_hana_logo));
        cards.add(new CardVO("씨티",R.drawable.bank_city_logo));
        cards.add(new CardVO("IBK기업",R.drawable.bank_ibk_logo));
        cards.add(new CardVO("케이뱅크",R.drawable.bank_kbank_logo));
        cards.add(new CardVO("카카오뱅크",R.drawable.bank_kakaobank_logo));


        adapter = new GridAdapter_p(this, cards);
        gridView = (GridView)findViewById(R.id.cardGrid_p);
        gridView.setAdapter(adapter);
    }
    @Override
    public void onStart() {

        super.onStart();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                AccountVO list = AppManager.getInstance().getCurrentTransfer();
//                Object vo = (Object)adapterView.getAdapter().getItem(i);
//                String bank_name = null;
//                String img;
//
//                for (Field field : vo.getClass().getDeclaredFields()) {
//                    field.setAccessible(true);
//                    Object value = null; // 필드에 해당하는 값을 가져옵니다.
//                    try {
//                        value = field.get(vo);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    if(String.valueOf(field.getName()).equals("name"))
//                    {
//                        bank_name = String.valueOf(value);
//                        list.setBank(bank_name);
//                        Log.i("모은",bank_name);
//                    }
//                    if(String.valueOf(field.getName()).equals("image"))
//                    {
//                        img = String.valueOf(value);
//                        list.setImg(Integer.parseInt(img));
//                        Log.i("모은",img);
//                    }
//                }
//                AppManager.getInstance().setCurrentTransfer(list);
////                TransferActivity.activity.finish();
//                finish();
//
//                    Intent intent = new Intent(getApplicationContext(), AccountNumberActivity.class);
//                    startActivity(intent);

                    goToNextActivity(new AccountNumberActivity());
            }
        });


    }
    private void goToNextActivity(Activity activity) {
        finish();
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);
    }
}