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

public class RedeemPromotionByClientIdFragment extends BaseDevFragment {

    private TextInputLayout inputClientId;
    private TextInputLayout inputPromotionCode;

    private IApiCall call;

    public static RedeemPromotionByClientIdFragment newInstance() { return new RedeemPromotionByClientIdFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_redeem_promotion_by_client_id, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputClientId = view.findViewById(R.id.input_client_id);
        inputPromotionCode = view.findViewById(R.id.input_promotion_code);
        view.findViewById(R.id.redeem_client_id).setOnClickListener(v -> getPromotion());
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

        inputClientId.setError(null);

        String clientId = inputClientId.getEditText().getText().toString();
        String promotionCode = inputPromotionCode.getEditText().getText().toString();

        if (ValidationUtils.isEmpty(clientId)) {
            isValid = false;
            inputClientId.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.redeemPromotionByClientId(promotionCode, clientId);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}