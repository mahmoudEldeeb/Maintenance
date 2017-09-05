package com.m_eldeeb.maintenance.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.m_eldeeb.maintenance.R;
import com.m_eldeeb.maintenance.activity.AddHourNotified;
import com.m_eldeeb.maintenance.activity.UpdateHourWork;


public class ReceiveMessage extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage.getData().get("catId"),remoteMessage.getData().get("modelId"),
                remoteMessage.getData().get("catName"),remoteMessage.getData().get("modelName"),remoteMessage.getData().get("MacineRowId"));

    }

  private void sendNotification(String catId,String modelId,String catName,String modelName,String MacineRowId) {
      int id = getBaseContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
              .getInt("notifyId", 0);
if(id>=1000){
    SharedPreferences.Editor editor = getBaseContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
    editor.putInt("notifyId", 0);
    editor.commit();
}
else {
    SharedPreferences.Editor editor = getBaseContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit();
    editor.putInt("notifyId", id+1);
    editor.commit();
}
      Intent intent1 = new Intent(this, AddHourNotified.class);
intent1.putExtra("catId",catId);
      intent1.putExtra("modelId",modelId);
      intent1.putExtra("catName",catName);
      intent1.putExtra("modelName",modelName);
      intent1.putExtra("MacineRowId",MacineRowId);
      intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent1,
              PendingIntent.FLAG_ONE_SHOT);

      Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
              .setSmallIcon(R.mipmap.ic_launcher)
              .setContentTitle("الصيانة")
              .setContentText("اضف ساعات العمل ")
              .setAutoCancel(true)
              .setSound(defaultSoundUri)
              .setContentIntent(pendingIntent);

      NotificationManager notificationManager =
              (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

  }
}