package com.passta.a2ndproj.start.dialogue;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.start.activity.Page2Activity;

import java.util.List;

public class CheckDeleteLocationInPage2 extends Dialog {
    private Button cancelButton, deleteButton;
    private Page2Activity page2Activity;
    private int position;

    public CheckDeleteLocationInPage2(@NonNull Context context, int position,String tag) {
        super(context);
        this.position = position;
        this.page2Activity = (Page2Activity)context;
        setContentView(R.layout.dialog_check_delete_location);

        AppDatabase db = AppDatabase.getInstance(context);


        //디바이스크기에맞게 가로사이즈 지정하기위함
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = this.getWindow();
        int x = (int) (size.x * 0.9f);
        int y = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setLayout(x, y);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.81f);

        cancelButton = findViewById(R.id.cancel_button_check_delete_dialog);
        deleteButton = findViewById(R.id.delete_button_check_delete_dialog);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //삭제 진행
                new UserListDataDeleteAsyncTask(db.userListDAO(),tag).execute();
                page2Activity.list.remove(position);
                page2Activity.mAdapter.notifyDataSetChanged();
                dismiss();
            }
        });

        show();

    }

    //장소 삭제 클래스
    public class UserListDataDeleteAsyncTask extends AsyncTask<UserListDTO, Void, Void> {
        private UserListDAO userListDAO;
        private String tag;

        UserListDataDeleteAsyncTask(UserListDAO userListDAO, String tag) {
            this.userListDAO = userListDAO;
            this.tag = tag;
        }

        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {

            userListDAO.delete(tag);
            return null;
        }

    }
}
