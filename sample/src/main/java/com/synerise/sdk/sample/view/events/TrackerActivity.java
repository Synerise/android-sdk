package com.synerise.sdk.sample.view.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent;
import com.synerise.sdk.sample.BuildConfig;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.model.MySerializableObject;
import com.synerise.sdk.sample.view.events.widgets.TrackerViewActivity;

import java.util.UUID;

public class TrackerActivity extends AppCompatActivity {

    private TextView randomEvent;

    public static Intent createIntent(Context context) {
        return new Intent(context, TrackerActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        randomEvent = findViewById(R.id.random_event_text);

        findViewById(R.id.add_event_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {TrackerActivity.this.addEvent();}
        });
        findViewById(R.id.force_send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {TrackerActivity.this.flush();}
        });
        findViewById(R.id.save_random_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {TrackerActivity.this.addRandomEvent();}
        });
        findViewById(R.id.go_view_tracker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {TrackerActivity.this.goToTrackerViewActivity();}
        });
    }

    // ****************************************************************************************************************************************

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

        Tracker.send(new CustomEvent("ButtonClick", "addEventButton", params));
    }

    private void flush() {
        Tracker.flush();
    }

    private void addRandomEvent() {
        String uuid = UUID.randomUUID().toString();
        randomEvent.setText(uuid);
        Tracker.send(new CustomEvent("addRandomEvent", uuid));
    }

    private void goToTrackerViewActivity() {
        startActivity(TrackerViewActivity.createIntent(this));
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.send(new VisitedScreenEvent(getClass().getSimpleName()));
    }
}
