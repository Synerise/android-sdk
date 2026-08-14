package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.inapp.inline.InlineInAppMessageData;
import com.synerise.sdk.injector.inapp.inline.InlineInAppSize;
import com.synerise.sdk.injector.inapp.inline.OnInlineInAppViewListener;
import com.synerise.sdk.injector.inapp.ui.inline.InlineInAppView;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InlineInAppFixVerificationActivity extends BaseActivity {

    private static final String DRAIN_PLACEMENT_KEY = "inlinependingdrainverification";

    private EditText inputPriorityKey;
    private EditText inputCleanupKey;
    private FrameLayout priorityContainer;
    private FrameLayout drainContainer;
    private FrameLayout cleanupContainer;
    private TextView logText;
    private InlineInAppView priorityView;
    private InlineInAppView drainView;
    private InlineInAppView cleanupView;
    private String capturedCleanupHash;
    private int cleanupRemoveCount;
    private long drainStartMillis;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppFixVerificationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drainStartMillis = System.currentTimeMillis();
        setContentView(R.layout.activity_inline_inapp_fix_verification);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_fix_verification);

        bindViews();
        setupPrioritySection();
        setupDrainSection();
        setupCleanupSection();
    }

    private void bindViews() {
        inputPriorityKey = findViewById(R.id.input_priority_key);
        inputCleanupKey = findViewById(R.id.input_cleanup_key);
        priorityContainer = findViewById(R.id.priority_container);
        drainContainer = findViewById(R.id.drain_container);
        cleanupContainer = findViewById(R.id.cleanup_container);
        logText = findViewById(R.id.log_text);
    }

    private void setupPrioritySection() {
        findViewById(R.id.btn_priority_create).setOnClickListener(v -> createPriorityComponent());
        findViewById(R.id.btn_priority_render).setOnClickListener(v -> renderPriorityComponent());
    }

    private void createPriorityComponent() {
        String key = inputPriorityKey.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            appendLog("priority", "placementKey is empty");
            return;
        }
        if (priorityView != null) {
            priorityView.release();
            priorityContainer.removeView(priorityView);
        }
        priorityView = createAttachedView(priorityContainer, key, createPriorityListener());
        appendLog("priority", "created & attached key=" + key);
    }

    private void renderPriorityComponent() {
        if (priorityView == null) {
            appendLog("priority", "no component — tap Create first");
            return;
        }
        appendLog("priority", "render() called");
        priorityView.render();
    }

    private void setupDrainSection() {
        drainView = createAttachedView(drainContainer, DRAIN_PLACEMENT_KEY, createDrainListener());
        appendLog("drain", "attached key=" + DRAIN_PLACEMENT_KEY + " (auto-renders on attach; "
                + "cold-start via adb to hit the parked window)");
    }

    private void setupCleanupSection() {
        findViewById(R.id.btn_cleanup_render).setOnClickListener(v -> renderCleanupComponent());
        findViewById(R.id.btn_cleanup_close).setOnClickListener(v -> closeCleanupByHash());
        findViewById(R.id.btn_cleanup_render_again).setOnClickListener(v -> renderCleanupAgain());
    }

    private void renderCleanupComponent() {
        String key = inputCleanupKey.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            appendLog("cleanup", "placementKey is empty");
            return;
        }
        if (cleanupView != null) {
            cleanupView.release();
            cleanupContainer.removeView(cleanupView);
        }
        capturedCleanupHash = null;
        cleanupRemoveCount = 0;
        cleanupView = createAttachedView(cleanupContainer, key, createCleanupListener());
        appendLog("cleanup", "created & attached key=" + key + " (auto-renders on attach)");
    }

    private void closeCleanupByHash() {
        if (TextUtils.isEmpty(capturedCleanupHash)) {
            appendLog("cleanup", "no captured hash — render first");
            return;
        }
        appendLog("cleanup", "closeInAppMessage(hash=" + capturedCleanupHash + ")");
        Injector.closeInAppMessage(capturedCleanupHash);
    }

    private void renderCleanupAgain() {
        if (cleanupView == null) {
            appendLog("cleanup", "no component — tap Render first");
            return;
        }
        cleanupRemoveCount = 0;
        cleanupView.setVisibility(View.VISIBLE);
        appendLog("cleanup", "render() again");
        cleanupView.render();
    }

    private InlineInAppView createAttachedView(FrameLayout container, String key,
                                               OnInlineInAppViewListener listener) {
        InlineInAppView view = Injector.createInlineInAppView(this, key);
        view.setOnInlineInAppViewListener(listener);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, dp(300));
        container.addView(view, params);
        return view;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private OnInlineInAppViewListener createPriorityListener() {
        return new OnInlineInAppViewListener() {
            @Override
            public void onProcessingStarted() {
                appendLog("priority:onProcessingStarted", "");
            }

            @Override
            public void onLoaded(InlineInAppMessageData data) {
                appendLog("priority:onLoaded", "hash=" + campaignHashOf(data));
            }

            @Override
            public void onUpdated(InlineInAppMessageData data) {
                appendLog("priority:onUpdated", "hash=" + campaignHashOf(data));
            }

            @Override
            public void onFailed(InlineInAppMessageData data, String error) {
                appendLog("priority:onFailed", error);
            }

            @Override
            public void onRemove(InlineInAppMessageData data) {
                appendLog("priority:onRemove", "hash=" + campaignHashOf(data));
            }

            @Override
            public void onSizeChanged(InlineInAppMessageData data, InlineInAppSize size) {
                appendLog("priority:onSizeChanged", describeSize(size));
            }
        };
    }

    private OnInlineInAppViewListener createDrainListener() {
        return new OnInlineInAppViewListener() {
            @Override
            public void onProcessingStarted() {
                appendLog("drain:onProcessingStarted", elapsedSinceOnCreate());
            }

            @Override
            public void onLoaded(InlineInAppMessageData data) {
                appendLog("drain:onLoaded", elapsedSinceOnCreate() + " hash=" + campaignHashOf(data));
            }

            @Override
            public void onUpdated(InlineInAppMessageData data) {
                appendLog("drain:onUpdated", elapsedSinceOnCreate() + " hash=" + campaignHashOf(data));
            }

            @Override
            public void onFailed(InlineInAppMessageData data, String error) {
                appendLog("drain:onFailed", elapsedSinceOnCreate() + " " + error);
            }

            @Override
            public void onRemove(InlineInAppMessageData data) {
                appendLog("drain:onRemove", elapsedSinceOnCreate() + " hash=" + campaignHashOf(data));
            }

            @Override
            public void onSizeChanged(InlineInAppMessageData data, InlineInAppSize size) {
                appendLog("drain:onSizeChanged", elapsedSinceOnCreate() + " " + describeSize(size));
            }
        };
    }

    private OnInlineInAppViewListener createCleanupListener() {
        return new OnInlineInAppViewListener() {
            @Override
            public void onProcessingStarted() {
                appendLog("cleanup:onProcessingStarted", "");
            }

            @Override
            public void onLoaded(InlineInAppMessageData data) {
                capturedCleanupHash = campaignHashOf(data);
                appendLog("cleanup:onLoaded", "captured hash=" + capturedCleanupHash);
            }

            @Override
            public void onUpdated(InlineInAppMessageData data) {
                appendLog("cleanup:onUpdated", "hash=" + campaignHashOf(data));
            }

            @Override
            public void onFailed(InlineInAppMessageData data, String error) {
                appendLog("cleanup:onFailed", error);
            }

            @Override
            public void onRemove(InlineInAppMessageData data) {
                cleanupRemoveCount++;
                appendLog("cleanup:onRemove", "#" + cleanupRemoveCount + " hash=" + campaignHashOf(data));
                if (cleanupView != null) {
                    cleanupView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSizeChanged(InlineInAppMessageData data, InlineInAppSize size) {
                appendLog("cleanup:onSizeChanged", describeSize(size));
            }
        };
    }

    private String campaignHashOf(InlineInAppMessageData data) {
        return data != null ? data.getCampaignHash() : "null";
    }

    private String describeSize(InlineInAppSize size) {
        return "px=" + size.getWidthPx() + "x" + size.getHeightPx()
                + " dp=" + size.getWidthDp() + "x" + size.getHeightDp();
    }

    private String elapsedSinceOnCreate() {
        return "+" + (System.currentTimeMillis() - drainStartMillis) + "ms";
    }

    private void appendLog(String tag, String message) {
        String line = timeFormat.format(new Date()) + " [" + tag + "] " + message + "\n";
        runOnUiThread(() -> logText.append(line));
    }
}
