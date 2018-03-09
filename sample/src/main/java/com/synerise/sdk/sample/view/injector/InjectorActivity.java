package com.synerise.sdk.sample.view.injector;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.synerise.sdk.core.utils.SystemUtils;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;

public class InjectorActivity extends AppCompatActivity {

    private FirebaseIdChangeBroadcastReceiver broadcastReceiver;
    private TextView firebaseIdTv;

    public static Intent createIntent(Context context) {
        return new Intent(context, InjectorActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injector);

        firebaseIdTv = findViewById(R.id.firebaseId);
        firebaseIdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InjectorActivity.this.copyFirebaseIdTv();
            }
        });

        broadcastReceiver = new FirebaseIdChangeBroadcastReceiver();
        broadcastReceiver.setListener(new FirebaseIdChangeBroadcastReceiver.ActionListener() {
            @Override
            public void onAction() {
                InjectorActivity.this.onFirebaseIdChanged();
            }
        });
    }

    // ****************************************************************************************************************************************

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

    private void onFirebaseIdChanged() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken(); // get `google-services.json` from Firebase console first
        if (TextUtils.isEmpty(refreshedToken)) {
            firebaseIdTv.setText(R.string.unknown_firebase_id);
        } else {
            firebaseIdTv.setText(refreshedToken);
        }
    }

    // ****************************************************************************************************************************************

    private void copyFirebaseIdTv() {
        SystemUtils.copyTextToClipboard(this, firebaseIdTv.getText());
        Toast.makeText(this, R.string.firebase_id_has_been_copied, Toast.LENGTH_LONG).show();
    }
}
