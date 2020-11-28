package com.passta.a2ndproj.start.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.FilterDAO;
import com.passta.a2ndproj.data.FilterDTO;
import com.passta.a2ndproj.data.MsgDAO;
import com.passta.a2ndproj.data.MsgDTO;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.main.MsgCategoryPoint_VO;
import com.passta.a2ndproj.main.Msg_VO;
import com.passta.a2ndproj.network.RetrofitClient;
import com.passta.a2ndproj.network.ServiceApi;
import com.passta.a2ndproj.utility.RecyclerViewDecoration;
import com.passta.a2ndproj.start.adapter.Adapter_page2;
import com.passta.a2ndproj.start.dialogue.Dialogue_add_location;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Page2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Page2";
    private TextView add_location;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView next;
    private String tag;
    private String location_si;
    private String location_gu;
    public Adapter_page2 mAdapter;
    public List<UserListDTO> list;
    public List<FilterDTO> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        setStatusBar();

        //db생성
        AppDatabase db = AppDatabase.getInstance(this);
        new DatabaseAsyncTask(db.userListDAO()).execute();
        //filter db를 디폴트값으로 set해준다.
        new FiterDatabaseAsyncTask(db.filterDAO()).execute();

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
            int imgNumber = data.getIntExtra("imgNumber",0);
            Log.d("모은", tag + " " + location + Integer.toString(imgNumber));
            location_si = location.split(" ")[0];
            location_gu = location.split(" ")[1];

            UserListDTO lst = new UserListDTO(tag, location_si, location_gu,imgNumber,true);
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
        getWindow().setStatusBarColor(Color.parseColor("#ffffff"));//색 지정

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
        mAdapter = new Adapter_page2(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerViewDecoration(10)); // 높이 맞추기
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL)); // 구분선


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if(list.size()==0){
                    Toast.makeText(this, "수신 지역은 반드시 한개 이상 저장 돼 있어야 합니다.", Toast.LENGTH_LONG).show();
                    break;
                }
                Intent intent = new Intent(getApplicationContext(), Page3Activity.class);
                startActivity(intent);
                break;
            case R.id.add_location:
                intent = new Intent(getApplicationContext(), Dialogue_add_location.class);
                intent.putExtra("type","start");
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
//            if (list.size() == 0) {
//                Log.i("모은 데이터베이스", "null");
//                userListDAO.insert(new UserListDTO("모은", "서울특별시", "광진구",R.drawable.cafe1,true));
//            }
            list = userListDAO.loadUserList();
//            for (int i = 0; i < list.size(); i++) {
//                Log.i("모은 데이터베이스", "nullx");
//                Log.i("모은 데이터베이스", list.get(i).getTag());
//
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initialize_recyclerview();
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

    public class FiterDatabaseAsyncTask extends AsyncTask<FilterDTO,Void,Void> {

        private FilterDAO filterDAO;

        FiterDatabaseAsyncTask(FilterDAO filterDAO)
        {
            this.filterDAO = filterDAO;
        }
        @Override
        protected Void doInBackground(FilterDTO... filterDTOS) {
            filterList = filterDAO.loadFilterList();
            if(filterList.size() == 0)
            {
                filterDAO.insert(new FilterDTO(0,2,2,2,2,2));
                filterList = filterDAO.loadFilterList();
            }

            return null;
        }
    }

}