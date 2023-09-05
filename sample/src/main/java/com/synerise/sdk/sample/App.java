package com.synerise.sdk.sample;

import android.content.Intent;

import androidx.multidex.MultiDexApplication;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.core.listeners.OnLocationUpdateListener;
import com.synerise.sdk.core.listeners.OnRegisterForPushListener;
import com.synerise.sdk.core.listeners.SyneriseListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.types.enums.HostApplicationType;
import com.synerise.sdk.core.types.enums.MessagingServiceType;
import com.synerise.sdk.core.types.enums.TrackMode;
import com.synerise.sdk.core.utils.SystemUtils;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.callback.InjectorSource;
import com.synerise.sdk.injector.callback.OnInjectorListener;
import com.synerise.sdk.injector.callback.OnNotificationListener;
import com.synerise.sdk.injector.callback.model.NotificationInfo;
import com.synerise.sdk.sample.dagger.AppComponent;
import com.synerise.sdk.sample.dagger.ConfigModule;
import com.synerise.sdk.sample.dagger.DaggerAppComponent;
import com.synerise.sdk.sample.dagger.MainModule;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.service.LocationService;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;

import javax.inject.Inject;

import static com.synerise.sdk.sample.service.MyFirebaseMessagingService.CHANNEL_HIGH_PRIORITY_ID;
import static com.synerise.sdk.sample.service.MyFirebaseMessagingService.CHANNEL_HIGH_PRIORITY_NAME;
import static com.synerise.sdk.sample.service.MyFirebaseMessagingService.CHANNEL_ID;
import static com.synerise.sdk.sample.service.MyFirebaseMessagingService.CHANNEL_NAME;

public class App extends MultiDexApplication
        implements OnInjectorListener, // optional action callback
        OnRegisterForPushListener,
        OnLocationUpdateListener,
        SyneriseListener {

    private static final String TAG = App.class.getSimpleName();

    private AppComponent component;

    @Inject
    AccountManager accountManager;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());

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
        Synerise.settings.sdk.shouldDestroySessionOnApiKeyChange = true;
        Synerise.settings.notifications.setEncryption(true);
        Synerise.settings.inAppMessaging.renderingTimeout = 5000;
        Synerise.Builder.with(this, syneriseClientApiKey, appId)
                .mesaggingServiceType(MessagingServiceType.GMS)
                .syneriseDebugMode(true)
                .crashHandlingEnabled(true)
                .pushRegistrationRequired(this)
                .locationUpdateRequired(this)
                .initializationListener(this)
                .notificationDefaultChannelId(CHANNEL_ID)
                .notificationDefaultChannelName(CHANNEL_NAME)
                .notificationHighPriorityChannelId(CHANNEL_HIGH_PRIORITY_ID)
                .notificationHighPriorityChannelName(CHANNEL_HIGH_PRIORITY_NAME)
                .baseUrl(null)
                .hostApplicationType(HostApplicationType.NATIVE_ANDROID)
                .setRequestValidationSalt("your salt here")
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }

    @Override
    public void onLocationUpdateRequired() {
        // Retrieving location is on application side.
        // AppearedInLocation event should be send.
        LocationService.startLocation(this);
    }

    @Override
    public void onRegisterForPushRequired() {
        // your logic here
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.d(TAG, "Retrieve token Successful : " + token);
                IApiCall call = Client.registerForPush(token, true);
                call.execute(() -> Log.d(TAG, "Register for Push succeed: " + token),
                        apiError -> Log.w(TAG, "Register for push failed: " + token));

                Intent intent = FirebaseIdChangeBroadcastReceiver.createFirebaseIdChangedIntent();
                LocalBroadcastManager.getInstance(App.this).sendBroadcast(intent);
            } else{
                Log.w(TAG, "token should not be null...");
            }
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

    @Override
    public void onInitializationCompleted() {
        // your action here
        Injector.setOnNotificationListener(new OnNotificationListener() {
            @Override
            public void onNotificationReceived(NotificationInfo notificationInfo) {
                Log.i(TAG, "on push received: " + notificationInfo.getCampaignHashId() + " title: " + notificationInfo.getCampaignTitle() + "payload: " + notificationInfo.getPayload());
            }

            @Override
            public void onNotificationClicked(NotificationInfo notificationInfo) {
                Log.i(TAG, "on push clicked: " + notificationInfo.getCampaignHashId() + " title: " + notificationInfo.getCampaignTitle() + "payload: " + notificationInfo.getPayload());

            }

            @Override
            public void onNotificationDismissed(NotificationInfo notificationInfo) {
                Log.i(TAG, "on push dismissed: " + notificationInfo.getCampaignHashId() + " title: " + notificationInfo.getCampaignTitle() + "payload: " + notificationInfo.getPayload());
            }

            @Override
            public void onActionButtonClicked(NotificationInfo notificationInfo, String actionButton) {
                Log.i(TAG, "on action button clicked: " + notificationInfo.getCampaignHashId() + " title: " + notificationInfo.getCampaignTitle() + " action button: " + actionButton + "payload: " + notificationInfo.getPayload());
            }
        });
    }

    @Override
    public void onInitializationFailed() {

    }
}