package com.synerise.sdk.sample.ui.dev.profile.pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.GetPromotionsByClientIdFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.GetPromotionsByCodeFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.GetPromotionsByEmailFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.GetPromotionsByExternalIdFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.GetPromotionsByPhoneFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.GetPromotionsByUuidFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.GetPromotionsFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.RedeemPromotionByClientIdFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.RedeemPromotionByCustomIdFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.RedeemPromotionByEmailFragment;
import com.synerise.sdk.sample.ui.dev.profile.pager.pages.RedeemPromotionByPhoneFragment;

public class ProfileApiPagerAdapter extends FragmentPagerAdapter {

    enum PagesEnum {
        GET_CLIENT(R.string.profile_get_client),
        CREATE_CLIENT(R.string.profile_client_create),
        REGISTER_CLIENT(R.string.profile_client_register),
        UPDATE_CLIENT(R.string.profile_client_update),
        DELETE_CLIENT(R.string.profile_client_delete),
        RESET_PASSWORD(R.string.profile_password_reset),
        CONFIRM_RESET(R.string.profile_password_confirm),
        GET_TOKEN(R.string.get_token),
        GET_PROMOTIONS(R.string.profile_get_promotions),
        GET_PROMOTIONS_BY_EXTERNAL_ID(R.string.profile_get_promotions_by_external_id),
        GET_PROMOTIONS_BY_PHONE(R.string.profile_get_promotions_by_phone),
        GET_PROMOTIONS_BY_CLIENT_ID(R.string.profile_get_promotions_by_client_id),
        GET_PROMOTIONS_BY_EMAIL(R.string.profile_get_promotions_by_email),
        GET_PROMOTIONS_BY_CODE(R.string.profile_get_promotions_by_code),
        GET_PROMOTIONS_BY_UUID(R.string.profile_get_promotions_by_uuid),
        REDEEM_PROMOTIONS_BY_PHONE(R.string.profile_redeem_promotions_by_phone),
        REDEEM_PROMOTIONS_BY_EMAIL(R.string.profile_redeem_promotions_by_email),
        REDEEM_PROMOTIONS_BY_CUSTOM_ID(R.string.profile_redeem_promotions_by_custom_id),
        REDEEM_PROMOTIONS_BY_CLIENT_ID(R.string.profile_redeem_promotions_by_client_id);

        @StringRes private final int title;

        PagesEnum(@StringRes int title) {
            this.title = title;
        }

        public CharSequence getTitle(Context context) {
            return context.getString(title);
        }
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
        PagesEnum pagesEnum = PagesEnum.values()[position];
        switch (pagesEnum) {
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
            case GET_PROMOTIONS:
                fragment = GetPromotionsFragment.newInstance();
                break;
            case GET_PROMOTIONS_BY_EXTERNAL_ID:
                fragment = GetPromotionsByExternalIdFragment.newInstance();
                break;
            case GET_PROMOTIONS_BY_PHONE:
                fragment = GetPromotionsByPhoneFragment.newInstance();
                break;
            case GET_PROMOTIONS_BY_CLIENT_ID:
                fragment = GetPromotionsByClientIdFragment.newInstance();
                break;
            case GET_PROMOTIONS_BY_EMAIL:
                fragment = GetPromotionsByEmailFragment.newInstance();
                break;
            case GET_PROMOTIONS_BY_CODE:
                fragment = GetPromotionsByCodeFragment.newInstance();
                break;
            case GET_PROMOTIONS_BY_UUID:
                fragment = GetPromotionsByUuidFragment.newInstance();
                break;
            case REDEEM_PROMOTIONS_BY_PHONE:
                fragment = RedeemPromotionByPhoneFragment.newInstance();
                break;
            case REDEEM_PROMOTIONS_BY_CLIENT_ID:
                fragment = RedeemPromotionByClientIdFragment.newInstance();
                break;
            case REDEEM_PROMOTIONS_BY_CUSTOM_ID:
                fragment = RedeemPromotionByCustomIdFragment.newInstance();
                break;
            case REDEEM_PROMOTIONS_BY_EMAIL:
                fragment = RedeemPromotionByEmailFragment.newInstance();
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
        return PagesEnum.values()[position].getTitle(context);
    }

    @Override
    public int getCount() {
        return PagesEnum.values().length;
    }
}
