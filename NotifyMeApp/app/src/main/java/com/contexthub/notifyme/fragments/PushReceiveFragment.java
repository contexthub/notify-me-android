package com.contexthub.notifyme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chaione.contexthub.sdk.ContextHub;
import com.contexthub.notifyme.R;
import com.contexthub.notifyme.model.PushNotificationHistory;
import com.contexthub.notifyme.model.ReceivedPushNotification;
import com.contexthub.notifyme.push.NotificationHandler;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andy on 10/27/14.
 */
public class PushReceiveFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadHistory();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register with ContextHub's instance of Otto event bus for handling PushReceivedEvent
        ContextHub.getInstance().getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Unregister from event bus
        ContextHub.getInstance().getBus().unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_receive, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_clear) {
            PushNotificationHistory.delete();
            loadHistory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onPushReceivedEvent(NotificationHandler.PushReceivedEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadHistory();
            }
        });
    }

    private void loadHistory() {
        setListAdapter(new HistoryAdapter(getActivity(), PushNotificationHistory.load()));
        setListShown(true);
    }

    class HistoryAdapter extends ArrayAdapter<ReceivedPushNotification> {

        public HistoryAdapter(Context context, List<ReceivedPushNotification> objects) {
            super(context, -1, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.received_push_notification_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            ReceivedPushNotification notification = getItem(position);
            holder.message.setText(getString(R.string.message_label, notification.getMessage()));

            holder.customPayload.setText(getString(R.string.custom_payload_label, notification.getCustomPayload()));

            String isBackground = getString(notification.isBackground() ? R.string.yes : R.string.no);
            holder.background.setText(getString(R.string.background_label, isBackground));

            holder.date.setText(notification.getReceivedDate().toString());

            return convertView;
        }
    }

    class ViewHolder {

        @InjectView(R.id.received_push_notification_message) TextView message;
        @InjectView(R.id.received_push_notification_custom_payload) TextView customPayload;
        @InjectView(R.id.received_push_notification_background) TextView background;
        @InjectView(R.id.received_push_notification_date) TextView date;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
