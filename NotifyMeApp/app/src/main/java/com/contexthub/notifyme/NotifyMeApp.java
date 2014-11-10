package com.contexthub.notifyme;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.chaione.contexthub.sdk.ContextHub;
import com.chaione.contexthub.sdk.SensorPipelineEvent;
import com.chaione.contexthub.sdk.SensorPipelineListener;
import com.chaione.contexthub.sdk.push.Push;
import com.contexthub.notifyme.push.NotificationHandler;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andy on 10/27/14.
 */
public class NotifyMeApp extends Application implements SensorPipelineListener {

    private static Context instance;

    public NotifyMeApp() {
        instance = this;
    }

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHub.init(this, "YOUR-APP-ID-HERE");
        ContextHub.getInstance().addSensorPipelineListener(this);

        List<String> tags = Arrays.asList(getString(R.string.device_tag_1), getString(R.string.device_tag_2));
        Push.init(instance, "YOUR-GCM-PROJECT-ID-HERE", Build.MODEL, tags, MainActivity.class, new NotificationHandler());

        Prefs.initPrefs(this);
    }

    @Override
    public void onEventReceived(SensorPipelineEvent event) {
        Log.d(getClass().getName(), event.getEventDetails().toString());
    }

    @Override
    public boolean shouldPostEvent(SensorPipelineEvent sensorPipelineEvent) {
        return true;
    }

    @Override
    public void onBeforeEventPosted(SensorPipelineEvent sensorPipelineEvent) {

    }

    @Override
    public void onEventPosted(SensorPipelineEvent sensorPipelineEvent) {

    }
}
