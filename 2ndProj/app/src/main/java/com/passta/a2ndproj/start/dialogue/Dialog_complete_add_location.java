package com.passta.a2ndproj.start.dialogue;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.start.activity.Page2Activity;

import org.w3c.dom.Text;

public class Dialog_complete_add_location extends AppCompatActivity {

    private Button confirmButton;
    private String nowType,location,tag;
    private int imgNumber;
    private TextView tagTextView,loactionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue_complete_add_location2);

        Log.d("test","Test");
        Intent intent1 = getIntent();
        nowType = intent1.getStringExtra("nowType");
        tag = intent1.getStringExtra("tag");
        location = intent1.getStringExtra("location");
        imgNumber = intent1.getIntExtra("imgNumber", 0);

        //디바이스크기에맞게 가로사이즈 지정하기위함
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = this.getWindow();
        int x = (int) (size.x * 0.9f);
        int y = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setLayout(x, y);
        Log.d("test",nowType);
        //밝기 조정
        if(nowType.equals("main")){
            getWindow().setDimAmount(0.88f);
        }



        confirmButton = findViewById(R.id.confirm_complete_add_location);
        tagTextView = findViewById(R.id.name_tag_dialog_complete_add_location);
        loactionTextView = findViewById(R.id.loaction_dialog_complete_add_location);

        tagTextView.setText(tag);
        loactionTextView.setText(location);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nowType.equals("start")) {
                    Intent intent = new Intent(getApplicationContext(), Page2Activity.class);
                    intent.putExtra("tag", tag);
                    intent.putExtra("location", location);
                    intent.putExtra("imgNumber", imgNumber);
                    intent.putExtra("isFinished",true);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if(nowType.equals("main")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("tag", tag);
                    intent.putExtra("location", location);
                    intent.putExtra("imgNumber", imgNumber);
                    intent.putExtra("isFinished",true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

}
