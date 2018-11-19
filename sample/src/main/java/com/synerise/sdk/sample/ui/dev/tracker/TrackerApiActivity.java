package com.synerise.sdk.sample.ui.dev.tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.sample.BuildConfig;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.model.MySerializableObject;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.util.UUID;

public class TrackerApiActivity extends BaseActivity {

    private TextView randomEvent;

    public static Intent createIntent(Context context) {
        return new Intent(context, TrackerApiActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_tracker);

        ToolbarHelper.setUpChildToolbar(this, R.string.tracker_api);

        randomEvent = findViewById(R.id.random_event_text);
        findViewById(R.id.add_event_button).setOnClickListener(v -> addEvent());
        findViewById(R.id.force_send_button).setOnClickListener(v -> flush());
        findViewById(R.id.save_random_event).setOnClickListener(v -> addRandomEvent());
        findViewById(R.id.go_view_tracker).setOnClickListener(v -> goToTrackerViewActivity());
    }

    private void addEvent() {
        TrackerParams params = new TrackerParams.Builder()
                .add("applicationName", getString(R.string.app_name))
                .add("screenName", getClass().getSimpleName())
                .add("appId", BuildConfig.APPLICATION_ID)
                .add("versionCode", BuildConfig.VERSION_CODE)
                .add("versionName", BuildConfig.VERSION_NAME)
                .add("isGreat", true)
                .add("count", 0x7fffffffffffffffL)
                .add("customObject", new MySerializableObject())
                .build();
        Tracker.send(new CustomEvent("sampleAddEvent", "sampleLabel", params));
    }

    private void flush() {
        Tracker.flush();
    }

    private void addRandomEvent() {
        String uuid = UUID.randomUUID().toString();
        randomEvent.setText(uuid);
        Tracker.send(new CustomEvent("sampleAddRandomEvent", uuid));
    }

    private void goToTrackerViewActivity() {
        startActivity(TrackerViewActivity.createIntent(this));
    }
}