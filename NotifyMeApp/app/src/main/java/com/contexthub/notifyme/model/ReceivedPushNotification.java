package com.contexthub.notifyme.model;

import android.os.Bundle;
import android.text.TextUtils;

import com.contexthub.notifyme.Constants;

import java.io.Serializable;
import java.util.Date;

/**
 * Metadata for a received push notification
 */
public class ReceivedPushNotification implements Serializable {

    String message;
    String customPayload;
    boolean background;
    Date receivedDate;

    public ReceivedPushNotification(Bundle bundle) {
        message = bundle.getString(Constants.KEY_MESSAGE, "");
        customPayload = bundle.getString(Constants.KEY_CUSTOM_PAYLOAD, "");
        background = TextUtils.isEmpty(message);
        receivedDate = new Date();
    }

    public String getMessage() {
        return message;
    }

    public String getCustomPayload() {
        return customPayload;
    }

    public boolean isBackground() {
        return background;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }
}
