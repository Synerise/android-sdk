package com.synerise.sdk.sample.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.callback.OnBannerListener;
import com.synerise.sdk.injector.callback.OnWalkthroughListener;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dashboard.DashboardActivity;

import java.util.Map;

public class SplashActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Injector.getWalkthrough();
        /*
        If app was invisible to user (minimized or destroyed) and campaign banner came in - Synerise SDK makes it neat and simple.
        Simple push message is presented to the user and launcher activity is fired after click on push.
        It is a prefect moment for you to pass this data and SDK will verify whether it is campaign banner
        and if so, banner will be presented within the app.
        If your launcher activity last quite longer, check onNewIntent(Intent) implementation below.
         */
        boolean isSynerisePush = Injector.handlePushPayload(getIntent().getExtras());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Injector.setOnWalkthroughListener(new OnWalkthroughListener() {
            @Override
            public void onLoadingError(ApiError error) {
                error.printStackTrace();
                navigate();
            }

            @Override
            public void onLoaded() {
                if (Injector.isLoadedWalkthroughUnique()) {
                    Injector.showWalkthrough();
                } else {
                    navigate();
                }
            }

            @Override
            public void onClosed() {
                navigate();
            }
        });
        Injector.setOnBannerListener(new OnBannerListener() {
            @Override
            public boolean shouldPresent(Map<String, String> data) {
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Injector.removeWalkthroughListener();
        Injector.removeBannerListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        /*
        So it seems like your launcher activity is not necessarily a short splash or it just simply takes some time before moving on.
        In this case it is preferred to override this method and set `android:launchMode="singleTop"` within your AndroidManifest activity declaration.
        The reason is very simple, when campaign banner came (with Open App action type) while app was minimized,
        simple push is presented to the user in the system tray.
        If your launcher activity was already created and push was clicked - your onCreate(Bundle) method will not be called.
         */
        boolean isSynerisePush = Injector.handlePushPayload(intent.getExtras());
    }

    // ****************************************************************************************************************************************

    private void navigate() {
        startActivity(DashboardActivity.createIntent(this));
        finish();
    }
}
