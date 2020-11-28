package com.passta.a2ndproj.start.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.R;

import java.util.regex.Pattern;


public class AccountNumberActivity extends AppCompatActivity {
    public int MAX_COUNT; // 계좌 최대 입력 갯수
    private TextView next; // 동의 버튼
    private TextView bankNameTextView; // 은행 이름
    private ImageView back;
    TextView warningText; // 경고 문고
    EditText accountNumberEditText;
    boolean canNextButton;
    public String bankName;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_number);
        setStatusBar();

        Intent intent = getIntent();
        bankName = intent.getStringExtra("cardName");


        MAX_COUNT = 15;
        next = findViewById(R.id.next_account_number_activity);
        bankNameTextView = findViewById(R.id.bank_text_account_number_activity);
        accountNumberEditText = findViewById(R.id.txt_pay);
        back = findViewById(R.id.back_account_number_activity);
        warningText = findViewById(R.id.warningText);
        warningText.setText("");

        bankNameTextView.setText(bankName + " 은행의 계좌 번호를");

    }
    @Override
    public void onStart() {

        super.onStart();

        accountNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // 입력 글자 수 제한
                if(charSequence.length() > MAX_COUNT){
                    accountNumberEditText.setText(accountNumberEditText.getText().toString().substring(0,MAX_COUNT));
                    accountNumberEditText.setSelection(accountNumberEditText.length());
                }

                Pattern ps = Pattern.compile("^[0-9]{11,15}|''+$");

                if (!ps.matcher(charSequence).matches()) {
                    warningText.setText("계좌번호를 확인 바랍니다.");
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
                    Intent intent = new Intent(getApplicationContext(), AccountPasswordActivity.class);
                    intent.putExtra("cardName", bankName);
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