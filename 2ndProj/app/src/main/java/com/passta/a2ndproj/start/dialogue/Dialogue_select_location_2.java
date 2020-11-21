package com.passta.a2ndproj.start.dialogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.utility.RecyclerViewDecoration;
import com.passta.a2ndproj.start.adapter.Adapter_location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Dialogue_select_location_2 extends AppCompatActivity {

    private ArrayList<String> mArrayList = new ArrayList<>();;
    private Adapter_location mAdapter;
    private RecyclerView mRecyclerView;
    private int numberOfColumns;
    private static final String TAG = "Dialogue_select_location_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue_select_location_2);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);

        InitializeView();
        initialize_recyclerview(1, position);


    }
    public void InitializeView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);

    }
    public void initialize_recyclerview(int numberOfColumns, int position)
    {
        numberOfColumns = numberOfColumns;
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
//        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        String filename = "gu_"+Integer.toString(position);
        set_list(mArrayList,filename);



        mAdapter = new Adapter_location(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerViewDecoration(50)); // 높이 맞추기
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL)); // 구분선


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String str = mArrayList.get(position).toString();

                Toast.makeText(getApplicationContext(), "position : " + position + "/ String : " + str, Toast.LENGTH_LONG).show();
                Dialogue_select_location dsl = (Dialogue_select_location)Dialogue_select_location.activity;


                Log.d("모은", "addOnItemTouchListener");
                Intent intent = new Intent(getApplicationContext(),Dialogue_select_location.class);
                intent.putExtra("position", position);
                intent.putExtra("location_gu", str);
                setResult(RESULT_OK, intent);

                finish();

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Dialogue_select_location_2.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Dialogue_select_location_2.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    private void set_list(ArrayList<String> mArrayList,String filename)
    {
//        mArrayList.add("서울특별시");
//        mArrayList.add("부산광역시");

        mArrayList.clear();


        int id = getResources().getIdentifier(filename, "raw", getPackageName());
        InputStream inputData = getResources().openRawResource(id);

        try {
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputData,"MS949"));
            while(true){
                String string= bufferedReader.readLine();

                if(string != null){
                    mArrayList.add(string);
                }else{

                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }
}