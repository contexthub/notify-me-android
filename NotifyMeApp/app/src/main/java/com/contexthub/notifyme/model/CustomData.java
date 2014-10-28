package com.contexthub.notifyme.model;

/**
* A simple class to demonstrate sending custom data to a device via a background push notification
*/
public class CustomData {
    String customPayload;

    public CustomData(String customPayload) {
        this.customPayload = customPayload;
    }
}
