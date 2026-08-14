package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class InlineInAppDeeplinkTargetActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppDeeplinkTargetActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inline_inapp_deeplink_target);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_deeplink_target);
    }
}
