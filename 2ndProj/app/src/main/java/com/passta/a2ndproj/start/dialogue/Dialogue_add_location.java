package com.passta.a2ndproj.start.dialogue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.R;
import com.passta.a2ndproj.start.activity.Page2Activity;
import com.passta.a2ndproj.start.adapter.AdapterImageLocation;
import com.passta.a2ndproj.start.adapter.Adapter_location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dialogue_add_location extends AppCompatActivity implements View.OnClickListener {

    private TextView current_location;
    private TextView set_location;
    private Button confirm;
    private Button cancel;
    private TextView location;
    private EditText tag_editing;
    private RecyclerView imgRecyclerView;
    private ArrayList<Integer> imgIdList;
    private LinearLayout spaceView;

    private LocationManager locationManager;
    private static final String TAG = "dialogue_add_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue_add_location);

        InitializeView();
        SetListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent data) {

        super.onActivityResult(requestCode, resultcode, data);
        Log.d("모은", "onActivityResult(add)");

        if (resultcode == RESULT_OK) {
            String location_si = data.getStringExtra("location_si");
            String location_gu = data.getStringExtra("location_gu");
            Log.d("모은", location_si + " " + location_gu);
            location.setVisibility(View.VISIBLE);
            location.setText(location_si + " " + location_gu);

        }
    }

    public void InitializeView() {
        current_location = findViewById(R.id.current_location);
        set_location = findViewById(R.id.set_location);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        location = findViewById(R.id.location);
        tag_editing = findViewById(R.id.edit_tag_text);
        imgRecyclerView = findViewById(R.id.recyclerview_add_location);
        spaceView = findViewById(R.id.spaceview_add_location);

        imgIdList = new ArrayList<>();
        imgIdList.add(R.drawable.home);
        imgIdList.add(R.drawable.school);
        imgIdList.add(R.drawable.company);
        imgIdList.add(R.drawable.home2);
        imgIdList.add(R.drawable.school2);
        imgIdList.add(R.drawable.company2);
        imgIdList.add(R.drawable.home3);
        imgIdList.add(R.drawable.cafe1);
        imgIdList.add(R.drawable.foodshop);
        imgIdList.add(R.drawable.sport1);

        AdapterImageLocation adapterImageLocation = new AdapterImageLocation(imgIdList);
        imgRecyclerView.setAdapter(adapterImageLocation);
        imgRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(45));

        //디바이스크기에맞게 가로사이즈 지정하기위함
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = this.getWindow();
        int x = (int) (size.x * 0.9f);
        int y = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setLayout(x, y);

    }

    public void SetListener() {
        current_location.setOnClickListener(this);
        set_location.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        tag_editing.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    spaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int) getResources().getDimension(R.dimen.space)));
                }
                else {
                    //포커스 떠날때 키보드내리기
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tag_editing.getWindowToken(), 0);

                    if(tag_editing.getText().toString().equals("")){
                        spaceView.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.current_location:


                getLocation();

                break;
            case R.id.set_location:
                Intent intent = new Intent(getBaseContext(), Dialogue_select_location.class);
                startActivityForResult(intent, 1003);
                break;
            case R.id.confirm:

                // tag 랑 위치설정 둘 다 다 되어있는지 확인
                if (tag_editing.getText().toString().equals("") || tag_editing.getText().toString() == null) {
                    Toast.makeText(this, "태그 편집이 필요합니다", Toast.LENGTH_SHORT).show();
                } else if (location.getText() == null) {
                    Toast.makeText(this, "지역선택이 필요합니다", Toast.LENGTH_SHORT).show();
                } else if (tag_editing.getText().toString() != null && location.getText() != null) {
                    intent = new Intent(getApplicationContext(), Page2Activity.class);
                    intent.putExtra("tag", tag_editing.getText().toString());
                    intent.putExtra("location", location.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        } else {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


            double lng = 0;
            double lat = 0;


            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (lastKnownLocation != null) {
                lng = lastKnownLocation.getLongitude();
                lat = lastKnownLocation.getLatitude();

                Log.d(TAG, "longtitude=" + lng + ", latitude=" + lat);
                ReverseGeocoding(lng, lat);
            }
        }


    }


    public void ReverseGeocoding(double lng, double lat) {

        //위도와 경도 값으로 고유 명칭 얻기
        List<Address> address = null;
        Geocoder g = new Geocoder(getApplicationContext());

        try {
            address = g.getFromLocation(lat, lng, 10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "입출력오류");
        }
        if (address != null) {
            if (address.size() == 0) {
                Toast.makeText(getApplicationContext(), "주소 찾기 오류(Geocoder)", Toast.LENGTH_SHORT);
            } else {

                Log.d("모은", "찾은 주소 : " + address.get(0).getAdminArea());
                Log.d("모은", "찾은 주소 : " + address.get(0).getAddressLine(0));

                String address_str = address.get(0).getAddressLine(0);
                String[] lst = address_str.split(" ");


                location.setText(lst[1] + " " + lst[2]);
                location.setVisibility(View.VISIBLE);
            }
        }
    }

    public void checkPermission() {
        //Android 6.0 부터는 source 안에서도 권한 체크 필요
//        android.permission.ACCESS_FINE_LOCATION (GPS허가)
//        android.permission.ACCESS_COARSE_LOCATION (셀룰러 허가)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    //공백 추가 클래스
    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // 마지막 아이템이 아닌 경우, 공백 추가
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.right = verticalSpaceHeight;
            }
        }
    }

}
