# Changelog
All notable changes to this project will be documented in this file.

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