package com.synerise.sdk.sample;

import android.support.multidex.MultiDexApplication;

import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.sample.dagger.AppComponent;
import com.synerise.sdk.sample.dagger.ConfigModule;
import com.synerise.sdk.sample.dagger.DaggerAppComponent;
import com.synerise.sdk.sample.dagger.MainModule;

import static com.synerise.sdk.event.BaseViewAspect.TrackMode.FINE;

public class App extends MultiDexApplication
        // implements OnInjectorListener // optional action callback
{

    private AppComponent component;

    // ****************************************************************************************************************************************

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent
                .builder()
                .mainModule(new MainModule(this))
                .configModule(new ConfigModule())
                .build();

        initSynerise();
    }

    // ****************************************************************************************************************************************

    private void initSynerise() {

        String syneriseBusinessProfileApiKey = getString(R.string.synerise_business_api_key);
        String syneriseClientApiKey = getString(R.string.synerise_client_api_key);
        String appId = getString(R.string.app_name);

        final boolean DEBUG_MODE = true;
        Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
                        .notificationIcon(R.drawable.ic_cart)
                        .syneriseDebugMode(DEBUG_MODE)
                        .trackerDebugMode(DEBUG_MODE)
                        .injectorDebugMode(DEBUG_MODE)
                        .trackerTrackMode(FINE)
                        //.customClientConfig(new CustomClientAuthConfig("http://testurl.com"))
                        .build();
    }

    // ****************************************************************************************************************************************

    public AppComponent getComponent() {
        return component;
    }

/*    @Override
    public void onOpenUrl(String url) {
        // your action here
    }

    @Override
    public void onDeepLink(String deepLink) {
        // your action here
    }*/
}