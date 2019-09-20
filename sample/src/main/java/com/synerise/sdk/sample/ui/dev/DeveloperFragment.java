package com.synerise.sdk.sample.ui.dev;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synerise.sdk.chat.Chat;
import com.synerise.sdk.chat.listener.OnChatListener;
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
            view.findViewById(R.id.chat).setOnClickListener(v -> initializeChat());
            ((TextView) view.findViewById(R.id.text_build)).setText(getString(R.string.developer_info,
                                                                              BuildConfig.APPLICATION_ID,
                                                                              BuildConfig.BUILD_TYPE,
                                                                              BuildConfig.FLAVOR,
                                                                              BuildConfig.VERSION_CODE,
                                                                              BuildConfig.VERSION_NAME));
        }
    }

    private void initializeChat() {
        Chat chat = new Chat("5C89955F-9C9F-86A6-607D-211DED21F7BF");
        chat.toolbarBackgroundColor = R.color.charcoal;
        chat.toolbarTitleColor = R.color.white;
        chat.toolbarTitle = "Chat Communicator";
        chat.closeButtonAlignment = RelativeLayout.ALIGN_PARENT_LEFT;
        chat.setCloseButtonImage(ContextCompat.getDrawable(Synerise.getApplicationContext(), R.drawable.ic_arrow_back));
        chat.closeButtonText = "CloseButton";
        chat.errorText = "Sorry for problems!";
        chat.errorTextColor = R.color.amaranth;
        chat.setChatListener(new OnChatListener() {
            @Override
            public void onLoad() {
                chat.show();
            }

            @Override
            public void onError(Error error) {
            }

            @Override
            public void onLoading() {
            }
        });
        chat.load();
    }
}