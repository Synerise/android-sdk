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

/*
    Support for handleIntent(intent) method in previous versions of firebase-messaging.
    This method is currently marked as final in FirebaseMessagingService and cannot be overridden.

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        Injector.handlePushPayload(intent.getExtras());
    }
*/
}
