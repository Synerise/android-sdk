package com.synerise.sdk.sample.ui.dev.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.profile.adapter.ProfileApi;
import com.synerise.sdk.sample.ui.dev.profile.adapter.ProfileApiRecyclerAdapter;
import com.synerise.sdk.sample.ui.dev.profile.promotions.PromotionApisActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import static com.synerise.sdk.sample.ui.dev.profile.FragmentContainerActivity.DEFAULT_TYPE;

public class ProfileApiActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileApiActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_api);

        RecyclerView recycler = findViewById(R.id.profile_api_recycler);

        ToolbarHelper.setUpChildToolbar(this, R.string.profile_api);

        ProfileApiRecyclerAdapter adapter = new ProfileApiRecyclerAdapter(this,
                                                                          this::onProfileApiClicked,
                                                                          this::onPromotionApiClicked,
                                                                          ProfileApi.values());

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    // ****************************************************************************************************************************************

    private void onProfileApiClicked(ProfileApi profileApi) {
        startActivity(FragmentContainerActivity.createIntent(this, profileApi.ordinal(), DEFAULT_TYPE));
    }

    private void onPromotionApiClicked() {
        startActivity(PromotionApisActivity.createIntent(this));
    }
}