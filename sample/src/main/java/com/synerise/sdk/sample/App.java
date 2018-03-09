package com.synerise.sdk.sample;

import android.support.multidex.MultiDexApplication;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.synalter.Synalter;

import static com.synerise.sdk.event.BaseViewAspect.TrackMode.FINE;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        String syneriseBusinessProfileApiKey = getString(R.string.synerise_business_api_key);
        String syneriseClientApiKey = getString(R.string.synerise_client_api_key);
        String appId = getString(R.string.app_name);

        final boolean DEBUG_MODE = true;

        Tracker.init(this, syneriseBusinessProfileApiKey, appId);
        Tracker.setDebugMode(DEBUG_MODE);
        Tracker.setTrackMode(FINE);

        Profile.init(this, syneriseBusinessProfileApiKey, appId);

        Client.init(this, syneriseClientApiKey, appId);

        Injector.init(this, syneriseBusinessProfileApiKey, appId);
        Injector.setDebugMode(DEBUG_MODE);

        Synalter.init(this, syneriseBusinessProfileApiKey, appId);
    }
}