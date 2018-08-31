package com.synerise.sdk.sample.ui.dev.apiAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientChangePasswordFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientConfirmPhoneUpdateFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientDeleteAccountFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientGetAccountFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientGetTokenFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientRequestPhoneUpdateFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientUpdateAccountFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.promotion.ClientActivatePromotionByCodeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.promotion.ClientActivatePromotionByUuidFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.promotion.ClientGetPromotionsFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.voucher.ClientAssignVoucherCodeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.voucher.ClientGetAssignedVoucherCodesFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.voucher.ClientGetOrAssignVoucherFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevActivateClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevConfirmResetFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevCreateClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevDeleteClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevGetClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevGetTokenFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevRegisterClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevResetPasswordFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.DevUpdateClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.GetPromotionsByClientIdFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.GetPromotionsByCodeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.GetPromotionsByEmailFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.GetPromotionsByExternalIdFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.GetPromotionsByPhoneFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.GetPromotionsByUuidFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.GetPromotionsFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.RedeemPromotionByClientIdFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.RedeemPromotionByCustomIdFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.RedeemPromotionByEmailFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion.RedeemPromotionByPhoneFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.voucher.ProfileAssignVoucherCodeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.voucher.ProfileGetOrAssignVoucherFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.voucher.ProfileGetVoucherCodesFragment;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class FragmentContainerActivity extends BaseActivity {

    private static final String PROFILE_API_ORDINAL = "PROFILE_API_ORDINAL";

    public static Intent createIntent(Context context, int ordinal) {
        return new Intent(context, FragmentContainerActivity.class)
                .putExtra(PROFILE_API_ORDINAL, ordinal);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_profile);

        int ordinal = getIntent().getIntExtra(PROFILE_API_ORDINAL, 0);

        Fragment fragment;

        SyneriseSdkApi syneriseSdkApi = SyneriseSdkApi.values()[ordinal];
        fragment = getDevProfileFragment(syneriseSdkApi);
        ToolbarHelper.setUpChildToolbar(this, syneriseSdkApi.getTitle());

        if (fragment != null)
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    // ****************************************************************************************************************************************

    private Fragment getDevProfileFragment(SyneriseSdkApi syneriseSdkApi) {
        switch (syneriseSdkApi) {
            // PROFILE
            case GET_CLIENT:
                return DevGetClientFragment.newInstance();
            case CREATE_CLIENT:
                return DevCreateClientFragment.newInstance();
            case REGISTER_CLIENT:
                return DevRegisterClientFragment.newInstance();
            case UPDATE_CLIENT:
                return DevUpdateClientFragment.newInstance();
            case DELETE_CLIENT:
                return DevDeleteClientFragment.newInstance();
            case ACTIVATE_CLIENT:
                return DevActivateClientFragment.newInstance();
            case RESET_PASSWORD:
                return DevResetPasswordFragment.newInstance();
            case CONFIRM_RESET:
                return DevConfirmResetFragment.newInstance();
            case PROFILE_GET_TOKEN:
                return DevGetTokenFragment.newInstance();
            //            case PROFILE_PROMOTIONS: group
            //            case PROFILE_VOUCHERS: group
            // PROFILE PROMOTIONS
            case GET_PROMOTIONS:
                return GetPromotionsFragment.newInstance();
            case GET_PROMOTIONS_BY_EXTERNAL_ID:
                return GetPromotionsByExternalIdFragment.newInstance();
            case GET_PROMOTIONS_BY_PHONE:
                return GetPromotionsByPhoneFragment.newInstance();
            case GET_PROMOTIONS_BY_CLIENT_ID:
                return GetPromotionsByClientIdFragment.newInstance();
            case GET_PROMOTIONS_BY_EMAIL:
                return GetPromotionsByEmailFragment.newInstance();
            case GET_PROMOTIONS_BY_CODE:
                return GetPromotionsByCodeFragment.newInstance();
            case GET_PROMOTIONS_BY_UUID:
                return GetPromotionsByUuidFragment.newInstance();
            case REDEEM_PROMOTIONS_BY_PHONE:
                return RedeemPromotionByPhoneFragment.newInstance();
            case REDEEM_PROMOTIONS_BY_CLIENT_ID:
                return RedeemPromotionByClientIdFragment.newInstance();
            case REDEEM_PROMOTIONS_BY_CUSTOM_ID:
                return RedeemPromotionByCustomIdFragment.newInstance();
            case REDEEM_PROMOTIONS_BY_EMAIL:
                return RedeemPromotionByEmailFragment.newInstance();
            // PROFILE VOUCHER
            case PROFILE_GET_OR_ASSIGN_VOUCHER:
                return ProfileGetOrAssignVoucherFragment.newInstance();
            case PROFILE_GET_VOUCHER_CODES:
                return ProfileGetVoucherCodesFragment.newInstance();
            case PROFILE_ASSIGN_VOUCHER_CODE:
                return ProfileAssignVoucherCodeFragment.newInstance();
                // CLIENT
            case GET_ACCOUNT:
                return ClientGetAccountFragment.newInstance();
            case UPDATE_ACCOUNT:
                return ClientUpdateAccountFragment.newInstance();
            case DELETE_ACCOUNT:
                return ClientDeleteAccountFragment.newInstance();
            case CHANGE_PASSWORD:
                return ClientChangePasswordFragment.newInstance();
            case CLIENT_GET_TOKEN:
                return ClientGetTokenFragment.newInstance();
            case REQUEST_PHONE_UPDATE:
                return ClientRequestPhoneUpdateFragment.newInstance();
            case CONFIRM_PHONE_UPDATE:
                return ClientConfirmPhoneUpdateFragment.newInstance();
            // CLIENT PROMOTIONS
            case CLIENT_GET_PROMOTIONS:
                return ClientGetPromotionsFragment.newInstance();
            case ACTIVATE_PROMOTION_BY_UUID:
                return ClientActivatePromotionByUuidFragment.newInstance();
            case ACTIVATE_PROMOTION_BY_CODE:
                return ClientActivatePromotionByCodeFragment.newInstance();
            // CLIENT VOUCHER
            case CLIENT_ASSIGN_VOUCHER_CODE:
                return ClientAssignVoucherCodeFragment.newInstance();
            case CLIENT_GET_ASSIGNED_VOUCHER_CODES:
                return ClientGetAssignedVoucherCodesFragment.newInstance();
            case CLIENT_GET_OR_ASSIGN_VOUCHER:
                return ClientGetOrAssignVoucherFragment.newInstance();
            default:
                return null;
        }
    }
}
