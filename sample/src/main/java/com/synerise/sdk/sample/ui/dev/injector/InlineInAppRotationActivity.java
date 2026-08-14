package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.inapp.InAppCustomMethodCompletion;
import com.synerise.sdk.injector.inapp.inline.InlineInAppMessageData;
import com.synerise.sdk.injector.inapp.inline.InlineInAppSize;
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

/**
 * Manual test: inline InApp survives rotation because this Activity declares
 * android:configChanges (orientation|screenSize) and is therefore NOT recreated.
 * The inline view + WebView are preserved, so no callbacks should fire on rotation.
 */
public class InlineInAppRotationActivity extends BaseActivity {

    private static final String PLACEMENT_KEY = "rotationinline";

    private InlineInAppView inlineInAppView;
    private TextView logText;
    private ScrollView logScroll;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppRotationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_inapp_rotation);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_rotation);

        logText = findViewById(R.id.log_text);
        logScroll = findViewById(R.id.log_scroll);
        FrameLayout container = findViewById(R.id.container);

        inlineInAppView = Injector.createInlineInAppView(this, PLACEMENT_KEY);
        inlineInAppView.setOnInlineInAppViewListener(createListener());
        container.addView(inlineInAppView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        appendLog("init", "created with key=" + PLACEMENT_KEY);

        Injector.setOnInlineInAppListener(createGlobalListener());
    }

    private OnInlineInAppViewListener createListener() {
        return new OnInlineInAppViewListener() {
            @Override
            public void onProcessingStarted() {
                appendLog("onProcessingStarted", "");
            }

            @Override
            public void onLoaded(InlineInAppMessageData data) {
                appendLog("onLoaded", "hash=" + data.getCampaignHash());
            }

            @Override
            public void onUpdated(InlineInAppMessageData data) {
                appendLog("onUpdated", "hash=" + data.getCampaignHash());
            }

            @Override
            public void onFailed(InlineInAppMessageData data, String error) {
                appendLog("onFailed", error);
            }

            @Override
            public void onRemove(InlineInAppMessageData data) {
                appendLog("onRemove", data != null ? "hash=" + data.getCampaignHash() : "");
            }

            @Override
            public void onSizeChanged(InlineInAppMessageData data, InlineInAppSize size) {
                appendLog("onSizeChanged", "onSizeChanged px=" + size.getWidthPx() + "x" + size.getHeightPx() + " dp=" + size.getWidthDp() + "x" + size.getHeightDp());
            }
        };
    }

    private OnInlineInAppListener createGlobalListener() {
        return new OnInlineInAppListener() {
            @Override
            public void onInlineInAppAvailable(InlineInAppView view, InlineInAppMessageData data) {
                appendLog("onInlineInAppAvailable", "hash=" + data.getCampaignHash());
            }

            @Override
            public Map<String, Object> onContextFromAppRequired(InlineInAppMessageData data) {
                appendLog("onContextFromAppRequired", "");
                return new HashMap<>();
            }

            @Override
            public void onOpenedUrl(InlineInAppMessageData data, String url) {
                appendLog("onOpenedUrl", url);
            }

            @Override
            public void onOpenedDeepLink(InlineInAppMessageData data, String deepLink) {
                appendLog("onOpenedDeepLink", deepLink);
            }

            @Override
            public void onCustomAction(InlineInAppMessageData data, String name, Map<String, Object> params) {
                appendLog("onCustomAction", name + " params=" + params);
            }

            @Override
            public void onCustomMethod(String name, HashMap<String, Object> params,
                                       InlineInAppMessageData data, InAppCustomMethodCompletion completion) {
                appendLog("onCustomMethod", name + " params=" + params);
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
