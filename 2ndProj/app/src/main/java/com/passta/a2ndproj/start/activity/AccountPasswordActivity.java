package com.passta.a2ndproj.start.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.R;

import java.util.regex.Pattern;

public class AccountPasswordActivity extends AppCompatActivity {

    public int MAX_COUNT; // 계좌 비밀번호 갯수
    EditText passwordEditText;
    TextView warningText;
    boolean canNextButton;
    private ImageView back;
    private TextView next,bankText;
    private String bankName;
    int index;
    Intent secondIntent;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_password);
        setStatusBar();

        Intent intent = getIntent();
        bankName = intent.getStringExtra("cardName");

        MAX_COUNT = 4;
        warningText = findViewById(R.id.warningPasswordText);
        passwordEditText = findViewById(R.id.accountPasswordEdit);
        next = findViewById(R.id.next_account_password_activity);
        back = findViewById(R.id.back_account_password_activity);
        bankText = findViewById(R.id.bank_name_account_password_activity);
        index = 0;

        bankText.setText(bankName + " 은행의 계좌 비밀번호를");


    }

    @Override
    public void onStart() {

        super.onStart();

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                // 입력 글자 수 제한

                if(charSequence.length() > MAX_COUNT){
                    passwordEditText.setText(passwordEditText.getText().toString().substring(0,MAX_COUNT));
                    passwordEditText.setSelection(passwordEditText.length());
                }

                Pattern ps = Pattern.compile("^[0-9]{0,4}+$");

                if (!ps.matcher(charSequence).matches()) {
                    warningText.setText("비밀번호를 확인 바랍니다.");
                    canNextButton = false;
                }
                else{
                    warningText.setText("");
                    canNextButton = true;
                }

                if(charSequence.length() == 0){
                    warningText.setText("");
                    canNextButton = false;
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canNextButton == true){
                    Intent intent = new Intent(getApplicationContext(), AccountGetDataActivity.class);
                    startActivity(intent);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#ffffff"));//색 지정

    }

}
