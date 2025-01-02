package com.synerise.sdk.sample.ui.dev.apiAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientChangeApiKeyFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientChangePasswordFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientConfirmEmailChangeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientConfirmPhoneUpdateFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientDeleteAccountFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientDestroySessionFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientGetAccountFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientGetTokenFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientRecognizeAnonymusFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientRegenerateUuidFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientRequestEmailChangeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientRequestPhoneUpdateFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.ClientUpdateAccountFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.DevActivateClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.DevAuthenticateOAuthFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.DevConfirmClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.DevConfirmResetFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.DevRegisterClientFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.DevResetPasswordFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientActivatePromotionByCodeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientActivatePromotionByUuidFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientAssignVoucherCodeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientDeactivatePromotionByCodeFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientDeactivatePromotionByUuidFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientGetAssignedVoucherCodesFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientGetOrAssignVoucherFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientGetPromotionByUuidFragment;
import com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion.ClientGetPromotionsFragment;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class FragmentContainerActivity extends BaseActivity {

    private static final String PROFILE_API_ORDINAL = "PROFILE_API_ORDINAL";

    public static Intent createIntent(Context context, int ordinal) {
        return new Intent(context, FragmentContainerActivity.class)
                .putExtra(PROFILE_API_ORDINAL, ordinal);
    }

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

    private Fragment getDevProfileFragment(SyneriseSdkApi syneriseSdkApi) {
        switch (syneriseSdkApi) {
            // CLIENT
            case CHANGE_API_KEY:
                return ClientChangeApiKeyFragment.newInstance();
            case REGISTER_CLIENT:
                return DevRegisterClientFragment.newInstance();
            case AUTHENTICATE_OAUTH:
                return DevAuthenticateOAuthFragment.newInstance();
            case ACTIVATE_CLIENT:
                return DevActivateClientFragment.newInstance();
            case CONFIRM_CLIENT:
                return DevConfirmClientFragment.newInstance();
            case RESET_PASSWORD:
                return DevResetPasswordFragment.newInstance();
            case CONFIRM_RESET:
                return DevConfirmResetFragment.newInstance();
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
            case CLIENT_REGENERATE_UUID:
                return ClientRegenerateUuidFragment.newInstance();
            case REQUEST_PHONE_UPDATE:
                return ClientRequestPhoneUpdateFragment.newInstance();
            case CONFIRM_PHONE_UPDATE:
                return ClientConfirmPhoneUpdateFragment.newInstance();
            case REQUEST_EMAIL_CHANGE:
                return ClientRequestEmailChangeFragment.newInstance();
            case CONFIRM_EMAIL_CHANGE:
                return ClientConfirmEmailChangeFragment.newInstance();
            case RECOGNIZE_ANONYMOUS:
                return ClientRecognizeAnonymusFragment.newInstance();
            case DESTROY_SESSION:
                return ClientDestroySessionFragment.newInstance();

            // PROMOTIONS
            case CLIENT_GET_PROMOTIONS:
                return ClientGetPromotionsFragment.newInstance();
            case CLIENT_GET_PROMOTIONS_BY_UUID:
                return ClientGetPromotionByUuidFragment.newInstance();
            case ACTIVATE_PROMOTION_BY_UUID:
                return ClientActivatePromotionByUuidFragment.newInstance();
            case ACTIVATE_PROMOTION_BY_CODE:
                return ClientActivatePromotionByCodeFragment.newInstance();
            case DEACTIVATE_PROMOTION_BY_UUID:
                return ClientDeactivatePromotionByUuidFragment.newInstance();
            case DEACTIVATE_PROMOTION_BY_CODE:
                return ClientDeactivatePromotionByCodeFragment.newInstance();
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
