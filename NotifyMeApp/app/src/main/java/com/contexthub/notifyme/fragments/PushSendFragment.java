package com.contexthub.notifyme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaione.contexthub.sdk.ContextHub;
import com.chaione.contexthub.sdk.PushNotificationProxy;
import com.chaione.contexthub.sdk.callbacks.Callback;
import com.chaione.contexthub.sdk.model.CustomDataPushNotification;
import com.chaione.contexthub.sdk.model.PushNotification;
import com.chaione.contexthub.sdk.model.PushNotificationMessage;
import com.contexthub.notifyme.R;
import com.contexthub.notifyme.model.CustomData;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andy on 10/27/14.
 */
public class PushSendFragment extends Fragment implements Callback<Object> {

    @InjectView(R.id.send_message) EditText message;
    @InjectView(R.id.send_type_device_id) RadioButton deviceIdButton;
    @InjectView(R.id.send_type_alias) RadioButton aliasButton;
    @InjectView(R.id.send_type_tags) RadioButton tagsButton;
    @InjectView(R.id.send_type_value_label) TextView targetLabel;
    @InjectView(R.id.send_type_value) EditText targetValue;
    @InjectView(R.id.send_custom_payload) EditText customPayload;
    @InjectView(R.id.send_visibility) SwitchCompat visibilitySwitch;

    private PushNotificationProxy proxy = new PushNotificationProxy();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        targetValue.setText(ContextHub.getInstance().getDeviceID());
    }

    @OnClick({R.id.send_type_device_id, R.id.send_type_alias, R.id.send_type_tags})
    public void onSendTypeSelected(View view) {
        switch (view.getId()) {
            case R.id.send_type_device_id:
                targetLabel.setText(R.string.device_id);
                targetValue.setHint(R.string.device_id);
                break;
            case R.id.send_type_alias:
                targetLabel.setText(R.string.alias);
                targetValue.setHint(R.string.comma_separated_aliases);
                break;
            case R.id.send_type_tags:
                targetLabel.setText(R.string.tags);
                targetValue.setHint(R.string.comma_separated_tags);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_send, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_push) {
            if(visibilitySwitch.isChecked()) {
                sendBackgroundNotification();
            }
            else {
                sendForegroundNotification();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendForegroundNotification() {
        PushNotificationMessage notification =
                new PushNotificationMessage(message.getText().toString());
        applyRecipientFilter(notification);
        proxy.sendPushNotificationMessage(notification, this);
    }

    private void sendBackgroundNotification() {
        CustomData customData =
                new CustomData(customPayload.getText().toString());
        CustomDataPushNotification<CustomData> notification =
                new CustomDataPushNotification<CustomData>(customData);
        applyRecipientFilter(notification);
        proxy.sendCustomDataPushNotification(notification, this);
    }

    private void applyRecipientFilter(PushNotification notification) {
        String value = targetValue.getText().toString();
        if(deviceIdButton.isChecked()) {
            notification.getDeviceIds().add(value);
        }
        else if(aliasButton.isChecked()) {
            notification.getAliases().addAll(splitCommaSeparatedString(value));
        }
        else if(tagsButton.isChecked()) {
            notification.getTags().addAll(splitCommaSeparatedString(value));
        }
    }

    private ArrayList<String> splitCommaSeparatedString(String delimitedText){
        ArrayList<String> tags = new ArrayList<String>(Arrays.asList(delimitedText.split(",")));
        return tags;
    }

    @Override
    public void onSuccess(Object o) {
        Toast.makeText(getActivity(), R.string.push_message_sent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
