package com.synerise.sdk.sample.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FirebaseIdChangeBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_FIREBASE_ID_CHANGE = FirebaseIdChangeBroadcastReceiver.class.getPackage() + ".ACTION_FIREBASE_ID_CHANGE";

    private ActionListener listener = ActionListener.NULL;

    public static Intent createFirebaseIdChangedIntent() {
        Intent intent = new Intent();
        intent.setAction(FirebaseIdChangeBroadcastReceiver.ACTION_FIREBASE_ID_CHANGE);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_FIREBASE_ID_CHANGE.equals(intent.getAction())) {
            this.listener.onAction();
        }
    }

    public void setListener(ActionListener actionListener) {
        if (actionListener == null) this.listener = ActionListener.NULL;
        else this.listener = actionListener;
    }

    public interface ActionListener {
        ActionListener NULL = () -> {};
        void onAction();
    }
}
