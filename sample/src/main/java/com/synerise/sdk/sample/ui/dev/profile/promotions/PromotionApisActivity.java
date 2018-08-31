package com.synerise.sdk.sample.ui.dev.profile.promotions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.apiAdapter.SyneriseSdkApi;
import com.synerise.sdk.sample.ui.dev.apiAdapter.ApiRecyclerAdapter;
import com.synerise.sdk.sample.ui.dev.apiAdapter.FragmentContainerActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class PromotionApisActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, PromotionApisActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synerise_sdk_api);
        RecyclerView recycler = findViewById(R.id.api_recycler);

        ToolbarHelper.setUpChildToolbar(this, R.string.default_promotion_apis);

        ApiRecyclerAdapter adapter = new ApiRecyclerAdapter(LayoutInflater.from(this),
                                                            this::onPromotionApiClicked,
                                                            SyneriseSdkApi.getPromotionApis());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    // ****************************************************************************************************************************************

    private void onPromotionApiClicked(SyneriseSdkApi syneriseSdkApi) {
        if (!syneriseSdkApi.isGroup())
            startActivity(FragmentContainerActivity.createIntent(this, syneriseSdkApi.ordinal()));
    }
}
