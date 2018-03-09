# Synerise Android SDK - User documentation

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
        classpath 'com.android.tools.build:gradle:3.0.1'

        classpath 'com.synerise.sdk:synerise-gradle-plugin:3.0.2'
        classpath 'org.aspectj:aspectjtools:1.8.13'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
```

Import dependency in your app/build.gradle file and apply plugin:
```
apply plugin: 'com.android.application'
apply plugin: 'synerise-plugin'
```
```
...
dependencies {
  ...
  // Synerise Mobile SDK
  implementation 'com.synerise.sdk:synerise-mobile-sdk:3.1.6-RC1'
}
```

### Errors

#### MultiDex

Sometimes MultiDex errors may occur. In that case please enable MultiDex as follows (API >= 21):
```
defaultConfig {
    applicationId "com.your.app"
    minSdkVersion 21
    ...
    multiDexEnabled true
}
```
or for API < 21:
```
defaultConfig {
    applicationId "com.your.app"
    minSdkVersion 19
    ...
    multiDexEnabled true
}
```
```
dependencies {
    ...
    // MultiDex
    implementation 'com.android.support:multidex:1.0.2'
    ...
}
```
```
public class YourApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ...
    }
```
You can find more information about MultiDex under this link: https://developer.android.com/studio/build/multidex.html

#### AndroidManifest Merger

Sometimes AndroidManifest Merger errors may occur. In that case please paste following code
```
<application
    ...
    tools:replace="android:theme">
```
in your AndroidManifest application tag.

#### Dagger

Sometimes Dagger errors may occur. In case of following error:
```
Error:Execution failed for task ':app:compileDevDebugJavaWithJavac'.
com.google.common.util.concurrent.UncheckedExecutionException: java.lang.annotation.IncompleteAnnotationException: dagger.Provides missing element type
```
please make sure you are running the latest version of Dagger (SDK runs 2.14.1).

## Event Tracker

You can log events from your mobile app to Synerise platform with Tracker class.
First of all, you need to initialize Tracker with `init` method and provide `Api Key`, `Application name` and `Application instance`.<br>
To get `Api Key` sign in to your Synerise account and go to https://app.synerise.com/api/. Please generate new `Api Key` for `Business Profile` Audience.<br>
Init method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.

### Initialize

In your `Application` class:

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String syneriseApiKey = getString(R.string.synerise_api_key);
        String appName = getString(R.string.app_name);

        Tracker.init(this, syneriseApiKey, appName);
    }
```

and in your /values strings file (e.g. `string.xml`):

```
<resources>
    <string name="app_name" translatable="false">Your GREAT application name</string>
    <string name="synerise_api_key" translatable="false">A75DA38F-A2E9-5A25-2884-38AD12B98FAA</string> <!-- replace with valid api key -->
</resources>
```

### Debug

You can receive some simple logs about events by enabling debug mode, which is disabled by default.
```
Tracker.setDebugMode(true);
```
Note: It is not recommended to use debug mode in release version of your application.

### View tracking

As of now, Tracker also supports auto-tracking mode which can be enabled with
```
Tracker.setTrackMode(FINE);
```

Auto-tracking is *disabled* by default. Accepted values for setTrackMode()
```
EAGER - listeners set to onTouch() only
PLAIN - listeners set to onClick() only
FINE  - listeners are attached to nearly everything in your app
DISABLED - listeners are disabled
```

### Send event

To send Event simply use `Tracker`:
```
Tracker.send(new CustomEvent("ButtonClick", "addEventButton"));
```

You can also pass your custom parameters:
```
TrackerParams params = new TrackerParams.Builder()
                .add("name", "John")
                .add("age", 27)
                .add("isGreat", true)
                .add("lastOrder", 384.28)
                .add("count", 0x7fffffffffffffffL)
                .add("someObject", new MySerializableObject(0))
                .build();

Tracker.send(new CustomEvent("ButtonClick", "addEventButton", params));
```

### Events

#### - Session Events
Group of events related to user's session.

##### LoggedInEvent
Record a 'client logged in' event.

##### LoggedOutEvent
Record a 'client logged out' event.

#### - Products Events
Group of events related to products and cart.

##### AddedToFavoritesEvent
Record a 'client added product to favorites' event.

##### AddedToCartEvent
Record a 'client added product to cart' event.

##### RemovedFromCartEvent
Record a 'client removed product from cart' event.

#### - Transaction Events
Group of events related to user's transactions.

##### CancelledTransactionEvent
Record a 'client cancelled transaction' event.

##### CompletedTransactionEvent
Record a 'client completed transaction' event.

#### - Other Events
Group of uncategorized events related to user's location and actions.

##### AppearedInLocationEvent
Record a 'client appeared in location' event.

##### HitTimerEvent
Record a 'client hit timer' event. This could be used for profiling or activity time monitoring - you can send "hit timer" when your client starts doing something and send it once again when finishes, but this time with different time signature. Then you can use our analytics engine to measure e.g. average activity time.

##### SearchedEvent
Record a 'client searched' event.

##### SharedEvent
Record a 'client shared' event.

##### VisitedScreenEvent
Record a 'client visited screen' event.

##### Custom Event
This is the only event which requires `action` field. Log your custom data with TrackerParams class.

#### - Other features

##### Tracker.flush()
Flush method forces sending events from queue to server.


## Client

First of all, you need to initialize Client with `init` method and provide `Api Key`, `Application name` and `Application instance`.
Init method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.
In Client SDK you can also provide you custom `Authorization Configuration`. At this moment, configuration handles `Base URL` changes.

### Initialize

In your `Application` class:

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String syneriseClientApiKey = getString(R.string.synerise_client_api_key);
        String appName = getString(R.string.app_name);

        Client.init(this, syneriseClientApiKey, appName);
    }
```

and in your /values strings file (e.g. `string.xml`):

```
<resources>
    <string name="app_name" translatable="false">Your GREAT application name</string>
    <string name="synerise_client_api_key" translatable="false">A75DA38F-A2E9-5A25-2884-38AD12B98FAA</string> <!-- replace with valid api key -->
</resources>
```

### Features

#### Client.logIn(email, password, deviceId)
Log in a client in order to obtain the JWT token, which could be used in subsequent requests. The token is valid for 1 hour.
This SDK will refresh token before each call if it is expiring (but not expired).
Method requires valid and non-null email and password. Device ID is optional.
Method returns `IApiCall` to execute request.

#### Client.getAccount()
Use this method to get client's account information.
This method returns `IDataApiCall` with parametrized `AccountInformation` object to execute request.

#### Client.updateAccount(accountInformation)
Use this method to update client's account information.
This method requires `AccountInformation` Builder Pattern object with client's account information. Not provided fields are not modified.
Method returns `IApiCall` to execute request.

## Profile

First of all, you need to initialize Profile with `init` method and provide `Api Key`, `Application name` and `Application instance`.
Init method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.

### Initialize

In your `Application` class:

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String syneriseApiKey = getString(R.string.synerise_api_key);
        String appName = getString(R.string.app_name);

        Profile.init(this, syneriseApiKey, appName);
    }
```

and in your /values strings file (e.g. `string.xml`):

```
<resources>
    <string name="app_name" translatable="false">Your GREAT application name</string>
    <string name="synerise_api_key" translatable="false">A75DA38F-A2E9-5A25-2884-38AD12B98FAA</string> <!-- replace with valid api key -->
</resources>
```

### Features

#### Profile.createClient(createClient)
Create a new client record if no identifier has been assigned for him before in Synerise.
This method requires `CreateClient` Builder Pattern object with client's optional data. Not provided fields are not modified.
Method returns IApiCall object to execute request.

#### Profile.registerClient(registerClient)
Register new Client with email, password and optional data.
This method requires `RegisterClient` Builder Pattern object with client's email, password and optional data. Not provided fields are not modified.
Method returns IApiCall object to execute request.

#### Profile.updateClient(updateClient)
Update client with ID and optional data.
This method requires `UpdateClient` Builder Pattern object with client's optional data. Not provided fields are not modified.
Method returns IApiCall object to execute request.

#### Profile.deleteClient(id)
Delete client with ID.
This method requires client's id.
Method returns IApiCall object to execute request.

#### Profile.requestPasswordReset(email)
Request client's password reset with email. Client will receive a token on provided email address in order to use Profile.confirmResetPassword(password, token).
This method requires client's email.
Method returns IApiCall object to execute request.

#### Profile.confirmResetPassword(password, token)
Confirm client's password reset with new password and token provided by Profile.requestPasswordReset(email).
This method requires client's password and confirmation token sent on email address.
Method returns IApiCall object to execute request.

## Injector

The Synerise Android InjectorSDK is designed to be simple to develop with, allowing you to integrate Synerise Mobile Content into your apps easily.
For more info about Synerise visit the [Synerise Website](http://synerise.com)

First of all, you need to initialize Injector with `init` method and provide `Api Key`, `Application name` and `Application instance`.
Init method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.

### Initialize

In your `Application` class:

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String syneriseApiKey = getString(R.string.synerise_api_key);
        String appName = getString(R.string.app_name);

        Injector.init(this, syneriseApiKey, appName);
    }
```

and in your /values strings file (e.g. `string.xml`):

```
<resources>
    <string name="app_name" translatable="false">Your GREAT application name</string>
    <string name="synerise_api_key" translatable="false">A75DA38F-A2E9-5A25-2884-38AD12B98FAA</string> <!-- replace with valid api key -->
</resources>
```

### Debug
You can receive some simple logs about injector by enabling debug mode, which is disabled by default.
```
Injector.setDebugMode(true);
```
Note: It is not recommended to use debug mode in release version of your application.

### Mobile Content integration

#### Banners

To integrate handling Mobile Content banners you have to register your app for push notifications first. Incoming push notifications have to be passed to `Injector`. `Injector` will then handle payload and display banner if provided payload is correctly validated.

### Handling push notifications

You have to pass incoming push notification payload to `Injector` in your `FirebaseMessagingService` implementation:

```
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Injector.handlePushPayload(remoteMessage.getData());
    }
}
```

Also register for push messages (note that *Profile* needs to be initialized):
```
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        IApiCall call = Profile.registerForPush(refreshedToken);
        call.execute(new ActionListener() {
            @Override
            public void onAction() {
                Log.d(TAG, "Register for Push Successful " + refreshedToken);
            }
        }, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {
              // handle error and try again later
            }
        });
    }
}
```
Please remember to register services in AndroidManifest as follows:
```
<application
        android:name=".App"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        ...

        <service
            android:name=".service.MyFirebaseMessagingService">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>

        <service
            android:name=".service.MyFirebaseInstanceIDService">
            <intent-filter>
                <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
            </intent-filter>
        </service>

    </application>
```

### Walkthrough

Walkthrough is called automatically during Injector initialization.<br>
The moment SDK gets successful response with walkthrough data, activity is started (naturally atop of activities stack).<br>
Note, that all intents to start new activities from your launcher activity are blocked in order to not cover walkthrough.
These intents are first distincted and then launched in order they were fired after walkthrough finishes.<br>
Note, that explained mechanism only works for support activities and it *does not* handle starting activities for result.<br>
Moreover, walkthrough may be launched only once. Simple flag is saved locally when activity is created,
therefore cleaning app data causes in calling API again.

## Synalter

First of all, you need to initialize Synalter with `init` method and provide `Api Key`, `Application name` and `Application instance`.
Init method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.

### Initialize

In your `Application` class:

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String syneriseApiKey = getString(R.string.synerise_api_key);
        String appName = getString(R.string.app_name);

        Synalter.init(this, syneriseApiKey, appName);
    }
```

and in your /values strings file (e.g. `string.xml`):

```
<resources>
    <string name="app_name" translatable="false">Your GREAT application name</string>
    <string name="synerise_api_key" translatable="false">A75DA38F-A2E9-5A25-2884-38AD12B98FAA</string> <!-- replace with valid api key -->
</resources>
```

### Required data
In order to modify your view's content, it's ID and location must be provided.<br>
For instance, if you would like to change some button text, 2 values must be integrated:<br>
1. Component path (e.g. com.example.app.your.path.to.activity.ButtonActivity)
2. ID (e.g. button_activity_button)

### Features

#### Synalter.setSynalterTimeout(timeout)
This method sets Synalter initial API call timeout. Synalter initial API call is called only once, before launcher activity is created.<br>
Note that API call blocks UI thread, which causes blank screen before launching your launcher activity.<br>
Blank screen color depends on your *windowBackground* color set in your default style. <br>
Timeout is a time value in millis, which by default is set to 5000.

#### Synalter.setSynalterUpdateInterval(interval)
This method sets Synalter data update interval. After this time, Synalter will try to fetch data from API and cache it for later use.<br>
In case of success, interval indicates time of next attempt.<br>
If cashed data is returned and Synalter data refresh time is expired, API call will be executed in background.<br>
Interval is a time value in seconds, which by default is set to 3600.

## Author

Synerise, developer@synerise.com. If you need support please feel free to contact us.

## License

InjectorSDK is available under the MIT license. See the LICENSE file for more info.