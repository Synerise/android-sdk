package com.synerise.sdk.sample.ui.dev.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.core.settings.TrackerSettings;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class SettingsActivity extends BaseActivity {

    private TextView settingChosen;

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_settings);

        ToolbarHelper.setUpChildToolbar(this, R.string.settings);

        settingChosen = findViewById(R.id.setting_chosen);
        findViewById(R.id.sdk_enable_button).setOnClickListener(v -> sdkEnable(true));
        findViewById(R.id.sdk_disable_button).setOnClickListener(v -> sdkEnable(false));
        findViewById(R.id.autotracking_enable_button).setOnClickListener(v -> autotrackingEnable(true));
        findViewById(R.id.autotracking_disable_button).setOnClickListener(v -> autotrackingEnable(false));
        findViewById(R.id.customtracking_enable_button).setOnClickListener(v -> customtrackingEnable(true));
        findViewById(R.id.customtracking_disable_button).setOnClickListener(v -> customtrackingEnable(false));
        findViewById(R.id.notification_enable_button).setOnClickListener(v -> notificationsEnable(true));
        findViewById(R.id.notification_disable_button).setOnClickListener(v -> notificationsEnable(false));
    }

    private void sdkEnable(boolean enabled) {
        Synerise.settings.sdk.enabled = enabled;
        settingChosen.setText("sdk enabled = " + enabled);
    }

    private void autotrackingEnable(boolean enabled) {
        Synerise.settings.tracker.autoTracking.enabled = enabled;
        settingChosen.setText("autotracking enabled = " + enabled);
    }

    private void customtrackingEnable(boolean enabled) {
        Synerise.settings.tracker.tracking.enabled = enabled;
        settingChosen.setText("customtracking enabled = " + enabled);
    }

    private void notificationsEnable(boolean enabled) {
        Synerise.settings.notifications.enabled = enabled;
        settingChosen.setText("notifications enabled = " + enabled);
    }
}
