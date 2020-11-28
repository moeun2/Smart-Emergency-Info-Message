package com.passta.a2ndproj.main.Dialog;

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

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.MsgDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.main.DataVO.Msg_VO;
import com.passta.a2ndproj.main.Callback.DialogDeleteListener;

public class CheckDeleteMsgDialog extends Dialog {
    private Button cancelButton, deleteButton;
    private DialogDeleteListener dialogDeleteListener;

    public CheckDeleteMsgDialog(@NonNull Context context, MainActivity mainActivity, Msg_VO msgVo, int chlidPosition,
                                DialogDeleteListener dialogDeleteListener) {
        super(context);
        this.dialogDeleteListener = dialogDeleteListener;

        setContentView(R.layout.dialog_check_delete_msg);

        //디바이스크기에맞게 가로사이즈 지정하기위함
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = this.getWindow();
        int x = (int) (size.x * 0.9f);
        int y = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setLayout(x, y);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.9f);

        cancelButton = findViewById(R.id.cancel_button_check_delete_msg_dialog);
        deleteButton = findViewById(R.id.delete_button_check_delete_msg_dialog);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // oneDayMSgList 에서 삭제 -> msgList 에서 삭제 -> db에서 삭제.

                //삭제
                Loop1:
                for (int i = 0; i < mainActivity.oneDayMsgDataList.size(); i++) {

                    int size = mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().size();

                    Loop2:
                    for (int j = 0; j < size; j++) {

                        if (mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().get(j).getId() ==
                                msgVo.getId()) {
                            mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().remove(j);

                            //메세지 하나도 없을 경우 날짜 자체를 삭제
                            if (mainActivity.oneDayMsgDataList.get(i).getMsgArrayList().size() == 0) {
                                mainActivity.oneDayMsgDataList.remove(i);
                            }
                            break Loop1;
                        }
                    }
                }

                for (int i = 0; i < mainActivity.msgDataList.size(); i++) {
                    if (mainActivity.msgDataList.get(i).getId() == msgVo.getId()) {
                        mainActivity.msgDataList.remove(i);
                        break;
                    }
                }

                new MsgDataDeleteAsyncTask(mainActivity.db.MsgDAO(), msgVo.getId()).execute();

                mainActivity.oneDayMsgRecyclerViewAdapter.notifyDataSetChanged();
                dialogDeleteListener.dissmissListener();
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        show();

    }

    //장소 삭제 클래스
    public class MsgDataDeleteAsyncTask extends AsyncTask<UserListDTO, Void, Void> {
        private MsgDAO msgDAO;
        private int id;

        public MsgDataDeleteAsyncTask(MsgDAO msgDAO, int id) {
            this.msgDAO = msgDAO;
            this.id = id;
        }

        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {

            msgDAO.deleteOneMsg(id);
            return null;
        }

    }
}
