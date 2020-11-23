package com.passta.a2ndproj.start.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.utility.RecyclerViewDecoration;
import com.passta.a2ndproj.start.adapter.Adapter_page2;
import com.passta.a2ndproj.start.dialogue.Dialogue_add_location;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class Page2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Page2";
    private TextView add_location;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView next;
    private String tag;
    private String location_si;
    private String location_gu;
    private Adapter_page2 mAdapter;
    private List<UserListDTO> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        setStatusBar();

        //db생성
        AppDatabase db = AppDatabase.getInstance(this);
        new DatabaseAsyncTask(db.userListDAO()).execute();

        InitializeView();
        SetListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent data) {

        super.onActivityResult(requestCode, resultcode, data);
        Log.d("모은", "onActivityResult(page2)");

        if (resultcode == RESULT_OK) {


            tag = data.getStringExtra("tag");
            String location = data.getStringExtra("location");
            Log.d("모은", tag + " " + location);
            location_si = location.split(" ")[0];
            location_gu = location.split(" ")[1];

            UserListDTO lst = new UserListDTO(tag, location_si, location_gu);
            AppDatabase db = AppDatabase.getInstance(this);
            new DatabaseInsertAsyncTask(db.userListDAO(), lst).execute();
            list.add(lst);
            mAdapter.notifyDataSetChanged();
            Log.d("모은", location_si + " " + location_gu);

        }
    }


    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#6bc7ee"));//색 지정

    }

    public void InitializeView() {
        add_location = (TextView) findViewById(R.id.add_location);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        next = (TextView) findViewById(R.id.next);

    }

    public void SetListener() {
        add_location.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    public void initialize_recyclerview() {
        int numberOfColumns = 1;
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
//        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        if (list == null) {
            Log.d("모은", "null이라네");
        }
        mAdapter = new Adapter_page2(list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerViewDecoration(10)); // 높이 맞추기
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL)); // 구분선


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                Intent intent = new Intent(getApplicationContext(), Page3Activity.class);
                startActivity(intent);
                break;
            case R.id.add_location:
                intent = new Intent(getApplicationContext(), Dialogue_add_location.class);
                startActivityForResult(intent, 1003);
                break;
        }
    }

    public class DatabaseAsyncTask extends AsyncTask<UserListDTO, Void, Void> {


        private UserListDAO userListDAO;

        DatabaseAsyncTask(UserListDAO userListDAO) {
            this.userListDAO = userListDAO;
        }


        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {
            list = userListDAO.loadUserList();
            if (list.size() == 0) {
                Log.i("모은 데이터베이스", "null");
                userListDAO.insert(new UserListDTO("모은", "서울특별시", "광진구"));
            }
            list = userListDAO.loadUserList();
            for (int i = 0; i < list.size(); i++) {
                Log.i("모은 데이터베이스", "nullx");
                Log.i("모은 데이터베이스", list.get(i).getTag());

                initialize_recyclerview();

            }

            return null;
        }
    }

    public class DatabaseInsertAsyncTask extends AsyncTask<UserListDTO, Void, Void> {
        private UserListDAO userListDAO;
        private UserListDTO userListDTO;

        DatabaseInsertAsyncTask(UserListDAO userListDAO, UserListDTO userListDTO) {
            this.userListDAO = userListDAO;
            this.userListDTO = userListDTO;
        }


        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {


            Log.i("모은 데이터베이스", "insert");
            userListDAO.insert(userListDTO);

            List<UserListDTO> lst = userListDAO.loadUserList();
            for (int i = 0; i < lst.size(); i++) {
                Log.i("모은 데이터베이스", "nullx");
                Log.i("모은 데이터베이스", lst.get(i).getTag());

            }
            list = lst;


            return null;
        }
    }


}