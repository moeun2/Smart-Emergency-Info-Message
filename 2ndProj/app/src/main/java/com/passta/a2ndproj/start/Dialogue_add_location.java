package com.passta.a2ndproj.start;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.passta.a2ndproj.R;

import java.io.IOException;
import java.util.List;

public class Dialogue_add_location extends AppCompatActivity implements View.OnClickListener {

    private TextView current_location;
    private TextView set_location;
    private TextView confirm;
    private TextView cancel;
    private TextView location;

    private LocationManager locationManager;
    private static final String TAG = "dialogue_add_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogue_add_location);


        InitializeView();
        SetListener();




    }

    public void InitializeView()
    {
        current_location = findViewById(R.id.current_location);
        set_location = findViewById(R.id.set_location);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        location = findViewById(R.id.location);
    }

    public void SetListener(){
        current_location.setOnClickListener(this);
        set_location.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.current_location:
                getLocation();

                break;
            case R.id.set_location:
                Intent intent = new Intent(getBaseContext(),Dialogue_select_location.class);
                startActivity(intent);
                break;
            case R.id.confirm:
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    public void getLocation()
    {
        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        checkPermission();

        double lng = 0;
        double lat = 0;

        //최초 위치만을 가져오게 설정함 (location change 무시 -> 필요시 리스너 추가 가능)
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (lastKnownLocation != null) {
            lng = lastKnownLocation.getLongitude();
            lat = lastKnownLocation.getLatitude();

            Log.d(TAG, "longtitude=" + lng + ", latitude=" + lat);
            ReverseGeocoding(lng, lat);
        }




    }



    public void ReverseGeocoding(double lng, double lat){

        //위도와 경도 값으로 고유 명칭 얻기
        List<Address> address=null;
        Geocoder g = new Geocoder(getApplicationContext());

        try {
            address = g.getFromLocation(lat,lng,10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"입출력오류");
        }
        if(address!=null){
            if(address.size()==0){
                Toast.makeText(getApplicationContext(),"주소 찾기 오류(Geocoder)",Toast.LENGTH_SHORT);
            }else{
                Log.d(TAG,"찾은 주소 : "+ address.get(0).toString());
                location.setText("hi");
                location.setVisibility(View.VISIBLE);
            }
        }
    }
    public void checkPermission()
    {
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

}
