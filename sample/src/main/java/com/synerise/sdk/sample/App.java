package com.synerise.sdk.sample;

import android.content.Intent;

import androidx.multidex.MultiDexApplication;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.iid.FirebaseInstanceId;
import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.core.listeners.OnLocationUpdateListener;
import com.synerise.sdk.core.listeners.OnRegisterForPushListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.types.enums.TrackMode;
import com.synerise.sdk.core.utils.SystemUtils;
import com.synerise.sdk.injector.callback.InjectorSource;
import com.synerise.sdk.injector.callback.OnInjectorListener;
import com.synerise.sdk.sample.dagger.AppComponent;
import com.synerise.sdk.sample.dagger.ConfigModule;
import com.synerise.sdk.sample.dagger.DaggerAppComponent;
import com.synerise.sdk.sample.dagger.MainModule;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

import static com.synerise.sdk.sample.service.MyFirebaseMessagingService.CHANNEL_HIGH_PRIORITY_ID;
import static com.synerise.sdk.sample.service.MyFirebaseMessagingService.CHANNEL_HIGH_PRIORITY_NAME;
import static com.synerise.sdk.sample.service.MyFirebaseMessagingService.CHANNEL_ID;
import static com.synerise.sdk.sample.service.MyFirebaseMessagingService.CHANNEL_NAME;

public class App extends MultiDexApplication
        implements OnInjectorListener, // optional action callback
                   OnRegisterForPushListener,
                   OnLocationUpdateListener {

    private static final String TAG = App.class.getSimpleName();

    private AppComponent component;

    @Inject AccountManager accountManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this,
                    new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build(),
                    new Crashlytics());

        component = DaggerAppComponent
                .builder()
                .mainModule(new MainModule(this))
                .configModule(new ConfigModule())
                .build();

        initSynerise();

        Fresco.initialize(this);
    }

    private void initSynerise() {

        component.inject(this);

        NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled();
        String syneriseClientApiKey = accountManager.getClientProfileApiKey();
        String appId = getString(R.string.app_name);


        Synerise.settings.tracker.autoTracking.trackMode = TrackMode.FINE;
        Synerise.settings.tracker.setMinimumBatchSize(11);
        Synerise.settings.tracker.setMaximumBatchSize(99);
        Synerise.settings.tracker.setAutoFlushTimeout(4999);
        Synerise.settings.injector.automatic = true;
        Synerise.settings.tracker.locationAutomatic = true;
        Synerise.settings.sdk.shouldDestroySessionOnApiKeyChange = true;

        Synerise.Builder.with(this, syneriseClientApiKey, appId)
                        .notificationIcon(R.drawable.ic_cart)
                        .notificationIconColor(ContextCompat.getColor(this, R.color.amaranth))
                        .syneriseDebugMode(true)
                        .crashHandlingEnabled(true)
                        .pushRegistrationRequired(this)
                        .locationUpdateRequired(this)
                        .notificationDefaultChannelId(CHANNEL_ID)
                        .notificationDefaultChannelName(CHANNEL_NAME)
                        .notificationHighPriorityChannelId(CHANNEL_HIGH_PRIORITY_ID)
                        .notificationHighPriorityChannelName(CHANNEL_HIGH_PRIORITY_NAME)
                        .baseUrl(null)
                        .build();
    }

    public AppComponent getComponent() {
        return component;
    }

    @Override
    public void onLocationUpdateRequired() {
        // allow SDK to send location event automatically
        // LocationService.startLocation(this);
    }

    @Override
    public void onRegisterForPushRequired() {
        // your logic here
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String refreshedToken = instanceIdResult.getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            IApiCall call = Client.registerForPush(refreshedToken, true);
            call.execute(() -> Log.d(TAG, "Register for Push succeed: " + refreshedToken),
                         apiError -> Log.w(TAG, "Register for push failed: " + refreshedToken));

            Intent intent = FirebaseIdChangeBroadcastReceiver.createFirebaseIdChangedIntent();
            LocalBroadcastManager.getInstance(App.this).sendBroadcast(intent);
        });
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