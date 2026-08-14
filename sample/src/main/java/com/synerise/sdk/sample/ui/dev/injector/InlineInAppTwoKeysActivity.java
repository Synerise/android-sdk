package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
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

public class InlineInAppTwoKeysActivity extends BaseActivity {

    private static final String KEY_ON_DEMAND = "inlinedemand";
    private static final String KEY_EVENT = "inlineevent";

    private InlineInAppView onDemandView;
    private InlineInAppView eventView;
    private TextView logText;
    private ScrollView logScroll;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppTwoKeysActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_inapp_two_keys);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_two_keys);

        logText = findViewById(R.id.log_text);
        logScroll = findViewById(R.id.log_scroll);

        FrameLayout containerOnDemand = findViewById(R.id.container_on_demand);
        onDemandView = Injector.createInlineInAppView(this, KEY_ON_DEMAND);
        onDemandView.setOnInlineInAppViewListener(createListener("OnDemand"));
        containerOnDemand.addView(onDemandView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        appendLog("init", "created ON_DEMAND view key=" + KEY_ON_DEMAND);

        FrameLayout containerEvent = findViewById(R.id.container_event);
        eventView = Injector.createInlineInAppView(this, KEY_EVENT);
        eventView.setOnInlineInAppViewListener(createListener("Event"));
        containerEvent.addView(eventView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        appendLog("init", "created EVENT view key=" + KEY_EVENT);

        Injector.setOnInlineInAppListener(createGlobalListener());

        findViewById(R.id.btn_render).setOnClickListener(v -> {
            appendLog("action", "render() called on " + KEY_ON_DEMAND);
            onDemandView.render();
        });

        findViewById(R.id.btn_send_event).setOnClickListener(v -> {
            String eventName = "inline.two_keys";
            appendLog("action", "sending event: " + eventName);
            CustomEvent event = new CustomEvent(eventName, "Inline two keys trigger");
            Tracker.send(event);
        });
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
        runOnUiThread(() -> {
            logText.append(line);
            logScroll.fullScroll(View.FOCUS_DOWN);
        });
    }
}
