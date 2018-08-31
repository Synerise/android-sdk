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
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.promotion.ProfilePromotionResponse;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class GetPromotionsByCodeFragment extends BaseDevFragment {

    private TextInputLayout inputCode;

    private IDataApiCall<ProfilePromotionResponse> call;

    public static GetPromotionsByCodeFragment newInstance() { return new GetPromotionsByCodeFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_get_promotion_by_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputCode = view.findViewById(R.id.input_code);
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

        inputCode.setError(null);

        String code = inputCode.getEditText().getText().toString();

        if(ValidationUtils.isEmpty(code)){
            isValid = false;
            inputCode.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.getPromotionByCode(code);
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