package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class InlineInAppDebugActivity extends BaseActivity {

    private EditText inputWidth;
    private EditText inputHeight;
    private EditText inputPlacementKey;
    private FrameLayout componentContainer;
    private TextView logText;
    private InlineInAppView inlineInAppView;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppDebugActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_inapp_debug);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_debug);

        inputWidth = findViewById(R.id.input_width);
        inputHeight = findViewById(R.id.input_height);
        inputPlacementKey = findViewById(R.id.input_placement_key);
        componentContainer = findViewById(R.id.component_container);
        logText = findViewById(R.id.log_text);

        findViewById(R.id.btn_create).setOnClickListener(v -> createComponent());
        findViewById(R.id.btn_render).setOnClickListener(v -> {
            if (inlineInAppView == null) {
                appendLog("action", "no component — tap Create first");
                return;
            }
            appendLog("action", "render() called");
            inlineInAppView.render();
        });
        findViewById(R.id.btn_clear).setOnClickListener(v -> clearComponent());

        Injector.setOnInlineInAppListener(createGlobalListener());
    }

    private void createComponent() {
        String key = inputPlacementKey.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            appendLog("action", "placementKey is empty");
            return;
        }

        clearComponent();

        int width = parseDp(inputWidth, FrameLayout.LayoutParams.MATCH_PARENT);
        int height = parseDp(inputHeight, dp(300));

        inlineInAppView = Injector.createInlineInAppView(this, key);
        inlineInAppView.setOnInlineInAppViewListener(createListener());
        componentContainer.addView(inlineInAppView, new FrameLayout.LayoutParams(width, height));
        appendLog("action", "created key=" + key
                + " size=" + describe(width) + "x" + describe(height));
    }

    private void clearComponent() {
        if (inlineInAppView != null) {
            componentContainer.removeView(inlineInAppView);
            inlineInAppView = null;
            appendLog("action", "component cleared");
        }
    }

    private int parseDp(EditText input, int fallback) {
        String text = input.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return fallback;
        try {
            return dp(Integer.parseInt(text));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private String describe(int sizePx) {
        return sizePx == FrameLayout.LayoutParams.MATCH_PARENT ? "match" : sizePx + "px";
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
        runOnUiThread(() -> logText.append(line));
    }
}
