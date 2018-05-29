# Changelog
All notable changes to this project will be documented in this file.

## [3.2.0] - 2018-05-29
### Added
- SMS sign in and sign up support
- RxJava2 features to extract from IApiCall
- Fetch banners on demand
- Fetch push messages on demand
- Banner is showed after available images get loaded

### Removed
- Dagger2 for Xamarin compability
- Validation for internal modules initialization
- `onBindViewHolder()` aspect tracking
- Walkthrough transformations

### Changed
- Banners are now displayed after all available images get loaded
- Internal keys, refactors and optimizations
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