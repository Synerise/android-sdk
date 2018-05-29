package com.synerise.sdk.sample.ui.dev.client.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientGetAccountFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientGetTokenFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientUpdateAccountFragment;

import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.COUNT;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.GET_ACCOUNT;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.GET_TOKEN;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.UPDATE_ACCOUNT;

public class ClientApiPagerAdapter extends FragmentPagerAdapter {

    interface Pages {
        int GET_ACCOUNT = 0;
        int UPDATE_ACCOUNT = 1;
        int GET_TOKEN = 2;
        int COUNT = 3;
    }

    private final Context context;

    // ****************************************************************************************************************************************

    public ClientApiPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    // ****************************************************************************************************************************************

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case GET_ACCOUNT:
                fragment = ClientGetAccountFragment.newInstance();
                break;
            case UPDATE_ACCOUNT:
                fragment = ClientUpdateAccountFragment.newInstance();
                break;
            case GET_TOKEN:
            default:
                fragment = ClientGetTokenFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case GET_ACCOUNT:
                return context.getString(R.string.get_account);
            case UPDATE_ACCOUNT:
                return context.getString(R.string.update_account);
            case GET_TOKEN:
            default:
                return context.getString(R.string.get_token);
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
