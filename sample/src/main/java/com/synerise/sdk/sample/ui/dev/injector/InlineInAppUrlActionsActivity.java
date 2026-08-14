package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.callback.OnInjectorListener;
import com.synerise.sdk.injector.callback.SyneriseSource;
import com.synerise.sdk.injector.inapp.InAppCustomMethodCompletion;
import com.synerise.sdk.injector.inapp.inline.InlineInAppMessageData;
import com.synerise.sdk.injector.inapp.inline.InlineInAppSize;
import com.synerise.sdk.injector.inapp.inline.OnInlineInAppListener;
import com.synerise.sdk.injector.inapp.inline.OnInlineInAppViewListener;
import com.synerise.sdk.injector.inapp.ui.inline.InlineInAppView;
import com.synerise.sdk.injector.ui.handler.InjectorActionHandler;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Manual test for the inline InApp two-level link fallback: OnInlineInAppListener wins, then
 * OnInjectorListener, then the SDK opens the browser / deep-link Intent itself.
 */
public class InlineInAppUrlActionsActivity extends BaseActivity {

    private static final String PLACEMENT_KEY = "demand";
    private static final String TAG_CONFIG = "config";
    private static final String TAG_ACTION = "action";
    private static final String TAG_VIEW = "view";
    private static final String TAG_INLINE_LISTENER = "inlineListener";
    private static final String TAG_INJECTOR_LISTENER = "injectorListener";
    private static final String TAG_SYSTEM_FALLBACK = "systemFallback-expected";

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    private OnInjectorListener injectorListenerReplacedOnEntry;
    private ListenerMode listenerMode = ListenerMode.INLINE_LISTENER;
    private InlineInAppView inlineInAppView;
    private TextView logText;
    private ScrollView logScroll;
    private Button listenerModeButton;

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppUrlActionsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_inapp_url_actions);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_url_actions);

        logText = findViewById(R.id.log_text);
        logScroll = findViewById(R.id.log_scroll);
        listenerModeButton = findViewById(R.id.btn_listener_mode);
        listenerModeButton.setOnClickListener(v -> cycleListenerMode());
        findViewById(R.id.btn_render).setOnClickListener(v -> render());
        findViewById(R.id.btn_clear_log).setOnClickListener(v -> logText.setText(""));

        injectorListenerReplacedOnEntry = InjectorActionHandler.getOnInjectorListener();
        attachInlineView();
        applyListenerMode();
    }

    @Override
    protected void onDestroy() {
        Injector.removeInlineInAppListener();
        InjectorActionHandler.setOnInjectorListener(injectorListenerReplacedOnEntry);
        super.onDestroy();
    }

    private void attachInlineView() {
        FrameLayout container = findViewById(R.id.container);
        inlineInAppView = Injector.createInlineInAppView(this, PLACEMENT_KEY);
        inlineInAppView.setOnInlineInAppViewListener(buildViewListener());
        container.addView(inlineInAppView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        log(TAG_CONFIG, "Inline view created with key=" + PLACEMENT_KEY);
    }

    private void render() {
        log(TAG_ACTION, "render() called with mode " + getString(listenerMode.labelResId));
        inlineInAppView.render();
    }

    private void cycleListenerMode() {
        listenerMode = listenerMode.next();
        applyListenerMode();
    }

    private void applyListenerMode() {
        boolean inlineListenerWanted = listenerMode == ListenerMode.INLINE_LISTENER;
        if (inlineListenerWanted) {
            Injector.setOnInlineInAppListener(buildInlineListener());
        } else {
            Injector.removeInlineInAppListener();
        }
        InjectorActionHandler.setOnInjectorListener(
                listenerMode == ListenerMode.INJECTOR_LISTENER ? buildInjectorListener() : null);
        listenerModeButton.setText(getString(R.string.inline_inapp_url_actions_mode,
                getString(listenerMode.labelResId)));
        log(TAG_CONFIG, "Mode → " + getString(listenerMode.labelResId)
                + ". Listeners are captured while the campaign renders, so press render() again now.");
        logExpectationForMode();
    }

    private void logExpectationForMode() {
        if (listenerMode == ListenerMode.INLINE_LISTENER) {
            log(TAG_CONFIG, "Expect [inlineListener] onOpenedUrl / onOpenedDeepLink and nothing else.");
            return;
        }
        if (listenerMode == ListenerMode.INJECTOR_LISTENER) {
            log(TAG_CONFIG, "Expect [injectorListener] onOpenUrl / onDeepLink; the browser must NOT open.");
            return;
        }
        log(TAG_SYSTEM_FALLBACK, "No listener registered: nothing is logged at click time, the SDK opens "
                + "the browser / deep-link Intent itself.");
    }

    private OnInlineInAppListener buildInlineListener() {
        return new OnInlineInAppListener() {
            @Override
            public void onInlineInAppAvailable(InlineInAppView view, InlineInAppMessageData data) {
                log(TAG_INLINE_LISTENER, "onInlineInAppAvailable key=" + data.getPlacementKey());
            }

            @Override
            public Map<String, Object> onContextFromAppRequired(InlineInAppMessageData data) {
                return new HashMap<>();
            }

            @Override
            public void onOpenedUrl(InlineInAppMessageData data, String url) {
                log(TAG_INLINE_LISTENER, "onOpenedUrl (final url, UTM appended) " + url);
            }

            @Override
            public void onOpenedDeepLink(InlineInAppMessageData data, String deepLink) {
                log(TAG_INLINE_LISTENER, "onOpenedDeepLink (raw, never UTM appended) " + deepLink);
            }

            @Override
            public void onCustomAction(InlineInAppMessageData data, String name, Map<String, Object> params) {
                log(TAG_INLINE_LISTENER, "onCustomAction " + name + " params=" + params);
            }

            @Override
            public void onCustomMethod(String name, HashMap<String, Object> params,
                                       InlineInAppMessageData data, InAppCustomMethodCompletion completion) {
                log(TAG_INLINE_LISTENER, "onCustomMethod " + name + " params=" + params);
                completion.success(params);
            }
        };
    }

    private OnInjectorListener buildInjectorListener() {
        return new OnInjectorListener() {
            @Override
            public void onOpenUrl(SyneriseSource source, String url) {
                log(TAG_INJECTOR_LISTENER, "onOpenUrl source=" + source
                        + " (final url, UTM appended) " + url);
            }

            @Override
            public void onDeepLink(SyneriseSource source, String deepLink) {
                log(TAG_INJECTOR_LISTENER, "onDeepLink source=" + source
                        + " (raw, never UTM appended) " + deepLink);
            }
        };
    }

    private OnInlineInAppViewListener buildViewListener() {
        return new OnInlineInAppViewListener() {
            @Override
            public void onProcessingStarted() {
                log(TAG_VIEW, "onProcessingStarted");
            }

            @Override
            public void onLoaded(InlineInAppMessageData data) {
                log(TAG_VIEW, "onLoaded hash=" + data.getCampaignHash());
            }

            @Override
            public void onUpdated(InlineInAppMessageData data) {
                log(TAG_VIEW, "onUpdated hash=" + data.getCampaignHash());
            }

            @Override
            public void onFailed(InlineInAppMessageData data, String error) {
                log(TAG_VIEW, "onFailed " + error);
            }

            @Override
            public void onRemove(InlineInAppMessageData data) {
                log(TAG_VIEW, "onRemove");
            }

            @Override
            public void onSizeChanged(InlineInAppMessageData data, InlineInAppSize size) {
                log(TAG_VIEW, "onSizeChanged px=" + size.getWidthPx() + "x" + size.getHeightPx());
            }
        };
    }

    private void log(String tag, String message) {
        String line = timeFormat.format(new Date()) + " [" + tag + "] " + message + "\n";
        runOnUiThread(() -> {
            if (isFinishing() || logText == null) return;
            logText.append(line);
            logScroll.post(() -> logScroll.fullScroll(View.FOCUS_DOWN));
        });
    }

    private enum ListenerMode {
        INLINE_LISTENER(R.string.inline_inapp_url_actions_mode_inline),
        INJECTOR_LISTENER(R.string.inline_inapp_url_actions_mode_injector),
        NO_LISTENERS(R.string.inline_inapp_url_actions_mode_none);

        private final int labelResId;

        ListenerMode(int labelResId) {
            this.labelResId = labelResId;
        }

        private ListenerMode next() {
            ListenerMode[] modes = values();
            return modes[(ordinal() + 1) % modes.length];
        }
    }
}
