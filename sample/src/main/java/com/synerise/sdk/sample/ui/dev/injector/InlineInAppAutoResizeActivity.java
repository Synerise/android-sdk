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

public class InlineInAppAutoResizeActivity extends BaseActivity {

    private static final String DEFAULT_PLACEMENT_KEY = "autoresize";

    private InlineInAppView inlineInAppView;
    private EditText inputPlacementKey;
    private FrameLayout container;
    private TextView sizeLabel;
    private TextView logText;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppAutoResizeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_inapp_autoresize);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_autoresize);

        inputPlacementKey = findViewById(R.id.input_placement_key);
        sizeLabel = findViewById(R.id.size_label);
        logText = findViewById(R.id.log_text);
        container = findViewById(R.id.component_container);

        createComponent(DEFAULT_PLACEMENT_KEY);

        findViewById(R.id.btn_create).setOnClickListener(v -> {
            String key = inputPlacementKey.getText().toString().trim();
            if (TextUtils.isEmpty(key)) {
                appendLog("action", "placementKey is empty");
                return;
            }
            createComponent(key);
        });

        findViewById(R.id.btn_render).setOnClickListener(v -> {
            if (inlineInAppView == null) {
                appendLog("action", "no component — tap Create first");
                return;
            }
            appendLog("action", "render() called");
            inlineInAppView.render();
        });

        findViewById(R.id.btn_reset).setOnClickListener(v -> resizeComponent(0, 0));

        Injector.setOnInlineInAppListener(createGlobalListener());
    }

    private void createComponent(String key) {
        if (inlineInAppView != null) {
            container.removeView(inlineInAppView);
        }
        inlineInAppView = Injector.createInlineInAppView(this, key);
        inlineInAppView.setOnInlineInAppViewListener(createListener());
        container.addView(inlineInAppView, new FrameLayout.LayoutParams(0, 0));
        sizeLabel.setText("Component (current: 0x0):");
        appendLog("init", "created key=" + key + " size=0x0");
    }

    // JS reports CSS pixels — WebView CSS px maps 1:1 to dp, so convert via density
    private void resizeComponent(int widthDp, int heightDp) {
        int widthPx = widthDp == 0 ? 0 : dp(widthDp);
        int heightPx = heightDp == 0 ? 0 : dp(heightDp);
        FrameLayout.LayoutParams params =
                (FrameLayout.LayoutParams) inlineInAppView.getLayoutParams();
        params.width = widthPx;
        params.height = heightPx;
        inlineInAppView.setLayoutParams(params);
        sizeLabel.setText("Component (current: " + widthDp + "x" + heightDp + " dp):");
        appendLog("resize", "component rescaled to " + widthDp + "x" + heightDp + " dp"
                + " (" + widthPx + "x" + heightPx + " px)");
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
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
                appendLog("onRemove", "");
                resizeComponent(0, 0);
            }

            @Override
            public void onSizeChanged(InlineInAppMessageData data, InlineInAppSize size) {
                appendLog("onSizeChanged", "onSizeChanged px=" + size.getWidthPx() + "x" + size.getHeightPx() + " dp=" + size.getWidthDp() + "x" + size.getHeightDp());
                resizeComponent((int) size.getWidthDp(), (int) size.getHeightDp());
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
