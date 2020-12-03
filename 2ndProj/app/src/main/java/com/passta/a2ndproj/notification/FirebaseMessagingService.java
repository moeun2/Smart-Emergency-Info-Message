package com.passta.a2ndproj.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.text.Html;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.passta.a2ndproj.IntroLoadingActivity;
import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.FilterDAO;
import com.passta.a2ndproj.data.FilterDTO;
import com.passta.a2ndproj.data.MsgDAO;
import com.passta.a2ndproj.data.MsgDTO;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.main.DataVO.MsgCategoryPoint_VO;
import com.passta.a2ndproj.main.DataVO.Msg_VO;
import com.passta.a2ndproj.start.activity.Page2Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";


    private String  chId;



    public ArrayList<Msg_VO> classifiedMsgDataList;
    public ArrayList<Msg_VO> msgDataList;


    private List<UserListDTO> list;
    public List<UserListDTO> locationList;

    private List<MsgDTO> msgList;
    private List<FilterDTO> filterList;

    //MsgVO
    private int id;
    private String title,msg, sido, gusi, sendingDate,senderLocation;
    private MsgCategoryPoint_VO msgCategoryPoint; //index 0. 동선 1. 발생방역 2. 안전수칙 3.재난날씨 4. 경제금융
    private int level;
    private int circleImageViewId;
    private int categroyIndex;
    private double totalMsgPoint;
    ArrayList<String> dateList;

    // MsgCategoryPoint_VO
    private double coronaRoute;
    private double coronaUpbreak;
    private double coronaSafetyRule;
    private double economy;
    private double disaster;

    private MsgDTO msgDTO;

    boolean flag;
    boolean allowsReciving;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private boolean isCheckedLevel1;
    private boolean isCheckedLevel2;
    private boolean isCheckedLevel3;

    private boolean isCheckedAudioNotification;

    private boolean isCheckedVibrationNotification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        pref = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
        allowsReciving = pref.getBoolean("allowsReciving",true);
        isCheckedLevel1 = pref.getBoolean("isCheckedLevel1", true);
        isCheckedLevel2 = pref.getBoolean("isCheckedLevel2", true);
        isCheckedLevel3 = pref.getBoolean("isCheckedLevel3", true);
        isCheckedAudioNotification = pref.getBoolean("isCheckedAudioNotification", true);

        isCheckedVibrationNotification = pref.getBoolean("isCheckedVibrationNotification", true);

        Log.i("모은","isCheckedLevel1 : "+isCheckedLevel1);
        Log.i("모은","isCheckedAudioNotification : "+isCheckedAudioNotification);
        Log.i("모은","isCheckedVibrationNotification : "+isCheckedVibrationNotification);
        ExecutorService es = Executors.newSingleThreadExecutor();

        es.submit(this::getUserInfo);
        es.submit(this::getFilterInfo);
        es.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("모은","onMessageReceived");
                Log.d(TAG, "From: " + remoteMessage.getFrom());

                // Check if message contains a data payload.
                if (remoteMessage.getData().size() > 0) {
                    Log.e(TAG, "Message data payload: " + remoteMessage.getData().get("my_key"));
                    Log.i("모은", "시간: " + remoteMessage.getData().get("time"));


                    if (/* Check if data needs to be processed by long running job */ true) {
                        // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                        scheduleJob();
                    } else {
                        // Handle message within 10 seconds
                        handleNow();
                    }

                }

                chId = "test v1";

                msg = remoteMessage.getData().get("bodyStr");
                Html.fromHtml(msg);
                title = remoteMessage.getData().get("title");
                sido = remoteMessage.getData().get("sido");
                gusi = remoteMessage.getData().get("gusi");

                coronaRoute = Double.parseDouble(remoteMessage.getData().get("co_route"));
                coronaUpbreak = Double.parseDouble(remoteMessage.getData().get("co_utbreak_quarantine"));
                coronaSafetyRule = Double.parseDouble(remoteMessage.getData().get("co_safetyTips"));
                economy = Double.parseDouble(remoteMessage.getData().get("economy_finance"));
                disaster = Double.parseDouble(remoteMessage.getData().get("disaster_weather"));
                msgCategoryPoint = new MsgCategoryPoint_VO(coronaRoute,coronaUpbreak,coronaSafetyRule,disaster,economy);


                senderLocation = sido +" "+ gusi;
                sendingDate = remoteMessage.getData().get("sendingDate");
                dateList = returnDayString(sendingDate);


            }
        });
        es.submit(this::setCategory);
        es.submit(this::calculateTotalMsgPointAndLevel);

        es.submit(this::insertDatabase);

//        es.submit(this::setNotification);
        es.shutdown();

//        setCategory();
//        calculateTotalMsgPointAndLevel();










    }

    public void insertDatabase()
    {
        Log.i("모은","insertDatabaseq");
        flag = false;
//        Log.i("모은","flag1 = "+Boolean.toString(flag));
        msgDTO = new MsgDTO( dateList.get(0), dateList.get(1), msg, senderLocation, level, circleImageViewId, msgCategoryPoint.getCoronaRoute(),msgCategoryPoint.getCoronaUpbreak(),msgCategoryPoint.getCoronaSafetyRule(), msgCategoryPoint.getDisaster(),msgCategoryPoint.getEconomy(), totalMsgPoint, categroyIndex);


        Log.i("모은","insertDatabase2");
        for (int i = 0 ; i< list.size(); i++)
        {

            Log.i("모은","insertDatabase3");
            Log.i("모은","원db"+list.get(i).getLocation_si());
            Log.i("모은","받은거"+sido);
            Log.i("모은","원db"+list.get(i).getLocation_gu());
            Log.i("모은","받은거"+gusi);
            //전체 예외처리 해야함

            if ((list.get(i).getLocation_si().equals(sido) && list.get(i).getLocation_gu().equals(gusi)) || list.get(i).getLocation_si().equals("중대본") || sido.equals("중대본") || (list.get(i).getLocation_si().equals(sido) && gusi.equals("전체")) ||(list.get(i).getLocation_si().equals(sido) && list.get(i).getLocation_gu().equals("전체")) )
            {
                Log.i("모은","insertDatabase4");

                Log.i("모은",sido);

                Log.i("모은",gusi);
                flag = true;
                Log.i("모은","flag2 = "+Boolean.toString(flag));
                AppDatabase db = AppDatabase.getInstance(this);
                new DatabaseMsgInsertAsyncTask(db.MsgDAO(),msgDTO).execute();
                break;

            }
        }


    }
    public void getMsgData(){

        Log.i("모은","getMsgData");
        AppDatabase db = AppDatabase.getInstance(this);
        new DatabaseGetMsgAsyncTask(db.MsgDAO()).execute();
    }

    public void getUserInfo()
    {
        Log.i("모은","getUserInfo");
        AppDatabase db = AppDatabase.getInstance(this);
        new DatabaseGetLocationAsyncTask(db.userListDAO()).execute(); //유저 위치 정보 불러오기

    }
    public void getFilterInfo()
    {
        Log.i("모은","getFilterInfo");
        AppDatabase db = AppDatabase.getInstance(this);
        new DatabaseFilterAsyncTask(db.filterDAO()).execute(); // 유저 필터 정보 불러오기
    }

    public ArrayList<String> returnDayString(String sendingTime){

        String dayString = sendingTime.split(" ")[0];
        String timeString = sendingTime.split(" ")[1];

        dayString = dayString.substring(0,dayString.indexOf("/")) + "년 " +
                dayString.substring(dayString.indexOf("/") + 1,dayString.lastIndexOf("/")) + "월 " + dayString.substring(dayString.lastIndexOf("/") + 1) + "일";

        ArrayList<String> list = new ArrayList<>();
        list.add(dayString);
        list.add(timeString);
        return list;
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setNotification() {
        Log.i("모은","setNotification1");

        if (flag && allowsReciving) {
            Log.i("모은", "setNotification");
            // 알림 왔을 때 화면 켜지게 하기
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wakeLock.acquire(3000);


            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //TOP 에서 push 알림 등장
            intent.putExtra("noti",true);
            PendingIntent contentIntent = PendingIntent.getActivity(this, (int)(System.currentTimeMillis()/1000), new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (level == 1 && isCheckedLevel1) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, chId)
                        .setSmallIcon(R.drawable.background_msg_circle_item)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setNumber(999)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);


                if(isCheckedAudioNotification)
                {
//                    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                    mBuilder.setSound(defaultSoundUri);
                }
                else{
                    mBuilder.setSound(null);
                }
                if(isCheckedVibrationNotification)
                {
                    mBuilder.setVibrate(new long[]{1, 1000});
                }
                else{
                    mBuilder.setVibrate(null);
                }


                NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(mBuilder);
                style.bigText(msg).setBigContentTitle(title);

                //오레오 버전 대응
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String chName = "test channel";
                    NotificationChannel channel = new NotificationChannel(chId, chName, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify((int)(System.currentTimeMillis()/1000), mBuilder.build());
            } else if (level == 2 && isCheckedLevel2) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, chId)
                        .setSmallIcon(R.drawable.background_msg_circle_item_1)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setNumber(999)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);


                if(isCheckedAudioNotification)
                {
//                    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                    mBuilder.setSound(defaultSoundUri);
                }
                else{
                    mBuilder.setSound(null);
                }
                if(isCheckedVibrationNotification)
                {
                    mBuilder.setVibrate(new long[]{1, 1000});
                }
                else{
                    mBuilder.setVibrate(null);
                }


                NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(mBuilder);
                style.bigText(msg).setBigContentTitle(title);

                //오레오 버전 대응
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String chName = "test channel";
                    NotificationChannel channel = new NotificationChannel(chId, chName, notificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify((int)(System.currentTimeMillis()/1000), mBuilder.build());

            } else if(level == 3 && isCheckedLevel3) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, chId)
                        .setSmallIcon(R.drawable.background_msg_circle_item_2)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setNumber(999)
                        .setPriority(Notification.PRIORITY_MAX);


                if(isCheckedAudioNotification)
                {
//                    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                    mBuilder.setSound(defaultSoundUri);
                }
                else{
                    mBuilder.setSound(null);
                }
                if(isCheckedVibrationNotification)
                {
                    mBuilder.setVibrate(new long[]{1, 1000});
                }
                else{
                    mBuilder.setVibrate(null);
                }



                NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(mBuilder);
                style.bigText(msg).setBigContentTitle(title);

                //오레오 버전 대응
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String chName = "test channel";
                    NotificationChannel channel = new NotificationChannel(chId, chName, notificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify((int)(System.currentTimeMillis()/1000), mBuilder.build());

            }
        }

    }

    private void handleNow() {
    }

    private void scheduleJob() {
    }

    /**
     * user의 위치 정보 가져오기
     */
    public class DatabaseGetLocationAsyncTask extends AsyncTask<UserListDTO, Void, Void> {



        private UserListDAO userListDAO;

        DatabaseGetLocationAsyncTask(UserListDAO userListDAO) {
            this.userListDAO = userListDAO;
        }

        @Override
        protected Void doInBackground(UserListDTO... userListDTOS) {
            Log.i("모은", "DatabaseGetLocationAsyncTask doInBackground");
            list = userListDAO.loadUserList();

            for (int i = 0; i < list.size(); i++) {
                Log.i("모은", list.get(i).getLocation_si() + " " + list.get(i).getLocation_gu());
            }
            return null;
        }
    }

    /**
     * 받은 재난문자 DB에 insert
     */
    public class DatabaseMsgInsertAsyncTask extends AsyncTask<MsgDTO, Void, Void> {


        private MsgDAO msgDAO;
        private MsgDTO msgDTO;

        DatabaseMsgInsertAsyncTask(MsgDAO msgDAO, MsgDTO msgDTO) {
            this.msgDAO = msgDAO;
            this.msgDTO = msgDTO;
        }




        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(MsgDTO... msgDTOS) {

            Log.i("모은","DatabaseMsgInsertAsyncTask");
            Log.i("모은" ,msgDTO.getMsgText());
            msgDAO.insert(msgDTO);

//            msgList = msgDAO.loadMsgList();
//
//            for (int i = 0; i < msgList.size(); i++) {
//
//                Log.i("모은 DatabaseMsgInsertAsyncTask", msgList.get(i).getMsgText());
//
//            }

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setNotification();
        }





    }
    /**
     * 받은 재난문자 DB에 insert
     */
    public class DatabaseGetMsgAsyncTask extends AsyncTask<MsgDTO, Void, Void> {


        private MsgDAO msgDAO;


        DatabaseGetMsgAsyncTask(MsgDAO msgDAO) {
            this.msgDAO = msgDAO;

        }




        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(MsgDTO... msgDTOS) {

            Log.i("모은","DatabaseGetMsgAsyncTask");
            msgList = msgDAO.loadMsgList();



            for (int i = 0; i < msgList.size(); i++) {

                Log.i("모은 DatabaseGetMsgAsyncTask", msgList.get(i).getMsgText());

            }

            return null;
        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            setNotification();
//        }
    }

    public void setCategory(){
        Log.i("모은","setCategory");
        double[] cate = new double[]{msgCategoryPoint.getCoronaRoute(),msgCategoryPoint.getCoronaUpbreak(),msgCategoryPoint.getCoronaSafetyRule(),
                msgCategoryPoint.getDisaster(),msgCategoryPoint.getEconomy()
        };
        int maxIndex = 0;
        for(int i=0;i<5;i++){
            if(cate[maxIndex]<cate[i]){
                maxIndex = i;
            }
        }

        categroyIndex = maxIndex;
    }
    public void calculateTotalMsgPointAndLevel(){
        Log.i("모은","calculateTotalMsgPointAndLevel");
        FilterDTO filterDTO = filterList.get(0);

        totalMsgPoint = (returnWeight(filterDTO.filter_1) * msgCategoryPoint.getCoronaRoute()) + (returnWeight(filterDTO.filter_2) * msgCategoryPoint.getCoronaUpbreak()) +
                (returnWeight(filterDTO.filter_3) * msgCategoryPoint.getCoronaSafetyRule()) + (returnWeight(filterDTO.filter_4) * msgCategoryPoint.getDisaster()) +
                (returnWeight(filterDTO.filter_5) * msgCategoryPoint.getEconomy());

        if(totalMsgPoint < 400)
            level = 1;
        else if(totalMsgPoint>= 400 && totalMsgPoint<650)
            level = 2;
        else
            level = 3;

        // level에 따른 이미지 값 다르게 주기
        switch (level){
            case 1 :
                this.circleImageViewId = R.drawable.level1;
                break;

            case 2 :
                this.circleImageViewId = R.drawable.level2;
                break;

            case 3 :
                this.circleImageViewId = R.drawable.level3;
                break;
        }
        Log.i("모은", "level : "+String.valueOf(level));
    }
    public double returnWeight(int input){
        switch (input){
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 5;
            case 4:
                return 9;
        }
        return 0;
    }

    /**
     * user의 filter 정보 가져오기
     */
    public class DatabaseFilterAsyncTask extends AsyncTask<FilterDTO, Void, Void> {

        private FilterDAO filterDAO;

        DatabaseFilterAsyncTask(FilterDAO filterDAO)
        {
            this.filterDAO = filterDAO;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(FilterDTO... filterDTOS) {
            filterList = filterDAO.loadFilterList();

            for (int i = 0; i < filterList.size(); i++) {

                Log.i("모은 DatabaseFilterAsyncTask", String.valueOf(filterList.get(i).getFilter_1()));

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getMsgData();
        }
    }

}