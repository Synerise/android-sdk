package com.synerise.sdk.sample.anrtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Debug;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.core.listeners.OnRegisterForPushListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.types.enums.PushRegistrationOrigin;
import com.synerise.sdk.error.ApiError;

/** Debug-only switches, written from adb into the "anr_fix_test_switches" prefs file, that reshape App init for the ANR-fix manual scenarios. */
public class AnrTestHarness {

    public static final String LOG_TAG = "AnrFixTest";
    public static final String SWITCHES_PREFS_NAME = "anr_fix_test_switches";
    private static final String SWITCH_WAIT_FOR_DEBUGGER = "wait_for_debugger";
    private static final String SWITCH_METHOD_TRACE_MS = "method_trace_ms";
    private static final String METHOD_TRACE_FILE_NAME = "anrfix.trace";
    private static final int METHOD_TRACE_BUFFER_BYTES = 64 * 1024 * 1024;
    private static final int METHOD_TRACE_SAMPLING_INTERVAL_MICROS = 500;
    private static final String SWITCH_STRICT_MODE = "strict_mode";
    private static final String SWITCH_PUSH_ENCRYPTION = "push_encryption";
    private static final String SWITCH_DO_NOT_TRACK = "do_not_track";
    private static final String SWITCH_API_KEY_OVERRIDE = "api_key";
    private static final String SWITCH_DESTROY_FLAG_BEFORE_BUILD = "destroy_flag_before_build";
    private static final String SWITCH_DESTROY_FLAG_AFTER_BUILD = "destroy_flag_after_build";
    private static final String SWITCH_READ_SIGNED_IN_AFTER_BUILD = "read_signed_in_after_build";
    private static final String SWITCH_DESTROY_SESSION_AFTER_BUILD = "destroy_session_after_build";
    private static final String SWITCH_CHANGE_API_KEY_AFTER_BUILD = "change_api_key_after_build";
    private static final String SWITCH_REGISTER_PUSH_IN_CALLBACK = "register_push_in_callback";
    private static final String SYNTHETIC_PUSH_TOKEN_PREFIX = "anr-fix-test-push-token-";

    private final Context context;
    private final SharedPreferences switches;
    private long buildStartedAtMillis;

    public AnrTestHarness(Context context) {
        this.context = context;
        this.switches = context.getSharedPreferences(SWITCHES_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean hasAnySwitch() {
        return !switches.getAll().isEmpty();
    }

    public void applyBeforeBuild() {
        if (!hasAnySwitch()) return;
        Log.i(LOG_TAG, "switches=" + switches.getAll());
        if (switches.getBoolean(SWITCH_WAIT_FOR_DEBUGGER, false)) {
            Log.i(LOG_TAG, "waiting for a JDWP debugger before build()");
            Debug.waitForDebugger();
        }
        if (switches.getBoolean(SWITCH_STRICT_MODE, false)) {
            enableStrictModeLogging();
        }
        startMethodTracingIfRequested();
        Synerise.settings.notifications.setEncryption(switches.getBoolean(SWITCH_PUSH_ENCRYPTION, true));
        Boolean destroyFlagBeforeBuild = readOptionalBoolean(SWITCH_DESTROY_FLAG_BEFORE_BUILD);
        if (destroyFlagBeforeBuild != null) {
            Synerise.settings.sdk.shouldDestroySessionOnApiKeyChange = destroyFlagBeforeBuild;
        }
    }

    public String resolveApiKey(String sampleApiKey) {
        String override = switches.getString(SWITCH_API_KEY_OVERRIDE, null);
        return TextUtils.isEmpty(override) ? sampleApiKey : override;
    }

    /** Null when the switch is absent so the sample keeps its default of not passing initialDoNotTrack at all. */
    public Boolean doNotTrackOverride() {
        return switches.contains(SWITCH_DO_NOT_TRACK) ? switches.getBoolean(SWITCH_DO_NOT_TRACK, false) : null;
    }

    public void markBuildStarted() {
        buildStartedAtMillis = SystemClock.elapsedRealtime();
    }

    public void runAfterBuild() {
        long buildDurationMillis = SystemClock.elapsedRealtime() - buildStartedAtMillis;
        Log.i(LOG_TAG, "build() returned thread=" + currentThreadName() + " durationMs=" + buildDurationMillis);
        if (!hasAnySwitch()) return;
        applyDestroyFlagAfterBuild();
        if (switches.getBoolean(SWITCH_READ_SIGNED_IN_AFTER_BUILD, false)) {
            readSessionStateTimed("afterBuild");
        }
        if (switches.getBoolean(SWITCH_DESTROY_SESSION_AFTER_BUILD, false)) {
            runTimed("destroySession afterBuild", Client::destroySession);
        }
        String newApiKey = switches.getString(SWITCH_CHANGE_API_KEY_AFTER_BUILD, null);
        if (!TextUtils.isEmpty(newApiKey)) {
            runTimed("changeApiKey afterBuild", () -> Client.changeApiKey(newApiKey));
        }
    }

    public void onRegisterForPushRequired(PushRegistrationOrigin origin) {
        Log.i(LOG_TAG, "onRegisterForPushRequired origin=" + origin + " thread=" + currentThreadName()
                + " sinceBuildStartMs=" + (SystemClock.elapsedRealtime() - buildStartedAtMillis));
        if (!switches.getBoolean(SWITCH_REGISTER_PUSH_IN_CALLBACK, false)) return;
        runTimed("registerForPush inCallback origin=" + origin, () -> {
            IApiCall call = Client.registerForPush(SYNTHETIC_PUSH_TOKEN_PREFIX + origin.name().toLowerCase(), true);
            call.execute(() -> Log.i(LOG_TAG, "registerForPush inCallback onAction thread=" + currentThreadName()),
                    apiError -> Log.i(LOG_TAG, "registerForPush inCallback onFailure httpCode=" + ((ApiError) apiError).getHttpCode()
                            + " thread=" + currentThreadName()));
        });
    }

    public static void readSessionStateTimed(String label) {
        long startedAt = SystemClock.elapsedRealtime();
        boolean isSignedIn = Client.isSignedIn();
        long signedInMillis = SystemClock.elapsedRealtime() - startedAt;
        boolean isSimpleAuth = Client.isSignedInViaSimpleAuthentication();
        String uuid = Client.getUuid();
        Log.i(LOG_TAG, label + " isSignedIn=" + isSignedIn + " isSignedInMs=" + signedInMillis
                + " isSignedInViaSimpleAuthentication=" + isSimpleAuth + " uuid=" + uuid
                + " thread=" + currentThreadName());
    }

    public static void runTimed(String label, Runnable action) {
        long startedAt = SystemClock.elapsedRealtime();
        try {
            action.run();
            Log.i(LOG_TAG, label + " ok durationMs=" + (SystemClock.elapsedRealtime() - startedAt)
                    + " thread=" + currentThreadName());
        } catch (Throwable throwable) {
            Log.e(LOG_TAG, label + " threw after " + (SystemClock.elapsedRealtime() - startedAt) + "ms", throwable);
        }
    }

    public static String currentThreadName() {
        Thread thread = Thread.currentThread();
        return thread.getName() + (Looper.myLooper() == Looper.getMainLooper() ? "(main)" : "");
    }

    private void startMethodTracingIfRequested() {
        String traceDurationMillis = switches.getString(SWITCH_METHOD_TRACE_MS, null);
        if (traceDurationMillis == null) return;
        String tracePath = new java.io.File(context.getFilesDir(), METHOD_TRACE_FILE_NAME).getAbsolutePath();
        Debug.startMethodTracingSampling(tracePath, METHOD_TRACE_BUFFER_BYTES, METHOD_TRACE_SAMPLING_INTERVAL_MICROS);
        Log.i(LOG_TAG, "method tracing (sampling) started -> " + tracePath);
        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
            Debug.stopMethodTracing();
            Log.i(LOG_TAG, "method tracing stopped");
        }, Long.parseLong(traceDurationMillis));
    }

    private void applyDestroyFlagAfterBuild() {
        Boolean destroyFlagAfterBuild = readOptionalBoolean(SWITCH_DESTROY_FLAG_AFTER_BUILD);
        if (destroyFlagAfterBuild == null) return;
        Synerise.settings.sdk.shouldDestroySessionOnApiKeyChange = destroyFlagAfterBuild;
        Log.i(LOG_TAG, "shouldDestroySessionOnApiKeyChange set AFTER build to " + destroyFlagAfterBuild);
    }

    /** Accepts the value whether adb stored it as a boolean or as the text true/false. */
    private Boolean readOptionalBoolean(String key) {
        Object value = switches.getAll().get(key);
        return value == null ? null : Boolean.parseBoolean(String.valueOf(value));
    }

    private void enableStrictModeLogging() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectCustomSlowCalls()
                .penaltyLog()
                .build());
        Log.i(LOG_TAG, "StrictMode thread policy enabled (penaltyLog)");
    }
}
