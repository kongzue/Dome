package com.kongzue.dome.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.kongzue.dome.MainActivity;
import com.kongzue.dome.R;
import com.kongzue.dome.domain.PushInfo;

import cn.bmob.push.PushConstants;

/**
 * Created by ZhangChao on 2017/7/20.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);

        Gson gson = new Gson();
        PushInfo pushInfo = gson.fromJson(message, PushInfo.class);

        String alert = pushInfo.getAlert();
        String articleurl = pushInfo.getArticleurl();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder1 = new Notification.Builder(context);
        builder1.setSmallIcon(R.mipmap.ic_launcher); //设置图标
        builder1.setContentTitle("通知"); //设置标题
        builder1.setContentText(alert); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intents =new Intent (context,MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, intents, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        nm.notify(0, notification1); // 通过通知管理器发送通知
    }

}