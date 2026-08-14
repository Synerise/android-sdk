package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
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

public class InlineInAppSearchHostActivity extends BaseActivity {

    private static final String PLACEMENT_KEY = "searchtop";
    private static final String DEEPLINK_DETAIL = "sample://inline-detail";

    private View hostLayout;
    private View searchLayout;
    private FrameLayout inlineContainer;
    private InlineInAppView inlineInAppView;
    private InlineInAppView goneInlineView;
    private TextView logText;
    private boolean contentLoaded;
    private long createdAtMs;
    private long searchOpenedAtMs;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppSearchHostActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createdAtMs = SystemClock.elapsedRealtime();
        setContentView(R.layout.activity_inline_inapp_search_host);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_search);

        hostLayout = findViewById(R.id.host_layout);
        searchLayout = findViewById(R.id.search_layout);
        inlineContainer = findViewById(R.id.inline_container);
        logText = findViewById(R.id.log_text);

        // Pre-render at 0x0 — AUTOMATIC fires on attach, WebView loads invisibly
        inlineInAppView = Injector.createInlineInAppView(this, PLACEMENT_KEY);
        inlineInAppView.setOnInlineInAppViewListener(createListener());
        inlineContainer.addView(inlineInAppView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        appendLog("init", "pre-rendering key=" + PLACEMENT_KEY + " at 0x0");

        Injector.setOnInlineInAppListener(createGlobalListener());

        findViewById(R.id.search_bar).setOnClickListener(v -> openSearch());
        findViewById(R.id.btn_open_laggy).setOnClickListener(v ->
                startActivity(InlineInAppSearchActivity.createIntent(this)));

        setUpGoneVariant();
    }

    // Variant 3: create + load an inline now but keep it GONE (it still attaches → renders while hidden);
    // the button just flips it VISIBLE — same live WebView, no re-render.
    private void setUpGoneVariant() {
        FrameLayout goneContainer = findViewById(R.id.gone_container);
        goneInlineView = Injector.createInlineInAppView(this, PLACEMENT_KEY);
        goneInlineView.setOnInlineInAppViewListener(createGoneListener());
        goneContainer.addView(goneInlineView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        goneInlineView.setVisibility(View.GONE);
        appendLog("variant3", "created key=" + PLACEMENT_KEY + ", loading while GONE");

        findViewById(R.id.btn_reveal_gone).setOnClickListener(v -> {
            goneInlineView.setVisibility(View.VISIBLE);
            appendLog("variant3", "revealed (GONE → VISIBLE), WebView already loaded, no re-render");
        });
    }

    private OnInlineInAppViewListener createGoneListener() {
        return new OnInlineInAppViewListener() {
            @Override
            public void onProcessingStarted() {
                appendLog("onProcessingStarted", "");
            }

            @Override
            public void onLoaded(InlineInAppMessageData data) {
                appendLog("variant3.onLoaded", "loaded while GONE, hash=" + data.getCampaignHash());
            }

            @Override
            public void onUpdated(InlineInAppMessageData data) {
                appendLog("variant3.onUpdated", "hash=" + data.getCampaignHash());
            }

            @Override
            public void onFailed(InlineInAppMessageData data, String error) {
                appendLog("variant3.onFailed", error);
            }

            @Override
            public void onRemove(InlineInAppMessageData data) {
                appendLog("variant3.onRemove", "");
            }

            @Override
            public void onSizeChanged(InlineInAppMessageData data, InlineInAppSize size) {
                appendLog("variant3.onSizeChanged", "onSizeChanged px=" + size.getWidthPx() + "x" + size.getHeightPx() + " dp=" + size.getWidthDp() + "x" + size.getHeightDp());
            }
        };
    }

    private void openSearch() {
        searchOpenedAtMs = SystemClock.elapsedRealtime();
        hostLayout.setVisibility(View.GONE);
        searchLayout.setVisibility(View.VISIBLE);
        setInlineFullscreen(true);
        appendLog("search", "opened; contentLoaded=" + contentLoaded
                + " (preloaded " + (searchOpenedAtMs - createdAtMs) + "ms earlier)");
    }

    private void closeSearch() {
        searchLayout.setVisibility(View.GONE);
        hostLayout.setVisibility(View.VISIBLE);
        setInlineFullscreen(false);
        appendLog("search", "closed; inline back to 0x0 (still loaded)");
    }

    private void setInlineFullscreen(boolean fullscreen) {
        android.view.ViewGroup.LayoutParams params = inlineContainer.getLayoutParams();
        params.height = fullscreen
                ? android.view.ViewGroup.LayoutParams.MATCH_PARENT : 0;
        inlineContainer.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
        if (searchLayout.getVisibility() == View.VISIBLE) {
            closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private OnInlineInAppViewListener createListener() {
        return new OnInlineInAppViewListener() {
            @Override
            public void onProcessingStarted() {
                appendLog("onProcessingStarted", "");
            }

            @Override
            public void onLoaded(InlineInAppMessageData data) {
                contentLoaded = true;
                appendLog("onLoaded", "+" + (SystemClock.elapsedRealtime() - createdAtMs)
                        + "ms after host onCreate, hash=" + data.getCampaignHash());
            }

            @Override
            public void onUpdated(InlineInAppMessageData data) {
                contentLoaded = true;
                appendLog("onUpdated", "hash=" + data.getCampaignHash());
            }

            @Override
            public void onFailed(InlineInAppMessageData data, String error) {
                appendLog("onFailed", error);
            }

            @Override
            public void onRemove(InlineInAppMessageData data) {
                contentLoaded = false;
                appendLog("onRemove", "");
                setInlineFullscreen(false);
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
                if (DEEPLINK_DETAIL.equals(deepLink)) {
                    startActivity(InlineInAppDeeplinkTargetActivity.createIntent(InlineInAppSearchHostActivity.this));
                }
            }

            @Override
            public void onCustomAction(InlineInAppMessageData data, String name, Map<String, Object> params) {
                appendLog("onCustomAction", name);
            }

            @Override
            public void onCustomMethod(String name, HashMap<String, Object> params,
                                       InlineInAppMessageData data, InAppCustomMethodCompletion completion) {
                appendLog("onCustomMethod", name);
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
