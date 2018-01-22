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
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;

public class InjectorActivity extends AppCompatActivity {

    private static final int ONBOARDING_REQUEST_CODE = 201;
    private static final int WELCOME_REQUEST_CODE = 202;
    private static final int WALKTHROUGH_REQUEST_CODE = 203;

    private static final String BUCKET_NAME = "foo";

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

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InjectorActivity.this.onButtonClick(view);
            }
        };
        findViewById(R.id.show_onboarding).setOnClickListener(clickListener);
        findViewById(R.id.show_welcome_screen).setOnClickListener(clickListener);
        findViewById(R.id.show_walkthrough).setOnClickListener(clickListener);

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

    private void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.show_onboarding:
                Injector.showOnboardingIfPresent(this, ONBOARDING_REQUEST_CODE, BUCKET_NAME);
                break;
            case R.id.show_welcome_screen:
                Injector.showWelcomeScreenIfPresent(this, WELCOME_REQUEST_CODE, BUCKET_NAME);
                break;
            case R.id.show_walkthrough:
                Injector.showWalkthroughIfPresent(this, WALKTHROUGH_REQUEST_CODE, BUCKET_NAME);
                break;
        }
    }

    private void copyFirebaseIdTv() {
        SystemUtils.copyTextToClipboard(this, firebaseIdTv.getText());
        Toast.makeText(this, R.string.firebase_id_has_been_copied, Toast.LENGTH_LONG).show();
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ONBOARDING_REQUEST_CODE) {
            if (resultCode == Injector.ResultCodes.OK) {
                Toast.makeText(this, "onActivityResult: ONBOARDING - OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == Injector.ResultCodes.NOTHING_TO_SHOW) {
                Toast.makeText(this, "onActivityResult: ONBOARDING - NOTHING_TO_SHOW", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "onActivityResult: ONBOARDING - " + resultCode, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == WELCOME_REQUEST_CODE) {
            if (resultCode == Injector.ResultCodes.OK) {
                Toast.makeText(this, "onActivityResult: WELCOME SCREEN - OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == Injector.ResultCodes.NOTHING_TO_SHOW) {
                Toast.makeText(this, "onActivityResult: WELCOME SCREEN - NOTHING_TO_SHOW", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "onActivityResult: WELCOME SCREEN - " + resultCode, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == WALKTHROUGH_REQUEST_CODE) {
            if (resultCode == Injector.ResultCodes.OK) {
                Toast.makeText(this, "onActivityResult: WALKTHROUGH - OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == Injector.ResultCodes.NOTHING_TO_SHOW) {
                Toast.makeText(this, "onActivityResult: WALKTHROUGH - NOTHING_TO_SHOW", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "onActivityResult: WALKTHROUGH - " + resultCode, Toast.LENGTH_LONG).show();
            }
        }
    }
}
