package com.contexthub.notifyme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaione.contexthub.sdk.DeviceProxy;
import com.chaione.contexthub.sdk.callbacks.Callback;
import com.chaione.contexthub.sdk.model.Device;
import com.contexthub.notifyme.R;
import com.contexthub.notifyme.widget.ClipboardTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andy on 10/27/14.
 */
public class DeviceFragment extends Fragment implements Callback<Device> {

    @InjectView(R.id.device_alias) ClipboardTextView alias;
    @InjectView(R.id.device_id) ClipboardTextView deviceId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        DeviceProxy proxy = new DeviceProxy();
        proxy.getCurrentDevice(this);
    }

    @Override
    public void onSuccess(Device device) {
        alias.setText(device.getAlias());
        deviceId.setText(device.getDeviceId());
    }

    @Override
    public void onFailure(Exception e) {
        Log.d(getClass().getName(), e.getMessage());
    }
}
