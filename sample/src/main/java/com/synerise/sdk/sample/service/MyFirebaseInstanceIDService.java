package com.synerise.sdk.sample.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.utils.Lh;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;



public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    /**
     * The registration token may change when:
     * <p>
     * The app deletes Instance ID
     * The app is restored on a new device
     * The user uninstalls/reinstall the app
     * The user clears app data.
     */

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken != null) {
            IApiCall call = Profile.registerForPush(refreshedToken);
            call.execute(new ActionListener() {
                @Override
                public void onAction() {
                    Log.d(TAG, "Register for Push Successful " + refreshedToken);
                }
            }, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    Log.w(TAG, "Register for Push FAILED " + refreshedToken);
                }
            });

            Intent intent = FirebaseIdChangeBroadcastReceiver.createFirebaseIdChangedIntent();
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}
