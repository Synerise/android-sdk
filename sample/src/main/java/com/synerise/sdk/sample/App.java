package com.synerise.sdk.sample;

import android.app.Application;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.profile.Profile;

/**
 * Created by Jerzy Wierzchowski on 11/6/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String syneriseBusinessProfileApiKey = getString(R.string.synerise_business_api_key);
        String syneriseClientApiKey = getString(R.string.synerise_client_api_key);
        String appId = getString(R.string.app_name);

        final boolean DEBUG_MODE = true;

        Tracker.init(this, syneriseBusinessProfileApiKey, appId);
        Tracker.setDebugMode(DEBUG_MODE);

        Client.init(this, syneriseClientApiKey, appId);
        Client.setDebugMode(DEBUG_MODE);

        // Custom Configuration
        // CustomClientAuthConfig customClientAuthConfig = new CustomClientAuthConfig("http://127.0.0.1/");
        // Client.init(this, syneriseClientApiKey, appId, customClientAuthConfig);

        Profile.init(this, syneriseBusinessProfileApiKey, appId);
        Profile.setDebugMode(DEBUG_MODE);

        Injector.init(this, syneriseBusinessProfileApiKey, appId);
        Injector.setDebugMode(DEBUG_MODE);
    }
}