package com.synerise.sdk.sample.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        boolean isSynerisePush = Injector.handlePushPayload(remoteMessage.getData());

        if (!isSynerisePush) {
            // overriding this method causes simple notification to not being displayed while app is visible to user
            // this is an example usage of building your non-Synerise notification
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            if (notification != null) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id");
                builder.setContentTitle(notification.getTitle());
                builder.setContentText(notification.getBody());
                // resource of icon to show must be drawable resource (not mipmap) due to Android Oreo adaptive icons restrictions
                builder.setSmallIcon(R.drawable.ic_cart);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    // mandatory for Android O
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notificationManager.createNotificationChannel(
                                new NotificationChannel("my_channel_id", "my_channel_name", NotificationManager.IMPORTANCE_HIGH));
                    }
                    notificationManager.notify(new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE), builder.build());
                }
            }
        }
    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);

        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken != null) {
            IApiCall call = Profile.registerForPush(refreshedToken);
            call.execute(() -> Log.d(TAG, "Register for Push succeed: " + refreshedToken),
                         apiError -> Log.w(TAG, "Register for push failed: " + refreshedToken));

            Intent intent = FirebaseIdChangeBroadcastReceiver.createFirebaseIdChangedIntent();
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
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