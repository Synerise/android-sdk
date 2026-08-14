package com.synerise.sdk.sample.ui.dev.inapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.inapp.InAppCustomMethodCompletion;
import com.synerise.sdk.injector.inapp.InAppMessageData;
import com.synerise.sdk.injector.inapp.OnInAppListener;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.util.HashMap;

/**
 * Manual test: an SDK-managed overlay InApp survives rotation. This Activity
 * intentionally omits android:configChanges so the Activity really recreates;
 * the SDK re-shows the same WebView on the new instance with no new events.
 * Expect a single onShown, the same content, and no onDismissed on rotation.
 */
public class OverlayRotationActivity extends BaseActivity {

    private static final String TRIGGER_EVENT = "overlay.rotation";

    private TextView logView;
    private ScrollView logScroll;

    public static Intent createIntent(Context context) {
        return new Intent(context, OverlayRotationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_overlay_rotation);
        ToolbarHelper.setUpChildToolbar(this, R.string.overlay_rotation);

        logView = findViewById(R.id.inapp_log);
        logScroll = findViewById(R.id.log_scroll);
        findViewById(R.id.send_trigger_event).setOnClickListener(v -> sendTriggerEvent());

        Injector.setOnInAppListener(buildListener());
        log("InApp listener registered. Trigger the overlay, then rotate the device.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Injector.removeInAppListener();
    }

    /** Fires the custom event a configured overlay campaign listens for. */
    private void sendTriggerEvent() {
        Tracker.send(new CustomEvent(TRIGGER_EVENT, "Overlay rotation test"));
        log("Sent trigger event: " + TRIGGER_EVENT);
    }

    private OnInAppListener buildListener() {
        return new OnInAppListener() {
            @Override
            public boolean shouldShow(InAppMessageData data) {
                return true;
            }

            @Override
            public void onShown(InAppMessageData data) {
                log("onShown: " + data.getCampaignHash());
            }

            @Override
            public void onDismissed(InAppMessageData data) {
                log("onDismissed: " + data.getCampaignHash());
            }

            @Override
            public void onHandledOpenUrl(InAppMessageData data) {
                log("onHandledOpenUrl");
            }

            @Override
            public void onHandledOpenDeepLink(InAppMessageData data) {
                log("onHandledOpenDeepLink");
            }

            @Override
            public HashMap<String, Object> onContextFromAppRequired(InAppMessageData data) {
                return null;
            }

            @Override
            public void onCustomAction(String identifier, HashMap<String, Object> params, InAppMessageData data) {
                log("onCustomAction: " + identifier + " params=" + params);
            }

            @Override
            public void onCustomMethod(String name, HashMap<String, Object> params,
                                       InAppMessageData data, InAppCustomMethodCompletion completion) {
                log("onCustomMethod: " + name + " params=" + params);
                completion.success(params);
            }
        };
    }

    private void log(String message) {
        runOnUiThread(() -> {
            if (isFinishing() || logView == null) return;
            logView.append(message + "\n");
            if (logScroll != null) {
                logScroll.post(() -> logScroll.fullScroll(ScrollView.FOCUS_DOWN));
            }
        });
    }
}
