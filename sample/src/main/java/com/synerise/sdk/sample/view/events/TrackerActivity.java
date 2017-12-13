package com.synerise.sdk.sample.view.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent;
import com.synerise.sdk.event.model.model.UnitPrice;
import com.synerise.sdk.event.model.transaction.CancelledTransactionEvent;
import com.synerise.sdk.sample.BuildConfig;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.model.MySerializableObject;

import java.util.Currency;
import java.util.UUID;

public class TrackerActivity extends AppCompatActivity {

    private TextView randomEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        Button addEventBt = findViewById(R.id.add_event_button);
        Button forceSendBt = findViewById(R.id.force_send_button);
        Button addRandomEventBt = findViewById(R.id.save_random_event);
        randomEvent = findViewById(R.id.random_event_text);

        addEventBt.setOnClickListener(v -> addEvent());
        forceSendBt.setOnClickListener(v -> flush());
        addRandomEventBt.setOnClickListener(v -> addRandomEvent());
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

        CancelledTransactionEvent transactionEvent = new CancelledTransactionEvent("Transaction1");

        Currency currency = Currency.getInstance("PLN");
        transactionEvent.setDiscountAmount(new UnitPrice(1, currency));
        Tracker.send(transactionEvent);
    }

    private void flush() {
        Tracker.flush();
    }

    private void addRandomEvent() {
        String uuid = UUID.randomUUID().toString();
        randomEvent.setText(uuid);
        Tracker.send(new CustomEvent("addRandomEvent", uuid));
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.send(new VisitedScreenEvent(getClass().getSimpleName()));
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, TrackerActivity.class);
    }
}
