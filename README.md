# Synerise Android SDK - User documentation #

## Event Tracker ##

You can log events from your mobile app to Synerise platform with Tracker class.
First of all, you need to initialize Tracker with `init` method and provide `Api Key`, `Application name` and `Application instance`.
Init method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.

### Api Key ###
To get `Api Key` sign in to your Synerise account and go to https://app.synerise.com/api/.
Please generate new `Api Key` for `Business Profile` Audience.

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

### Tracker ###

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

#### In your /values strings file (e.g. `string.xml`): ####

```
<resources>
    <string name="app_name" translatable="false">Your GREAT application name</string>
    <string name="synerise_api_key" translatable="false">A75DA38F-A2E9-5A25-2884-38AD12B98FAA</string> <!-- replace with valid api key -->
</resources>
```

## Events ###

### Session Events ###
Group of events related to user's session.

#### LoggedInEvent ####
Record a 'client logged in' event.

#### LoggedOutEvent ####
Record a 'client logged out' event.


### Products Events ###
Group of events related to products and cart.

#### AddedToFavoritesEvent ####
Record a 'client added product to favorites' event.

#### AddedToCartEvent ####
Record a 'client added product to cart' event.

#### RemovedFromCartEvent ####
Record a 'client removed product from cart' event.


### Transaction Events ###
Group of events related to user's transactions.

#### CancelledTransactionEvent ####
Record a 'client cancelled transaction' event.

#### CompletedTransactionEvent ####
Record a 'client completed transaction' event.


### Other Events ###
Group of uncategorized events related to user's location and actions.

#### AppearedInLocationEvent ####
Record a 'client appeared in location' event.

#### HitTimerEvent ###
Record a 'client hit timer' event. This could be used for profiling or activity time monitoring - you can send "hit timer" when your client starts doing something and send it once again when finishes, but this time with different time signature. Then you can use our analytics engine to measure e.g. average activity time.

#### SearchedEvent ###
Record a 'client searched' event.

#### SharedEvent ###
Record a 'client shared' event.

#### VisitedScreenEvent ###
Record a 'client visited screen' event.


### Custom Event ###
This is the only event which requires `action` field. Log your custom data with TrackerParams class.


### Tracker.setDebugMode(isDebugModeEnabled) ###
This method enables/disables console logs from Tracker SDK.
It is not recommended to use debug mode in release version of your application.



## Client ###

First of all, you need to initialize Client with `init` method and provide `Api Key`, `Application name` and `Application instance`.
Init method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.
In Client SDK you can also provide you custom `Authorization Configuration`. At this moment, configuration handles `Base URL` changes.

### Client.logIn(email, password, deviceId) ###
Log in a client in order to obtain the JWT token, which could be used in subsequent requests. The token is valid for 1 hour.
This SDK will refresh token before each call if it is expiring (but not expired).
Method requires valid and non-null email and password. Device ID is optional. // todo: what is deviceId
Method returns `IApiCall` to execute request.

### Client.getAccount() ###
Use this method to get client's account information.
This method returns `IDataApiCall` with parametrized `AccountInformation` object to execute request.

### Client.updateAccount(accountInformation) ###
Use this method to update client's account information.
This method requires `AccountInformation` Builder Pattern object with client's account information. Not provided fields are not modified.
Method returns `IApiCall` to execute request.

### Client.setDebugMode(isDebugModeEnabled) ###
This method enables/disables console logs from Client SDK.
It is not recommended to use debug mode in release version of your application.



## Profile ###

First of all, you need to initialize Profile with `init` method and provide `Api Key`, `Application name` and `Application instance`.
Init method can be called only once during whole application lifecycle, so it is recommended to call this method in your `Application` class.

### Profile.createClient(createClient) ###
Create a new client record if no identifier has been assigned for him before in Synerise.
This method requires `CreateClient` Builder Pattern object with client's optional data. Not provided fields are not modified.
Method returns IApiCall object to execute request.

### Profile.registerClient(registerClient) ###
Register new Client with email, password and optional data.
This method requires `RegisterClient` Builder Pattern object with client's email, password and optional data. Not provided fields are not modified.
Method returns IApiCall object to execute request.

### Profile.updateClient(updateClient) ###
Update client with ID and optional data.
This method requires `UpdateClient` Builder Pattern object with client's optional data. Not provided fields are not modified.
Method returns IApiCall object to execute request.

### Profile.deleteClient(id) ###
Delete client with ID.
This method requires client's id.
Method returns IApiCall object to execute request.

### Profile.requestPasswordReset(email) ###
Request client's password reset with email. Client will receive a token on provided email address in order to use Profile.confirmResetPassword(password, token).
This method requires client's email.
Method returns IApiCall object to execute request.

### Profile.confirmResetPassword(password, token) ###
Confirm client's password reset with new password and token provided by Profile.requestPasswordReset(email).
This method requires client's password and confirmation token sent on email address.
Method returns IApiCall object to execute request.

### Profile.setDebugMode(isDebugModeEnabled) ###
This method enables/disables console logs from Profile SDK.
It is not recommended to use debug mode in release version of your application.