# Changelog
All notable changes to this project will be documented in this file.
## [5.3.0] - 2023-04-30
### Changed
- Root Activity for inApp rendering is now retrieved after delay has ended. Changing from screen A to B before delay has ended will result in inApp showed on screen B.

## [5.2.1] - 2023-04-26
### Changed
- default value of `recommendationEventType` to `recommendation.seen` for backward compatibility

## [5.2.0] - 2023-04-21
### Added
- `RecommendationViewEvent` which can store multiple productsId in one event.
- `recommendationEventType` field to contentWidget in order tu change event `recommendation.seen` to `recommendation.view`
- update of ssl pins for all domains.

### Removed
- QUERY_ALL_PACKAGES permission.

## [5.1.0] - 2023-03-27
### Added
- new method for sign out `Client.signOut(ClientSignOutMode, Boolean)`. Old method is now deprecated.

## [4.10.3] - 2023-03-27
### Added
- new method for sign out `Client.signOut(ClientSignOutMode, Boolean)`. Old method is now deprecated.

## [5.0.1] - 2023-03-14
### Added
- new context passing mechanism based on androidx.startup library

### Changed
- `compileSdk` and `targetSdk` are now set to 33.
- upgrade of build.gradle, picasso, fresco, rxJava, okhttp, retrofit, room and work libraries.

## [4.11.3] - 2023-03-03
### Fixed
- ANR issue in InApps. Removed blocking mechanism.

## [4.10.2] - 2023-03-03
### Fixed
- ANR issue in InApps. Removed blocking mechanism.

## [4.10.1] - 2023-02-27
### Fixed
- ANR issue in `renderJinjaForceCheckSegment` method

## [4.11.0] - 2023-02-17
### Added
- queueing of session refreshing mechanism. You should see decrease in signInAnonymous and token refresh requests.

## [4.10.0] - 2023-01-27
### Changed
- `Client.signOut(mode)` is now asynchronous and returns IApiCall.

## [4.9.0] - 2023-01-06
### Added
- `OnNotificationListener` callbacks which triggers when notification is shown, clicked or dismissed.
- `NotificationInfo` object to share information about notification.

### Changed
- `User-Agent` header is now using package name instead of application's name.

## [4.8.3] - 2022-12-12
### Added
- `maxDefinitionUpdateIntervalLimit` to `InAppMessaggingSettings`

### Changed
- workManager initialization is done by default. For custom initialization please follow work manager documenation.

### Fixed
- `ActivityNotFoundException` is now handled when push is clicked. Doze mode is clearing application process and broadcastReceiver has no activity to be attached to.
- Improved timeout handling when getting inAppDefinitions

## [4.8.2] - 2022-12-01
### Fixed
- `IllegalArgumentException` in `PreMKeySecurityManager`
- issue with sending events
- timeout handler in checkSegment
- issue with triggering InApp by `push.click` event

### Changed
- client.applicationStarted is now collected only in foreground. You may see decrease in events quantity.

## [4.8.1] - 2022-10-19
### Changed
- Room version upgraded to 2.4.3

### Fixed
- `ArrayIndexOutOfBoundsException`

## [4.7.1] - 2022-10-19
### Fixed
- `ArrayIndexOutOfBoundsException`

## [4.8.0] - 2022-10-10
### Changed
- `JobIntentService` is no longer used in sdk.
- Sending events is based on workManager

## [4.7.0] - 2022-10-03
### Added
- We added in-app messaging module. In-app messages are designed to enhance the user experience in your mobile application without ever being intrusive. Customizable layouts can personalize content and style to create the perfect in-app message to fit your brand. Using Synerise segmentation, you can target and engage the most relevant audience.

### Removed
- Banner methods such as: `fetchBanners`, `getBanners`, `showBanner`.

## [4.6.2] - 2022-07-14
### Fixed
- `push.click` issue affecting only React-Native users.

## [4.6.1] - 2022-07-07
### Fixed
- IllegalArgumentException bad-base 64

## [4.6.0] - 2022-06-28
### Added
- new `Client.signOut(mode)` method with two modes: `SIGN_OUT` and `SIGN_OUT_WITH_SESSION_DESTROY`.
 They both notify the backend that the client is signed out, clear the client session on the device with a JWT Token, and work similiarly to `Client.signOut()`.
 Additionaly, `SIGN_OUT_WITH_SESSION_DESTROY` clears the anonymous session and regenerates the client UUID.

## [4.5.0] - 2022-06-21
### Added
- new properties in ContentWidget: `subtitile`, `identifier` and `loyaltyPoints`. These properties are added to `ContentWidgetBasicProductItemLayout`
and are provided with customization properties such as typeface, size, color, margins and additional label for loyalty points as suffix text. Appearance
of the properties is set in `ContentWidgetBasicProductItemLayout` and their data in `ContentWidgetRecommendationDataModel`. All properties are optional
and will not be displayed if they were not set in the data model.

## [4.4.0] - 2022-05-05
### Added
- migration to encrypted tokens (not affecting users)

### Changed
- events types to "custom". Events now can be distinguished via `action` parameter
- `authenticate`, `signInAnonymous`, `authenticateConditionally`, `refreshToken` methods are now moved to "v3" endpoints due to encrypted token migration

### Fixed
- cast exception in `NotificationOpenedReceiver`

## [4.3.1] - 2022-04-26
### Fixed
- issue with dismissBroadcastReceiver ( affecting only react-native users )

## [4.3.0] - 2022-04-13
### Added
- additional parameters to `RecommendationRequestBody` object. More filters for getting recommendations.
- upgraded picasso library to 2.71828 version

### Fixed
- memory leaks in AutoTracking
## [4.2.0] - 2022-03-08
### Added
- `setItemsIds` method to `RecommendationRequestBody` in order to get recommendations for multiple products

## [4.1.0] - 2022-02-28
### Added
- `ACTION_VIEW` for Intent inside deep link campaigns
- support for universal links to be send as DEEP_LINK

## [4.0.1] - 2022-02-10
### Added
- support for targetSdkVersion 31
- new mechanism for collecting statistics for android 12 and above called Notification Reverse Activity Trampoline

## [3.8.11] - 2022-02-10
### Added
- support for targetSdkVersion 31
- new mechanism for collecting statistics for android 12 and above called Notification Reverse Activity Trampoline

## [4.0.0] - 2022-01-04
### Changed
- `notificationIconColor` and `notificationIcon` should be set via meta-data in manifest and are not longer set in Synerise.Builder`
for more information please check documentation in push-notifications section

### Removed
- `notificationIcon` and `notificationIconColor` from Synerise.Builder
- `notificationChannelName` and `notificationChannelId`from Synerise.Builder due to end of deprecation time

## [3.8.10] - 2021-12-06
### Added
- IMPROVEMENTS IN CONTENT WIDGET:
- `badge` as a text shown on the image in `ContentWidget`
- `itemLabel` to show additional information about product in `ContentWidget`
- percentageLabel to show the discount percentage in `ContentWidget`
- image scalling in `ContentWidgetBasicProductItemLayout`
## [3.8.9] - 2021-11-05
### Added
- `errorCode` field to `ApiErrorBody`

## [3.8.8] - 2021-10-04
### Fixed
- `SecurityException` while retrieving NetworkConnectionType
- `NullPointerException` in `NotificationClickReceiver`and `NotificationDismissReceiver`

## [3.8.7] - 2021-09-27
### Added
- `USER_ACCOUNT_DELETED` to `ClientSessionEndReason` enum.

### Changed
- `gson - flatten` library deleted from dependencies.

## [3.8.6] - 2021-09-13
### Added
- `deviceId` field to `RegisterClient` class (Client registration process)

### Fixed
- `Client.signInConditionally` error with saving uuid
- `Client.authenticateConditionally` error with saving uuid

## [3.8.5] - 2021-08-19
### Fixed
- `Client.signInConditionally` error part 2

## [3.8.4] - 2021-08-19
### Fixed
- `Client.signInConditionally` error

## [3.8.3] - 2021-08-16
### Fixed
- tags changed to List of HashMap

## [3.8.2] - 2021-08-13
### Added
- methods to activate and deactivate promotions in a batch

### Changed
- updated `Promotion` object

## [3.8.1] - 2021-08-05
### Added
- Callback `CLIENT_REJECTED` for `OnClientStateChangeListener`

### Fixed
- issue with description in push notifications when image is not showing

## [3.8.0] - 2021-07-30
### Added
- new methods for authentication

### Fixed
- uuid autobackup issue
- picasso singleton clash

## [3.7.6] - 2021-07-05
### Fixed
- IOException when doing a lot of calls to Synerise

## [3.7.5] - 2021-05-14
### Added
- `messagingServiceType` method to Synerise.Builder. Distinguish between hms and gms
- `rxJavaErrorHandlingEnabled` method to Synerise.Builder
## [3.7.3] - 2021-04-29
### Changed
- Upgraded retrofit and gson libraries

### Fixed
- `EventService` crash when getting back from DozeMode

## [3.7.2] - 2021-04-13
### Fixed
- `itemId` not initialized in Recommendation class

## [3.7.1] - 2021-03-31
### Added
- `getScreenView()` method
- `requestEmailChange` method

### Changed
- 'requestEmailChangeByFacebook' is now deprecated
- `requestEmailChange(String email, String password, @Nullable String uuid)` is now deprecated

## [3.7.0] - 2021-02-23
### Added
- `OnRecommendationModelMapper` callback responsible for mapping objects
- `ContentWidgetRecommendationOptions` new object for configuration of ContentWidget
- `ContentWidgetRecommendationDataModel` model needed for displaying items in widget

### Changed
- `ContentWidget` is now using recommendation v2
- Images in ContentWidget are now scalling as centerInside

## [3.6.22] - 2021-02-08
### Added
- `SyneriseListener` as a callback. Called when Synerise sdk initialization is completed.
- 'hostApplicationType' to Synerise builder.
- Sending app Started event after first change of setting: sdk.enable
### Fixed
- RuntimeException on TimeChangedReceiver

## [3.6.21] - 2021-01-21
### Added
- `backgroundRestricted` field to appStarted event
### Changed
- ACCESS FINE LOCATION is no longer required by sdk

### Fixed
- System time change during session will not affect events time
- Currency issues on ContentWidget
- autotracking fix: Stop counting fragments/activities with no view

## [3.6.20] - 2020-12-04
### Changed
- Upgrade of FCM dependencies
- Upgrade of AGP
- Added retry mechanism on image downloading in push notifications

## [3.6.19] - 2020-11-23
### Added
- `WalkthroughResponse` returned in Walkthrough callback `onLoaded`
- `deleteAccount` method
- `isBackendTimeSyncRequired` property in `TrackerSettings`

### Changed
- Information about device root is moved to applicationStarted event

## [3.6.18] - 2020-10-08
### Changed
- `Injector.decryptPushPayload` is now throwing `DecryptionException`
- `systemPushConsent` state depends on available notificationChannels for Oreo + versions

### Deleted
- `Chat` module

## [3.6.17] - 2020-09-24
### Changed
- minimum android sdk version to 21
- memory optimalisation in autoTracking module

## [3.6.16] - 2020-09-08
### Added
- push encryption mechanism
- `Injector.decryptPushPayload` and `Injector.isPushEncrypted` methods.
- `setEncryption` method in `NotificationSettings`

### Changed
- `client.applicationCrashed` in `SyneriseNotification` into CustomEvent `push.imageTimeout`

## [3.6.15] - 2020-08-24
### Added
- time synchronization with server
- `time` parameter to events

## [3.6.14] - 2020-07-28
### Changed
- timeout to 60s

## [3.6.13] - 2020-07-07
### Added
- `authenticateByOAuthIfRegistered` and  `deleteAccountByOAuth` method in Client module

### Changed
- `authenticateByFacebookRegistered` to `authenticateByFacebookIfRegistered`

## [3.6.12] - 2020-06-30
### Added
- maintaining session when changing apiKey within business profile
- `shouldDestroySessionOnApiKeyChange` in Settings

## [3.6.11] - 2020-06-09
### Fixed
- nullPointerException on Android 5.0
- flattenException while deserialization

## [3.6.10] - 2020-05-20
### Fixed
- mixed cases in class names after obfuscation

## [3.6.9] - 2020-05-12
### Added
- `refreshToken` method
- `getCustomId` method to `Token`

### Fixed
- invalidCastException in `Campaign`

## [3.6.8] - 2020-04-29
### Fixed
- Token issues

## [3.6.7] - 2020-04-22
### Changed
- `getDocument` and `getDocuments` are now returning Object and a List of Objects

## [3.6.6] - 2020-04-15
### Changed
- `excludedClasses` is now also excluding screen-visited event

## [3.6.5] - 2020-04-06
### Added
- dontusemixedcaseclassnames to proguard file

### Fixed
- `Settings` change to non static fields
- JavaDoc not showing issue
- BuildConfig is now visible

## [3.6.4] - 2020-03-30
### Added
- `Client.destroySession` method
- AutoTracking for click's on actionButtons in Push Notifications
- `Client.regenerateUuid` method with customerIdentifier as a argument

### Fixed
- issues with autoBackup

### Removed
- deprecated methods in Promotions Module

## [3.6.3] - 2020-03-16
### Fixed
- problem with facebook login
- pre marshmallow errors on start

## [3.6.2] - 2020-03-09
### Fixed
- autotracking collecting clicks on ViewGroup instances

## [3.6.1] - 2020-02-28
### Changed
- autotracking 2.0 is now working
- local Walkthrough is cleared while changing apiKey
- android-aspectjx and synerise-plugin plugin is no longer needed

## [3.6.0] - 2020-02-11
### Changed
- autotracking is not working (KNOWN ISSUE)
- crashHandler is now sending whole stacktrace
- Security related changes and refactoring

### Removed
- setting `tags` in Client

## [3.5.9] - 2020-02-11
### Changed
- aspectJ config for androidX

## [3.5.8] - 2020-01-23
### Added
- `customId` to OAuthPayload
- reinitialization mechanism

### Removed
- `CustomClientAuthConfig` from Synerise

## [3.5.7] - 2019-12-17
### Fixed
- IllegalArgumentException x must be < bitmap.width
- Added encoding to query parameters in PromotionsApi

## [3.5.6] - 2019-12-16
### Added
- 'priority' field to `Promotion`
- `priority` as a sorting parameter to `PromotionApiQuery`

### Fixed
- removing events mechanism

## [3.5.5] - 2019-12-03
### Added
- `InjectorActionHandler`

## [3.5.4] - 2019-11-27
### Fixed
- `itemPriceColor` showing wrong color
- `type` in events: `CancelledPushEvent`, `ClickedPushEvent`, `ViewedPushEvent`

## [3.5.3] - 2019-11-13
### Fixed
- SecurityException on JobIntentService

## [3.5.2] - 2019-10-18
### Added
- `itemRegularPriceColor`, `itemRegularPriceSize`, `priceGroupSeparator` and `priceDecimalSeparator` to ContentWidgetBasicProductItemLayout

## [3.5.1] - 2019-10-16
### Added
- `getDocuments()` method to get documents by schema

## [3.5.0] - 2019-10-08
### Added
- `crashHandlingEnabled()` method to Synerise init (1).

### Fixed
- migration to androidX

1. [Crash Handler documentation](https://help.synerise.com/developers/android-sdk/installation-and-configuration/)

## [3.4.9] - 2019-09-30
### Fixed
- `SecurityException` in JobIntentService

## [3.4.8] - 2019-09-27
### Added
- `SyneriseExceptionHandler` to get information about crashes
- `OnClientStateChangeListener` to listen for signIn/SignOut changes

### Fixed
- `isSignedIn` method is taking expirationTime into account

## [3.4.7] - 2019-09-20
### Added
- `Chat` module to communicate with users
- Possibility to sign out remotely

## [3.4.6] - 2019-09-02
### Added
- `OTHER` as a sex value

### Fixed
- AssertionError when parsing `Event` toString

## [3.4.5] - 2019-08-19
### Added
- Like/Dislike imageButton to `ContentWidget`
- `timeFrom` is not optional in `ClientEventsQuery`

## [3.4.4] - 2019-08-08
### Added
- `app-version` as a query parameter in getWalkthrough
- Sale price as a additional feature to `ContentWidget`
- `Client.getEvents` method to retrieve events for a client
- Event's label max length

### Changed
- `injectorAutomatic`, `locationAutomatic`, `maxBatchSize`, `minBatchSize` moved to `Settings` module

## [3.4.3] - 2019-07-31
### Added
- flatten library
- `excludedClasses` to exclude views from autotracking

### Fixed
- bitmap IllegalArgumentException fixed

## [3.4.2] - 2019-07-22
### Added
- `getDocument` method
- `recognizeAnonymus` method to Client module
- `agreement` flag to registerForPush method
- `PromotionsApiQuery` model to create payload for getPromotions methods

### Changed
- `productId` in `ContentWidgetOptions` moved to HashMap
- `getPromotions` are now replaced with getPromotions using `PromotionsApiQuery`
- `getPromotions` using arguments are now deprecated

## [3.4.1] - 2019-07-04
### Added
- SimplePush test to developer section

## [3.4.0] - 2019-06-29
### Added
- `Content` module to SDK
- `ContentWidget` widget with Slider and Grid Layouts

## [3.3.22] - 2019-06-25
### Added
- `cellCarrier`, `cellCountry`, `cellType`, `networkType`, `networkCountry` and `cellRoaming` attributes to `AppStartedEvent`
- detector for smartphone/tablet detection

### Changed
- `facebookSdk` updated to version 4.41.0

### Removed
- `TransactionEvent`, `CompletedTransactionEvent` and `CancelledTransactionEvent` classes

## [3.3.21] - 2019-06-12
### Changed
- `eventTime` is not send via event anymore

## [3.3.20] - 2019-06-11
### Fixed
- NullPointerException when adding event to storage

## [3.3.19] - 2019-06-10
### Fixed
- MandatoryUpdate will not fire push notification any more

## [3.3.18] - 2019-06-03
### Added
 - `setMinTokenRefreshInterval` method in `settings.sdk` to manage refresh interval

## [3.3.17] - 2019-05-22
### Fixed
- Prevented from file descriptor leakage

## [3.3.16] - 2019-05-15
### Added
- `GetPromotionByCode` and `GetPromotionByUuid` methods
- push.click event

## [3.3.15] - 2019-04-29
### Added
- High Priority notification channel for Android Oreo+ `NotificationHighPriorityChannelId` and `NotificationHighPriorityChannelName`
- `Settings` module
- separate event for autotracking

### Fixed
- BackStack is not overrided when push notification comes up
- BackStack is now working with deeplinking

### Changed
- `time` from Event moved to `parameters` as `sourceTime`
- `notificationChannelName` and `NotificationChannelId` changed to `NotificationDefaultChannelName` and `NotificationDefaultChannelId`

## [3.3.14] - 2019-04-11
### Added
- new synerise plugin version which requires another apply plugin: 'android-aspectjx'
- `usesCleartextTraffic` flag as true in manifest to support loading images on Android 9.0 +

### Fixed
- Retrieving app state when starting app from notification banner.
- Upgraded version of fresco.

## [3.3.13] - 2019-04-03
### Added
- `isNotificationLaunched` flag to stop asking for banners and walkthrough when notification is launched
- `notificationIconColor` method to define color of notification icon
- `ProductEvent` and `RecommendationEvent` classes to create specific events
- `ProductViewEvent`, `RecommendationSeenEvent`, `RecommendationClickEvent` events

### Fixed
- notification icon were not displayed properly

## [3.3.12] - 2019-04-01
### Changed
- `baseUrl` to "https://api.snrapi.com/"

## [3.3.11] - 2019-03-27
### Added
- `authId` optional parameter added to `Client.authenticateByOAuth`, `Client.authenticateByFacebookRegistered` and `Client.authenticateByFacebook` methods

### Changed
- `TargetSdkVersion` upgraded to 28
- `CompileSdkVersion` upgraded to 28

### Removed
- `Push.RECEIVED` event will not be tracked

## [3.3.10] - 2019-03-04
### Added
- `ProductViewEvent` event type
- `RecommendationClickEvent` event type
- `RecommendationSeenEvent` event type

### Changed
- `Promotions.getPromotions()` parameters are now optional (nullable)

### Fixed
- Notifications content text on some devices were not displayed properly

### Removed
- `Client.confirmEmailChangeByFacebook()` method, use `Client.confirmEmailChange()` instead
- `_` character validation when adding custom `Attributes`

## [3.3.9] - 2019-02-15
### Added
- `Client.requestEmailChangeByFacebook()` method
- `Client.confirmEmailChangeByFacebook()` method

### Changed
- Internal UUID mechanism in case of account deletion

### Fixed
- `OnErrorNotImplementedException` error will not crash your app anymore

## [3.3.8] - 2019-02-07
### Added
- `Agreements` fields getters

## [3.3.7] - 2019-02-07
### Added
- Optional `smsAgreement` parameter to `Client.confirmPhoneUpdate()` method

## [3.3.6] - 2019-02-05
### Added
- `Client.changeApiKey()` method to dynamically change API Key

### Removed
- Optional `Agreements` and `Attributes` parameters from `Client.authenticateByFacebookRegistered()` method

## [3.3.5] - 2019-02-03
### Added
- `Client.regenerateUuid()` method to generate new uuid for anonymous client.
- `Client.authenticateByOAuth()` method to sign in with already prepared OAuth authorization.
- Optional `Agreements` and `Attributes` parameters to `Client.authenticateByFacebook()` and `Client.authenticateByFacebookRegistered()` methods

### Changed
- `Firebase Core` version update (to 16.0.7)
- Password moved from `Client.confirmEmailChange()` to `Client.requestEmailChange()`

### Fixed
- Anonymous token will not overwrite authenticated one anymore, during some possible cases.

## [3.3.4] - 2019-01-28
### Changed
- `AspectJ` version update (to 1.9.2)
- `Synerise Gradle Plugin` version update (to 3.0.3)

### Fixed
- `Client.deleteAccountByFacebook()` from now signs out client properly

## [3.3.3] - 2019-01-20
### Added
- `Client.requestEmailChange()` method
- `Client.confirmEmailChange()` method
- `Client.deleteAccountByFacebook()` method
- `Client.authenticateByFacebookRegistered()` method

### Changed
- Google Services updated to 4.2.0
- `Client.getToken()` from now returns Token instance instead of JWT String
- Auto-tracking labels

### Fixed
- `Synerise Simple Push` containing long message and image did not display text properly

### Removed
- `timestamp` field from `ApiErrorBody` class

## [3.3.2] - 2019-01-01
### Added
- `Synerise.locationAutomatic(boolean)` method to obtain user location and send location event automatically.

### Changed
- RxJava2 version updated to 2.2.4

### Fixed
- `UndeliverableException` exceptions won't be thrown (via setting error handler with `RxJavaPlugins`)

## [3.3.1] - 2018-12-18
### Added
- `Synerise.notificationChannelId(String)` method
- `Client.confirmAccount(String)` method

### Changed
- Unknown notification priority is now treated with default importance
- `Client.deleteAccount()` from now requires client's password

### Fixed
- `Promotions.getPromotions` method from now handles empty lists

### Removed
- `Client.registerAccountWithoutActivation` method. Now you can use `Client.registerAccount` and control behaviour from the backend

## [3.3.0] - 2018-11-19
### Added
- `Promotions` module
- `CacheManager` to obtain cached API models

### Changed
- Some `Promotion` entity fields types
- `Client.changePassword()` now requires old client's password
- `Profile` methods were distributed between `Client` and `Promotions`
- `Client.getPromotions` -> `Promotions.getPromotions`
- `Client.getPromotion` -> `Promotions.getPromotion`
- `Client.activatePromotionBy` -> `Promotions.activatePromotion()`
- `Client.deactivatePromotionBy` -> `Promotions.deactivatePromotion()`
- `Client.getOrAssignVoucher` -> `Promotions.getOrAssignVoucher`
- `Client.assignVoucherCode` -> `Promotions.assignVoucher`
- `Client.getAssignedVoucherCodes` -> `Promotions.getAssignedVoucherCodes`
- `Profile.registerClientByEmail` -> `Client.registerAccount`
- `Profile.registerClientByEmailWithoutActivation` -> `Client.registerAccountWithoutActivation`
- `Profile.registerForPush` -> `Client.registerForPush`
- `Profile.activateClient` -> `Client.activateAccount`
- `Profile.requestPasswordReset` -> `Client.requestPasswordReset`
- `Profile.confirmResetPassword` -> `Client.confirmPasswordReset`
- Major parts of authorization module

### Removed
- Overall validation
- `Client.getAnalytics` method
- `Synerise.clientRefresh(boolean)` method
- `Synerise.poolUuid(String)` method
- `Client.createAuthToken` method
- `Profile.getClient` method
- `Profile.createClient` method
- `Profile.registerClientByPhone` method
- `Profile.confirmPhoneRegistration` method
- `Profile.updateClient` method
- `Profile.deleteClient` method
- `Profile.getToken` method
- `Profile.getPromotions` method
- `Profile.getPromotionsBy` method
- `Profile.redeemPromotionBy` method
- `Profile.getOrAssignVoucher` method
- `Profile.getClientVoucherCodes` method
- `Profile.assignVoucherCode` method

## [3.2.14] - 2018-11-12
### Removed
- Overall SDK validation

## [3.2.13] - 2018-11-07
### Fixed
- `JsonParseException` caused of `birthDate` being a `Date` field type (now is `String`)

## [3.2.12] - 2018-10-31
### Changed
- `ApiErrorCause` `path` attribute changed to `field`
- API version incremented to `4.3`

### Fixed
- `SQLiteStorage.deleteAppStarted` NullPointerException occurring only on specific devices
- `EventService.onHandleWork` RuntimeException occurring on devices running Android 8.1 and later

## [3.2.11] - 2018-10-21
### Added
- Unique `code` to each `ApiErrorCause`
- `Client.authenticateByFacebook()` method to obtain client's auth token with Facebook one

### Fixed
- `Event` local and stream class incompatibility
- `MobileInfoInterceptor.intercept(Chain)` error causing `OutOfMemoryError` on specific devices 

### Removed
- Obsolete `GetOrAssignPayload` model class

## [3.2.10] - 2018-10-14
### Added
- `ApiError` public fields like `path`, `message`, `status`, `error`, `timestamp` and `errors` list
- `Object` value as an argument to `TrackerParams` event optional parameters

### Changed
- Client's personal information (`AccountInformation`) entity
- Deep links configuration within Manifest

### Fixed
- Auto-tracking CompoundButton spannable string exception

## [3.2.9] - 2018-10-08
### Changed
- Profile promotions response structure

### Removed
- Overall password validation

## [3.2.8] - 2018-09-28
### Added
- Client method to obtain external client's authorization token

### Fixed
- Profile's `getClientVoucherCodes()` entity
- Profile's `getPromotionsBy(parameter)` entity
- Client's `getAssignedVoucherCodes()` entity

### Changed
- Firebase version incrementation (17.3.2)

## [3.2.7] - 2018-09-26
### Added
- Promotion deactivation by uuid and code
- Profile promotions take `limit` as a parameter to fetch your data

### Changed
- Get client promotions with new status and presentation options
- Birth date and phone number are now nullable within Client and Profile's entities
- `Client.getAssignedVoucherCodes()` return type is now `ClientVoucherCodesResponse`
- Push campaign  key `push.receiveInBackground` changed to `push.received`

### Fixed
- Android O activity's screen orientation lock
- Get client voucher codes endpoint url

## [3.2.6] - 2018-08-31
### Added
- Silent command pushes
- Change client's password optional validation

### Changed
- Deep links are now based on URI scheme which allows to pass extra query data

### Fixed
- Synerise notification channels

## [3.2.5] - 2018-08-30
### Added
- Assign voucher Profile and Client API methods
- Get assigned vouchers Profile and Client API methods
- Get or assign voucher Profile and Client API methods
- Location callback

### Changed
- New auto-tracking labels and actions

## [3.2.4] - 2018-08-16
### Added
- New Profile activate account feature
- New Client delete account feature
- New Client change password feature
- New Client request phone update feature
- New Client confirm phone update feature

### Changed
- Major documentation update
- Tracker events nomenclature

## [3.2.3] - 2018-08-01
### Added
- Synerise push messages type detection

### Changed
- Tracker and Injector debug modes are now merged with Synerise debug mode
- Firebase version incrementation (17.1.0)
- Google Play Services version incrementation (4.0.0)
- Gradle version incrementation (3.3.0-alpha03)
- Push message type is now validated with it's content type, not message type

### Removed
- FirebaseInstanceIDService implementation as it is deprecated with new Firebase (17.1.0)

### Fixed
- Standardize event source type
- Password validation

## [3.2.2] - 2018-06-26
### Added
- Synerise Promotions
- Synerise Vouchers activation
- From now it is possible to define custom base url to apply across whole SDK

### Changed
- Javadoc update
- Compile and target sdk updated to 27

## [3.2.1] - 2018-06-11
### Added
- Auto-refreshing Client token in order to sustain it's session
- Setting custom client's email on demand
- Client's Analytics metrics
- Intent extras are now extractable from Synerise Simple Notification

### Removed
- Obsolete verification of internal modules initialization

## [3.2.0] - 2018-05-29
### Added
- SMS sign in and sign up support
- RxJava2 features to extract from IApiCall
- Fetch banners on demand
- Fetch push messages on demand
- Banner is showed after available images get loaded

### Removed
- Dagger2 for Xamarin compatibility
- Validation for internal modules initialization
- `onBindViewHolder()` aspect tracking
- Walkthrough transformations

### Changed
- Banners are now displayed after all available images get loaded
- Internal keys, refactor and optimizations
- Custom client id is now custom identifier

### Fixed
- Few minor internal bugs

## [3.1.9] - 2018-05-08
### Added
- Tracker minimum batch size configuration
- Tracker maximum batch size configuration
- Tracker auto flush timeout configuration
- Triggers for campaign banners

### Changed
- OnBannerListener `shouldPresent(Map)` method argument changed to `shouldPresent(TemplateBanner)`

### Fixed
- Custom indicators layout

## [3.1.8] - 2018-04-24
### Added
- Banner optional callbacks
- Walkthrough optional callbacks
- Mandatory Update and First Run Message support
- Reminder about confirming email after sign up
- New parameters to AppStartedEvent
- New placeholders while loading images

### Removed
- Welcome screen

### Changed
- Gradle version to 3.1.1
- New uuid mechanism

### Fixed
- Walkthrough pager loop
- Android O importance bug
- Keyboard covering input

## [3.1.7] - 2018-04-08
### Added
- Signing client in sends also client's UUID
- Models with email and/or password validation
- Sending VisitScreen event from aspect
- Support for Tracker and Android O *Background Execution Limits*
- Common templates for walkthrough, banners and welcome screen

### Changed
- Major SDK core optimization
- Major Injector rebuild

### Fixed
- OkHttp header encoding
- Event duplication
- Nullable serialized models
- Synalter multiple modifications of same view

## [3.1.6-RC5] - 2018-03-22
### Fixed
- Walkthrough and WelcomeScreen are now retrieved from API

## [3.1.6-RC4] - 2018-03-22
### Added
- Client.logIn() sign in with SDK generated UUID

### Changed
- Sample app fix for signIn() and getClient()

### Fixed
- Get client profile model

## [3.1.6-RC3] - 2018-03-20
### Added
- Retrieve client's UUID from Client

### Fixed
- Get client profile model

## [3.1.6-RC2] - 2018-03-09
### Added
- Unit and Instrumental tests

### Removed
- Injector base url configuration

### Changed
- Updated Support, MultiDex, RxAndroid, Dagger, OkHttp versions

### Fixed
- Walkthrough page transformations fix
- TextInputLayout hints for Oreo

## [3.1.6-RC1] - 2018-02-13
### Added
- Synalter - powerful tool to modify content

### Changed
- README is now up to date
- Sample app is now up to date
- Dagger2, RxJava2 and OkHttp versions update
- Walkthrough does not require to be called on demand

### Fixed
- Walkthrough page transformations

### Removed
- Onboarding
- Welcome Screen
- Redundant debug modes in *Profile* and *Client*

## [3.1.5-RC5] - 2018-01-23
### Changed
- Walkthrough

### Fixed
- AppStartedEvent is now unique and queued
- Removing duplicated events

## [3.1.5-RC4] - 2018-01-22
### Fixed
- Removed redundant colors

## [3.1.5-RC3] - 2018-01-19
### Changed
- README
- Code cleanup

### Fixed
- Detecting duplicated events

## [3.1.5-RC2] - 2018-01-15
### Added
- Changelog
- Tracking views interactions with AspectJ
- Removing duplicated events
- MultiDex in sample/build.gradle
- TrackerViewActivity to test tracking views interactions

### Changed
- Application class is now MultiDexApplication
- BaseViewAspect class has now dedicated overloaded methods

### Fixed
- Removed underscores in `ProfileFeaturesActivity#createClient()` attributes (sample app)