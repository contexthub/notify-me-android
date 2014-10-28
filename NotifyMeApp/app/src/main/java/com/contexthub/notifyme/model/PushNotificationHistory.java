package com.contexthub.notifyme.model;

import android.os.Bundle;

import com.contexthub.notifyme.Constants;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

/**
 * A list of {@link com.contexthub.notifyme.model.ReceivedPushNotification} instances stored in shared preferences
 */
public class PushNotificationHistory extends ArrayList<ReceivedPushNotification> {

    private static Gson gson = new Gson();

    public static void save(Bundle bundle) {
        ReceivedPushNotification notification = new ReceivedPushNotification(bundle);
        PushNotificationHistory history = load();
        history.add(0, notification);
        Prefs.putString(Constants.KEY_PUSH_HISTORY, gson.toJson(history, PushNotificationHistory.class));
    }

    public static PushNotificationHistory load() {
        try {
            String data = Prefs.getString(Constants.KEY_PUSH_HISTORY, "");
            PushNotificationHistory history = gson.fromJson(data, PushNotificationHistory.class);
            return history == null ? new PushNotificationHistory() : history;
        }
        catch (Exception e) {
            return new PushNotificationHistory();
        }
    }
}
