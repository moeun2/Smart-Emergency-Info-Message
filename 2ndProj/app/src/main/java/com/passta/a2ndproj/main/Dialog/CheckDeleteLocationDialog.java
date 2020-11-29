package com.passta.a2ndproj.main.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.MsgDAO;
import com.passta.a2ndproj.data.MsgDTO;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;

import java.util.List;

public class CheckDeleteLocationDialog extends Dialog {
    private Button cancelButton, deleteButton;
    private int position;
    private MainActivity mainActivity;

    public CheckDeleteLocationDialog(@NonNull Context context, int position) {
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                if (mainActivity.hashtagUpDataList.size() == 3) {
                    Toast.makeText(context, "수신 지역은 반드시 한개 이상 저장 돼 있어야 합니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mainActivity.calculateUpHashtagClickedNumber() == 1 && mainActivity.hashtagUpDataList.get(position).isClicked()) {
                    Toast.makeText(context, "수신 지역은 반드시 한개 이상 할당 돼 있어야 합니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                //삭제 주소.
                String removedLocation = mainActivity.userList.get(position - 2).getLocation_si() + " " + mainActivity.userList.get(position - 2).getLocation_gu();
                String removedLocation2 = mainActivity.userList.get(position - 2).getLocation_si() + " " + "전체";

                boolean hasSameSi = false;
                boolean isTotalGu = false;

                //문자를 삭제할때 같은 si 가 없으면 ~~시 전체 문자도 삭제해주고 있다면 ~~시 전체문자는 삭제해 주지않는다.
                for (int i = 0; i < mainActivity.userList.size(); i++) {
                    String temp = mainActivity.userList.get(i).getLocation_si();

                    if (i != (position - 2) && temp.equals(removedLocation.split(" ")[0])) {
                        hasSameSi = true;
                        break;
                    }
                }

                if (mainActivity.userList.get(position - 2).getLocation_gu().equals("전체"))
                    isTotalGu = true;

                // 삭제된 장소가 ~~시 전체 인 경우
                if (isTotalGu) {

                    int size = mainActivity.msgDataList.size();

                    //데베에서 삭제하는 작업.
                    LoopP:
                    for (int i = 0; i < size; i++) {

                        //문자 메세지가 삭제하려는 시랑 같은 시 일 경우 들어가서
                        if (mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[0].equals(mainActivity.userList.get(position - 2).getLocation_si())) {

                            for (int j = 0; j < mainActivity.userList.size(); j++) {

                                //같은거 컨티뉴
                                if (j == position - 2)
                                    continue;

                                if(mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[1].equals("전체"))
                                    continue LoopP;

                                // 삭제하려는 ~~시 전체 에서 ~~시와 같은 시가 다른 해쉬태그로 저장돼있는경우
                                if (mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[1].equals(mainActivity.userList.get(j).getLocation_gu()))
                                    continue LoopP;
                            }
                            new MsgDataDeleteAsyncTaskInTotalGu(mainActivity.db.MsgDAO(),mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[0],
                                    mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[1]).execute();
                        }
                    }
                    //같은 시가 없는 경우에는 데베에서 ~~시 전체도 삭제
                    if (!hasSameSi)
                        new MsgDataDeleteAsyncTask(mainActivity.db.MsgDAO(), removedLocation2).execute();

                    if (hasSameSi) {

                        LoopP2:
                        for (int i = 0; i < size; i++) {

                            if (mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[0].equals(removedLocation.split(" ")[0])) {

                                for(int j=0;j<mainActivity.userList.size();j++) {

                                    //같은거 컨티뉴
                                    if (j == position - 2)
                                        continue;

                                    if(mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[1].equals("전체"))
                                        continue LoopP2;

                                    //유저리스트에 저장된 장소 중에 같은 시 다른 구 가 있을 경우 그경우는 continue
                                    if(mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[1].equals(
                                            mainActivity.userList.get(j).getLocation_gu()))
                                        continue LoopP2;
                                }
                                mainActivity.msgDataList.remove(i);
                                size--;
                                i--;
                            }
                        }
                    } else {
                        LoopP3:
                        for (int i = 0; i < size; i++) {

                            if (mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[0].equals(removedLocation.split(" ")[0])) {

                                for(int j=0;j<mainActivity.userList.size();j++) {

                                    //같은거 컨티뉴
                                    if (j == position - 2)
                                        continue;

                                    //유저리스트에 저장된 장소 중에 같은 시 다른 구 가 있을 경우 그경우는 continue
                                    if(mainActivity.msgDataList.get(i).getSenderLocation().split(" ")[1].equals(
                                            mainActivity.userList.get(j).getLocation_gu()) && !mainActivity.msgDataList.get(i).getSenderLocation()
                                            .split(" ")[1].equals("전체"))
                                        continue LoopP3;
                                }
                                mainActivity.msgDataList.remove(i);
                                size--;
                                i--;
                            }
                        }
                    }
                } else {

                    new MsgDataDeleteAsyncTask(mainActivity.db.MsgDAO(), removedLocation).execute();
                    //같은 시가 없는 경우에는 데베에서 ~~시 전체도 삭제
                    if (!hasSameSi)
                        new MsgDataDeleteAsyncTask(mainActivity.db.MsgDAO(), removedLocation2).execute();


                    int size = mainActivity.msgDataList.size();
                    if (hasSameSi) {
                        for (int i = 0; i < size; i++) {
                            if (mainActivity.msgDataList.get(i).getSenderLocation().equals(removedLocation)) {
                                mainActivity.msgDataList.remove(i);
                                size--;
                                i--;
                            }
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            if (mainActivity.msgDataList.get(i).getSenderLocation().equals(removedLocation) ||
                                    mainActivity.msgDataList.get(i).getSenderLocation().equals(removedLocation2)) {
                                mainActivity.msgDataList.remove(i);
                                size--;
                                i--;

                            }
                        }
                    }
                }
                //해쉬태그삭제
                new UserListDataDeleteAsyncTask(mainActivity.db.userListDAO(), mainActivity.userList.get(position - 2).getTag()).execute();
                mainActivity.hashtagUpDataList.remove(position);
                mainActivity.hashtagUpRecyclerViewAdapter.notifyDataSetChanged();

                //필터 데이터 분류
                mainActivity.classifyMsgData();
                mainActivity.createOneDayMsgDataList();
                mainActivity.oneDayMsgRecyclerViewAdapter.notifyDataSetChanged();

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

            List<UserListDTO> lst = userListDAO.loadUserList();
            mainActivity.userList = lst;
            return null;
        }

    }

    public class MsgDataDeleteAsyncTask extends AsyncTask<MsgDTO, Void, Void> {

        private MsgDAO msgDAO;
        private String location;

        public MsgDataDeleteAsyncTask(MsgDAO msgDAO, String location) {
            this.msgDAO = msgDAO;
            this.location = location;
        }

        @Override
        protected Void doInBackground(MsgDTO... msgDTOS) {
            msgDAO.delete(location);
            return null;
        }
    }

    public class MsgDataDeleteAsyncTaskInTotalGu extends AsyncTask<MsgDTO, Void, Void> {

        private MsgDAO msgDAO;
        private String locationSi, locationGu;

        public MsgDataDeleteAsyncTaskInTotalGu(MsgDAO msgDAO, String locationSi, String locationGu) {
            this.msgDAO = msgDAO;
            this.locationSi = locationSi;
            this.locationGu = locationGu;
        }

        @Override
        protected Void doInBackground(MsgDTO... msgDTOS) {
            msgDAO.deleteLocationSiGu(locationSi, locationGu);
            return null;
        }
    }

}
