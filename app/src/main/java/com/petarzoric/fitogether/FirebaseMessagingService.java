package com.petarzoric.fitogether;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by petarzoric on 15.01.18.
 */
    /*
        Klasse für die Notifications.
        Unterscheidet zwischen Request und Messages Notifications und baut dementsprechend
        Notifications.
        Zieht Datan aus der Index.js Klasse im Functions Folder.
        Arbeitet mit Firebase Functions, siehe:
        https://console.firebase.google.com/u/2/project/pem1718-f8aa0/functions/list
    */


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notificationTitle = remoteMessage.getNotification().getTitle();
        String notificationBody = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();

        String from_user_id = remoteMessage.getData().get("from_user_id");
        String type = remoteMessage.getData().get("type");
        System.out.println(from_user_id);
        System.out.println("-----------------------------------------");

        //Falls die Notifcation vom Type Request ist
        if(type.equals("request")){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationBody);

            Intent resultIntent = new Intent(click_action);
            resultIntent.putExtra("user_id", from_user_id);



            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);



            int mNotificationId = (int) System.currentTimeMillis();
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

            //Falls sie nicht vom Type Request ist(und somit Type Message ist, da wir nur diese 2 Types haben)
        } else {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationBody);
            Intent resultIntent = new Intent(click_action);


            FirebaseDatabase.getInstance().getReference().child("Users2").child(from_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   String user_name = dataSnapshot.child("name").getValue().toString();
                   /*
                   Komischerweise funktioniert das nicht ganz, das Name Field in der Chat view bleibt leer,
                   wenn man auf die Notification klickt
                    */
                    resultIntent.putExtra("user_name", user_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            resultIntent.putExtra("user_id", from_user_id);









            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);



            int mNotificationId = (int) System.currentTimeMillis();
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }



    }
}
