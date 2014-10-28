package com.contexthub.notifyme.model;

import android.os.Bundle;

import com.contexthub.notifyme.Constants;

import java.io.Serializable;
import java.util.Date;

/**
 * Metadata for a received push notification
 */
public class ReceivedPushNotification implements Serializable {

    String alert;
    boolean customPayload;
    boolean background;
    Date receivedDate;

    public ReceivedPushNotification(Bundle bundle) {
        alert = bundle.getString(Constants.KEY_MESSAGE, "");
        customPayload = bundle.containsKey(Constants.KEY_CUSTOM_PAYLOAD);
        background = bundle.containsKey(Constants.KEY_MESSAGE);
        receivedDate = new Date();
    }

    public String getAlert() {
        return alert;
    }

    public boolean isCustomPayload() {
        return customPayload;
    }

    public boolean isBackground() {
        return background;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }
}
