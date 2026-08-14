package com.synerise.sdk.sample.ui.dev.injector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class InlineInAppActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, InlineInAppActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_inline_inapp);
        ToolbarHelper.setUpChildToolbar(this, R.string.inline_inapp_api);

        findViewById(R.id.btn_single).setOnClickListener(v ->
                startActivity(InlineInAppSingleActivity.createIntent(this)));
        findViewById(R.id.btn_same_key).setOnClickListener(v ->
                startActivity(InlineInAppSameKeyActivity.createIntent(this)));
        findViewById(R.id.btn_on_demand).setOnClickListener(v ->
                startActivity(InlineInAppOnDemandActivity.createIntent(this)));
        findViewById(R.id.btn_two_keys).setOnClickListener(v ->
                startActivity(InlineInAppTwoKeysActivity.createIntent(this)));
        findViewById(R.id.btn_priority).setOnClickListener(v ->
                startActivity(InlineInAppPriorityActivity.createIntent(this)));
        findViewById(R.id.btn_lifecycle).setOnClickListener(v ->
                startActivity(InlineInAppLifecycleActivity.createIntent(this)));
        findViewById(R.id.btn_reattach).setOnClickListener(v ->
                startActivity(InlineInAppReattachActivity.createIntent(this)));
        findViewById(R.id.btn_global_listener).setOnClickListener(v ->
                startActivity(InlineInAppGlobalListenerActivity.createIntent(this)));
        findViewById(R.id.btn_edge_cases).setOnClickListener(v ->
                startActivity(InlineInAppEdgeCasesActivity.createIntent(this)));
        findViewById(R.id.btn_config).setOnClickListener(v ->
                startActivity(InlineInAppConfigActivity.createIntent(this)));
        findViewById(R.id.btn_fix_verification).setOnClickListener(v ->
                startActivity(InlineInAppFixVerificationActivity.createIntent(this)));
        findViewById(R.id.btn_debug).setOnClickListener(v ->
                startActivity(InlineInAppDebugActivity.createIntent(this)));
        findViewById(R.id.btn_autoresize).setOnClickListener(v ->
                startActivity(InlineInAppAutoResizeActivity.createIntent(this)));
        findViewById(R.id.btn_set_component_size).setOnClickListener(v ->
                startActivity(InlineInAppSetComponentSizeActivity.createIntent(this)));
        findViewById(R.id.btn_search).setOnClickListener(v ->
                startActivity(InlineInAppSearchHostActivity.createIntent(this)));
        findViewById(R.id.btn_rotation).setOnClickListener(v ->
                startActivity(InlineInAppRotationActivity.createIntent(this)));
        findViewById(R.id.btn_url_actions).setOnClickListener(v ->
                startActivity(InlineInAppUrlActionsActivity.createIntent(this)));
    }
}
