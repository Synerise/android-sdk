package com.synerise.sdk.sample.ui.dev.apiAdapter;

import android.support.annotation.StringRes;

import com.synerise.sdk.sample.R;

import java.util.Arrays;
import java.util.List;

public enum SyneriseSdkApi {

    // PROFILE API
    GET_CLIENT(R.string.profile_get_client),
    CREATE_CLIENT(R.string.profile_client_create),
    REGISTER_CLIENT(R.string.profile_client_register),
    UPDATE_CLIENT(R.string.profile_client_update),
    DELETE_CLIENT(R.string.profile_client_delete),
    ACTIVATE_CLIENT(R.string.profile_client_activate),
    RESET_PASSWORD(R.string.profile_password_reset),
    CONFIRM_RESET(R.string.profile_password_confirm),
    PROFILE_GET_TOKEN(R.string.get_token),
    PROFILE_PROMOTIONS(R.string.default_promotion_apis, true),
    PROFILE_VOUCHERS(R.string.default_vouchers_api, true),

    // PROFILE PROMOTION API
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
    REDEEM_PROMOTIONS_BY_CLIENT_ID(R.string.profile_redeem_promotions_by_client_id),

    // PROFILE VOUCHERS API
    PROFILE_GET_OR_ASSIGN_VOUCHER(R.string.get_or_assign_voucher),
    PROFILE_GET_VOUCHER_CODES(R.string.get_voucher_codes),
    PROFILE_ASSIGN_VOUCHER_CODE(R.string.assign_voucher_code),

    // CLIENT API
    GET_ACCOUNT(R.string.get_account),
    UPDATE_ACCOUNT(R.string.update_account),
    DELETE_ACCOUNT(R.string.delete_account),
    CHANGE_PASSWORD(R.string.client_change_password),
    CLIENT_GET_TOKEN(R.string.get_token),
    CLIENT_PROMOTIONS(R.string.default_promotion_apis, true),
    CLIENT_VOUCHERS(R.string.default_vouchers_api, true),
    REQUEST_PHONE_UPDATE(R.string.client_phone_update),
    CONFIRM_PHONE_UPDATE(R.string.client_confirm_phone_update),

    // CLIENT PROMOTION API
    CLIENT_GET_PROMOTIONS(R.string.client_get_promotions),
    ACTIVATE_PROMOTION_BY_UUID(R.string.client_activate_promotions_by_uuid),
    ACTIVATE_PROMOTION_BY_CODE(R.string.client_activate_promotions_by_code),
    DEACTIVATE_PROMOTION_BY_UUID(R.string.client_deactivate_promotions_by_uuid),
    DEACTIVATE_PROMOTION_BY_CODE(R.string.client_deactivate_promotions_by_code),

    // CLIENT VOUCHERS API
    CLIENT_GET_OR_ASSIGN_VOUCHER(R.string.get_or_assign_voucher),
    CLIENT_ASSIGN_VOUCHER_CODE(R.string.assign_voucher_code),
    CLIENT_GET_ASSIGNED_VOUCHER_CODES(R.string.get_assigned_voucher_codes);

    @StringRes private final int title;
    private final boolean isGroup;

    SyneriseSdkApi(@StringRes int title) {
        this.title = title;
        this.isGroup = false;
    }

    SyneriseSdkApi(@StringRes int title, boolean isGroup) {
        this.title = title;
        this.isGroup = isGroup;
    }

    @StringRes
    public int getTitle() {
        return title;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public static List<SyneriseSdkApi> getProfileApis() {
        return Arrays.asList(GET_CLIENT,
                             CREATE_CLIENT,
                             REGISTER_CLIENT,
                             UPDATE_CLIENT,
                             DELETE_CLIENT,
                             ACTIVATE_CLIENT,
                             RESET_PASSWORD,
                             CONFIRM_RESET,
                             PROFILE_GET_TOKEN,
                             PROFILE_PROMOTIONS,
                             PROFILE_VOUCHERS);
    }

    public static List<SyneriseSdkApi> getPromotionApis() {
        return Arrays.asList(GET_PROMOTIONS,
                             GET_PROMOTIONS_BY_EXTERNAL_ID,
                             GET_PROMOTIONS_BY_PHONE,
                             GET_PROMOTIONS_BY_CLIENT_ID,
                             GET_PROMOTIONS_BY_EMAIL,
                             GET_PROMOTIONS_BY_CODE,
                             GET_PROMOTIONS_BY_UUID,
                             REDEEM_PROMOTIONS_BY_PHONE,
                             REDEEM_PROMOTIONS_BY_EMAIL,
                             REDEEM_PROMOTIONS_BY_CUSTOM_ID,
                             REDEEM_PROMOTIONS_BY_CLIENT_ID);
    }

    public static List<SyneriseSdkApi> getClientApis() {
        return Arrays.asList(GET_ACCOUNT,
                             UPDATE_ACCOUNT,
                             DELETE_ACCOUNT,
                             CHANGE_PASSWORD,
                             CLIENT_GET_TOKEN,
                             REQUEST_PHONE_UPDATE,
                             CONFIRM_PHONE_UPDATE,
                             CLIENT_PROMOTIONS,
                             CLIENT_VOUCHERS);
    }

    public static List<SyneriseSdkApi> getClientPromotionApis() {
        return Arrays.asList(CLIENT_GET_PROMOTIONS,
                             ACTIVATE_PROMOTION_BY_UUID,
                             ACTIVATE_PROMOTION_BY_CODE);
    }

    public static List<SyneriseSdkApi> getClientVoucherApis() {
        return Arrays.asList(CLIENT_GET_OR_ASSIGN_VOUCHER,
                             CLIENT_ASSIGN_VOUCHER_CODE,
                             CLIENT_GET_ASSIGNED_VOUCHER_CODES);
    }

    public static List<SyneriseSdkApi> getProfileVoucherApis() {
        return Arrays.asList(PROFILE_GET_OR_ASSIGN_VOUCHER,
                             PROFILE_GET_VOUCHER_CODES,
                             PROFILE_ASSIGN_VOUCHER_CODE);
    }
}
