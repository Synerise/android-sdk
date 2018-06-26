package com.synerise.sdk.sample;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.iid.FirebaseInstanceId;
import com.synerise.sdk.core.OnRegisterForPushListener;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.utils.SystemUtils;
import com.synerise.sdk.injector.callback.InjectorSource;
import com.synerise.sdk.injector.callback.OnInjectorListener;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.dagger.AppComponent;
import com.synerise.sdk.sample.dagger.ConfigModule;
import com.synerise.sdk.sample.dagger.DaggerAppComponent;
import com.synerise.sdk.sample.dagger.MainModule;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

import static com.synerise.sdk.event.BaseViewAspect.TrackMode.FINE;

public class App extends MultiDexApplication
        implements OnInjectorListener, // optional action callback
                   OnRegisterForPushListener {

    private static final String TAG = App.class.getSimpleName();

    private AppComponent component;

    @Inject AccountManager accountManager;

    // ****************************************************************************************************************************************

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        component = DaggerAppComponent
                .builder()
                .mainModule(new MainModule(this))
                .configModule(new ConfigModule())
                .build();

        initSynerise();

        Fresco.initialize(this);
    }

    // ****************************************************************************************************************************************

    private void initSynerise() {

        component.inject(this);

        String syneriseBusinessProfileApiKey = accountManager.getBusinessProfileApiKey();
        String syneriseClientApiKey = accountManager.getClientProfileApiKey();
        String appId = getString(R.string.app_name);

        final boolean DEBUG_MODE = BuildConfig.DEBUG;

        Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
                        .notificationIcon(R.drawable.ic_cart)
                        .syneriseDebugMode(DEBUG_MODE)
                        .trackerDebugMode(DEBUG_MODE)
                        .injectorDebugMode(DEBUG_MODE)
                        .clientRefresh(true)
                        .poolUuid(null)
                        .trackerTrackMode(FINE)
                        .trackerMinBatchSize(10)
                        .trackerMaxBatchSize(100)
                        .trackerAutoFlushTimeout(5000)
                        .injectorAutomatic(true)
                        .pushRegistrationRequired(this)
                        .baseUrl(null)
                        //.customClientConfig(new CustomClientAuthConfig("http://your-base-url.com"))
                        .build();
    }

    // ****************************************************************************************************************************************

    public AppComponent getComponent() {
        return component;
    }

    // ****************************************************************************************************************************************

    @Override
    public void onRegisterForPushRequired() {
        // your logic here
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken != null) {
            IApiCall call = Profile.registerForPush(refreshedToken);
            call.execute(() -> Log.d(TAG, "Register for Push succeed: " + refreshedToken),
                         apiError -> Log.w(TAG, "Register for push failed: " + refreshedToken));

            Intent intent = FirebaseIdChangeBroadcastReceiver.createFirebaseIdChangedIntent();
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    @Override
    public boolean onOpenUrl(InjectorSource source, String url) {
        // your action here
        SystemUtils.openURL(this, url); // default behavior
        return source != InjectorSource.WALKTHROUGH; // default behavior
    }

    @Override
    public boolean onDeepLink(InjectorSource source, String deepLink) {
        // your action here
        SystemUtils.openDeepLink(this, deepLink); // default behavior
        return source != InjectorSource.WALKTHROUGH; // default behavior
    }
}