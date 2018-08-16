package com.synerise.sdk.sample.ui.dev.client.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientActivatePromotionByCodeFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientActivatePromotionByUuidFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientChangePasswordFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientConfirmPhoneUpdateFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientDeleteAccountFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientGetAccountFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientGetPromotionsFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientGetTokenFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientRequestPhoneUpdateFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientUpdateAccountFragment;

import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.ACTIVATE_PROMOTION_BY_CODE;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.ACTIVATE_PROMOTION_BY_UUID;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.CHANGE_PASSWORD;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.CONFIRM_PHONE_UPDATE;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.COUNT;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.DELETE_ACCOUNT;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.GET_ACCOUNT;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.GET_PROMOTIONS;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.GET_TOKEN;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.REQUEST_PHONE_UPDATE;
import static com.synerise.sdk.sample.ui.dev.client.pager.ClientApiPagerAdapter.Pages.UPDATE_ACCOUNT;

public class ClientApiPagerAdapter extends FragmentPagerAdapter {

    interface Pages {
        int GET_ACCOUNT = 0;
        int UPDATE_ACCOUNT = 1;
        int DELETE_ACCOUNT = 2;
        int CHANGE_PASSWORD = 3;
        int GET_TOKEN = 4;
        int GET_PROMOTIONS = 5;
        int ACTIVATE_PROMOTION_BY_UUID = 6;
        int ACTIVATE_PROMOTION_BY_CODE = 7;
        int REQUEST_PHONE_UPDATE = 8;
        int CONFIRM_PHONE_UPDATE = 9;
        int COUNT = 10;
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
            case DELETE_ACCOUNT:
                fragment = ClientDeleteAccountFragment.newInstance();
                break;
            case CHANGE_PASSWORD:
                fragment = ClientChangePasswordFragment.newInstance();
                break;
            case GET_PROMOTIONS:
                fragment = ClientGetPromotionsFragment.newInstance();
                break;
            case ACTIVATE_PROMOTION_BY_UUID:
                fragment = ClientActivatePromotionByUuidFragment.newInstance();
                break;
            case ACTIVATE_PROMOTION_BY_CODE:
                fragment = ClientActivatePromotionByCodeFragment.newInstance();
                break;
            case REQUEST_PHONE_UPDATE:
                fragment = ClientRequestPhoneUpdateFragment.newInstance();
                break;
            case CONFIRM_PHONE_UPDATE:
                fragment = ClientConfirmPhoneUpdateFragment.newInstance();
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
            case DELETE_ACCOUNT:
                return context.getString(R.string.delete_account);
            case CHANGE_PASSWORD:
                return context.getString(R.string.client_change_password);
            case GET_PROMOTIONS:
                return context.getString(R.string.client_get_promotions);
            case ACTIVATE_PROMOTION_BY_UUID:
                return context.getString(R.string.client_activate_promotions);
            case ACTIVATE_PROMOTION_BY_CODE:
                return context.getString(R.string.client_activate_promotions);
            case REQUEST_PHONE_UPDATE:
                return context.getString(R.string.client_phone_update);
            case CONFIRM_PHONE_UPDATE:
                return context.getString(R.string.client_confirm_phone_update);
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
