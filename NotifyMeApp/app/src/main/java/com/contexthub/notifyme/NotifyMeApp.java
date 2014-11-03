package com.contexthub.notifyme;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.chaione.contexthub.sdk.ContextHub;
import com.chaione.contexthub.sdk.DeviceProxy;
import com.chaione.contexthub.sdk.SensorPipelineEvent;
import com.chaione.contexthub.sdk.SensorPipelineListener;
import com.chaione.contexthub.sdk.callbacks.Callback;
import com.chaione.contexthub.sdk.model.Device;
import com.chaione.contexthub.sdk.push.Push;
import com.contexthub.notifyme.push.NotificationHandler;
import com.pixplicity.easyprefs.library.Prefs;

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
        Push.init(instance, "YOUR-GCM-PROJECT-ID-HERE", MainActivity.class, new NotificationHandler());
        Prefs.initPrefs(this);
        setDeviceTags();
    }

    private void setDeviceTags() {
        final DeviceProxy proxy = new DeviceProxy();
        proxy.getCurrentDevice(new Callback<Device>() {
            @Override
            public void onSuccess(Device device) {
                device.getTags().add(getString(R.string.device_tag_1));
                device.getTags().add(getString(R.string.device_tag_2));
                proxy.updateDevice(device.getDeviceId(), device, updateDeviceCallback);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(getClass().getName(), e.getMessage());
            }
        });
    }
    private Callback<Device> updateDeviceCallback = new Callback<Device>() {
        @Override
        public void onSuccess(Device device) {
        }

        @Override
        public void onFailure(Exception e) {
            Log.d(getClass().getName(), e.getMessage());
        }
    };

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
