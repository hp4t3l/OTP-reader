package com.talentuploaded.hpatel.smsreader;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context;
    private int REQUEST_CODE = 1;
    TextView display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context= this.getApplicationContext();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED){
            final IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            context.registerReceiver(receiver,filter);
            Toast.makeText(context,"registered ", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},REQUEST_CODE);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


            }else{
                Toast.makeText(this,"You denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        final SmsManager sms = SmsManager.getDefault();

        @Override
        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equalsIgnoreCase("otp")) {
//                final String message = intent.getStringExtra("message");
//                //Do whatever you want with the code here
//                 Toast.makeText(context,"message "+message,Toast.LENGTH_LONG).show();
//            }
            final Bundle bundle = intent.getExtras();
            try{
                if(bundle!=null){
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for(int i = 0; i<pdusObj.length;i++){
                    SmsMessage currentMsg = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String sender = currentMsg.getDisplayOriginatingAddress();
                    String message = currentMsg.getDisplayMessageBody().split(":")[1];
                    message = message.split("-")[0];
                    message = message.split(" ")[3];
                    message = message.substring(0,message.length()-1);
                    display = (TextView)findViewById(R.id.smsCodeReader);
                    display.setText(message);

                    Log.d("SmsReceiver ","sender "+sender+" message "+message);

                }
            }
            }catch (Exception e){
                Log.e("smsrece","Ex : "+e);
            }


        }
    };
}
