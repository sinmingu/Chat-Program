package com.example.sinmingu.login.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.EditText;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    StringBuilder sms = new StringBuilder();
    StringBuilder cnumber = new StringBuilder();

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");

                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                for (int i = 0; i < pdusObj.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }

                for (SmsMessage smsMessage : messages) {
                    sms.append(smsMessage.getMessageBody());
                }

                sms.toString();

                for (int i = 0; i < sms.length(); i++) {
                    if (sms.toString().charAt(i) >= 48 && sms.toString().charAt(i) <= 57) {
                        cnumber.append(sms.toString().charAt(i));
                    }
                }

                Intent it = new Intent();
                it.putExtra("smsnumber", new String(cnumber));
                it.setAction("recvSMS");
                context.sendBroadcast(it);
            }
        }
        Toast.makeText(context, cnumber, Toast.LENGTH_SHORT).show();

    }

}


