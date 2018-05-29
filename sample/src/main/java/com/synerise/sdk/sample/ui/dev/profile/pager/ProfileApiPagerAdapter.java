package com.synerise.sdk.sample.ui.dev.profile.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.DevConfirmResetFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.DevCreateClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.DevDeleteClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.DevGetClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.DevGetTokenFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.DevRegisterClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.DevResetPasswordFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.DevUpdateClientFragment;

import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.CONFIRM_RESET;
import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.COUNT;
import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.CREATE_CLIENT;
import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.DELETE_CLIENT;
import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.GET_CLIENT;
import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.GET_TOKEN;
import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.REGISTER_CLIENT;
import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.RESET_PASSWORD;
import static com.synerise.sdk.sample.ui.dev.profile.pager.ProfileApiPagerAdapter.Pages.UPDATE_CLIENT;

public class ProfileApiPagerAdapter extends FragmentPagerAdapter {

    interface Pages {
        int GET_CLIENT = 0;
        int CREATE_CLIENT = 1;
        int REGISTER_CLIENT = 2;
        int UPDATE_CLIENT = 3;
        int DELETE_CLIENT = 4;
        int RESET_PASSWORD = 5;
        int CONFIRM_RESET = 6;
        int GET_TOKEN = 7;
        int COUNT = 8;
    }

    private final Context context;

    // ****************************************************************************************************************************************

    public ProfileApiPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    // ****************************************************************************************************************************************

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case GET_CLIENT:
                fragment = DevGetClientFragment.newInstance();
                break;
            case CREATE_CLIENT:
                fragment = DevCreateClientFragment.newInstance();
                break;
            case REGISTER_CLIENT:
                fragment = DevRegisterClientFragment.newInstance();
                break;
            case UPDATE_CLIENT:
                fragment = DevUpdateClientFragment.newInstance();
                break;
            case DELETE_CLIENT:
                fragment = DevDeleteClientFragment.newInstance();
                break;
            case RESET_PASSWORD:
                fragment = DevResetPasswordFragment.newInstance();
                break;
            case CONFIRM_RESET:
                fragment = DevConfirmResetFragment.newInstance();
                break;
            case GET_TOKEN:
            default:
                fragment = DevGetTokenFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case GET_CLIENT:
                return context.getString(R.string.profile_get_client);
            case CREATE_CLIENT:
                return context.getString(R.string.profile_client_create);
            case REGISTER_CLIENT:
                return context.getString(R.string.profile_client_register);
            case UPDATE_CLIENT:
                return context.getString(R.string.profile_client_update);
            case DELETE_CLIENT:
                return context.getString(R.string.profile_client_delete);
            case RESET_PASSWORD:
                return context.getString(R.string.profile_password_reset);
            case CONFIRM_RESET:
                return context.getString(R.string.profile_password_confirm);
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
