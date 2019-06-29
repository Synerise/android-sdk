package com.synerise.sdk.sample.ui.dev.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class ContentApiActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ContentApiActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_api);

        ToolbarHelper.setUpChildToolbar(this, R.string.content_api);

        findViewById(R.id.slider_widget).setOnClickListener(v -> startActivity(WidgetHorizontalSliderActivity.createIntent(this)));
        findViewById(R.id.gridview_widget).setOnClickListener(v -> startActivity(WidgetGridViewActivity.createIntent(this)));
    }
}
