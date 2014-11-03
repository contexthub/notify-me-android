package com.contexthub.notifyme.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.contexthub.notifyme.R;

/**
 * Created by andy on 11/3/14.
 */
public class ClipboardTextView extends TextView implements View.OnClickListener {

    public ClipboardTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(),
                getContext().getString(R.string.copied_to_clipboard, getText()),
                Toast.LENGTH_SHORT).show();
    }
}
