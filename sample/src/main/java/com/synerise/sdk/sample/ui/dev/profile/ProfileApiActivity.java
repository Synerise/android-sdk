package com.synerise.sdk.sample.ui.dev.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class ProfileApiActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileApiActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_profile);

        ViewPager viewPager = findViewById(R.id.view_pager);

        ToolbarHelper.setUpChildToolbar(this, R.string.profile_api);

        // adapter
        ProfileApiPagerAdapter adapter = new ProfileApiPagerAdapter(this, getSupportFragmentManager());

        // view pager
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);

        // tab layout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}