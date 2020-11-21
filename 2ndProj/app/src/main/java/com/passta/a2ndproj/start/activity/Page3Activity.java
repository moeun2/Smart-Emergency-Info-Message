package com.passta.a2ndproj.start.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.FilterDAO;
import com.passta.a2ndproj.data.FilterDTO;
import com.warkiz.widget.IndicatorSeekBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class Page3Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView next;
    private TextView back;
    private IndicatorSeekBar seekbar1;
    private IndicatorSeekBar seekbar2;
    private IndicatorSeekBar seekbar3;
    private IndicatorSeekBar seekbar4;
    private IndicatorSeekBar seekbar5;
    private int seekbar1_progress;
    private int seekbar2_progress;
    private int seekbar3_progress;
    private int seekbar4_progress;
    private int seekbar5_progress;
    private List<FilterDTO> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        setStatusBar();
        InitializeView();
        SetListener();
    }
    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#6bc7ee"));//색 지정

    }
    public void InitializeView()
    {

        next = (TextView)findViewById(R.id.next);
        back = (TextView)findViewById(R.id.back);
        seekbar1 = (IndicatorSeekBar)findViewById(R.id.seekbar1);
        seekbar2 = (IndicatorSeekBar)findViewById(R.id.seekbar2);
        seekbar3 = (IndicatorSeekBar)findViewById(R.id.seekbar3);
        seekbar4 = (IndicatorSeekBar)findViewById(R.id.seekbar4);
        seekbar5 = (IndicatorSeekBar)findViewById(R.id.seekbar5);

    }

    public void SetListener(){
        next.setOnClickListener(this);
        back.setOnClickListener(this);

    }


    //4,3,2,1
    public void onClick(View v){
        switch (v.getId()){
            case R.id.next:
                getSeekbarProgress();
                goToNextActivity(new Page4Activity());
                break;
            case R.id.back:
                goToNextActivity(new Page2Activity());
                break;
        }
    }
    private void goToNextActivity(Activity activity) {
        finish();
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);
    }
    private void getSeekbarProgress(){

         seekbar1_progress = Math.abs(seekbar1.getProgress() - 5);
         seekbar2_progress = Math.abs(seekbar2.getProgress() - 5);
         seekbar3_progress = Math.abs(seekbar3.getProgress() - 5);
         seekbar4_progress = Math.abs(seekbar4.getProgress() - 5);
         seekbar5_progress = Math.abs(seekbar5.getProgress() - 5);


        //db생성
        AppDatabase db = AppDatabase.getInstance(this);
        new DatabaseAsyncTask(db.filterDAO()).execute();
    }


    public class DatabaseAsyncTask extends AsyncTask<FilterDTO,Void,Void> {

        private FilterDAO filterDAO;

        DatabaseAsyncTask(FilterDAO filterDAO)
        {
            this.filterDAO = filterDAO;
        }
        @Override
        protected Void doInBackground(FilterDTO... filterDTOS) {
            list = filterDAO.loadFilterList();
            if(list.size() == 0)
            {
                filterDAO.insert(new FilterDTO(0,seekbar1_progress,seekbar2_progress,seekbar3_progress,seekbar4_progress,seekbar5_progress));
                list = filterDAO.loadFilterList();
            }
            else{
                filterDAO.update(new FilterDTO(0,seekbar1_progress,seekbar2_progress,seekbar3_progress,seekbar4_progress,seekbar5_progress));
                for(int i=0;i<list.size();i++)
                {
                    Log.i("모은 filter db 확인", String.valueOf(list.get(i).getFilter_1()));
                }
            }
            return null;
        }
    }




}