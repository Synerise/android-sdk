package com.synerise.sdk.sample.ui.dev.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientActivatePromotionByCodeFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientActivatePromotionByUuidFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientChangePasswordFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientConfirmPhoneUpdateFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientDeleteAccountFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientGetAccountFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientGetPromotionsFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientRequestPhoneUpdateFragment;
import com.synerise.sdk.sample.ui.dev.client.pager.pages.ClientUpdateAccountFragment;
import com.synerise.sdk.sample.ui.dev.profile.fragments.DevGetTokenFragment;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class DevClientActivity extends BaseActivity {

    private static final String CLIENT_API_POSITION = "CLIENT_API_POSITION";

    public static Intent createIntent(Context context, int position) {
        return new Intent(context, DevClientActivity.class).putExtra(CLIENT_API_POSITION, position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_client);

        ClientApi clientApi = ClientApi.values()[getIntent().getIntExtra(CLIENT_API_POSITION, 0)];
        Fragment fragment = getDevClientFragment(clientApi);

        ToolbarHelper.setUpChildToolbar(this, clientApi.getTitle());

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    private Fragment getDevClientFragment(ClientApi clientApi) {
        switch (clientApi) {
            case GET_ACCOUNT:
                return ClientGetAccountFragment.newInstance();
            case UPDATE_ACCOUNT:
                return ClientUpdateAccountFragment.newInstance();
            case DELETE_ACCOUNT:
                return ClientDeleteAccountFragment.newInstance();
            case CHANGE_PASSWORD:
                return ClientChangePasswordFragment.newInstance();
            case GET_PROMOTIONS:
                return ClientGetPromotionsFragment.newInstance();
            case ACTIVATE_PROMOTION_BY_UUID:
                return ClientActivatePromotionByUuidFragment.newInstance();
            case ACTIVATE_PROMOTION_BY_CODE:
                return ClientActivatePromotionByCodeFragment.newInstance();
            case REQUEST_PHONE_UPDATE:
                return ClientRequestPhoneUpdateFragment.newInstance();
            case CONFIRM_PHONE_UPDATE:
                return ClientConfirmPhoneUpdateFragment.newInstance();
            case GET_TOKEN:
            default:
                return DevGetTokenFragment.newInstance();
        }
    }
}
