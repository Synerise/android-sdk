package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion;

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
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class RedeemPromotionByEmailFragment extends BaseDevFragment {

    private TextInputLayout inputEmail;
    private TextInputLayout inputPromotionCode;

    private IApiCall call;

    public static RedeemPromotionByEmailFragment newInstance() { return new RedeemPromotionByEmailFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_redeem_promotion_by_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.input_email);
        inputPromotionCode = view.findViewById(R.id.input_promotion_code);
        view.findViewById(R.id.redeem_email).setOnClickListener(v -> getPromotion());
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

        inputEmail.setError(null);

        String email = inputEmail.getEditText().getText().toString();
        String promotionCode = inputPromotionCode.getEditText().getText().toString();

        try {
            ValidationUtils.validateEmail(email);
        } catch (InvalidEmailException e) {
            isValid = false;
            inputEmail.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.redeemPromotionByEmail(promotionCode, email);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}