package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.FirebaseIdChangeBroadcastReceiver;
import com.synerise.sdk.sample.util.SystemUtils;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.util.HashMap;
import java.util.Map;

import static com.synerise.sdk.injector.SynerisePushKeys.CONTENT;
import static com.synerise.sdk.injector.SynerisePushKeys.CONTENT_TYPE;
import static com.synerise.sdk.injector.SynerisePushKeys.ISSUER;
import static com.synerise.sdk.injector.SynerisePushKeys.MESSAGE_TYPE;

public class InjectorApiActivity extends BaseActivity {

    private TextView firebaseId;
    private FirebaseIdChangeBroadcastReceiver broadcastReceiver;

    public static Intent createIntent(Context context) {
        return new Intent(context, InjectorApiActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_injector);

        ToolbarHelper.setUpChildToolbar(this, R.string.injector_api);

        firebaseId = findViewById(R.id.firebase_id);
        firebaseId.setOnClickListener(v -> copyFirebaseIdTv());
        findViewById(R.id.click_copy).setOnClickListener(v -> copyFirebaseIdTv());
        findViewById(R.id.get_simple_push).setOnClickListener(v -> getSimplePush());

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

    private void getSimplePush() {
        Map<String, String> data = new HashMap<>();
        String content = "{\"notification\":{\"action\":{\"item\":\"https://app.synerise.com/login\",\"type\":\"OPEN_URL\"},\"body\":\"Zobacz co słychać w Synerise! \",\"title\":\"Synerise test push \"},\"campaign\":{\"variant_id\":1157896,\"title\":\"testSynerise\",\"type\":\"Mobile push\",\"hash_id\":\"085cf70c-0239-452d-bbed-d1f9c94e21e5\"}}";
        data.put(ISSUER.getApiKey(), "Synerise");
        data.put(CONTENT_TYPE.getApiKey(), "simple-push");
        data.put(MESSAGE_TYPE.getApiKey(), "static-content");
        data.put(CONTENT.getApiKey(), content);
        boolean isSynerisePush = Injector.handlePushPayload(data);
    }
}