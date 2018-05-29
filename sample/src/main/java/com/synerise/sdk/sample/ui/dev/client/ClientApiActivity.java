package com.synerise.sdk.sample.ui.dev.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class ClientApiActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ClientApiActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_client);

        ViewPager viewPager = findViewById(R.id.view_pager);

        ToolbarHelper.setUpChildToolbar(this, R.string.client_api);

        // adapter
        ClientApiPagerAdapter adapter = new ClientApiPagerAdapter(this, getSupportFragmentManager());

        // view pager
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);

        // tab layout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}