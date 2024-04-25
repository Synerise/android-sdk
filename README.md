# Synerise Android SDK (v5.17.0)

[![Platform](https://img.shields.io/badge/platform-Android-orange.svg)](https://github.com/synerise/android-sdk)
[![Languages](https://img.shields.io/badge/language-Java-orange.svg)](https://github.com/synerise/android-sdk)
[![GitHub release](https://img.shields.io/github/release/Synerise/android-sdk.svg)](https://github.com/Synerise/android-sdk/releases) 
[![Synerise](https://img.shields.io/badge/www-synerise-green.svg)](https://synerise.com)
[![Documentation](https://img.shields.io/badge/docs-mobile%20sdk-brightgreen.svg)](https://hub.synerise.com/developers/mobile-sdk/)

---

## About
[Synerise](https://www.synerise.com) SDK for Android.

## Documentation
Most up-to-date documentation is available at [Developer Guide - Mobile SDK](https://hub.synerise.com/developers/mobile-sdk).

## Requirements
* Access to workspace
* A Profile API Key
* Minimum Android SDK version - 21
* Supported targetSdkVersion - 33

## Installation

Set maven path in your root/build.gradle file:
```
...
allprojects {
    repositories {
        google()
        jcenter()
        maven {  url 'https://pkgs.dev.azure.com/Synerise/AndroidSDK/_packaging/prod/maven/v1' }
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
                    classpath 'com.google.gms:google-services:4.3.3'
                    classpath 'io.fabric.tools:gradle:1.30.0'
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
  implementation 'com.synerise.sdk:synerise-mobile-sdk:5.8.0'
}
```

Finally, please make sure your `Instant Run` is disabled.

## Initialization
  
First of all, you need to initialize Synerise Android SDK and provide `Profile API Key`.
  
To get `Profile API Key`, please sign in to your Synerise account and visit [https://app.synerise.com/settings/apikeys](https://app.synerise.com/settings/apikeys).
Then, generate new `API Key` for `Profile` audience.
  
```java
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

## Author
Synerise, developer@synerise.com. If you need support please feel free to contact us.