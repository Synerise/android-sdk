package com.synerise.sdk.sample.ui.events;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.sample.BuildConfig;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.model.MySerializableObject;
import com.synerise.sdk.sample.ui.BaseFragment;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TrackerFragment extends BaseFragment {

    @BindView(R.id.random_event_text) TextView randomEvent;
    @BindView(R.id.add_event_button) Button addEvent;
    @BindView(R.id.force_send_button) Button forceSend;
    @BindView(R.id.save_random_event) Button saveEvent;
    @BindView(R.id.go_view_tracker) Button goTracker;
    private Unbinder unbinder;

    public static TrackerFragment newInstance() { return new TrackerFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        addEvent.setOnClickListener(v -> addEvent());
        forceSend.setOnClickListener(v -> flush());
        saveEvent.setOnClickListener(v -> addRandomEvent());
        goTracker.setOnClickListener(v -> goToTrackerViewActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        startActivity(TrackerViewActivity.createIntent(getActivity()));
    }
}
