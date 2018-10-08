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
        classpath 'com.android.tools.build:gradle:3.1.3'

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
  // Synerise Android SDK
  implementation 'com.synerise.sdk:synerise-mobile-sdk:3.2.9'
}
```
Finally, please make sure your `Instant Run` is disabled.

### Initialize

First of all, you need to initialize Synerise Android SDK via `with` method and provide `Business Api Key`, `Client Api Key`, `Application name` and `Application instance`.<br>
To get `Business Api Key` and  `Client Api Key`, please sign in to your Synerise account and visit https://app.synerise.com/api/.<br>
Then, generate new `Api Key` for `Business Profile` audience and new `Api Key` for `Client` audience.<br>

In your `Application` sub-class:

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initSynerise();
    }

    private void initSynerise() {

        String syneriseBusinessProfileApiKey = getString(R.string.synerise_business_api_key);
        String syneriseClientApiKey = getString(R.string.synerise_client_api_key);
        String appId = getString(R.string.app_name);

        final boolean DEBUG_MODE = BuildConfig.DEBUG;

        Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
                        .notificationIcon(R.drawable.notification_icon)
                        .syneriseDebugMode(DEBUG_MODE)
                        .clientRefresh(true)
                        .poolUuid("your-pool-uuid-here")
                        .trackerTrackMode(FINE)
                        .trackerMinBatchSize(10)
                        .trackerMaxBatchSize(100)
                        .trackerAutoFlushTimeout(5000)
                        .injectorAutomatic(false)
                        .pushRegistrationRequired(this)
                        .locationUpdateRequired(this)
                        .notificationChannelName("testChannelName")
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
    <string name="synerise_business_api_key" translatable="false">7C0C4C93-B38E-03B6-0038-3C5F71FA0312</string> <!-- replace with valid business api key -->
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
5. `.clientRefresh(boolean)` - enables automatic client's token refresh.
6. `.poolUuid(String)` - provide your pool's universally unique identifier to assign available voucher to the customer right before registration.
7. `.trackerTrackMode(TrackMode)` - sets proper mode for view tracking. See Tracker section below.
8. `.trackerMinBatchSize(int)` - sets minimum number of events in queue required to send them.
9. `.trackerMaxBatchSize(int)` - sets maximum number of events, which may be sent in a single batch.
10. `.trackerAutoFlushTimeout(int)` - sets time required to elapse before event's queue will attempt to be sent.
11. `.injectorAutomatic(boolean)` - simple flag may be provided to enable automatic mode in injector. See Injector section for more information.
12. `.pushRegistrationRequired(OnRegisterForPushListener)` - Synerise SDK may request you to register client for push notifications. This callback is called at after client signs in, signs up or deletes account.
13. `.locationUpdateRequired(OnLocationUpdateListener)` - this callback is called on demand via push notification, so it may be called at any point of time.
14. `.notificationChannelName(String)` - sets name of Push Notification Channel. For more info please check Injector section below.
15. `.baseUrl(String)` - you can provide your custom base URL to use your own API.
16. `.customClientConfig(CustomClientAuthConfig)` - you can also provide your custom Client `Authorization Configuration`. At this moment, configuration handles `Base URL` changes.
17. `.build()` - builds Synerise SDK with provided data. Please note, that `Synerise.Builder.with(..)` method is mandatory and `Synerise.Builder.build()` method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.<br>

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

## Tracker

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
EAGER - listeners are set to onTouch() only.
PLAIN - listeners are set to onClick() only.
FINE  - listeners are attached to nearly everything in your app (even to Activities and Fragments `onStart()` method to record VisitedScreen events).
DISABLED - listeners are disabled, which is default mode.
```

### Send event

To send some event just use `Tracker.send(Event)` method:
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

Tracker caches and enqueues all your events locally, so they all will be send eventually.<br>
It also supports Android O *Background Execution Limits* (https://developer.android.com/about/versions/oreo/background.html).

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

Log your custom data with `TrackerParams` class.

### Other features

#### Tracker.flush()
Flush method forces sending events from queue to server.
```
private void flush() {
    Tracker.flush();
}
```

#### Tracker.setCustomIdentifier(customIdentifier)
You can also pass your custom identifier to match your users in our CRM. Your custom identifier will be sent within every event in event params.
```
private void setCustomIdentifier(String customIdentifier) {
    Tracker.setCustomIdentifier(customIdentifier);
}
```

#### Tracker.setCustomEmail(customEmail)
You can also pass your custom email to match your users in our CRM. Your custom email will be sent within every event in event params.
```
private void setCustomEmail(String customEmail) {
    Tracker.setCustomEmail(customEmail);
}
```

## Client

### Features

#### Client.signInByEmail(email, password, deviceId)
Sign in a client in order to obtain the JWT token, which could be used in subsequent requests.<br>
The token is currently valid for 1 hour and SDK will refresh token before each call if it is expiring (but not expired).<br>
Method requires valid and non-null email and password. Device ID is optional.<br>
`signInByEmail` method also throws `InvalidEmailException` for you to catch and handle invalid email.<br>
Method returns `IApiCall` to execute request.<br>
Please note that you should NOT allow to sign in again (or sign up) when user is already signed in. Please sign out user first.<br>
Moreover, please do not create multiple instances nor call this method multiple times before execution.
```
private void signIn(String email, String password) {
    if (signInCall != null) signInCall.cancel();
    try {
        signInCall = Client.signIn(email, password, null);
    } catch (InvalidEmailException e) {
        textEmail.setError(getString(R.string.error_invalid_email));
    }

    signInCall.onSubscribe(() -> toggleLoading(true))
              .doFinally(() -> toggleLoading(false))
              .execute(this::onSignInSuccessful, this::onSignInFailure);
}
```

#### Client.signInByPhone(phone, password, deviceId)
Sign in a client in order to obtain the JWT token, which could be used in subsequent requests.<br>
The token is currently valid for 1 hour and SDK will refresh token before each call if it is expiring (but not expired).<br>
Method requires valid and non-null phone number and password. Device ID is optional.<br>
`signInByPhone` method also throws `InvalidPhoneNumberException` for you to catch and handle invalid phone number.<br>
Method returns `IApiCall` to execute request.<br>
Please note that you should NOT allow to sign in again (or sign up) when user is already signed in. Please sign out user first.<br>
Moreover, please do not create multiple instances nor call this method multiple times before execution.
```
private void signIn(String phone, String password) {
    if (signInCall != null) signInCall.cancel();
    try {
        signInCall = Client.signIn(phone, password, null);
    } catch (InvalidPhoneNumberException e) {
        textEmail.setError(getString(R.string.error_invalid_phone));
    }

    signInCall.onSubscribe(() -> toggleLoading(true))
              .doFinally(() -> toggleLoading(false))
              .execute(this::onSignInSuccessful, this::onSignInFailure);
    }
}
```

#### Client.signOut()
Signing client out clears email, id and generates new UUID for anonymous one.<br>
It also clears client's JWT token.
```
private void signOut() {
    Client.signOut();
}
```

#### Client.getAccount()
Use this method to get client's account information.<br>
This method returns `IDataApiCall` with parametrized `AccountInformation` object to execute request.
```
private void getAccount() {
    if (accountInfoCall != null) accountInfoCall.cancel();
    accountInfoCall = Client.getAccount();
    accountInfoCall.execute(success -> onSuccess(), this::onError);
}
```

#### Client.updateAccount(accountInformation)
Use this method to update client's account information.<br>
This method requires `AccountInformation` Builder Pattern object with client's account information. Not provided fields are not modified.
Method returns `IApiCall` to execute request.
```
private void updateAccount(String city, String company) {
    AccountInformation accountInformation = new AccountInformation();
    accountInformation.setCity(city).setCompany(company);

    if (apiCall != null) apiCall.cancel();
    apiCall = Client.updateAccount(accountInformation);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.getAnalytics()
Get all available Analytics metrics for the client.<br>
Please note that in order to use this method, Client must be signed in first.<br>
This method returns `IDataApiCall` with parametrized `List<AnalyticsMetrics>` object to execute request.
```
private void getAnalytics() {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.getAnalytics();
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.getAnalytics(name)
Fetch all available Analytics metrics for the client and return the first metric, which matches provided name.<br>
Please note that in order to use this method, Client must be signed in first.<br>
This method returns `IDataApiCall` with parametrized `AnalyticsMetrics` object to execute request.
```
private void getAnalytics(String name) {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.getAnalytics(name);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.getPromotions(excludeExpired)
Use this method to get all available promotions that are defined for this client.
Also, you are able to decide whether include or exclude already expired promotions.
This method returns `IDataApiCall` with parametrized `List<PromotionResponse>` object to execute request.
```
private void getPromotions(boolean excludeExpired) {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.getPromotions(excludeExpired);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.getPromotions(excludeExpired, statuses)
Use this method to get all possible combinations of promotions statuses, which are defined for this client.
Method returns promotions with statuses provided in the list. A special query is build upon this list.
Also, you are able to decide whether include or exclude already expired promotions.
This method returns `IDataApiCall` with parametrized `List<PromotionResponse>` object to execute request.
```
private void getPromotions(boolean excludeExpired, PromotionStatus[] statuses) {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.getPromotions(excludeExpired, statuses);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.activatePromotionByUuid(uuid)
Use this method to activate promotion that has uuid passed as parameter.
Method returns `IApiCall` to execute request.
```
private void activatePromotionByUuid(String uuid) {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.activatePromotionByUuid(uuid);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.activatePromotionByCode(code)
Use this method to activate promotion that has code passed as parameter.
Method returns `IApiCall` to execute request.
```
private void activatePromotionByCode(String code) {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.activatePromotionByCode(code);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.deactivatePromotionByUuid(uuid)
Use this method to deactivate promotion that has uuid passed as parameter.
Method returns `IApiCall` to execute request.
```
private void deactivatePromotionByUuid(String uuid) {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.deactivatePromotionByUuid(uuid);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.deactivatePromotionByCode(code)
Use this method to deactivate promotion that has code passed as parameter.
Method returns `IApiCall` to execute request.
```
private void deactivatePromotionByCode(String code) {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.deactivatePromotionByCode(code);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.getOrAssignVoucher(poolUuid)
Use this method to get voucher code only once or assign voucher with provided pool uuid for the client.
Methods returns IDataApiCall with parametrized AssignVoucherResponse object to execute request.
```
private void getOrAssignVoucher(String poolUuid) {
    if (call != null) call.cancel();
    call = Client.getOrAssignVoucher(poolUuid);
    call.execute(this::onSuccess, this::onError);
}
```

#### Client.getAssignedVoucherCodes()
Use this method to get client's voucher codes.
Methods returns IDataApiCall with parametrized VoucherCodesResponse object to execute request.
```
private void getAssignedVoucherCodes() {
    if (call != null) call.cancel();
    call = Client.getAssignedVoucherCodes();
    call.execute(this::onSuccess, this::onError);
}
```

#### Client.assignVoucherCode(poolUuid)
Use this method to assign voucher with provided pool uuid for the client.<br>
Every request returns different code until the pool is empty. 416 Http status code is returned when pool is empty.
Methods returns IDataApiCall with parametrized AssignVoucherResponse object to execute request.
```
private void assignVoucherCode(String poolUuid) {
    if (call != null) call.cancel();
    call = Client.assignVoucherCode(poolUuid);
    call.execute(this::onSuccess, this::onError);
}
```

#### Client.requestPhoneUpdate(phone)
Use this method to request phone number update. This action requires additional validation via pin code.
Method returns `IApiCall` to execute request.
```
private void requestPhoneUpdate(String phone) {
    if (apiCall != null) apiCall.cancel();
    try {
        apiCall = Client.requestPhoneUpdate(phone);
    } catch (InvalidPhoneNumberException e) { }

    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.confirmPhoneUpdate(phone, confirmationCode)
Use this method to confirm phone number update. This action requires to pass phone number and confirmation code as parameters.
Method returns `IApiCall` to execute request.
```
private void confirmPhoneUpdate(String phone, String confirmationCode) {
    if (apiCall != null) apiCall.cancel();
    try {
        apiCall = Client.confirmPhoneUpdate(phone, confirmationCode);
    } catch (InvalidPhoneNumberException e) { }

    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.changePassword(password)
Use this method to change client's password.<br>
New password will be saved automatically if `Synerise.getClientRefresh()` is true.<br>
Method returns `IApiCall` to execute request.
```
private void changePassword(String password) {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.changePassword(password);
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.changePassword(newPassword, oldPassword)
Use this method to change client's password.<br>
Old password will be compared with the one stored within SDK if and only if `Synerise.getClientRefresh()` is true.<br>
New password will be saved automatically if `Synerise.getClientRefresh()` is true.<br>
Method returns `IApiCall` to execute request.
```
private void changePassword(String newPassword, String oldPassword) {
    if (apiCall != null) apiCall.cancel();
    try {
        apiCall = Client.changePassword(newPassword, oldPassword);
    } catch (EmptyStoredPasswordException e1) { }
    catch (InvalidStoredPasswordException e2) { }

    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.deleteAccount()
Use this method to delete client's account.
Method returns `IApiCall` to execute request.
```
private void changePassword(String password) {
   if (deleteCall != null) deleteCall.cancel();
   deleteCall = Client.deleteAccount();
   deleteCall.execute(this::onSuccess, this::onError);
}
```

#### Client.createAuthTokenByUuid(uuid)
Use this method to obtain unregistered client's authorization token.
Note, that 401 http status code is returned if you used this method for the client already existing in Synerise database.
Method returns `IApiCall` to execute request.
```
private void createAuthTokenByUuid(String uuid) {
   if (createCall != null) createCall.cancel();
   createCall = Client.createAuthTokenByUuid(uuid);
   createCall.execute(this::onSuccess, this::onError);
}
```

#### Client.createAuthTokenByEmail(email)
Use this method to obtain unregistered client's authorization token.
Note, that 401 http status code is returned if you used this method for the client already existing in Synerise database.
Method returns `IApiCall` to execute request.
```
private void createAuthTokenByEmail(String email) {
   if (createCall != null) createCall.cancel();
   createCall = Client.createAuthTokenByEmail(email);
   createCall.execute(this::onSuccess, this::onError);
}
```

#### Client.createAuthTokenByCustomId(customId)
Use this method to obtain unregistered client's authorization token.
Note, that 401 http status code is returned if you used this method for the client already existing in Synerise database.
Method returns `IApiCall` to execute request.
```
private void createAuthTokenByCustomId(String customId) {
   if (createCall != null) createCall.cancel();
   createCall = Client.createAuthTokenByCustomId(customId);
   createCall.execute(this::onSuccess, this::onError);
}
```

#### Client.getToken()
Get valid JWT login token.<br>
Note, that error is thrown when Client is not logged in or token has expired and cannot be refreshed.<br>
Method returns `IDataApiCall` with parametrized `String` to execute request.
```
private void getToken() {
    if (apiCall != null) apiCall.cancel();
    apiCall = Client.getToken();
    apiCall.execute(this::onSuccess, this::onError);
}
```

#### Client.getUuid()
Retrieve current client UUID.
```
private String getUuid() {
    return Client.getUuid();
}
```

#### Client.isSignedIn()
Retrieve whether client is signed in (is client's token not expired).
```
private boolean isSignedIn() {
    return Client.isSignedIn();
}
```

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
Get client's account data with email. <br>
Note, that you have to be logged in as business profile and use Api Key, which has REALM_CLIENT scope assigned or you have to be logged in as user and have ROLE_CLIENT_SHOW role assigned.<br>
`getClient` method also throws InvalidEmailException for you to catch and handle invalid email.<br>
Method returns IDataApiCall with parametrized `ClientProfile` object to execute request.
```
private void getClient(String email) {
    if (getClientCall != null) getClientCall.cancel();
    try {
        getClientCall = Profile.getClient(email);
    } catch (InvalidEmailException e) { }

    getClientCall.execute(this::onSuccess, this::onError);
}
```

#### Profile.createClient(createClient)
Create a new client record if no identifier has been assigned for him before in Synerise.
This method requires `CreateClient` Builder Pattern object with client's optional data. Not provided fields are not modified.
Method returns `IApiCall` object to execute request.
```
private void createClient(String email, String city) {

    CreateClient createClient = new CreateClient();
    try {
        createClient.setEmail(email);
    } catch (InvalidEmailException e) { return; }

    createClient.setCity(city);
    if (call != null) call.cancel();
    call = Profile.createClient(createClient);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.registerClientByEmail(registerClient)
Register new Client with email, password and optional data.
This method requires `RegisterClient` Builder Pattern object with client's email, password and optional data. Not provided fields are not modified.
Method returns `IApiCall` object to execute request. Remember that account becomes active after successful email verification.<br>
Please note that you should NOT allow to sign in again (or sign up) when user is already signed in. Please sign out user first.<br>
Moreover, please do not create multiple instances nor call this method multiple times before execution.
```
private void signUp(RegisterClient registerClient, String name, String lastName) {
    if (signUpCall != null) signUpCall.cancel();
    signUpCall = Profile.registerClient(registerClient.setFirstName(name).setLastName(lastName));
    signUpCall.onSubscribe(() -> toggleLoading(true))
              .doFinally(() -> toggleLoading(false))
              .execute(this::onSignUpSuccessful, this::onSignUpFailure);
}
```

#### Profile.registerClientByEmailWithoutActivation(registerClient)
Register new Client with email, password and optional data.
This method requires `RegisterClient` Builder Pattern object with client's email, password and optional data. Not provided fields are not modified.
Method returns `IApiCall` object to execute request. Remember that account registered with this method becomes active without email verification.<br>
Please note that you should NOT allow to sign in again (or sign up) when user is already signed in. Please sign out user first.<br>
Moreover, please do not create multiple instances nor call this method multiple times before execution.
```
private void signUp(RegisterClient registerClient, String name, String lastName) {
    if (signUpCall != null) signUpCall.cancel();
    signUpCall = Profile.registerClientByEmailWithoutActivation(registerClient.setFirstName(name).setLastName(lastName));
    signUpCall.onSubscribe(() -> toggleLoading(true))
              .doFinally(() -> toggleLoading(false))
              .execute(this::onSignUpSuccessful, this::onSignUpFailure);
}
```

#### Profile.registerClientByPhone(registerClient)
Register new Client with phone, password and optional data.
This method requires `RegisterClient` Builder Pattern object with client's phone, password and optional data. Not provided fields are not modified.
Method returns `IApiCall` object to execute request. Remember that account becomes active after successful sms verification.<br>
Please note that you should NOT allow to sign in again (or sign up) when user is already signed in. Please sign out user first.<br>
Moreover, please do not create multiple instances nor call this method multiple times before execution.
```
private void signUp(RegisterClient registerClient) {
    if (signUpCall != null) signUpCall.cancel();
    signUpCall = Profile.registerClientByPhone(registerClient);
    signUpCall.onSubscribe(() -> toggleLoading(true))
              .doFinally(() -> toggleLoading(false))
              .execute(this::onSucess, this::onError);
}
```

#### Profile.confirmPhoneRegistration(phoneNumber, confirmationCode)
Verify provided phone number after registration. Confirmation code will be send via SMS. <br>
Method returns `IApiCall` object to execute request.
```
private void confirmCode(String confirmationCode) {
    if (confirmPhoneCall != null) confirmPhoneCall.cancel();
    try {
        confirmPhoneCall = Profile.confirmPhoneRegistration(phoneNumber, confirmationCode);
    } catch (InvalidPhoneNumberException e) { }

    confirmPhoneCall.onSubscribe(() -> toggleLoading(true))
                    .doFinally(() -> toggleLoading(false))
                    .execute(this::onConfirmPhoneNumberSuccess, this::onError);
}
```

#### Profile.updateClient(id, updateClient)
Update client with ID and optional data.
This method requires `UpdateClient` Builder Pattern object with client's optional data. Not provided fields are not modified.
Method returns `IApiCall` object to execute request.
```
private void updateClient(String email, String name, String lastName, String clientId) {
    UpdateClient updateClient = new UpdateClient();
    updateClient.setFirstName(name);
    updateClient.setLastName(lastName);
    try {
        updateClient.setEmail(email);
    } catch (InvalidEmailException e) { }

    if(updateCall != null) updateCall.cancel();
    updateCall = Profile.updateClient(clientId, updateClient);
    updateCall.onSubscribe(() -> toggleLoading(true))
              .doFinally(() -> toggleLoading(false))
              .execute(() -> onUpdateClientSuccessful(email, name, lastName),this::onError});
}
```

#### Profile.deleteClient(id)
Delete client with ID.
This method requires client's id.
Method returns `IApiCall` object to execute request.
```
private void deleteClient(long id){
    if (call != null) call.cancel();
    call = Profile.deleteClient(id);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.activateClient(email)
Activate client with email.
This method requires client's email.
Method returns `IApiCall` object to execute request.
```
private void activateClient(String email){
    if(call != null) call.cancel()
    try {
       call = Profile.activateClient(email);
    } catch (InvalidEmailException e) { }

    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.requestPasswordReset(email)
Request client's password reset with email. Client will receive a token on provided email address in order to use Profile.confirmResetPassword(password, token).
This method requires client's email.
Method returns `IApiCall` object to execute request.
```
private void requestPasswordReset(String email) {
    if (call != null) call.cancel();
    call = Profile.requestPasswordReset(resetRequest);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.confirmResetPassword(password, token)
Confirm client's password reset with new password and token provided by Profile.requestPasswordReset(email).
This method requires client's password and confirmation token sent on email address.
Method returns `IApiCall` object to execute request.
```
private void confirmResetPassword(String password, String token) {
    if (call != null) call.cancel();
    PasswordResetConfirmation confirmation = new PasswordResetConfirmation(password, token);
    call = Profile.confirmPasswordReset(confirmation);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getToken()
Get valid JWT login token.<br>
Method returns `IDataApiCall` with parametrized `String` to execute request.
```
public void getToken() {
    if (getTokenCall != null) getTokenCall.cancel();
    getTokenCall = Profile.getToken();
    getTokenCall.execute(this::onSuccess, this::onFailure);
}
```

#### Profile.getPromotions()
Use this method to get all available promotions that are defined for your business profile.<br>
Note that using this method causes limiting your response data to 100.
Method returns `IDataApiCall` with parametrized `ProfilePromotionResponse` to execute request.
```
private void getPromotions() {
    if (call != null) call.cancel();
    call = Profile.getPromotions();
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getPromotions(limit)
Use this method to get all available promotions that are defined for your business profile.
Method returns `IDataApiCall` with parametrized `ProfilePromotionResponse` to execute request.
```
private void getPromotions(int limit) {
    if (call != null) call.cancel();
    call = Profile.getPromotions(limit);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getPromotionsByExternalId(value)
Use this method to get promotion with externalId specified as value param.
Method returns `IDataApiCall` with parametrized `ProfilePromotionResponse` to execute request.
```
private void getPromotions(String externalId) {
    if (call != null) call.cancel();
    call = Profile.getPromotionsByExternalId(externalId);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getPromotionsByPhone(value)
Use this method to get promotion with phone specified as value param.
Method returns `IDataApiCall` with parametrized `ProfilePromotionResponse` to execute request.
```
private void getPromotions(String phone) {
    if (call != null) call.cancel();
    call = Profile.getPromotionsByPhone(phone);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getPromotionsByClientId(value)
Use this method to get promotion with clientId specified as value param.
Method returns `IDataApiCall` with parametrized `ProfilePromotionResponse` to execute request.
```
private void getPromotions(String clientId) {
    if (call != null) call.cancel();
    call = Profile.getPromotionsByClientId(clientId);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getPromotionsByEmail(value)
Use this method to get promotion with email specified as value param.
Method returns `IDataApiCall` with parametrized `ProfilePromotionResponse` to execute request.
```
private void getPromotionsByEmail(String email) {
    if (call != null) call.cancel();
    call = Profile.getPromotionsByEmail(email);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getPromotionByCode(value)
Use this method to get promotion with code specified as value param.
Method returns `IDataApiCall` with parametrized `ProfilePromotionResponse` to execute request.
```
private void getPromotions(String code) {
    if (call != null) call.cancel();
    call = Profile.getPromotionByCode(code);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getPromotionByUuid(value)
Use this method to get promotion with uuid specified as value param.
Method returns `IDataApiCall` with parametrized `ProfilePromotionResponse` to execute request.
```
private void getPromotions(String uuid) {
    if (call != null) call.cancel();
    call = Profile.getPromotionByUuid(uuid);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.redeemPromotionByPhone(promotionCode, phoneNumber)
Use this method to redeem promotion with specified promotionCode and phoneNumber
Method returns `IApiCall` object to execute request.
```
private void redeemPromotion(String promotionCode, String phoneNumber) {
    if (call != null) call.cancel();
    call = Profile.redeemPromotionByPhone(promotionCode, phoneNumber);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.redeemPromotionByClientId(promotionCode, clientId)
Use this method to redeem promotion with specified promotionCode and clientId
Method returns `IApiCall` object to execute request.
```
private void redeemPromotion(String promotionCode, String clientId) {
    if (call != null) call.cancel();
    call = Profile.redeemPromotionByClientId(promotionCode, clientId);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.redeemPromotionByCustomId(promotionCode, customId)
Use this method to redeem promotion with specified promotionCode and customId
Method returns `IApiCall` object to execute request.
```
private void redeemPromotion(String promotionCode, String customId) {
    if (call != null) call.cancel();
    call = Profile.redeemPromotionByCustomId(promotionCode, customId);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.redeemPromotionByEmail(promotionCode, email)
Use this method to redeem promotion with specified promotionCode and email
Method returns `IApiCall` object to execute request.
```
private void redeemPromotion(String promotionCode, String email) {
    if (call != null) call.cancel();
    call = Profile.redeemPromotionByEmail(promotionCode, email);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getOrAssignVoucher(poolUuid, clientUuid)
Use this method to get voucher code only once or assign voucher with provided pool uuid for the client.
Methods returns IDataApiCall with parametrized AssignVoucherResponse object to execute request.
```
private void getOrAssignVoucher(String poolUuid, String clientUuid) {
    if (call != null) call.cancel();
    call = Profile.getOrAssignVoucher(poolUuid, clientUuid);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.getClientVoucherCodes(clientUuid)
Use this method to get client's voucher codes.
Methods returns IDataApiCall with parametrized VoucherCodesResponse object to execute request.
```
private void getClientVoucherCodes(String clientUuid) {
    if (call != null) call.cancel();
    call = Profile.getClientVoucherCodes(clientUuid);
    call.execute(this::onSuccess, this::onError);
}
```

#### Profile.assignVoucherCode(poolUuid, clientUuid)
Use this method to assign voucher with provided pool uuid for the client.<br>
Every request returns different code until the pool is empty. 416 Http status code is returned when pool is empty.
Methods returns IDataApiCall with parametrized AssignVoucherResponse object to execute request.
```
private void assignVoucherCode(String poolUuid, String clientUuid) {
    if (call != null) call.cancel();
    call = Profile.assignVoucherCode(poolUuid, clientUuid);
    call.execute(this::onSuccess, this::onError);
}
```

## Injector

Injector is designed to be simple to develop with, allowing you to integrate Synerise Mobile Content into your apps easily.<br>

### Handling push notifications
In order to display push messages and banners properly, you have to pass incoming push notification payload to `Injector` in your `FirebaseMessagingService` implementation:
```
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        boolean isSynerisePush = Injector.handlePushPayload(remoteMessage.getData());
    }
}
```
Overriding `onMessageReceived(RemoteMessage)` causes simple notification to not being displayed while app is visible to user. <br>
Please check our sample app for example usage of building your non-Synerise notification. <br>
<br>
Also register for push messages:
```
    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);

        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken != null) {
            IApiCall call = Profile.registerForPush(refreshedToken);
            call.execute(() -> Log.d(TAG, "Register for Push succeed: " + refreshedToken),
                         apiError -> Log.w(TAG, "Register for push failed: " + refreshedToken));
        }
    }
```
and in your `Application` implementation:
```
public class App extends MultiDexApplication implements OnRegisterForPushListener {

    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

         Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
                                .notificationIcon(R.drawable.ic_notification_icon)
                                .pushRegistrationRequired(this)
                                ...
                                .build();
    }

    @Override
    public void onRegisterForPushRequired() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String refreshedToken = instanceIdResult.getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            IApiCall call = Profile.registerForPush(refreshedToken);
            call.execute(() -> Log.d(TAG, "Register for Push succeed: " + refreshedToken),
                         apiError -> Log.w(TAG, "Register for push failed: " + refreshedToken));
        });
    }
}
```
Please remember to register your service in AndroidManifest as follows:
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

    </application>
```
### Notification channel
Starting in Android 8.0 (API level 26), all notifications must be assigned to a channel or it will not appear.<br>
Due to this your notifications will be grouped in a channel. If you want to set name for this channel use `notificationChannelName(String)` method of Builder during SDK initialization. <br>
By default channel name is set to your application name.

### Campaign banner

#### Handling app invisibility
If app was invisible to user (minimized or destroyed) and campaign banner came in -
simple push message is presented to the user and launcher activity is fired after notification click.<br>
It is a prefect moment for you to pass this data and SDK will verify whether it is campaign banner
and if so, banner will be presented within the app (atop of your activities). Please note that banner is a translucent activity,
so your activity's `onStop()` method may not be called. Also banner activity is launched with 
FLAG_ACTIVITY_NEW_TASK flag so you can handle your activity stack properly.<br>
If your launcher activity last quite longer, check `onNewIntent(Intent)` implementation below.
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isSynerisePush = Injector.handlePushPayload(getIntent().getExtras());
    }
```
When it seems like your launcher activity is not necessarily a short splash or it just simply takes some time before moving on, you might be interested in `onNewIntent(Intent)` method.
In this case it is preferred to override this method and set `android:launchMode="singleTop"` within your AndroidManifest Activity declaration.
The reason is very simple - when campaign banner came while app was minimized, simple push is presented to the user in the system tray.
If your launcher activity was already created and notification was clicked - your onCreate(Bundle) method will not be called.
```
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        boolean isSynerisePush = Injector.handlePushPayload(intent.getExtras());
    }
```

##### Synerise Push type detection
Sometimes you may need to know whether incoming push message comes from Synerise and if so, what information is exactly carrying.<br>
`Injector.isSynerisePush(Map<String, String>)` only checks whether provided push data comes from Synerise.
It is validated by checking if incoming push contains "issuer" key with "Synerise" value.<br>
`Injector.isSyneriseSimplePush(Map<String, String>)` only checks whether provided push data comes from Synerise and is it specifically Synerise Simple Push.
It is validated by checking if incoming push contains "content-type" key with "simple-push" value.<br>
`Injector.isSyneriseBanner(Map<String, String>)` only checks whether provided push data comes from Synerise and is it specifically Synerise Banner.
It is validated by checking if incoming push contains "content-type" key with "template-banner" value.<br>
`Injector.isSilentCommand` only checks whether provided push data comes from Synerise and is it specifically Silent Command.
It is validated by checking if incoming push contains "content-type" key with "silent-command" value.<br>
`Injector.isSilentSdkCommand` only checks whether provided push data comes from Synerise and is it specifically Silent SDK Command.
It is validated by checking if incoming push contains "content-type" key with "silent-sdk-command" value.<br>

#### Push structure
You can also react to Synerise push notifications by yourself and that is why we would like to share our Synerise Simple Push and Synerise Banner push structure.<br>
Synerise Simple Push:<br>
```
{
  "data": {
    "issuer": "Synerise",
    "message-type": "static-content",
    "content-type": "simple-push",
    "content": {
      "notification": {
        "title": "Synerise Simple Push title",
        "body": "Synerise Simple Push message",
        "sound": "default",
        "icon": "http://images.synerise.com/marvellous_image.jpg",
        "priority": "HIGH",
        "action": {
          "item": "https://synerise.com",
          "type": "OPEN_URL"
        }
      },
      "buttons": [
        {
          "identifier": "button_1",
          "action": {
            "item": "https://synerise.com",
            "type": "OPEN_URL"
          },
          "text": "Button 1"
        },
        {
          "identifier": "button_2",
          "action": {
            "item": "syne://product?sku=el-oven",
            "type": "DEEP_LINKING"
          },
          "text": "Button 2"
        }
      ],
      "campaign": {
        "variant_id": 12345,
        "type": "Mobile push",
        "title": "Mobile push test campaign",
        "hash_id": "1893b5be-79c6-4432-xxxx-81e7bd4ea09d"
      }
    }
  }
}
```
Action types are: `DEEP_LINKING`, `OPEN_URL`, `OPEN_APP`.<br>
Priority types are: `NORMAL`, `HIGH`.<br>
<br>
Synerise Banner:<br>
```
{
  "notification": {
    "title": "Notification title if app was invisible",
    "body": "Notification message if app was invisible"
  },
  "data": {
    "issuer": "Synerise",
    "message-type": "dynamic-content",
    "content-type": "template-banner",
    "content": {
      "page": {
        "type": "image_with_text_atop",
        "button": {
          "is_enabled": true,
          "corner_radius": 40,
          "color": "#13e413",
          "text": "Navigate to Synerise",
          "text_color": "#1f74d9"
        },
        "image": {
          "url": "http://images.synerise.com/marvellous_image.jpg"
        },
        "close_button": {
          "is_enabled": true,
          "alignment": "LEFT"
        },
        "background": {
          "color": "#d319d3",
          "alpha": 0.5
        },
        "index": 0,
        "header": {
          "color": "#384350",
          "size": 35,
          "alpha": 1,
          "text": "SYNERISE"
        },
        "description": {
          "color": "#384350",
          "size": 20,
          "alpha": 1,
          "text": "Click below button to open Synerise website"
        },
        "action": {
          "item": "http://synerise.com",
          "type": "OPEN_URL"
        }
      },
      "auto_disappear": {
        "is_enabled": true,
        "timeout": 5
      },
      "campaign": {
        "variant_id": 12345,
        "type": "Mobile banner",
        "title": "Mobile banner test campaign",
        "hash_id": "1893b5be-79c6-4432-xxxx-81e7bd4ea09d"
      }
    }
  }
}
```
Banner types are: `color_as_background`, `image_as_background`, `image_with_text_atop`, `image_with_text_below`.

#### Optional callbacks
It is not always suitable for you to cover your Activities with any banners which may come.<br>
Fortunately, we have put this into deep consideration and as for now, we'd like to present our optional banner callbacks.
```
Injector.setOnBannerListener(new OnBannerListener() {

        @Override
        public boolean shouldPresent(Map<String, String> data) {
            return true;
        }

        @Override
        public void onPresented() {
            Log.d("DashboardActivity", "Banner has been presented.");
        }

        @Override
        public void onClosed() {
            Log.d("DashboardActivity", "Banner has been closed.");
        }
    });
```
Note, that all these methods are optional and overriding them is not required.<br>
1. `shouldPresent(Map)` - This method decides whether to show banner if any came. SDK allows showing banner by default.
`data` parameter stands for banner's necessary data for building process. Call `Injector.handlePushPayload(Map)` to create banner with provided data.
Return true to allow banner presentation, false otherwise.
2. `onPresented()` - This callback is fired when banner is successfully created and will be presented to user. Note, that banner will not be presented to user if it's content is invalid and therefore callback will not be fired.
3. `onClosed()` - This callback is fired the very moment before banner is closed.

Please remember to remove banner listener to stop receiving callbacks:
```
Injector.removeBannerListener();
```

#### Triggers
In order to show banner immediately after certain event occuration, you can send your banners from our panel with a trigger value.<br>
First of all, calling `Injector.fetchBanners()` will fetch available banners and then SDK will cache valid ones.
This method is also called during SDK initialization, so use it only when you wish to overwrite current banners in SDK cache.<br>
`Injector.getBanners()` provides valid banners right from SDK cache.
Note, that exact same banners are being searched for eventual campaign triggers.<br>
`Injector.showBanner(TemplateBanner)` shows banner immediately with no `OnBannerListener.shouldPresent(TemplateBanner)` check.
Callbacks will be fired anyway.<br>

### Campaign pushes
`Injector.getPushes()` gets all available simple and silent pushes for this client.
`IDataApiCall` with parametrized list of SynerisePushResponse is returned to execute request.

### Silent Commands
Synerise SDK let you trigger methods from application without user interaction.
To use silent commands, send silent push from `app.synerise.com` mobile campaign with content:
```
"data": {
    "issuer": "Synerise",
    "message-type": "dynamic-content",
    "content-type": "silent-command",
    "content": {
        "class_name": "com.example.YourClassName",
        "method_name": "YourMethodName",
        "method_parameters": [
            {
                "class_name": "com.example.ParameterClassName",
                "value": someValue,
                "position": positionOfParameter
            },
            ...
        ]
    }
}
```
You can receive all the data in your `FirebaseMessagingService`
```
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    ...

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    ...

    Map<String, String> data = remoteMessage.getData();
    if (Injector.isSilentCommand(data)) {
        try {
            SilentCommand silentCommand = Injector.getSilentCommand(data);
            // your logic here
        } catch (ValidationException e) {
            // handle validation exception
        }
    }
    ...
```

### Silent SDK Commands
Synerise SDK has already implemented methods. For now the only one is requesting localization.

#### Silent Localization Command
To request localization let your Application class implement OnLocationUpdateListener and also provide
it to Synerise Builder.

```
public class App extends MultiDexApplication implements OnLocationUpdateListener {

    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

         Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
                                .notificationIcon(R.drawable.ic_notification_icon)
                                .locationUpdateRequired(this)
                                ...
                                .build();
    }

    @Override
    public void onLocationUpdateRequired() {
        // your logic here
    }
}
```

Now anytime you send below frame from `app.synerise.com` then your application will receive callback onLocationUpdateRequired where your app can get device location.
For example implementation check our sample application.
```
"data": {
    "issuer": "Synerise",
    "message-type": "dynamic-content",
    "content-type": "silent-command",
    "content": {
        "class_name": "com.synerise.sdk.injector.Injector",
        "method_name": "GET_LOCATION",
        "method_parameters": []
    }
}
```
Note that incoming silent push command will awake or launch you application in the background which causes in SDK initialization.<br>
It means, for instance, that `AppStartedEvent` will be sent and/or banners will get fetched.

### Walkthrough
Synerise SDK provides multiple functionalities within Walkthrough implementation.<br>
First of all, you are able to specify Walkthrough behavior the moment SDK initializes:
```
Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
        .notificationIcon(R.drawable.ic_notification_icon)
        .injectorAutomatic(true)
        .build();
```
Setting `.injectorAutomatic(true)` will fetch Walkthrough right away. Note, that Walkthrough will be presented the moment it gets loaded atop of your current Activity if it's id is different than previously presented.<br>
Please note that walkthrough is translucent activity, so your activity's `onStop()` method may not be called. Also walkthrough activity is launched with FLAG_ACTIVITY_NEW_TASK flag so you can handle your activity stack properly. To control this behavior please fetch your Walkthrough manually with `Injector.getWalkthrough()`.
This method cancels previous API request (if any) and then starts loading Walkthrough asynchronously. Also, it must be called after `Injector.setOnWalkthroughListener(OnWalkthroughListener)` to receive callbacks properly. See Optional callbacks section below.<br>
You can check if Walkthrough is already loaded with `Injector.isWalkthroughLoaded()` method, which returns true if Walkthrough is already loaded.<br>
Moreover, there is an enhanced method to check if Walkthrough is loaded. `Injector.isLoadedWalkthroughUnique()` verifies whether loaded Walkthrough is different than previously presented. Every time any Walkthrough is presented it's id is cached locally, therefore you can control your flow more precisely.
In summary, method will return true if loaded Walkthrough is different than previously presented, false otherwise or if Walkthrough is not loaded.<br>
Finally, `Injector.showWalkthrough()` shows Walkthrough if loaded. This method may be called wherever in your application as many times you want. It also returns true if Walkthrough was loaded, false otherwise.

#### Optional callbacks
When you choose to load and present Walkthrough manually, you may be interested in following callbacks:
```
Injector.setOnWalkthroughListener(new OnWalkthroughListener() {

            @Override
            public void onLoadingError(ApiError error) {
                error.printStackTrace();
                goDashboard();
            }

            @Override
            public void onLoaded() {
                if (Injector.isLoadedWalkthroughUnique()) {
                    Injector.showWalkthrough();
                } else {
                    goDashboard();
                }
            }

            @Override
            public void onPresented() {
                super.onPresented();
            }

            @Override
            public void onClosed() {
                super.onClosed();
            }
        });
```
Note, that all these methods are optional and overriding them is not required. These are also called when Walkthrough was loaded automatically.<br>
1. `onLoadingError(ApiError)` - This callback is fired the moment after Walkthrough failed to get initialized. This error may be caused by unsuccessful API response or invalid Walkthrough content.
ApiError instance is provided to check error cause.
2. `onLoaded()` - This callback is fired the moment after Walkthrough is initialized successfully. Remember, that Walkthrough will be showed automatically if `Synerise.Builder.injectorAutomatic(boolean)` was set to true and if it's id is different than previously presented.
3. `onPresented()` - This callback is fired when Walkthrough is successfully created and will be presented to user. Note, that Walkthrough will not be presented to user if it's content is invalid and therefore callback will not be fired.
4. `onClosed()` - This callback is fired the very moment before Walkthrough is closed.

Please remember to remove Walkthrough listener to stop receiving callbacks:
```
Injector.removeWalkthroughListener();
```

### Action callbacks
You can specify your custom action when user interacts with your banner or walkthrough.<br>
There are 2 main actions user may choose so far - Open url and Deep link.<br>
SDK makes it simple for you and by default handles those actions with browser opening or in case of deep linking - opening Activity.<br>
To make your Activity available for deep linking, please use the following pattern in your AndroidManifest:
```
       <activity
            android:name=".ui.linking.DeepLinkingActivity">
            <intent-filter>
                <action android:name="syne://test" />
                <category android:name="android.intent.category.DEFAULT" />
                 <data android:scheme="syne"
                    android:host="test" />
            </intent-filter>
        </activity>
```
Note that your action name must be same as your uri scheme and host.
You can also specify which activity you want to be fired after closing deep linking one with:
```
       <activity
            android:name=".ui.linking.DeepLinkingActivity"
            android:parentActivityName=".ui.linking.ParentDeepLinkingActivity">
            <intent-filter>
                <action android:name="syne://test" />
                <category android:name="android.intent.category.DEFAULT" />
                 <data android:scheme="syne"
                    android:host="test" />
            </intent-filter>
       </activity>
```
To send deeplink from `app.synerise.com` for your mobile campaign define parameter `Deep link` as
```
syne://test?param=val
```
Where syne and test are scheme and host provided in intent filter, param is parameter name and val is value for the parameter.<br>
To receive data sent by deep link use below code:
```
String data = intent.getDataString();
    if (data != null) {
        Uri uri = Uri.parse(data);
        val = uri.getQueryParameter("param");
    }
```

If you are not happy about default behavior, please implement your own behavior like:
```
public class App extends Application implements OnInjectorListener {

    @Override
    public void onCreate() {
        super.onCreate();

        Synerise.Builder.with(this, syneriseBusinessProfileApiKey, syneriseClientApiKey, appId)
                        .notificationIcon(R.drawable.ic_notification_icon)
                        .build();
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
```

1. `onOpenUrl(InjectorSource, String)` - Callback is fired when user interacts with URL action. Return true if Activity should be closed after action execution, false otherwise.
2. `onDeepLink(InjectorSource, String)` - Callback is fired when user interacts with DEEP_LINKING action. Return true if Activity should be closed after action execution, false otherwise.

## Author
Synerise, developer@synerise.com. If you need support please feel free to contact us.