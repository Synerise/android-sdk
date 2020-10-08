package com.synerise.sdk.sample.ui.dev;

import android.app.ActivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.sample.BuildConfig;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.client.ClientApiActivity;
import com.synerise.sdk.sample.ui.dev.content.ContentApiActivity;
import com.synerise.sdk.sample.ui.dev.injector.InjectorApiActivity;
import com.synerise.sdk.sample.ui.dev.promotions.PromotionsApiActivity;
import com.synerise.sdk.sample.ui.dev.settings.SettingsActivity;
import com.synerise.sdk.sample.ui.dev.tracker.TrackerApiActivity;

import static android.content.Context.ACTIVITY_SERVICE;

public class DeveloperFragment extends BaseDevFragment {

    public static DeveloperFragment newInstance() { return new DeveloperFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developer_tools, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            view.findViewById(R.id.tracker_api).setOnClickListener(v -> startActivity(TrackerApiActivity.createIntent(activity)));
            view.findViewById(R.id.client_api).setOnClickListener(v -> startActivity(ClientApiActivity.createIntent(activity)));
            view.findViewById(R.id.promotions_api)
                .setOnClickListener(v -> startActivity(PromotionsApiActivity.createIntent(activity)));
            view.findViewById(R.id.injector_api).setOnClickListener(v -> startActivity(InjectorApiActivity.createIntent(activity)));
            view.findViewById(R.id.reset).setOnClickListener(v -> {
                ActivityManager activityManager = ((ActivityManager) activity.getSystemService(ACTIVITY_SERVICE));
                if (activityManager != null) activityManager.clearApplicationUserData();
            });
            view.findViewById(R.id.settings).setOnClickListener(v -> startActivity(SettingsActivity.createIntent(activity)));
            view.findViewById(R.id.content_api).setOnClickListener(v -> startActivity(ContentApiActivity.createIntent(activity)));
            ((TextView) view.findViewById(R.id.text_build)).setText(getString(R.string.developer_info,
                                                                              BuildConfig.APPLICATION_ID,
                                                                              BuildConfig.BUILD_TYPE,
                                                                              BuildConfig.FLAVOR,
                                                                              BuildConfig.VERSION_CODE,
                                                                              BuildConfig.VERSION_NAME));
        }
    }
}