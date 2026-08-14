package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.inapp.inline.InlineInAppMessageData;
import com.synerise.sdk.injector.inapp.inline.InlineInAppSize;
import com.synerise.sdk.injector.inapp.InAppCustomMethodCompletion;
import com.synerise.sdk.injector.inapp.inline.OnInlineInAppListener;
import com.synerise.sdk.injector.inapp.inline.OnInlineInAppViewListener;
import com.synerise.sdk.injector.inapp.ui.inline.InlineInAppView;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InlineInAppSameKeyActivity extends BaseActivity {

    private static final String PLACEMENT_KEY = "single";
    private TextView logText;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppSameKeyActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_inapp_same_key);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_same_key);

        logText = findViewById(R.id.log_text);

        createAndAttach(findViewById(R.id.container_1), "Component1", PLACEMENT_KEY);
        createAndAttach(findViewById(R.id.container_2), "Component2", PLACEMENT_KEY);
        createAndAttach(findViewById(R.id.container_3), "Component3", PLACEMENT_KEY);
        createAndAttach(findViewById(R.id.container_4), "Component4", "fourth");

        Injector.setOnInlineInAppListener(createGlobalListener());

        findViewById(R.id.btn_send_event_overlay).setOnClickListener(v -> {
            String eventName = "overlay.test";
            appendLog("action", "sending event: " + eventName);
            CustomEvent event = new CustomEvent(eventName, "Overlay test");
            Tracker.send(event);
        });

        findViewById(R.id.btn_send_event).setOnClickListener(v -> {
            String eventName = "inline.samekey";
            appendLog("action", "sending event: " + eventName);
            CustomEvent event = new CustomEvent(eventName, "Same key trigger");
            Tracker.send(event);
        });
    }

    private void createAndAttach(FrameLayout container, String tag, String key) {
        InlineInAppView view = Injector.createInlineInAppView(this, key);
        view.setOnInlineInAppViewListener(createListener(tag));
        container.addView(view, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        appendLog(tag, "created with key=" + key);
    }

    private OnInlineInAppViewListener createListener(String tag) {
        return new OnInlineInAppViewListener() {
            @Override
            public void onProcessingStarted() {
                appendLog("onProcessingStarted", "");
            }

            @Override
            public void onLoaded(InlineInAppMessageData data) {
                appendLog(tag, "onLoaded hash=" + data.getCampaignHash());
            }

            @Override
            public void onUpdated(InlineInAppMessageData data) {
                appendLog(tag, "onUpdated hash=" + data.getCampaignHash());
            }

            @Override
            public void onFailed(InlineInAppMessageData data, String error) {
                appendLog(tag, "onFailed: " + error);
            }

            @Override
            public void onRemove(InlineInAppMessageData data) {
                appendLog(tag, "onRemove");
            }

            @Override
            public void onSizeChanged(InlineInAppMessageData data, InlineInAppSize size) {
                appendLog(tag, "onSizeChanged px=" + size.getWidthPx() + "x" + size.getHeightPx() + " dp=" + size.getWidthDp() + "x" + size.getHeightDp());
            }
        };
    }

    private OnInlineInAppListener createGlobalListener() {
        return new OnInlineInAppListener() {
            @Override
            public void onInlineInAppAvailable(InlineInAppView view, InlineInAppMessageData data) {
                appendLog("GLOBAL", "onInlineInAppAvailable hash=" + data.getCampaignHash());
            }

            @Override
            public Map<String, Object> onContextFromAppRequired(InlineInAppMessageData data) {
                appendLog("GLOBAL", "onContextFromAppRequired");
                return new HashMap<>();
            }

            @Override
            public void onOpenedUrl(InlineInAppMessageData data, String url) {
                appendLog("GLOBAL", "onOpenedUrl: " + url);
            }

            @Override
            public void onOpenedDeepLink(InlineInAppMessageData data, String deepLink) {
                appendLog("GLOBAL", "onOpenedDeepLink: " + deepLink);
            }

            @Override
            public void onCustomAction(InlineInAppMessageData data, String name, Map<String, Object> params) {
                appendLog("GLOBAL", "onCustomAction: " + name);
            }

            @Override
            public void onCustomMethod(String name, HashMap<String, Object> params,
                                       InlineInAppMessageData data, InAppCustomMethodCompletion completion) {
                appendLog("GLOBAL", "onCustomMethod: " + name);
                completion.success(params);
            }
        };
    }

    @Override
    protected void onDestroy() {
        Injector.removeInlineInAppListener();
        super.onDestroy();
    }

    private void appendLog(String tag, String message) {
        String line = timeFormat.format(new Date()) + " [" + tag + "] " + message + "\n";
        runOnUiThread(() -> logText.append(line));
    }
}
