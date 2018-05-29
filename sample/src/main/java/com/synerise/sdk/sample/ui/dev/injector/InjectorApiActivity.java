package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;
import com.synerise.sdk.sample.util.SystemUtils;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class InjectorApiActivity extends BaseActivity {

    private TextView firebaseId;
    private FirebaseIdChangeBroadcastReceiver broadcastReceiver;

    public static Intent createIntent(Context context) {
        return new Intent(context, InjectorApiActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_injector);

        ToolbarHelper.setUpChildToolbar(this, R.string.injector_api);

        firebaseId = findViewById(R.id.firebase_id);
        firebaseId.setOnClickListener(v -> copyFirebaseIdTv());
        findViewById(R.id.click_copy).setOnClickListener(v -> copyFirebaseIdTv());

        broadcastReceiver = new FirebaseIdChangeBroadcastReceiver();
        broadcastReceiver.setListener(this::onFirebaseIdChanged);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onFirebaseIdChanged();
        IntentFilter filter = new IntentFilter(FirebaseIdChangeBroadcastReceiver.ACTION_FIREBASE_ID_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    // ****************************************************************************************************************************************

    private void copyFirebaseIdTv() {
        SystemUtils.copyTextToClipboard(this, firebaseId.getText());
    }

    private void onFirebaseIdChanged() {
        // get your `google-services.json` from Firebase console first
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (TextUtils.isEmpty(refreshedToken)) {
            firebaseId.setText(R.string.unknown_firebase_id);
        } else {
            firebaseId.setText(refreshedToken);
        }
    }
}