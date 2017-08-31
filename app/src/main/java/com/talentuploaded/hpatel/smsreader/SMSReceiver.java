package com.talentuploaded.hpatel.smsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hpatel on 27/8/17.
 */

public class SMSReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();


    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent
        final Bundle data = intent.getExtras();

        try{
            if(data!=null){
                final Object[] pdusObj = (Object[]) data.get("pdus");

                for(int i = 0 ;i<pdusObj.length;i++){
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String sender = currentMessage.getDisplayOriginatingAddress();

                    String message = currentMessage.getDisplayMessageBody();

                    Log.d("smsReceiver ","seder "+sender+"; message "+message);

                    Intent intent1 = new Intent("otp");
                    intent1.putExtra("message",message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                }
            }
        }catch (Exception e){
            Log.e("smsReceiver ","Exceprion: "+e);
        }


    }

}
