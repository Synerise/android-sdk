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
        classpath 'com.android.tools.build:gradle:3.1.1'
        classpath 'com.google.gms:google-services:3.2.0'

        classpath 'com.synerise.sdk:synerise-gradle-plugin:3.0.2'
        classpath 'org.aspectj:aspectjtools:1.8.13'
    }
}
```

Moreover, import dependency in your app/build.gradle file and apply plugin:
```
apply plugin: 'com.android.application'
apply plugin: 'synerise-plugin'
```
```
...
dependencies {
  ...
  // Synerise Mobile SDK
  implementation 'com.synerise.sdk:synerise-mobile-sdk:3.1.7'
}
```

### Initialize

First of all, you need to initialize Synerise Android SDK via `with` method and provide `Business Api Key`, `Client Api Key`, `Application name` and `Application instance`.<br>
To get `Business Api Key` and  `Client Api Key`, please sign in to your Synerise account and visit https://app.synerise.com/api/.<br>
Then, generate new `Api Key` for `Business Profile` audience and new `Api Key` for `Client` audience.<br>

In your `Application` sub-class:

```
public class App extends Application
// implements OnInjectorListener // optional Injector callback
{

    @Override
    public void onCreate() {
        super.onCreate();

        initSynerise();
    }

    private void initSynerise() {

        String syneriseBusinessProfileApiKey = getString(R.string.synerise_business_api_key);
        String syneriseClientApiKey = getString(R.string.synerise_client_api_key);
        String appId = getString(R.string.app_name);

        Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
                        .notificationIcon(R.drawable.notification_icon)
                        .syneriseDebugMode(true)
                        .trackerDebugMode(true)
                        .injectorDebugMode(true)
                        .trackerTrackMode(FINE)
                        .customClientConfig(new CustomClientAuthConfig("http://myBaseUrl.com/"))
                        .build();
        }
}
```

and in your /values strings file (e.g. `strings.xml`):

```
<resources>

    <string name="app_name" translatable="false">Your GREAT application name</string>
    <string name="synerise_business_api_key" translatable="false">7C0C4C93-B38E-03B6-0038-3C5F71FA0312</string> <!-- replace with valid business api key -->
    <string name="synerise_client_api_key" translatable="false">EF1AD0E0-532B-6AEE-6010-DEDC78F6E155</string> <!-- replace with valid client api key -->

    ...

</resources>
```

For your convenience, `Synerise.Builder` makes it possible to configure SDK the way you want it!<br>
Let's dive into some configurable functionalities:
1. .notificationIcon(@DrawableRes int) - if you're using campaign banners or push messages, it is recommended to pass your custom notification icon.
This icon is presented as a small notification icon. Note, that required icon must be a drawable resource (not mipmap) due to Android O adaptive icons restrictions.
Also, a default icon will be used if there is no custom icon provided.
2. .syneriseDebugMode(boolean) - simple flag may be provided in order to enable full network traffic logs. It is not recommended to use debug mode in release version of your app.
3. .trackerDebugMode(boolean) - you can receive some simple logs about sending events (like success, failure etc.) by enabling debug mode, which is disabled by default.
4. .injectorDebugMode(boolean) - you can receive some simple logs about Injector actions (like walkthrough or welcome screen availability) by enabling debug mode, which is disabled by default.
5. .trackerTrackMode(TrackMode) - sets proper mode for view tracking. See Tracker section below.
6. .customClientConfig(CustomClientAuthConfig) - you can also provide your custom Client `Authorization Configuration`. At this moment, configuration handles `Base URL` changes.
7. .build() - builds Synerise SDK with provided data. Please note, that `Synerise.Builder.with(..)` method is mandatory and `Synerise.Builder.build()` method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.<br>

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
    implementation 'com.android.support:multidex:1.0.3'
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

## Tracker

### Debug

You can receive some simple logs about events by enabling debug mode, which is disabled by default.
```
Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
    .trackerDebugMode(true)
    ...
    .build();
```
Note: It is not recommended to use debug mode in release version of your application.

### View tracking

As of now, Tracker also supports auto-tracking mode which can be enabled with:
```
Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
    .trackerTrackMode(FINE)
    ...
    .build();
```

Auto-tracking is *disabled* by default. Accepted values for trackerTrackMode(mode):
```
EAGER - listeners are set to onTouch() only
PLAIN - listeners are set to onClick() only
FINE  - listeners are attached to nearly everything in your app (even to activities and fragments `onStart()` method to record VisitedScreen events)
DISABLED - listeners are disabled
```

### Send event

To send some evens just use `Tracker.send(Event)` method:
```
Tracker.send(new CustomEvent("yourAction", "yourLabel"));
```

You can also pass your custom parameters:
```
TrackerParams params = new TrackerParams.Builder()
                .add("name", "John")
                .add("age", 27)
                .add("isGreat", true)
                .add("lastOrder", 384.28)
                .add("count", 0x7fffffffffffffffL)
                .add("someObject", new MySerializableObject())
                .build();

Tracker.send(new CustomEvent("yourAction", "yourLabel", params));
```

Tracker caches and enqueues all your events locally, so they all will be send when available.<br>
It also supports Android O *Background Execution Limits*.

### Events

#### - Session Events
Group of events related to user's session.

##### LoggedInEvent
Record a 'client logged in' event.
```
Tracker.send(new LoggedInEvent("Logged in label"));
```

##### LoggedOutEvent
Record a 'client logged out' event.
```
Tracker.send(new LoggedOutEvent("Logged out label"));
```

##### RegisteredEvent
Record a 'client registered' event.
```
Tracker.send(new RegisteredEvent("Registered label"));
```

#### - Products Events
Group of events related to products and cart.

##### AddedToFavoritesEvent
Record a 'client added product to favorites' event.
```
Tracker.send(new AddedToFavoritesEvent("Added to favorites label"));
```

##### AddedToCartEvent
Record a 'client added product to cart' event.
```
Tracker.send(new AddedToCartEvent("Added to cart label", "Sku", new UnitPrice(8, Currency.getInstance("USD")), 12));
```

##### RemovedFromCartEvent
Record a 'client removed product from cart' event.
```
Tracker.send(new RemovedFromCartEvent("Removed from cart label", "Sku", new UnitPrice(8, Currency.getInstance("USD")), 12));
```

#### - Transaction Events
Group of events related to user's transactions.

##### CancelledTransactionEvent
Record a 'client cancelled transaction' event.
```
Tracker.send(new CancelledTransactionEvent("Cancelled transaction label"));
```

##### CompletedTransactionEvent
Record a 'client completed transaction' event.
```
Tracker.send(new CompletedTransactionEvent("Completed transaction label"));
```

#### - Other Events
Group of uncategorized events related to user's location and actions.

##### AppearedInLocationEvent
Record a 'client appeared in location' event.
```
Tracker.send(new AppearedInLocationEvent("Appeared in location label", 52.26732, 27.37832));
```

##### HitTimerEvent
Record a 'client hit timer' event. This could be used for profiling or activity time monitoring - you can send "hit timer" when your client starts doing something and send it once again when finishes, but this time with different time signature. Then you can use our analytics engine to measure e.g. average activity time.
```
Tracker.send(new HitTimerEvent("Hit timer label"));
```

##### SearchedEvent
Record a 'client searched' event.
```
Tracker.send(new SearchedEvent("Searched label"));
```

##### SharedEvent
Record a 'client shared' event.
```
Tracker.send(new SharedEvent("Shared label"));
```

##### VisitedScreenEvent
Record a 'client visited screen' event.
```
Tracker.send(new VisitedScreenEvent("Visited screen label"));
```

##### Custom Event
This is the only event which requires `action` field.
```
Tracker.send(new CustomEvent("Custom action", "Custom label"));
```

Log your custom data with TrackerParams class.

### Other features

#### Tracker.flush()
Flush method forces sending events from queue to server.

#### Tracker.setClientId(id)
Synerise Client ID may be obtained after integration with Synerise API.


## Client

### Features

#### Client.signIn(email, password, deviceId)
Sign in a client in order to obtain the JWT token, which could be used in subsequent requests.<br>
The token is currently valid for 1 hour and SDK will refresh token before each call if it is expiring (but not expired).<br>
Method requires valid and non-null email and password. Device ID is optional.<br>
`signIn` mehtod also throws InvalidEmailException and InvalidPasswordException for you to catch and handle invalid email and/or password.<br>
Method returns `IApiCall` to execute request.
```
private void signIn(String email, String password) {
    try {
        signInCall = Client.signIn(email, password, null);
    } catch (InvalidEmailException e) {
        textEmail.setError(getString(R.string.error_invalid_email));
    } catch (InvalidPasswordException e) {
        textPassword.setError(getString(R.string.error_invalid_password));
    }
    if (signInCall != null) {
        signInCall.cancel();
        signInCall.onSubscribe(() -> toggleLoading(true))
                  .doFinally(() -> toggleLoading(false))
                  .execute(this::onSignInSuccessful, this::onSignInFailure);
    }
}
```

#### Client.signOut()
Signing client out causes in generating new UUID for a new anonymous one.

#### Client.getAccount()
Use this method to get client's account information.<br>
This method returns `IDataApiCall` with parametrized `AccountInformation` object to execute request.

#### Client.updateAccount(accountInformation)
Use this method to update client's account information.<br>
This method requires `AccountInformation` Builder Pattern object with client's account information. Not provided fields are not modified.
Method returns `IApiCall` to execute request.

#### Client.getToken()
Get valid JWT login token.<br>
Note, that error is thrown when Client is not logged in or token has expired and cannot be refreshed.<br>
Method returns `IDataApiCall` with parametrized `String` to execute request.

#### Client.getUUID()
Retrieve current client UUID.

#### Client.isSignedIn()
Retrieve whether client is signed in (is client's token not expired).

### CustomClientAuthConfig
You can also provide your custom Client `Authorization Configuration`. At this moment, configuration handles `Base URL` changes.
```
Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
    .customClientConfig(new CustomClientAuthConfig("http://myBaseUrl.com/"))
    ...
    .build();
```


## Profile

### Features

#### Profile.getClient(email)
Get client with email. <br>
Note, that you have to be logged in as business profile and use Api Key, which has REALM_CLIENT scope assigned or you have to be logged in as user and have ROLE_CLIENT_SHOW role assigned.<br>
`getClient` mehtod also throws InvalidEmailException for you to catch and handle invalid email.<br>
Method returns IDataApiCall with parametrized `ClientProfile` object to execute request.

#### Profile.createClient(createClient)
Create a new client record if no identifier has been assigned for him before in Synerise.
This method requires `CreateClient` Builder Pattern object with client's optional data. Not provided fields are not modified.
Method returns `IApiCall` object to execute request.

#### Profile.registerClient(registerClient)
Register new Client with email, password and optional data.
This method requires `RegisterClient` Builder Pattern object with client's email, password and optional data. Not provided fields are not modified.
Method returns `IApiCall` object to execute request.
```
private void signUp(RegisterClient registerClient, String name, String lastName) {
    if (signUpCall != null) signUpCall.cancel();
    signUpCall = Profile.registerClient(registerClient.setFirstName(name).setLastName(lastName));
    signUpCall.onSubscribe(() -> toggleLoading(true))
              .doFinally(() -> toggleLoading(false))
              .execute(this::onSignUpSuccessful, this::onSignUpFailure);
    }
```

#### Profile.updateClient(updateClient)
Update client with ID and optional data.
This method requires `UpdateClient` Builder Pattern object with client's optional data. Not provided fields are not modified.
Method returns `IApiCall` object to execute request.

#### Profile.deleteClient(id)
Delete client with ID.
This method requires client's id.
Method returns `IApiCall` object to execute request.

#### Profile.requestPasswordReset(email)
Request client's password reset with email. Client will receive a token on provided email address in order to use Profile.confirmResetPassword(password, token).
This method requires client's email.
Method returns `IApiCall` object to execute request.

#### Profile.confirmResetPassword(password, token)
Confirm client's password reset with new password and token provided by Profile.requestPasswordReset(email).
This method requires client's password and confirmation token sent on email address.
Method returns `IApiCall` object to execute request.

#### Profile.getToken()
Get valid JWT login token.<br>
Method returns `IDataApiCall` with parametrized `String` to execute request.

## Injector

Injector is designed to be simple to develop with, allowing you to integrate Synerise Mobile Content into your apps easily.<br>

### Debug
You can receive some simple logs about Injector by enabling debug mode, which is disabled by default.
```
Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
    .injectorDebugMode(true)
    ...
    .build();
```
Note: It is not recommended to use debug mode in release version of your application.

### Mobile Content integration

#### Banners

To integrate handling Mobile Content banners you have to register your app for push notifications first.<br>
Incoming push notifications have to be passed to `Injector`. `Injector` will handle payload and display banner if provided payload is correctly validated.

### Handling push notifications

You have to pass incoming push notification payload to `Injector` in your `FirebaseMessagingService` implementation:

```
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        boolean isSynerisePush = Injector.handlePushPayload(remoteMessage.getData());
    }
}
```

Also register for push messages:
```
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken != null) {
            IApiCall call = Profile.registerForPush(refreshedToken);
            call.execute(() -> Log.d(TAG, "Register for push succeed: " + refreshedToken),
                         apiError -> Log.w(TAG, "Register for push failed: " + refreshedToken));
        }
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

        <service android:name=".service.MyFirebaseMessagingService">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>

        <service android:name=".service.MyFirebaseInstanceIDService">
            <intent-filter>
                <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
            </intent-filter>
        </service>

    </application>
```

#### Campaign banner
If app was invisible to user (minimized or destroyed) and campaign banner came in - Synerise SDK makes it neat and simple.
Simple push message is presented to the user and launcher activity is fired after click on push.
It is a prefect moment for you to pass this data and SDK will verify whether it is campaign banner
and if so, banner will be presented within the app.
If your launcher activity last quite longer, check onNewIntent(Intent) implementation below.
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isSynerisePush = Injector.handlePushPayload(remoteMessage.getData());
    }
```
When it seems like your launcher activity is not necessarily a short splash or it just simply takes some time before moving on.
In this case it is preferred to override below method and set `android:launchMode="singleTop"` within your AndroidManifest activity declaration.
The reason is very simple, when campaign banner came (with Open App action type) while app was minimized,
simple push is presented to the user in the system tray.
If your launcher activity was already created and push was clicked - your onCreate(Bundle) method will not be called.
```
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        boolean isSynerisePush = Injector.handlePushPayload(remoteMessage.getData());
    }
```

#### Action callback
You can specify your custom action when user clicks on your banner, welcome screen, simple push button or walkthrough page.<br>
There are 2 main actions user may choose so far - Open url and Deep link.<br>
SDK makes it simple for you and by default handles those actions with browser opening or in case of deep linking - opening activity.<br>
To make your Activity available for deep linking, please use the following pattern in your AndroidManifest:
```
       <activity
            android:name=".ui.linking.DeepLinkingActivity">
            <intent-filter>
                <action android:name="deep_linking_key" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
```
You can also specify which activity you want to be fired after closing deep linking one with:
```
       <activity
            android:name=".ui.linking.DeepLinkingActivity"
            android:parentActivityName=".ui.linking.ParentDeepLinkingActivity">
            <intent-filter>
                <action android:name="deep_linking_key" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>

```
If you are not happy about default behavior, please implement your own behavior like:
```
public class App extends Application implements OnInjectorListener {

    @Override
    public void onCreate() {
        super.onCreate();

        initSynerise();
    }

    ...

    @Override
    public void onOpenUrl(String url) {
        // your action here
    }

    @Override
    public void onDeepLink(String deepLink) {
        // your action here
    }
```

### Walkthrough
Walkthrough is called automatically during Injector initialization.<br>
The moment SDK gets successful response with walkthrough data, activity is started (naturally atop of activities stack).<br>
Note, that all intents to start new activities from your launcher activity are blocked in order to not cover walkthrough.
These intents are first distincted and then launched in order they were fired after walkthrough finishes.<br>
Note, that explained mechanism only works for support activities (extending AppCompatActivity) and it *does not* handle starting activities for result.<br>
Moreover, walkthrough with given id may be launched only once. This id is saved locally when activity is created and will be checked whether received walkthrough is not the same.

### Welcome screen
Welcome screen is called automatically during SDK initialization.<br>
The moment SDK gets successful response with welcome screen data, activity is started (naturally atop of activities stack).<br>
Note, that all intents to start new activities from your launcher activity are blocked in order to not cover welcome screen.
These intents are first distincted and then launched in order they were fired after welcome screen finishes.<br>
Note, that explained mechanism only works for support activities (extending AppCompatActivity) and it *does not* handle starting activities for result.<br>
Moreover, welcome screen may be launched only once. Simple flag is saved locally when activity is created and SDK will not attempt to retrieve welcome screen from API again.

## Author
Synerise, developer@synerise.com. If you need support please feel free to contact us.