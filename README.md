# Notify Me (Push) Sample app

The Notify Me sample app that introduces you to the push features of the ContextHub Android SDK.

### Table of Contents

1. **[Purpose](#purpose)**
2. **[ContextHub Use Case](#contexthub-use-case)**
3. **[Background](#background)**
4. **[Getting Started](#getting-started)**
5. **[Setup](#setup)**
6. **[Running the Sample App](#running-the-sample-app)**
7. **[ADB Logcat](#adb-logcat)**
8. **[Sample Code](#sample-code)**
9. **[Usage](#usage)**
  - **[Registering for push](#registering-for-push)**
  - **[Sending push to device(s)](#sending-a-push-to-devices)**
  - **[Sending push to alias(es)](#sending-a-push-to-aliases)**
  - **[Sending push to tag(s)](#sending-a-push-to-tags)**
  - **[Sending push with custom data](#sending-a-push-with-custom-data)**
  - **[Receiving a push](#receiving-a-push)**
10. **[Final Words](#final-words)**
11. **[Troubleshooting](#troubleshooting)**

## Purpose

This sample application will show you how to send and receive foreground and background push notifications in ContextHub.

## ContextHub Use Case

In this sample application, we use ContextHub to interact with Google Cloud Messaging (GCM) so we can send push notifications to devices based on their device ID, alias, or tag. ContextHub takes care of translating each of those items to a push token which GCM needs to send the message to the correct device.

## Background

Push notifications allows you to increase user engagement with your application through timely alerts that a user can directly tap on to launch your app. Background notifications can silently wake up your app, do some processing, and then go back to sleep. The next time the user opens your app, it is filled with fresh content.

Rather than sending push notifications to tokens which GCM gives you (which can change at any time), ContextHub lets you deal with higher level concepts such as device ids, aliases, and tags:

1. A device ID is a 32-hexadecimal character string that is uniquely tied to a device. It will remain the same for the life of the app, and is based on the device, app id, and bundle id.
2. Aliases are a friendlier version of the device ID, though they do not need to be unique. Each device is restricted to only one alias, but multiple devices can have the same alias. A great use of aliases is to indicate all the devices that belong to the single person by setting the alias to be the same SHA1 hashed email address.
3. Lastly, tags let you put devices into different groups. A single device can have multiple tags (vs. one alias per device), which give you as a developer more control over which devices get which notifications without explicitly needing to know all the specific device IDs which will receive the notification.

## Getting Started

1. Get started by either forking or cloning the "contexthub/notify-me-android" repo. Visit [GitHub Help](https://help.github.com/articles/fork-a-repo) if you need help.
2. Go to [ContextHub](http://app.contexthub.com) and create a new application called "Notify Me".
3. Find the app id associated with the application you just created. Its format looks something like this: `13e7e6b4-9f33-4e97-b11c-79ed1470fc1d`.
4. Open up your project and put the app id into the `ContextHub.init(this, "YOUR-APP-ID-HERE")` method call in the `NotifyMeApp` class.
5. You are now ready to setup push notifications with your device.

## Setup

1. To use GCM with ContextHub, you'll need to create a Google API project and enable the GCM Service as described in the [Getting Started](http://developer.android.com/google/gcm/gs.html) documentation.
1. Return to your [ContextHub](http://app.contexthub.com) app and click Settings > Push Services.
1. Paste your GCM API key in the Auth Key text box at the bottom of the page, then click the "Save" button.
1. Back in Android Studio, replace `YOUR-GCM-PROJECT-ID-HERE` in the `NotifyMeApp` class with the GCM project id.
1. Review `AndroidManifest.xml` file to familiarize yourself with the permissions, intent service, and broadcast receiver you'll need to add to your own app in order to leverage ContextHub's push services.

## Running the sample app

1. Run the app on your device (push notifications do not work on emulators).
2. On the `Send` tab, tap on the "message" field and type in a short message.
3. By default, the app will send a message to your device id in the foreground. Try it now, tap `Push` in the upper right hand corner, and you should see a dialog when your message is received.
4. On the `Receive` tab, you should see your notification appear and whether it was a foreground/background push, if it had a custom payload, and what time it was received. Tap the row to see more detail.
4. Now tap the `Device` tab to see what your current alias and tags are. Tap on your alias (which in this app is your device name) to automatically copy it to your clipboard.
5. Go back to the `Send` tab and replace your device id with your alias. Send another message.
6. You should recent another message when the alert is received from GCM.
7. Lastly, try sending a message to a tag by going to the `Device` tab. Tap a tag to copy it to your clipboard, then selecting "tags" in the type section and paste your tag into the next section. Tap the switch button on the bottom to instead send a background notification instead of a foreground notification. Now tap `Push`.
8. You shouldn't see a message appear, but when you go to the receive tab, a new message should be present. If you repeat step 7 again, but immediately press the home button after sending your message, your device should vibrate to indicate that you got a new message.

## ADB Logcat

This sample app will log push notification responses from GCM so you can get an idea of the structure of the item you receive.

## Sample Code

In this sample, most of the important code that deals with push notifications occurs in the `NotifyMeApp` and `PushSendFragment` classes. `NotifyMeApp` deals with registering a device with GCM, registering the token, alias and tags with ContextHub and receiving push notifications. `PushSendFragment` handles sending push notifications in three different ways, to a device id, to multiple aliases, and to multiple tags.

In addition, the `PushReceiveFragment` and `NotificationHandler` classes deal with showing notifications that you have received with their contents; whether they were foreground or background pushes, the time they were received (which has no guarantee of immediacy with background pushes in relation to the time they were sent) and any custom payloads to be sent with the message. Key to this is the fact that the method `NotificationHandler.handlePushPayload(Context context, Bundle bundle)` will be called eventually with background notifications regardless of whether your application is in the foreground or background or not running at all, which gives added power to your applications to respond to a notification with a fresh state the next time a user opens your app.

## Usage

Below shows the basics of how the `PushNotificationProxy` class is used to send push notifications and how you can implement the `PushPayloadHandler` interface to receive push notifications:

##### Registering for push

```java
public class NotifyMeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register with ContextHub
        ContextHub.init(this, "YOUR-APP-ID-HERE");

        /* Initialize push service with your GCM sender id and the activity to
           launch when a notification is opened */
        Push.init(this, "YOUR-GCM-PROJECT-ID-HERE", MainActivity.class);
    }
}
```

##### Sending a push to device(s)

```java
// Send a push to 2 devices with IDs defined below
// Note: All device IDs are UUIDs, if a deviceID is not a valid UUID, then it is not a valid device ID
String deviceId1 = "20984403-690A-4098-8557-73B763F1DFFB";
String deviceId2 = "151E57AF-7E87-400F-A1E0-63C9E7811376";
PushNotificationMessage notification = new PushNotificationMessage("Test push notification message");
notification.getDeviceIds().add(deviceId1);
notification.getDeviceIds().add(deviceId2);
PushNotificationProxy proxy = new PushNotificationProxy();
proxy.sendPushNotificationMessage(notification, new Callback<Object>() {
    @Override
    public void onSuccess(Object result) {
        Toast.makeText(getActivity(), "Push notification sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
    }
});
```

##### Sending a push to alias(es)

```java
// Send a push notification to 2 devices, with aliases "Andy's Moto X" and "Chris' Nexus 5"
String alias1 = "Andy's Moto X";
String alias2 = "Chris' Nexus 5";
PushNotificationMessage notification = new PushNotificationMessage("Test push notification message");
notification.getAliases().add(alias1);
notification.getAliases().add(alias2);
PushNotificationProxy proxy = new PushNotificationProxy();
proxy.sendPushNotificationMessage(notification, new Callback<Object>() {
    @Override
    public void onSuccess(Object result) {
        Toast.makeText(getActivity(), "Push notification sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
    }
});
```

##### Sending a push to tag(s)

```java
// Send a push notification to all devices with tags "tag1" and "tag2"
// Note: If a device has both "tag1" and "tag2", they will receive the same notification twice
String tag1 = "tag1";
String tag2 = "tag2";
PushNotificationMessage notification = new PushNotificationMessage("Test push notification message");
notification.getTags().add(tag1);
notification.getTags().add(tag2);
PushNotificationProxy proxy = new PushNotificationProxy();
proxy.sendPushNotificationMessage(notification, new Callback<Object>() {
    @Override
    public void onSuccess(Object result) {
        Toast.makeText(getActivity(), "Push notification sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
    }
});
```

##### Sending a push with custom data

```java
// A class representing the custom data to send
public class MyCustomData {
    String appName;
    int version;

    public MyCustomData(String appName, int version) {
        this.appName = appName;
        this.version = version;
    }
}

// Send a push with custom data
String deviceId = "20984403-690A-4098-8557-73B763F1DFFB";
MyCustomData customData = new MyCustomData("NotifyMeApp", 1);
CustomDataPushNotification<MyCustomData> notification = new CustomDataPushNotification<MyCustomData>(customData);
notification.getDeviceIds().add(deviceId);
PushNotificationProxy proxy = new PushNotificationProxy();
proxy.sendCustomDataPushNotification(notification, new Callback<Object>() {
    @Override
    public void onSuccess(Object result) {
        Toast.makeText(getActivity(), "Push notification sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
    }
});
```

##### Receiving a push

```java
// Implement the PushPayloadHandler interface
public class NotificationHandler implements PushPayloadHandler {

    private static final int NOTIFICATION_ID = 1;
    private static final String KEY_MESSAGE = "message";

    @Override
    public void handlePushPayload(Context context, Bundle bundle) {
        if(bundle.containsKey(KEY_MESSAGE)) {
            showNotification(context, bundle);
        }
        else {
            // background processing of your custom push data
        }
    }

    private void showNotification(Context context, Bundle bundle) {
        String message = bundle.get(KEY_MESSAGE).toString();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
}


// Initialize your PushPayloadHandler in the call to Push.init()
public class NotifyMeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register with ContextHub
        ContextHub.init(this, "YOUR-APP-ID-HERE");

        /* Initialize push service with your GCM sender id, the activity to
           launch when a notification is opened, and an instance of the class that implements PushPayloadHandler */
        Push.init(this, "YOUR-GCM-PROJECT-ID-HERE", MainActivity.class, new NotificationHandler();
    }
}
```

## Final Words

That's it! Hopefully this sample application showed you how to send and receive push notifications using ContextHub. With push setup, you no longer need to call `LocationService.synchronize()` or `ProximityService.synchronize()` when geofences and beacons change on the server. You can also move onto the "awareness-android" sample app which shows how push enables your app to be aware of create, update, and delete changes to beacons, geofences, and vault items in near real-time.

## Troubleshooting

1. Ensure that you have carefully followed the instructions in the GCM [Getting Started](http://developer.android.com/google/gcm/gs.html) documentation.
1. Double-check that you have correctly configured [ContextHub](http://app.contexthub.com) with your GCM API key under Settings > Push Services.
1. Make sure you are calling `Push.init()` in your `Application` class to initialize the push notification service. Be sure to pass an instance of your `PushPayloadHandler` implementation.
1. Set a breakpoint in the `handlePushPayload` method of your `PushPayloadHandler` implementation to confirm it is being called.

Other:

1. There is no guarantee that a push notification will be delivered to a device, though GCM gives best effort when possible. If a device is offline when a push is sent, GCM will attempt to send it the next time the device is online.