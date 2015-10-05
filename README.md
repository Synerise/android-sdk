# Synerise Android SDK
Synerise SDK for Andoid

#Documentation with examples
http://synerise.github.io/android-sdk

#Initialize Synerise-SDK
Add to your AndroidManifest.xml Synerise Api Key, uses-permission, service declarations and diffrent code connected  with push messages and  beacon tracking.

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.com" >

    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.BLUETOOTH_PRIVILEGED" />
    <uses-permission android:name="android.permission.CALL_PHONE" />
     <!--sdk w aktualnej wersji wymaga dostêpu do konta u¿ytkownika-->
    <uses-permission android:name="android.permission.GET_ACCOUNTS"/>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>

    <supports-screens
        android:anyDensity="true"
        android:xlargeScreens="true"
        android:largeScreens="true"
        android:normalScreens="true"
        android:smallScreens="true" />


    <permission android:name="com.example.app.permission.C2D_MESSAGE"
        android:protectionLevel="signature" />

    <uses-permission android:name="com.example.app.permission.C2D_MESSAGE"/>

    <application
        android:allowBackup="true"
        android:hardwareAccelerated="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@android:style/Theme.Black.NoTitleBar"
        <activity
            android:name=".activity.MainActivity"
            android:label="@string/app_name"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
       
        ... 
       
        <receiver
            android:name="com.synerise.sdk.gcm.SyneriseGcmReceiver"
            android:permission="com.google.android.c2dm.permission.SEND" >
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                <category android:name="com.example.app" />
            </intent-filter>
        </receiver>
        <receiver android:name="com.synerise.sdk.ServiceStarter">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED"/>
            </intent-filter>
        </receiver>

      
        <service android:name="com.synerise.sdk.beacon.BeaconService"/>
        <service android:name="com.synerise.sdk.gcm.GcmIntentService" />
        <service android:name="com.synerise.sdk.gcm.CancelMessageService" />
        <service android:name="com.synerise.sdk.gcm.ReadMessageService" />
        
        <!-- apikey pozyskany z systemu synerise -->
        <meta-data android:name="com.humanoitgroup.synerise.ApiKey" android:value="synerise_api_key"/>
        <!-- number of project in Google Console, active Google Cloud Message Android API -->
        <!-- you must add Google Api Key to Synerise in Settings->Integration -->
        <meta-data android:name="com.humanoitgroup.synerise.senderId" android:value="@string/senderID"/>
        <meta-data android:name="beacon_uuid" android:value="uuid_1, uuid_2"/>


    </application>

</manifest>  
```


 In your main activity register device in Google Cloud Message, and start beacon service.
 ```
 public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash_screen);
  
        startService();
        
        RegisterGcmSynerise registerGcmSynerise = new RegisterGcmSynerise(this);
        String regIDs = registerGcmSynerise.getRegIds();
        if(regIDs == null || regIDs.isEmpty()){
            registerGcmSynerise.registerOnBackground(null);
        }
    }


    protected void startService(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            Intent intent = new Intent(this, com.synerise.sdk.beacon.BeaconService.class);
            PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 1000*30, pi);
        }
    }
}

```

#User tracking
If you tracking user events in android application, you must initialize tracker :

```
  package com.example.myapp;
  
  import android.app.Application; 
  import com.synerise.sdk.tracking.Tracker;
  public class MyApliaction extends Application{
     @Override
     public void onCreate(){
        super.onCreate();
        //initialize tracker, events was send, if is 
        //20 events in chache or first not send event was older than 120 seconds
        Tracker.initialize(this, 20, 120);
     }
  }
```

Track user events:

```
  ...
     myViewObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ...
                    ...
                    Tracker.trackEvent("my_event", new TrackerParams[]{
                      new TrackerParams("key_1", "value_1"),
                      ...
                    });
                }
            });
  ...
```

Track user showed screens:

 ```
  ...
  
  public class ExampleActivity extends Activity{
    ...
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ...
        Tracker.trackScreen("screen_name", new TrackerParams[]{
                      new TrackerParams("key_1", "value_1"),
                      ...
                    })
    }
    ...
  }
  
```

Track change user data:

 ```
 
  package com.example.code;
  
  ...
  import com.synerise.sdk.client.ClientData;
  import com.synerise.sdk.client.ClientDataCallback;
  import com.synerise.sdk.client.ClientDataFunction;
  import com.synerise.sdk.client.ClientSession;
  import com.synerise.sdk.tracking.Tracker;
  import com.synerise.sdk.tracking.TrackerParams; 
  ...
  
  public class ExampleUserDataActivity extends Activity{
    ...
    
    private void saveData(ClientData clientData){
      
       Tracker.client(new TrackerParams[]{
                    new TrackerParams("firstname", clientData.getFirstname()),
                    new TrackerParams("secondname", clientData.getLastname()),
                    new TrackerParams("email", clientData.getEmail()),
                    new TrackerParams("address", clientData.getAddress()),
                    new TrackerParams("city", clientData.getCity()),
                    new TrackerParams("phone", clientData.getPhone()),
                    new TrackerParams("zipCode",clientData.getZipCode())
            });
      
      ClientDataFunction clientDataFunction = new ClientDataFunction(this);
      clientDataFunction.updateUserData(clientData, new ClientDataCallback() {
                @Override
                public void onSuccess(ClientData clientData) {
                   //success code
                }

                @Override
                public void onError(int i, String s, String s1) {
                   //error code
                }
            });
     
    }
    ...
  }
  
```

You should know that Synerise has it own predefined Client model which is build with parameters:
  - email
  - firstname
  - secondname
  - adress
  - city
  - region
  - phone
  - sex
  - birthday


Tracker have function to send event after meetings, under code is example, how use beacon traking
with implementation interface BluetoothAdapter.LeScanCallback from standard android SDK. 

```
package ExampleLeScann implements BluetoothAdapter.LeScanCallback{
  
    Context context;
    
    public EcampleLeScann(Context c){
      this.context = c;
    }
  
  /**
   * your code  
   */ 
     @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Beacon beacon = IBeaconRecordParser.scanRecord(scanRecord);
         ...
         Tracker.createBeaconWithUUID(beacon.getMinor(), beacon.getMajor(), beacon.getUUID(), beacon.getCurrentPositionUser(), this.context);
         ...
    }
   
  /**
   * your code  
   */ 
  
}

  
```

Important!!! If you use a com.synerise.sdk.beacon.BeaconService, your application automaticaly send a beacon event.

If set custom user ident, invoke static function from Tracker.class

```
  ...
     Tracker.setCustomIdentify("my_ident");
  ...
```

