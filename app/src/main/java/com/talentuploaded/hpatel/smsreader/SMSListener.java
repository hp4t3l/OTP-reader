package com.talentuploaded.hpatel.smsreader;

/**
 * Created by hpatel on 27/8/17.
 */

public interface SMSListener {
    public void messageReceived(String msgsFrom,String messageText);
}

