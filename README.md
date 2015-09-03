# Synerise Android SDK
Synerise SDK for Andoid

#Documentation with examples
http://synerise.github.io/android-sdk

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

