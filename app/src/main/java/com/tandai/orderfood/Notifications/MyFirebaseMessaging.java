package com.tandai.orderfood.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tandai.orderfood.Model.Order;
import com.tandai.orderfood.Model.User;
import com.tandai.orderfood.OrderActivity;
import com.tandai.orderfood.R;
import com.tandai.orderfood.RestaurantViewOrderActivity;
import com.tandai.orderfood.ViewUserActivity;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userType ="";
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.getValue(User.class);
                userType = userInfo.getUserType();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage,userType);
                } else {
                    sendNotification(remoteMessage,userType);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void sendOreoNotification(RemoteMessage remoteMessage,String type){

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, OrderActivity.class);
        if(type.equals("restaurent")) {
            intent =new Intent(this, RestaurantViewOrderActivity.class);
        } else if (type.equals("admin")) {
            intent =new Intent(this, ViewUserActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(notification.getTitle(), notification.getBody(), pendingIntent,
                defaultSound, R.mipmap.ic_launcher);

        oreoNotification.getManager().notify(0, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage,String type) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, OrderActivity.class);
        if(type.equals("restaurent")) {
            intent = new Intent(this, RestaurantViewOrderActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setShowWhen(true)
                .setColor(Color.GREEN)
                .setSound(defaultSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        noti.notify(0, builder.build());
    }
}