package com.synerise.sdk.sample.ui.dev;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.client.ClientApiActivity;
import com.synerise.sdk.sample.ui.dev.injector.InjectorApiActivity;
import com.synerise.sdk.sample.ui.dev.profile.ProfileApiActivity;
import com.synerise.sdk.sample.ui.dev.tracker.TrackerApiActivity;

import static android.content.Context.ACTIVITY_SERVICE;

public class DeveloperFragment extends BaseDevFragment {

    public static DeveloperFragment newInstance() { return new DeveloperFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developer_tools, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tracker_api).setOnClickListener(v -> startActivity(TrackerApiActivity.createIntent(getContext())));
        view.findViewById(R.id.client_api).setOnClickListener(v -> startActivity(ClientApiActivity.createIntent(getContext())));
        view.findViewById(R.id.profile_api).setOnClickListener(v -> startActivity(ProfileApiActivity.createIntent(getContext())));
        view.findViewById(R.id.injector_api).setOnClickListener(v -> startActivity(InjectorApiActivity.createIntent(getContext())));
        view.findViewById(R.id.reset).setOnClickListener(v -> {
            ActivityManager activityManager = ((ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE));
            if (activityManager != null)
                activityManager.clearApplicationUserData();
        });
    }
}