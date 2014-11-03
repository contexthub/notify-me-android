package com.contexthub.notifyme.model;

/**
* A simple class to demonstrate sending custom data to a device via a background push notification
*/
public class CustomData {

    String message;
    String customPayload;

    public CustomData(String message, String customPayload) {
        this.message = message;
        this.customPayload = customPayload;
    }
}
