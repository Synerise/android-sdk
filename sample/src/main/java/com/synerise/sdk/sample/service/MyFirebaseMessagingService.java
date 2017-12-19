package com.synerise.sdk.sample.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.synerise.sdk.injector.Injector;

/**
 * Created by Marcel Skotnicki on 12/8/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Injector.handlePushPayload(remoteMessage.getData());
    }
}
