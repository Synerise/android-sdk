package com.synerise.sdk.sample.anrtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.client.RegisterClient;
import com.synerise.sdk.client.model.simpleAuth.ClientData;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.core.types.enums.ReservedSecurityKeys;
import com.synerise.sdk.core.types.model.Token;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Runs one Client action per intent (extra "action") so the ANR-fix scenarios can be driven from adb; every result is logged under AnrFixTest. */
public class AnrVerificationActivity extends BaseActivity {

    private static final String EXTRA_ACTION = "action";
    private static final String EXTRA_LOGIN = "login";
    private static final String EXTRA_PASSWORD = "password";
    private static final String EXTRA_EMAIL = "email";
    private static final String EXTRA_CUSTOM_ID = "custom_id";
    private static final String EXTRA_API_KEY = "api_key";
    private static final String EXTRA_PUSH_TOKEN = "push_token";
    private static final String EXTRA_FLAG = "flag";
    private static final String EXTRA_SWITCHES = "switches";

    private TextView logText;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr_fix_verification);
        ToolbarHelper.setUpChildToolbar(this, R.string.anr_fix_verification);
        logText = findViewById(R.id.anr_fix_log);
        runActionFrom(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        runActionFrom(intent);
    }

    private void runActionFrom(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);
        if (action == null) {
            appendLog("no action extra; use adb: am start -n <pkg>/.anrtest.AnrVerificationActivity --es action status");
            return;
        }
        appendLog("action=" + action + " on " + AnrTestHarness.currentThreadName());
        switch (action) {
            case "status": logSessionStatus(); break;
            case "signIn": signIn(intent.getStringExtra(EXTRA_LOGIN), intent.getStringExtra(EXTRA_PASSWORD)); break;
            case "register": register(intent.getStringExtra(EXTRA_EMAIL), intent.getStringExtra(EXTRA_PASSWORD)); break;
            case "simpleAuth": simpleAuthenticate(intent.getStringExtra(EXTRA_EMAIL), intent.getStringExtra(EXTRA_CUSTOM_ID)); break;
            case "signOut": timed("signOut", Client::signOut); logSessionStatus(); break;
            case "destroySession": timed("destroySession", Client::destroySession); logSessionStatus(); break;
            case "regenerateUuid": timed("regenerateUuid", () -> appendLog("regenerateUuid=" + Client.regenerateUuid())); break;
            case "changeApiKey": timed("changeApiKey", () -> Client.changeApiKey(intent.getStringExtra(EXTRA_API_KEY))); logSessionStatus(); break;
            case "registerForPush": registerForPush(intent.getStringExtra(EXTRA_PUSH_TOKEN)); break;
            case "setDestroyFlag": setDestroyFlag(intent.getBooleanExtra(EXTRA_FLAG, true)); break;
            case "deleteKeystoreAlias": deleteKeystoreAlias(); break;
            case "setSwitches": setSwitches(intent.getStringExtra(EXTRA_SWITCHES)); break;
            case "clearSwitches": clearSwitches(); break;
            default: appendLog("unknown action " + action);
        }
    }

    private void logSessionStatus() {
        timed("status", () -> AnrTestHarness.readSessionStateTimed("status"));
        appendLog("isSignedIn=" + Client.isSignedIn() + " viaSimpleAuth=" + Client.isSignedInViaSimpleAuthentication()
                + " uuid=" + Client.getUuid() + " apiKey=" + Synerise.getClientApiKey()
                + " destroyFlag=" + Synerise.settings.sdk.shouldDestroySessionOnApiKeyChange);
        IDataApiCall<Token> tokenCall = Client.retrieveToken();
        tokenCall.execute(token -> appendLog("token realm=" + token.getTokenRLM() + " origin=" + token.getOrigin()
                        + " clientId=" + token.getClientId() + " customId=" + token.getCustomId()
                        + " expiresInS=" + (token.getExpirationUnixTime() - System.currentTimeMillis() / 1000)),
                apiError -> appendLog("retrieveToken failed " + describe((ApiError) apiError)));
    }

    private void signIn(String login, String password) {
        IApiCall call = Client.signIn(login, password);
        call.execute(() -> { appendLog("signIn ok"); logSessionStatus(); },
                apiError -> appendLog("signIn failed " + describe((ApiError) apiError)));
    }

    private void register(String email, String password) {
        RegisterClient registerClient = new RegisterClient().setEmail(email).setPassword(password);
        IApiCall call = Client.registerAccount(registerClient);
        call.execute(() -> appendLog("register ok for " + email),
                apiError -> appendLog("register failed " + describe((ApiError) apiError)));
    }

    private void simpleAuthenticate(String email, String customId) {
        ClientData clientData = new ClientData().setEmail(email).setCustomId(customId);
        IApiCall call = Client.simpleAuthentication(clientData, customId);
        call.execute(() -> { appendLog("simpleAuth ok"); logSessionStatus(); },
                apiError -> appendLog("simpleAuth failed " + describe((ApiError) apiError)));
    }

    private void registerForPush(String pushToken) {
        timed("registerForPush", () -> {
            IApiCall call = Client.registerForPush(pushToken, true);
            call.execute(() -> appendLog("registerForPush onAction on " + AnrTestHarness.currentThreadName()),
                    apiError -> appendLog("registerForPush failed " + describe((ApiError) apiError)));
        });
    }

    private void setDestroyFlag(boolean flag) {
        Synerise.settings.sdk.shouldDestroySessionOnApiKeyChange = flag;
        appendLog("shouldDestroySessionOnApiKeyChange=" + flag);
    }

    /** Writes "key=value,key=value" into the harness switch prefs (true/false become booleans) for the next cold start. */
    private void setSwitches(String commaSeparatedSwitches) {
        SharedPreferences.Editor editor = getSharedPreferences(AnrTestHarness.SWITCHES_PREFS_NAME, MODE_PRIVATE).edit().clear();
        for (String pair : commaSeparatedSwitches.split(",")) {
            int separatorIndex = pair.indexOf('=');
            String key = pair.substring(0, separatorIndex).trim();
            String value = pair.substring(separatorIndex + 1).trim();
            if (value.equals("true") || value.equals("false")) editor.putBoolean(key, Boolean.parseBoolean(value));
            else editor.putString(key, value);
        }
        editor.commit();
        appendLog("switches set: " + getSharedPreferences(AnrTestHarness.SWITCHES_PREFS_NAME, MODE_PRIVATE).getAll());
    }

    private void clearSwitches() {
        getSharedPreferences(AnrTestHarness.SWITCHES_PREFS_NAME, MODE_PRIVATE).edit().clear().commit();
        appendLog("switches cleared");
    }

    private void deleteKeystoreAlias() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ReservedSecurityKeys.getKeyStore());
            keyStore.load(null);
            boolean existed = keyStore.containsAlias(ReservedSecurityKeys.getAlias());
            keyStore.deleteEntry(ReservedSecurityKeys.getAlias());
            appendLog("keystore alias deleted (existed=" + existed + ")");
        } catch (Exception exception) {
            appendLog("deleteKeystoreAlias failed: " + exception);
        }
    }

    private void timed(String label, Runnable action) {
        AnrTestHarness.runTimed(label, action);
    }

    private String describe(ApiError apiError) {
        return "httpCode=" + apiError.getHttpCode() + " type=" + apiError.getErrorType()
                + " body=" + (apiError.getErrorBody() == null ? null : apiError.getErrorBody().getMessage());
    }

    private void appendLog(@NonNull String line) {
        Log.i(AnrTestHarness.LOG_TAG, line);
        runOnUiThread(() -> logText.append(timeFormat.format(new Date()) + "  " + line + "\n"));
    }
}
