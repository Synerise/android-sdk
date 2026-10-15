package com.synerise.sdk.sample.ui.dev.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.messaging.FirebaseMessaging;
import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PushRegistrationBackgroundRetryActivity extends BaseActivity {

    private static final int DEDUP_BYPASS_SUFFIX_MIN = 100000;
    private static final int DEDUP_BYPASS_SUFFIX_MAX_EXCLUSIVE = 1000000;
    private static final boolean MOBILE_PUSH_AGREEMENT = true;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
    private final Random dedupBypassRandom = new Random();
    private TextView logText;

    public static Intent createIntent(Context context) {
        return new Intent(context, PushRegistrationBackgroundRetryActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_registration_background_retry);
        ToolbarHelper.setUpChildToolbar(this, R.string.push_registration_background_retry);

        logText = findViewById(R.id.log_text);
        findViewById(R.id.btn_register_for_push).setOnClickListener(view -> registerForPushWithFreshToken());
    }

    private void registerForPushWithFreshToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(this::registerForPushWithDedupBypassSuffix)
                .addOnFailureListener(exception -> appendLog("FCM token unavailable: " + exception.getClass().getSimpleName()));
    }

    /** A fresh per-instance Random: ThreadLocalRandom is seeded in the zygote, so its first value repeats on every cold start. */
    private void registerForPushWithDedupBypassSuffix(String firebaseToken) {
        String dedupBypassSuffix = String.valueOf(
                DEDUP_BYPASS_SUFFIX_MIN + dedupBypassRandom.nextInt(DEDUP_BYPASS_SUFFIX_MAX_EXCLUSIVE - DEDUP_BYPASS_SUFFIX_MIN));
        appendLog("sending " + dedupBypassSuffix);
        IApiCall<?> call = Client.registerForPush(firebaseToken + "-" + dedupBypassSuffix, MOBILE_PUSH_AGREEMENT);
        call.execute(() -> appendLog("succeeded"), apiError -> appendLog(describeFailure(apiError)));
    }

    private static String describeFailure(ApiError apiError) {
        return "failed: " + apiError.getMessage()
                + ", http=" + apiError.getHttpCode()
                + ", backgroundRetryScheduled=" + apiError.isBackgroundRetryScheduled();
    }

    private void appendLog(String message) {
        String line = timeFormat.format(new Date()) + " " + message + "\n";
        runOnUiThread(() -> logText.append(line));
    }
}
