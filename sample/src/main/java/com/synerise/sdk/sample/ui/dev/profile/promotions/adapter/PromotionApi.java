package com.synerise.sdk.sample.ui.dev.profile.promotions.adapter;

import android.support.annotation.StringRes;

import com.synerise.sdk.sample.R;

public enum PromotionApi {

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
    ASSIGN_VOUCHER(R.string.profile_assign_voucher);

    // ****************************************************************************************************************************************

    @StringRes private final int title;

    PromotionApi(@StringRes int title) {
        this.title = title;
    }

    @StringRes
    public int getTitle() {
        return title;
    }
}
