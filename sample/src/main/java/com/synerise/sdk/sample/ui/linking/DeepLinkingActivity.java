package com.synerise.sdk.sample.ui.linking;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class DeepLinkingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

        ToolbarHelper.setUpChildToolbar(this, R.string.deep_linking_title);
    }
}
