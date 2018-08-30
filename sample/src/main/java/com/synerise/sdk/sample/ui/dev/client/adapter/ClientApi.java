package com.synerise.sdk.sample.ui.dev.client.adapter;

import android.support.annotation.StringRes;

import com.synerise.sdk.sample.R;

public enum ClientApi {
    GET_ACCOUNT(R.string.get_account),
    UPDATE_ACCOUNT(R.string.update_account),
    DELETE_ACCOUNT(R.string.delete_account),
    CHANGE_PASSWORD(R.string.client_change_password),
    GET_TOKEN(R.string.get_token),
    GET_PROMOTIONS(R.string.client_get_promotions),
    ACTIVATE_PROMOTION_BY_UUID(R.string.client_activate_promotions),
    ACTIVATE_PROMOTION_BY_CODE(R.string.client_activate_promotions),
    REQUEST_PHONE_UPDATE(R.string.client_phone_update),
    CONFIRM_PHONE_UPDATE(R.string.client_confirm_phone_update);

    @StringRes private final int title;

    ClientApi(@StringRes int title) {
        this.title = title;
    }

    @StringRes
    public int getTitle() {
        return title;
    }

}
