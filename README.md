# Synerise Android SDK - User documentation

## Documentation
Most up-to-date documentation is available at: https://help.synerise.com/developers/android-sdk/
## Requirements

- Minimum Android SDK version - 19

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
        classpath 'com.android.tools.build:gradle:3.1.3'

        classpath 'com.synerise.sdk:synerise-gradle-plugin:3.0.4'
        classpath 'org.aspectj:aspectjtools:1.9.2'
    }
}
```

Moreover, import dependency in your app/build.gradle file and apply plugin:
```
apply plugin: 'com.android.application'
apply plugin: 'android-aspectjx'
apply plugin: 'synerise-plugin'
```
```
...
dependencies {
  ...
  // Synerise Android SDK
  implementation 'com.synerise.sdk:synerise-mobile-sdk:3.4.1'
}
```
### Optionally
You should use google play services auth version 15.0.0 as newer versions cause problems with tracking features of our SDK (we still work on that)

dependencies {
  ...
  // Google Play Services
  implementation 'com.google.android.gms:play-services-auth:15.0.0'
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

        Synerise.Builder.with(this, syneriseClientApiKey, appId)
                        .notificationIcon(R.drawable.notification_icon)
                        .notificationIconColor(ContextCompat.getColor(this, R.color.amaranth))
                        .syneriseDebugMode(DEBUG_MODE)
                        .trackerTrackMode(FINE)
                        .trackerMinBatchSize(10)
                        .trackerMaxBatchSize(100)
                        .trackerAutoFlushTimeout(5000)
                        .injectorAutomatic(false)
                        .pushRegistrationRequired(this)
                        .locationUpdateRequired(this)
                        .locationAutomatic(true)
                        .notificationDefaultChannelId("your-channel-id")
                        .notificationDefaultChannelName("your-channel-name")
                        .notificationHighPriorityChannelId("your-high-channel-id")
                        .notificationHighPriorityChannelName("your-high-channel-name")
                        .baseUrl("http://your-base-url.com/")
                        .customClientConfig(new CustomClientAuthConfig("http://your-base-url.com/"))
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

For your convenience, `Synerise.Builder` makes it possible to configure SDK the way you want it!<br>
Let's dive into some configurable functionalities:
1. `.notificationIcon(@DrawableRes int)` - if you're using campaign banners or push messages, it is recommended to pass your custom notification icon.
This icon is presented as a small notification icon. Note, that required icon must be a drawable resource (not mipmap) due to Android O adaptive icons restrictions.
Also, a default icon will be used if there is no custom icon provided.
2. `.syneriseDebugMode(boolean)` - simple flag may be provided in order to enable full network traffic logs. It is not recommended to use debug mode in release version of your app.
3. `.trackerDebugMode(boolean)` - you can receive some simple logs about sending events (like success, failure etc.) by enabling debug mode, which is disabled by default.
4. `.injectorDebugMode(boolean)` - you can receive some simple logs about Injector actions (like Walkthrough screen availability) by enabling debug mode, which is disabled by default.
5. `.trackerTrackMode(TrackMode)` - sets proper mode for view tracking. See Tracker section below.
6. `.trackerMinBatchSize(int)` - sets minimum number of events in queue required to send them.
7. `.trackerMaxBatchSize(int)` - sets maximum number of events, which may be sent in a single batch.
8. `.trackerAutoFlushTimeout(int)` - sets time required to elapse before event's queue will attempt to be sent.
9. `.injectorAutomatic(boolean)` - simple flag may be provided to enable automatic mode in injector. See Injector section for more information.
10. `.pushRegistrationRequired(OnRegisterForPushListener)` - Synerise SDK may request you to register client for push notifications. This callback is called at after client signs in, signs up or deletes account.
11. `.locationUpdateRequired(OnLocationUpdateListener)` - this callback is called on demand via push notification, so it may be called at any point of time.
12. `.locationAutomatic(boolean)` - to obtain user location and send location event automatically.
12. `.notificationDefaultChannelId(String)` - sets id of Push Notification Channel. For more info please check Injector section below.
13. `.notificationDefaultChannelName(String)` - sets name of Push Notification Channel. For more info please check Injector section below.
14. `.notificationHighPriorityChannelId(String)` - sets id of Push Notification Channel. For more info please check Injector section below.
15. `.notificationHighPriorityChannelName(String)` - sets name of Push Notification Channel. For more info please check Injector section below.
16. `.baseUrl(String)` - you can provide your custom base URL to use your own API.
17. `.customClientConfig(CustomClientAuthConfig)` - you can also provide your custom Client `Authorization Configuration`. At this moment, configuration handles `Base URL` changes.
18. `.build()` - builds Synerise SDK with provided data. Please note, that `Synerise.Builder.with(..)` method is mandatory and `Synerise.Builder.build()` method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.<br>
