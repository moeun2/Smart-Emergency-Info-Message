package com.passta.a2ndproj;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.passta.a2ndproj.data.AppDatabase;
import com.passta.a2ndproj.data.UserListDAO;
import com.passta.a2ndproj.data.UserListDTO;
import com.passta.a2ndproj.start.activity.Page2Activity;

import java.util.List;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    private List<UserListDTO> list;
    private String msg, chId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            String tag = remoteMessage.getNotification().getBody().toString();
            tag = tag.substring(3);
            UserListDTO lst = new UserListDTO(tag,"모은시","모은구");
            AppDatabase db = AppDatabase.getInstance(this);
            new FirebaseMessagingService.DatabaseAsyncTask(db.userListDAO(),lst).execute();


            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            chId = "test v1";
            msg = remoteMessage.getNotification().getBody();


            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //TOP 에서 push 알림 등장

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, chId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1, 1000})
                    .setContentIntent(contentIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //오레오 버전 대응
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String chName = "test channel";
                NotificationChannel channel = new NotificationChannel(chId, chName, notificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, mBuilder.build());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleNow() {
    }

    private void scheduleJob() {
    }

    public class DatabaseAsyncTask extends AsyncTask<UserListDTO, Void, Void> {


        private UserListDAO userListDAO;
        private UserListDTO userListDTO;

        DatabaseAsyncTask(UserListDAO userListDAO, UserListDTO userListDTO) {
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

