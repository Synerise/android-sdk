package com.synerise.sdk.sample.ui.dev.apiAdapter;

import android.support.annotation.StringRes;

import com.synerise.sdk.sample.R;

import java.util.Arrays;
import java.util.List;

public enum SyneriseSdkApi {

    // CLIENT API
    GET_ACCOUNT(R.string.get_account),
    REGISTER_CLIENT(R.string.profile_client_register),
    AUTHENTICATE_OAUTH(R.string.authenticate_oauth),
    ACTIVATE_CLIENT(R.string.profile_client_activate),
    CONFIRM_CLIENT(R.string.profile_client_confirm),
    RESET_PASSWORD(R.string.profile_password_reset),
    CONFIRM_RESET(R.string.profile_password_confirm),
    UPDATE_ACCOUNT(R.string.update_account),
    DELETE_ACCOUNT(R.string.delete_account),
    CHANGE_PASSWORD(R.string.client_change_password),
    CLIENT_GET_TOKEN(R.string.get_token),
    CLIENT_REGENERATE_UUID(R.string.regenerate_uuid),
    REQUEST_PHONE_UPDATE(R.string.client_phone_update),
    CONFIRM_PHONE_UPDATE(R.string.client_confirm_phone_update),
    REQUEST_EMAIL_CHANGE(R.string.client_request_email_change),
    CONFIRM_EMAIL_CHANGE(R.string.client_confirm_email_change),

    // PROMOTIONS API
    CLIENT_GET_PROMOTIONS(R.string.client_get_promotions),
    ACTIVATE_PROMOTION_BY_UUID(R.string.client_activate_promotions_by_uuid),
    ACTIVATE_PROMOTION_BY_CODE(R.string.client_activate_promotions_by_code),
    DEACTIVATE_PROMOTION_BY_UUID(R.string.client_deactivate_promotions_by_uuid),
    DEACTIVATE_PROMOTION_BY_CODE(R.string.client_deactivate_promotions_by_code),
    CLIENT_GET_OR_ASSIGN_VOUCHER(R.string.get_or_assign_voucher),
    CLIENT_ASSIGN_VOUCHER_CODE(R.string.assign_voucher_code),
    CLIENT_GET_ASSIGNED_VOUCHER_CODES(R.string.get_assigned_voucher_codes);

    @StringRes
    private final int title;

    SyneriseSdkApi(@StringRes int title) {
        this.title = title;
    }

    @StringRes
    public int getTitle() {
        return title;
    }

    public static List<SyneriseSdkApi> getClientApis() {
        return Arrays.asList(REGISTER_CLIENT,
                AUTHENTICATE_OAUTH,
                ACTIVATE_CLIENT,
                CONFIRM_CLIENT,
                RESET_PASSWORD,
                CONFIRM_RESET,
                GET_ACCOUNT,
                UPDATE_ACCOUNT,
                DELETE_ACCOUNT,
                CHANGE_PASSWORD,
                CLIENT_GET_TOKEN,
                CLIENT_REGENERATE_UUID,
                REQUEST_PHONE_UPDATE,
                CONFIRM_PHONE_UPDATE,
                REQUEST_EMAIL_CHANGE,
                CONFIRM_EMAIL_CHANGE);
    }

    public static List<SyneriseSdkApi> getPromotionApis() {
        return Arrays.asList(CLIENT_GET_PROMOTIONS,
                ACTIVATE_PROMOTION_BY_UUID,
                ACTIVATE_PROMOTION_BY_CODE,
                DEACTIVATE_PROMOTION_BY_CODE,
                DEACTIVATE_PROMOTION_BY_UUID,
                CLIENT_GET_OR_ASSIGN_VOUCHER,
                CLIENT_ASSIGN_VOUCHER_CODE,
                CLIENT_GET_ASSIGNED_VOUCHER_CODES);
    }
}