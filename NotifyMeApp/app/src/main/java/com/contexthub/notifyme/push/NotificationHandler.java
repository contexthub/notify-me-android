package com.contexthub.notifyme.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.chaione.contexthub.sdk.ContextHub;
import com.chaione.contexthub.sdk.push.PushPayloadHandler;
import com.contexthub.notifyme.Constants;
import com.contexthub.notifyme.R;
import com.contexthub.notifyme.model.PushNotificationHistory;

/**
 * Handles the receipt of ContextHub push notifications
 */
public class NotificationHandler implements PushPayloadHandler {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void handlePushPayload(Context context, Bundle bundle) {
        PushNotificationHistory.save(bundle);

        // Use ContextHub's instance of the Otto event bus to trigger history refresh in PushReceiveFragment
        ContextHub.getInstance().getBus().post(new PushReceivedEvent());

        if(bundle.containsKey(Constants.KEY_MESSAGE)) {
            showNotification(context, bundle);
        }
        else {
            Log.d(getClass().getName(), bundle.toString());
        }
    }

    private void showNotification(Context context, Bundle bundle) {
        String message = bundle.get(Constants.KEY_MESSAGE).toString();
        NotificationManager manager = NotificationManager.class.cast(context.getSystemService(Context.NOTIFICATION_SERVICE));
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(false)
                .setVibrate(new long[]{100, 500, 100, 500})
                .build();
        manager.notify(NOTIFICATION_ID, notification);
    }

    public class PushReceivedEvent{}
}
