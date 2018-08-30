package com.synerise.sdk.sample.ui.dev.profile.adapter;

import android.support.annotation.StringRes;

import com.synerise.sdk.sample.R;

public enum ProfileApi {
    GET_CLIENT(R.string.profile_get_client),
    CREATE_CLIENT(R.string.profile_client_create),
    REGISTER_CLIENT(R.string.profile_client_register),
    UPDATE_CLIENT(R.string.profile_client_update),
    DELETE_CLIENT(R.string.profile_client_delete),
    ACTIVATE_CLIENT(R.string.profile_client_activate),
    RESET_PASSWORD(R.string.profile_password_reset),
    CONFIRM_RESET(R.string.profile_password_confirm),
    GET_TOKEN(R.string.get_token),
    PROMOTIONS(R.string.default_promotion_apis);

    @StringRes private final int title;

    ProfileApi(@StringRes int title) {
        this.title = title;
    }

    @StringRes
    public int getTitle() {
        return title;
    }
}
