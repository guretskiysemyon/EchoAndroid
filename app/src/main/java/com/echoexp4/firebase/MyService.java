package com.echoexp4.firebase;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyService extends FirebaseMessagingService {


    public MyService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getNotification() != null) {
            createNotificationChannel();
             HashMap<String,String> map = split(remoteMessage.getNotification().getBody());
             AppDB db = AppDB.getDbInstance(getApplicationContext());
             NotificationCompat.Builder builder;
            if (map.get("type").equals("transfer")){
                builder = new NotificationCompat.Builder(this, "1")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(map.get("content"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                Message message = new Message(map.get("content"),map.get("created"),false, map.get("from"));
                db.allDao().addMessage(message);
                db.allDao().updateContact(map.get("content"),map.get("created"),map.get("from"));
            } else{
                builder = new NotificationCompat.Builder(this, "1")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                Contact contact = new Contact(map.get("from"),map.get("from"),null, map.get("server"));
                db.allDao().addContact(contact);
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        }
    }


    private HashMap<String,String> split(String message){
        HashMap<String, String> result = new HashMap<>();
        String[] split = message.split(",");
        for (String s: split){
            String[] temp = s.split(":");
            result.put(temp[0],temp[1]);
        }
        return result;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "name", importance);
            channel.setDescription("Demo channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}