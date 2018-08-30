package com.synerise.sdk.sample.ui.dev.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.profile.adapter.ProfileApi;
import com.synerise.sdk.sample.ui.dev.profile.fragments.AssignVoucherFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevActivateClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevConfirmResetFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevCreateClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevDeleteClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevGetClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevGetTokenFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevRegisterClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevResetPasswordFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevUpdateClientFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.GetPromotionsByClientIdFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.GetPromotionsByCodeFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.GetPromotionsByEmailFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.GetPromotionsByExternalIdFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.GetPromotionsByPhoneFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.GetPromotionsByUuidFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.GetPromotionsFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.RedeemPromotionByClientIdFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.RedeemPromotionByCustomIdFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.RedeemPromotionByEmailFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.RedeemPromotionByPhoneFragment;
import com.synerise.sdk.sample.ui.dev.profile.promotions.adapter.PromotionApi;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class FragmentContainerActivity extends BaseActivity {

    private static final String PROFILE_API_POSITION = "PROFILE_API_POSITION";
    private static final String PROFILE_API_TYPE = "PROFILE_API_TYPE";

    public static final int DEFAULT_TYPE = 0;
    public static final int PROMOTION_TYPE = 1;

    public static Intent createIntent(Context context, int position, int type) {
        return new Intent(context, FragmentContainerActivity.class)
                .putExtra(PROFILE_API_POSITION, position)
                .putExtra(PROFILE_API_TYPE, type);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_profile);

        int position = getIntent().getIntExtra(PROFILE_API_POSITION, 0);
        int type = getIntent().getIntExtra(PROFILE_API_TYPE, 0);

        Fragment fragment;

        if (DEFAULT_TYPE == type) {
            ProfileApi profileApi = ProfileApi.values()[getIntent().getIntExtra(PROFILE_API_POSITION, 0)];
            fragment = getDevProfileFragment(profileApi);
            ToolbarHelper.setUpChildToolbar(this, profileApi.getTitle());
        } else {
            PromotionApi promotionApi = PromotionApi.values()[position];
            fragment = getDevPromotionProfileFragment(promotionApi);
            ToolbarHelper.setUpChildToolbar(this, promotionApi.getTitle());
        }

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    // ****************************************************************************************************************************************

    private Fragment getDevProfileFragment(ProfileApi profileApi) {
        switch (profileApi) {
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
            case GET_TOKEN:
            default:
                return DevGetTokenFragment.newInstance();
        }
    }

    private Fragment getDevPromotionProfileFragment(PromotionApi promotionApi) {
        switch (promotionApi) {
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
            case ASSIGN_VOUCHER:
                return AssignVoucherFragment.newInstance();
            case GET_PROMOTIONS:
            default:
                return GetPromotionsFragment.newInstance();
        }
    }
}
