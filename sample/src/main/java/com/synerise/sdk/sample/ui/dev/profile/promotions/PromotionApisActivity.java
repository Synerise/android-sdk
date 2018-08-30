package com.synerise.sdk.sample.ui.dev.profile.promotions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.profile.FragmentContainerActivity;
import com.synerise.sdk.sample.ui.dev.profile.promotions.adapter.PromotionApi;
import com.synerise.sdk.sample.ui.dev.profile.promotions.adapter.PromotionApiRecyclerAdapter;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class PromotionApisActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, PromotionApisActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_apis);
        RecyclerView recycler = findViewById(R.id.promotion_api_recycler);

        ToolbarHelper.setUpChildToolbar(this, R.string.default_promotion_apis);

        PromotionApiRecyclerAdapter adapter = new PromotionApiRecyclerAdapter(LayoutInflater.from(this), this::onPromotionApiClicked);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    // ****************************************************************************************************************************************

    private void onPromotionApiClicked(PromotionApi promotionApi) {
        startActivity(FragmentContainerActivity.createIntent(this, promotionApi.ordinal(), FragmentContainerActivity.PROMOTION_TYPE));
    }
}
