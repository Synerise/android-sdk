package com.synerise.sdk.sample.ui.dev.profile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.utils.ValidationUtils;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class RedeemPromotionByCustomIdFragment extends BaseDevFragment {

    private TextInputLayout inputCustomId;
    private TextInputLayout inputPromotionCode;


    private IApiCall call;

    public static RedeemPromotionByCustomIdFragment newInstance() { return new RedeemPromotionByCustomIdFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_redeem_promotion_by_custom_id, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputCustomId = view.findViewById(R.id.input_custom_id);
        inputPromotionCode = view.findViewById(R.id.input_promotion_code);
        view.findViewById(R.id.redeem_custom_id).setOnClickListener(v -> getPromotion());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void getPromotion() {
        boolean isValid = true;

        inputCustomId.setError(null);

        String customId = inputCustomId.getEditText().getText().toString();
        String promotionCode = inputPromotionCode.getEditText().getText().toString();

        if(ValidationUtils.isEmpty(customId)){
            isValid = false;
            inputCustomId.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.redeemPromotionByCustomId(promotionCode, customId);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}