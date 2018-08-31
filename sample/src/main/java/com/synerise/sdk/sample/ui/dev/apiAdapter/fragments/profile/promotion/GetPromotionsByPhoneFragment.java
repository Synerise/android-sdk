package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.promotion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.core.utils.ValidationUtils;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidPhoneNumberException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.promotion.ProfilePromotionResponse;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class GetPromotionsByPhoneFragment extends BaseDevFragment {

    private TextInputLayout inputPhone;

    private IDataApiCall<ProfilePromotionResponse> call;

    public static GetPromotionsByPhoneFragment newInstance() { return new GetPromotionsByPhoneFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_get_promotion_by_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPhone = view.findViewById(R.id.input_phone);
        view.findViewById(R.id.get_promotion).setOnClickListener(v -> getPromotion());
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

        inputPhone.setError(null);

        String phone = inputPhone.getEditText().getText().toString();

        try {
            ValidationUtils.validatePhoneNumber(phone);
        } catch (InvalidPhoneNumberException e) {
            isValid = false;
            inputPhone.setError(getString(R.string.error_invalid_phone));
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.getPromotionsByPhone(phone);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void onSuccess(ProfilePromotionResponse profilePromotionResponse) {
        super.onSuccess();
    }
}