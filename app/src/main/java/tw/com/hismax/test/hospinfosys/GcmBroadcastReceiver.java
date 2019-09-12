package tw.com.hismax.test.hospinfosys;

/**
 * Created by apple on 16/5/1.
 */


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 接收來自GCM的訊息
 *
 * @author magiclen
 *
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    public static final int NOTIFICATION_ID = 0;
    PatientInfoObj patient;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);
        String msgShow;
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                Log.i(getClass() + " GCM ERROR", extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                Log.i(getClass() + " GCM DELETE", extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                Log.i(getClass() + " GCM MESSAGE", extras.toString());
                Intent i = new Intent(context, Setting.class);
                //Intent i = new Intent(context, Login_gcm.class);
                i.setAction("android.intent.action.MAIN");
                i.addCategory("android.intent.category.LAUNCHER");

                GetClientRegistrationId.sendLocalNotification(context, NOTIFICATION_ID,
                        R.drawable.gcmnotice, "行動醫療系統通知", extras
                                .getString("message"), "hospinfosys", false,
                        PendingIntent.getActivity(context, 0, i,
                                PendingIntent.FLAG_CANCEL_CURRENT));
            }
            //Ben*** : 保留訊息 ---------------------------s
            Log.d("BEN", "GCM訊息:"+ extras.getString("message"));
            //***Ben --- 取出 patient 資料-------s
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateandTime = sdf.format(new Date());
            patient = (PatientInfoObj) context.getApplicationContext();
            patient.addMsgList(new MessageItem(currentDateandTime, extras.getString("message")));
            //---------------------------------------------e
        }
        setResultCode(Activity.RESULT_OK);
    }
    
}
