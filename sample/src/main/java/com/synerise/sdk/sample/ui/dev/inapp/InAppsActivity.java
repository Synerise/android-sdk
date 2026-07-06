package com.synerise.sdk.sample.ui.dev.inapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

/** Developer tools → InApps. Lists InApp-related testing sub-sections. */
public class InAppsActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, InAppsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_inapps);

        ToolbarHelper.setUpChildToolbar(this, R.string.inapps);

        findViewById(R.id.inapp_context_from_app)
                .setOnClickListener(v -> startActivity(InAppContextActivity.createIntent(this)));
        findViewById(R.id.inapp_rotation_test)
                .setOnClickListener(v -> startActivity(InAppRotationTestActivity.createIntent(this)));
    }
}
