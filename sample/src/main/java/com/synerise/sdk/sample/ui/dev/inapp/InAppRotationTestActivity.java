package com.synerise.sdk.sample.ui.dev.inapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/** Rotatable on purpose (no screenOrientation lock, no configChanges) — rotation/dark-mode destroys and recreates it, exercising the overlay config-change teardown. */
public class InAppRotationTestActivity extends BaseActivity {

    private TextView logText;
    private ScrollView logScroll;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InAppRotationTestActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp_rotation_test);
        ToolbarHelper.setUpChildToolbar(this, R.string.inapp_rotation_test);

        logText = findViewById(R.id.log_text);
        logScroll = findViewById(R.id.log_scroll);

        findViewById(R.id.send_trigger_event).setOnClickListener(v -> sendTriggerEvent());

        Injector.setOnInAppListener(buildListener());
        appendLog("lifecycle", "onCreate — listener registered"
                + (savedInstanceState != null ? " (recreated after config change)" : ""));
    }

    @Override
    protected void onDestroy() {
        appendLog("lifecycle", "onDestroy — isChangingConfigurations=" + isChangingConfigurations());
        Injector.removeInAppListener();
        super.onDestroy();
    }

    /** Fires a custom event so a campaign with this trigger can display its InApp. */
    private void sendTriggerEvent() {
        Tracker.send(new CustomEvent("inapp.context.test", "InApp context test"));
        appendLog("action", "Sent trigger event: inapp.context.test");
    }

    private OnInAppListener buildListener() {
        return new OnInAppListener() {
            @Override
            public boolean shouldShow(InAppMessageData data) {
                appendLog("shouldShow", "hash=" + data.getCampaignHash());
                return true;
            }

            @Override
            public void onShown(InAppMessageData data) {
                appendLog("onShown", "hash=" + data.getCampaignHash());
            }

            @Override
            public void onDismissed(InAppMessageData data) {
                appendLog("onDismissed", data != null ? "hash=" + data.getCampaignHash() : "null data");
            }

            @Override
            public void onHandledOpenUrl(InAppMessageData data) {
                appendLog("onHandledOpenUrl", "");
            }

            @Override
            public void onHandledOpenDeepLink(InAppMessageData data) {
                appendLog("onHandledOpenDeepLink", "");
            }

            @Override
            public HashMap<String, Object> onContextFromAppRequired(InAppMessageData data) {
                appendLog("onContextFromAppRequired", "(background thread)");
                return new HashMap<>();
            }

            @Override
            public void onCustomAction(String identifier, HashMap<String, Object> params, InAppMessageData data) {
                appendLog("onCustomAction", identifier);
            }

            @Override
            public void onCustomMethod(String name, HashMap<String, Object> params,
                                       InAppMessageData data, InAppCustomMethodCompletion completion) {
                appendLog("onCustomMethod", name);
                completion.success(params);
            }
        };
    }

    private void appendLog(String tag, String message) {
        String line = timeFormat.format(new Date()) + " [" + tag + "] " + message + "\n";
        runOnUiThread(() -> {
            logText.append(line);
            logScroll.fullScroll(View.FOCUS_DOWN);
        });
    }
}
