package com.synerise.sdk.sample.ui.dev.inapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.EditText;
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

import java.util.HashMap;

/**
 * Developer tools → InApps → InApp Context From App.
 *
 * Hosts the demo callbacks for the app↔InApp communication feature:
 *  - seeds Injector.inAppContext (read in JS via SRInApp.getContextFromApp())
 *  - registers OnInAppListener.onCustomMethod (JS SRInApp.customMethod round-trip)
 *  - the "requestContextChange" customMethod mutates the context and calls
 *    Injector.notifyInAppContextChange() so JS SRInApp.onContextFromApp receives it.
 * Every callback is mirrored to the on-screen log.
 */
public class InAppContextActivity extends BaseActivity {

    private static final long SIMULATED_WORK_MS = 500L;
    private static final long MAX_SIMULATED_WORK_MS = 15_000L;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private TextView logView;
    private ScrollView logScroll;
    private EditText keyInput;
    private EditText valueInput;

    public static Intent createIntent(Context context) {
        return new Intent(context, InAppContextActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_inapp_context);

        ToolbarHelper.setUpChildToolbar(this, R.string.inapp_context_from_app);

        logView = findViewById(R.id.inapp_log);
        logScroll = findViewById(R.id.log_scroll);
        keyInput = findViewById(R.id.input_context_key);
        valueInput = findViewById(R.id.input_context_value);
        findViewById(R.id.add_to_context).setOnClickListener(v -> addToContext());
        findViewById(R.id.notify_context_change).setOnClickListener(v -> notifyContextChange());
        findViewById(R.id.reseed_context).setOnClickListener(v -> seedContext());
        findViewById(R.id.send_trigger_event).setOnClickListener(v -> sendTriggerEvent());

        seedContext();
        Injector.setOnInAppListener(buildListener());
        log("InApp listener registered. Trigger your campaign, then use the InApp buttons.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Injector.removeInAppListener();
    }

    /** Initial context exposed to InApp campaigns via SRInApp.getContextFromApp(). */
    private void seedContext() {
        Injector.inAppContext.clear();
        Injector.inAppContext.put("userName", "Konrad");
        Injector.inAppContext.put("loyaltyTier", "gold");
        Injector.inAppContext.put("cartItems", 3);
        Injector.inAppContext.put("changeCount", 0);
        log("Context reset to defaults (previous entries cleared): " + Injector.inAppContext);
    }

    /** Value is parsed to boolean/number when possible, else stored as a String. */
    private void addToContext() {
        String key = keyInput.getText() == null ? "" : keyInput.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            log("Add to context: key is empty");
            return;
        }
        String rawValue = valueInput.getText() == null ? "" : valueInput.getText().toString();
        Object value = parseValue(rawValue);
        Injector.inAppContext.put(key, value);
        log("Context put: " + key + " = " + value + " (" + value.getClass().getSimpleName() + ")");
        valueInput.getText().clear();
    }

    /** Pushes the current context to a live InApp (fires JS SRInApp.onContextFromApp). */
    private void notifyContextChange() {
        Injector.notifyInAppContextChange();
        log("notifyInAppContextChange() called. Context: " + Injector.inAppContext);
    }

    /** Interprets the raw text as boolean, then whole number, then decimal, else keeps it as a String. */
    private Object parseValue(String raw) {
        String value = raw.trim();
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {
            // not an integer
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
            // not a decimal
        }
        return value;
    }

    /** Fires a custom event so a campaign with this trigger can display its InApp. */
    private void sendTriggerEvent() {
        Tracker.send(new CustomEvent("inapp.context.test", "InApp context test"));
        log("Sent trigger event: inapp.context.test");
    }

    private OnInAppListener buildListener() {
        return new OnInAppListener() {
            @Override
            public boolean shouldShow(InAppMessageData data) {
                return true;
            }

            @Override
            public void onShown(InAppMessageData data) {
                log("onShown: " + data.getCampaignHash());
            }

            @Override
            public void onDismissed(InAppMessageData data) {
                log("onDismissed: " + data.getCampaignHash());
            }

            @Override
            public void onHandledOpenUrl(InAppMessageData data) {
                log("onHandledOpenUrl");
            }

            @Override
            public void onHandledOpenDeepLink(InAppMessageData data) {
                log("onHandledOpenDeepLink");
            }

            @Override
            public HashMap<String, Object> onContextFromAppRequired(InAppMessageData data) {
                return null;
            }

            @Override
            public void onCustomAction(String identifier, HashMap<String, Object> params, InAppMessageData data) {
                log("onCustomAction: " + identifier + " params=" + params);
            }

            @Override
            public void onCustomMethod(String name, HashMap<String, Object> params,
                                            InAppMessageData data, InAppCustomMethodCompletion completion) {
                long delay = resolveSimulatedDelay(params);
                log("onCustomMethod: " + name + " params=" + params + " delay=" + delay + "ms");
                // Simulate async work (e.g. calling your own backend), then resolve.
                mainHandler.postDelayed(() -> handleCustomMethod(name, params, completion), delay);
            }
        };
    }

    private long resolveSimulatedDelay(HashMap<String, Object> params) {
        Object raw = params != null ? params.get("delayMs") : null;
        long delay = raw instanceof Number ? ((Number) raw).longValue() : SIMULATED_WORK_MS;
        return Math.max(0L, Math.min(delay, MAX_SIMULATED_WORK_MS));
    }

    private void handleCustomMethod(String name, HashMap<String, Object> params,
                                    InAppCustomMethodCompletion completion) {
        if ("failingMethod".equals(name)) {
            log("→ failure: simulated");
            completion.failure("Simulated failure from the app for: " + name);
            return;
        }
        if ("requestContextChange".equals(name)) {
            // Full round-trip: mutate context, push it (fires JS onContextFromApp), then resolve.
            Object current = Injector.inAppContext.get("changeCount");
            int next = (current instanceof Number) ? ((Number) current).intValue() + 1 : 1;
            Injector.inAppContext.put("changeCount", next);
            Injector.inAppContext.put("loyaltyTier", next % 2 == 0 ? "gold" : "platinum");
            Injector.inAppContext.put("changedAt", System.currentTimeMillis());
            Injector.notifyInAppContextChange();
            log("→ context changed (#" + next + ") and notified");

            HashMap<String, Object> result = new HashMap<>();
            result.put("status", "context-updated");
            result.put("changeCount", next);
            completion.success(result);
            return;
        }
        if ("fetchUserPoints".equals(name)) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("userId", params != null ? params.get("userId") : null);
            result.put("points", 1280);
            result.put("currency", "PLN");
            log("→ success: " + result);
            completion.success(result);
            return;
        }
        // Default: echo unknown methods so they still resolve.
        HashMap<String, Object> result = new HashMap<>();
        result.put("handled", name);
        result.put("params", params);
        log("→ echo: " + result);
        completion.success(result);
    }

    private void log(String message) {
        runOnUiThread(() -> {
            if (isFinishing() || logView == null) return;
            logView.append(message + "\n");
            if (logScroll != null) {
                logScroll.post(() -> logScroll.fullScroll(ScrollView.FOCUS_DOWN));
            }
        });
    }
}
