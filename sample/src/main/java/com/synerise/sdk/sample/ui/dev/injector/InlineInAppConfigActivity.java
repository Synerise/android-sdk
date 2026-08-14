package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

public class InlineInAppConfigActivity extends BaseActivity {

    private static final String[] ON_DEMAND_KEYS = {
            "cappinglife", "cappingtime", "controlgroup", "dynamic", "delaythree"};
    private static final String[] AUTOMATIC_KEYS = {
            "schedulein", "scheduleout"};

    private TextView logText;
    private ScrollView logScroll;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppConfigActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_inapp_config);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_config);

        logText = findViewById(R.id.log_text);
        logScroll = findViewById(R.id.log_scroll);
        LinearLayout container = findViewById(R.id.sections_container);

        for (String key : ON_DEMAND_KEYS) {
            addSection(container, key, true);
        }
        for (String key : AUTOMATIC_KEYS) {
            addSection(container, key, false);
        }

        Injector.setOnInlineInAppListener(createGlobalListener());
    }

    private void addSection(LinearLayout container, String key, boolean withRenderButton) {
        TextView label = new TextView(this);
        label.setText(key + (withRenderButton ? " (ON_DEMAND)" : " (AUTOMATIC)"));
        container.addView(label);

        FrameLayout frame = new FrameLayout(this);
        frame.setBackgroundColor(0x11000000);
        LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(180));
        frameParams.topMargin = dp(4);
        container.addView(frame, frameParams);

        InlineInAppView view = Injector.createInlineInAppView(this, key);
        view.setOnInlineInAppViewListener(createListener(key));
        frame.addView(view, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        appendLog(key, "created");

        if (withRenderButton) {
            Button render = new Button(this);
            render.setText("render() " + key);
            render.setOnClickListener(v -> {
                appendLog(key, "render() called");
                view.render();
            });
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            btnParams.bottomMargin = dp(16);
            container.addView(render, btnParams);
        } else {
            View spacer = new View(this);
            container.addView(spacer, new LinearLayout.LayoutParams(0, dp(16)));
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
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
