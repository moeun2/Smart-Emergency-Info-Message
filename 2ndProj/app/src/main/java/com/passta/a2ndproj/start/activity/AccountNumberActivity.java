package com.passta.a2ndproj.start.activity;

import android.content.Intent;
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
    public static int BANK;
    Button button; // 동의 버튼
    TextView bankName; // 은행 이름
    TextView warningText; // 경고 문고
    EditText accountNumberEditText;
    boolean canNextButton;
    ImageView back_btn;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_number);

        MAX_COUNT = 15;
        button = findViewById(R.id.btn_next);
        bankName = findViewById(R.id.bankText);
        accountNumberEditText = findViewById(R.id.txt_pay);
        back_btn = findViewById(R.id.agreement_back_btn);
        warningText = findViewById(R.id.warningText);
        warningText.setText("");
        BANK = 0;

        TextView bankText = findViewById(R.id.bankText);
//        AccountVO accountVO = AppManager.getInstance().getCurrentTransfer();
//        bankText.setText(accountVO.getBank());


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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canNextButton == true){


                    Intent intent = new Intent(getApplicationContext(), AccountPasswordActivity.class);

                    startActivity(intent);
                }
            }
        });

    }


}