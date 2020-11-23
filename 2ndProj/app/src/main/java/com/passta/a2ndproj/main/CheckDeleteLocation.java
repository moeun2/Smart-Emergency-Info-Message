package com.passta.a2ndproj.main;

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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;

import java.util.List;

public class CheckDeleteLocation extends Dialog {
    private Button cancelButton, deleteButton;
    private int position;
    private MainActivity mainActivity;

    public CheckDeleteLocation(@NonNull Context context,int position) {
        super(context);
        this.position = position;
        this.mainActivity = (MainActivity) context;

        setContentView(R.layout.dialog_check_delete_location);

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

                if(mainActivity.hashtagUpDataList.size() == 2){
                    Toast.makeText(context, "수신 지역은 반드시 한개 이상 저장 돼 있어야 합니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mainActivity.calculateUpHashtagClickedNumber() == 1 && mainActivity.hashtagUpDataList.get(position).isClicked()){
                    Toast.makeText(context, "수신 지역은 반드시 한개 이상 할당 돼 있어야 합니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                new UserListDatabaseDeleteAsyncTask(mainActivity.db.userListDAO() ,mainActivity.userList.get(position-1).getTag()).execute();
                mainActivity.hashtagUpDataList.remove(position);
                mainActivity.hashtagUpRecyclerViewAdapter.notifyDataSetChanged();
                dismiss();
            }
        });

        show();
    }

    //장소 삭제 클래스
    public class UserListDatabaseDeleteAsyncTask extends AsyncTask<UserListDTO, Void, Void> {
        private UserListDAO userListDAO;
        private String tag;

        UserListDatabaseDeleteAsyncTask(UserListDAO userListDAO,String tag) {
            this.userListDAO = userListDAO;
            this.tag = tag;
        }

        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {

            userListDAO.delete(tag);

            List<UserListDTO> lst = userListDAO.loadUserList();
            mainActivity.userList = lst;
            return null;
        }
    }
}
