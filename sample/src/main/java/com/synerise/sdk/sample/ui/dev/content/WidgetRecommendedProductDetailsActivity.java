package com.synerise.sdk.sample.ui.dev.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.section.category.products.details.ProductActivity;

public class WidgetRecommendedProductDetailsActivity extends BaseActivity{

    public static Intent createIntent(Context context, String productSKU) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(BaseActivity.Args.SERIALIZABLE, productSKU);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
