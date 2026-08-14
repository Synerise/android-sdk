package com.synerise.sdk.sample.ui.dev.inapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.callback.OnInjectorListener;
import com.synerise.sdk.injector.callback.SyneriseSource;
import com.synerise.sdk.injector.inapp.InAppCustomMethodCompletion;
import com.synerise.sdk.injector.inapp.InAppMessageData;
import com.synerise.sdk.injector.inapp.OnInAppListener;
import com.synerise.sdk.injector.ui.handler.InjectorActionHandler;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Manual test for overlay InApp openUrl / openDeeplink: OnInjectorListener decides who opens the link,
 * OnInAppListener only observes, and with neither registered the SDK opens the browser itself.
 */
public class InAppUrlActionsActivity extends BaseActivity {

    private static final String URL_ACTIONS_TRIGGER_EVENT = "overlay.urlactions";
    private static final String LEGACY_OVERLAY_TRIGGER_EVENT = "overlay.test";
    private static final String TAG_CONFIG = "config";
    private static final String TAG_ACTION = "action";
    private static final String TAG_INJECTOR_LISTENER = "injectorListener";
    private static final String TAG_IN_APP_LISTENER = "inAppListener";
    private static final String TAG_SYSTEM_FALLBACK = "systemFallback-expected";

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    private OnInjectorListener injectorListenerReplacedOnEntry;
    private InjectorListenerMode injectorListenerMode = InjectorListenerMode.NOT_REGISTERED;
    private boolean inAppListenerRegistered;
    private TextView logView;
    private ScrollView logScroll;
    private Button injectorListenerButton;
    private Button inAppListenerButton;

    public static Intent createIntent(Context context) {
        return new Intent(context, InAppUrlActionsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_inapp_url_actions);
        ToolbarHelper.setUpChildToolbar(this, R.string.inapp_url_actions);

        logView = findViewById(R.id.inapp_log);
        logScroll = findViewById(R.id.log_scroll);
        injectorListenerButton = findViewById(R.id.toggle_injector_listener);
        inAppListenerButton = findViewById(R.id.toggle_inapp_listener);

        findViewById(R.id.send_trigger_event)
                .setOnClickListener(v -> sendTriggerEvent(URL_ACTIONS_TRIGGER_EVENT));
        findViewById(R.id.send_legacy_trigger_event)
                .setOnClickListener(v -> sendTriggerEvent(LEGACY_OVERLAY_TRIGGER_EVENT));
        injectorListenerButton.setOnClickListener(v -> cycleInjectorListenerMode());
        inAppListenerButton.setOnClickListener(v -> toggleInAppListener());
        findViewById(R.id.clear_log).setOnClickListener(v -> logView.setText(""));

        injectorListenerReplacedOnEntry = InjectorActionHandler.getOnInjectorListener();
        logEntryState();
        applyInjectorListenerMode();
        applyInAppListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Injector.removeInAppListener();
        InjectorActionHandler.setOnInjectorListener(injectorListenerReplacedOnEntry);
    }

    private void logEntryState() {
        if (injectorListenerReplacedOnEntry != null) {
            log(TAG_CONFIG, "An app-wide OnInjectorListener was already registered (App.java registers a "
                    + "no-op one). It is replaced while this screen is open and restored on exit.");
            return;
        }
        log(TAG_CONFIG, "No app-wide OnInjectorListener was registered on entry.");
    }

    private void cycleInjectorListenerMode() {
        injectorListenerMode = injectorListenerMode.next();
        applyInjectorListenerMode();
    }

    private void applyInjectorListenerMode() {
        boolean registered = injectorListenerMode != InjectorListenerMode.NOT_REGISTERED;
        InjectorActionHandler.setOnInjectorListener(registered ? buildInjectorListener() : null);
        injectorListenerButton.setText(getString(R.string.inapp_url_actions_injector_mode,
                getString(injectorListenerMode.labelResId)));
        log(TAG_CONFIG, "OnInjectorListener → " + getString(injectorListenerMode.labelResId));
        if (!registered) {
            log(TAG_SYSTEM_FALLBACK, "Nothing will be logged at click time: the SDK calls SystemUtils "
                    + "itself, so expect the browser / deep-link Intent to open.");
        }
    }

    private OnInjectorListener buildInjectorListener() {
        return new OnInjectorListener() {
            @Override
            public void onOpenUrl(SyneriseSource source, String url) {
                log(TAG_INJECTOR_LISTENER, "onOpenUrl source=" + source + " url=" + url);
                if (injectorListenerMode == InjectorListenerMode.DELEGATES_TO_SYSTEM) {
                    log(TAG_SYSTEM_FALLBACK, "Listener delegates to the SDK default — browser opens.");
                    OnInjectorListener.super.onOpenUrl(source, url);
                    return;
                }
                log(TAG_INJECTOR_LISTENER, "Handled inside the app — the browser must NOT open.");
            }

            @Override
            public void onDeepLink(SyneriseSource source, String deepLink) {
                log(TAG_INJECTOR_LISTENER, "onDeepLink source=" + source + " deepLink=" + deepLink);
                if (injectorListenerMode == InjectorListenerMode.DELEGATES_TO_SYSTEM) {
                    log(TAG_SYSTEM_FALLBACK, "Listener delegates to the SDK default — Intent is fired.");
                    OnInjectorListener.super.onDeepLink(source, deepLink);
                    return;
                }
                log(TAG_INJECTOR_LISTENER, "Handled inside the app — no Intent is fired.");
            }
        };
    }

    private void toggleInAppListener() {
        inAppListenerRegistered = !inAppListenerRegistered;
        applyInAppListener();
    }

    private void applyInAppListener() {
        if (inAppListenerRegistered) {
            Injector.setOnInAppListener(buildInAppListener());
        } else {
            Injector.removeInAppListener();
        }
        int stateResId = inAppListenerRegistered
                ? R.string.inapp_url_actions_state_registered
                : R.string.inapp_url_actions_state_not_registered;
        inAppListenerButton.setText(getString(R.string.inapp_url_actions_inapp_listener_state,
                getString(stateResId)));
        log(TAG_CONFIG, "OnInAppListener → " + getString(stateResId));
    }

    private OnInAppListener buildInAppListener() {
        return new OnInAppListener() {
            @Override
            public boolean shouldShow(InAppMessageData data) {
                return true;
            }

            @Override
            public void onShown(InAppMessageData data) {
                log(TAG_IN_APP_LISTENER, "onShown hash=" + data.getCampaignHash());
            }

            @Override
            public void onDismissed(InAppMessageData data) {
                log(TAG_IN_APP_LISTENER, "onDismissed hash=" + data.getCampaignHash());
            }

            @Override
            public void onHandledOpenUrl(InAppMessageData data) {
                log(TAG_IN_APP_LISTENER, "onHandledOpenUrl (notification only, UTM appended) url="
                        + data.getUrl());
            }

            @Override
            public void onHandledOpenDeepLink(InAppMessageData data) {
                log(TAG_IN_APP_LISTENER, "onHandledOpenDeepLink (notification only, no UTM) deepLink="
                        + data.getDeepLink());
            }

            @Override
            public HashMap<String, Object> onContextFromAppRequired(InAppMessageData data) {
                return null;
            }

            @Override
            public void onCustomAction(String identifier, HashMap<String, Object> params, InAppMessageData data) {
                log(TAG_IN_APP_LISTENER, "onCustomAction " + identifier + " params=" + params);
            }

            @Override
            public void onCustomMethod(String name, HashMap<String, Object> params,
                                       InAppMessageData data, InAppCustomMethodCompletion completion) {
                log(TAG_IN_APP_LISTENER, "onCustomMethod " + name + " params=" + params);
                completion.success(params);
            }
        };
    }

    private void sendTriggerEvent(String eventName) {
        Tracker.send(new CustomEvent(eventName, "InApp url actions test"));
        log(TAG_ACTION, "Sent trigger event: " + eventName);
    }

    private void log(String tag, String message) {
        String line = timeFormat.format(new Date()) + " [" + tag + "] " + message + "\n";
        runOnUiThread(() -> {
            if (isFinishing() || logView == null) return;
            logView.append(line);
            logScroll.post(() -> logScroll.fullScroll(View.FOCUS_DOWN));
        });
    }

    private enum InjectorListenerMode {
        NOT_REGISTERED(R.string.inapp_url_actions_injector_not_registered),
        HANDLES_ACTION(R.string.inapp_url_actions_injector_handles),
        DELEGATES_TO_SYSTEM(R.string.inapp_url_actions_injector_delegates);

        private final int labelResId;

        InjectorListenerMode(int labelResId) {
            this.labelResId = labelResId;
        }

        private InjectorListenerMode next() {
            InjectorListenerMode[] modes = values();
            return modes[(ordinal() + 1) % modes.length];
        }
    }
}
