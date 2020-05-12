# Synerise Android SDK - User documentation

## Documentation
Most up-to-date documentation is available at: https://help.synerise.com/developers/android-sdk/
## Requirements

- Minimum Android SDK version - 20

## Installation

Set maven path in your root/build.gradle file:
```
...
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://synerise.bintray.com/Android" }
    }
}
```

and import dependency:
```
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
            classpath 'com.android.tools.build:gradle:3.4.2'
            classpath 'org.aspectj:aspectjtools:1.9.4'
            classpath 'com.synerise.sdk:synerise-gradle-plugin:3.0.4'
    }
}
```

Moreover, import dependency in your app/build.gradle file and apply plugin:
```
apply plugin: 'com.android.application'
```
```
...
dependencies {
  ...
  // Synerise Android SDK
  implementation 'com.synerise.sdk:synerise-mobile-sdk:3.6.9'
}
```

Finally, please make sure your `Instant Run` is disabled.

### Initialize

First of all, you need to initialize Synerise Android SDK via `with` method and provide `Client Api Key`, `Application name` and `Application instance`.<br>
To get `Client Api Key`, please sign in to your Synerise account and visit https://app.synerise.com/api/.<br>
Then, generate new `Api Key` for `Client` audience.<br>

In your `Application` sub-class:

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initSynerise();
    }

    private void initSynerise() {

        String syneriseClientApiKey = getString(R.string.synerise_client_api_key);
        String appId = getString(R.string.app_name);

        final boolean DEBUG_MODE = BuildConfig.DEBUG;

        Synerise.settings.sdk.enabled = true;
        Synerise.settings.tracker.autoTracking.trackMode = FINE;
        Synerise.settings.tracker.setMinimumBatchSize(11);
        Synerise.settings.tracker.setMaximumBatchSize(99);
        Synerise.settings.tracker.setAutoFlushTimeout(4999);
        Synerise.settings.injector.automatic = true;
        Synerise.settings.tracker.locationAutomatic = true;

        Synerise.Builder.with(this, syneriseClientApiKey, appId)
                        .notificationIcon(R.drawable.notification_icon)
                        .notificationIconColor(ContextCompat.getColor(this, R.color.amaranth))
                        .syneriseDebugMode(true)
                        .crashHandlingEnabled(true)
                        .pushRegistrationRequired(this)
                        .locationUpdateRequired(this)
                        .notificationDefaultChannelId("your-channel-id")
                        .notificationDefaultChannelName("your-channel-name")
                        .notificationHighPriorityChannelId("your-high-channel-id")
                        .notificationHighPriorityChannelName("your-high-channel-name")
                        .baseUrl("http://your-base-url.com/")
                        .build();

    }
}
```

and in your /values strings file (e.g. `strings.xml`):

```
<resources>

    <string name="app_name" translatable="false">Your GREAT application name</string>
    <string name="synerise_client_api_key" translatable="false">EF1AD0E0-532B-6AEE-6010-DEDC78F6E155</string> <!-- replace with valid client api key -->

    ...

</resources>
```
