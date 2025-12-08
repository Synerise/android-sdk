package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.promotions.Promotions;
import com.synerise.sdk.promotions.model.AssignVoucherResponse;
import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.promotions.model.promotion.PromotionActivationKey;
import com.synerise.sdk.promotions.model.promotion.PromotionActivationOptions;
import com.synerise.sdk.promotions.model.promotion.PromotionIdentifier;
import com.synerise.sdk.promotions.model.promotion.SinglePromotionResponse;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientActivatePromotionByCodeFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private IDataApiCall<SinglePromotionResponse> dataApiCall;
    private TextInputLayout inputCode;

    public static ClientActivatePromotionByCodeFragment newInstance() { return new ClientActivatePromotionByCodeFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_activate_promotion_by_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputCode = view.findViewById(R.id.input_code);
        view.findViewById(R.id.activate_promotion).setOnClickListener(v -> activatePromotion());
        view.findViewById(R.id.activate_promotion_response).setOnClickListener(v -> activatePromotionWithResponse());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void activatePromotion() {

        inputCode.setError(null);

        boolean isValid = true;

        String code = inputCode.getEditText().getText().toString();
        if (TextUtils.isEmpty(code)) {
            isValid = false;
            inputCode.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (apiCall != null) apiCall.cancel();
            apiCall = Promotions.activatePromotionByCode(code);
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientActivatePromotionByCodeFragment.this.onFailure(apiError);
                }
            });
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void activatePromotionWithResponse() {

        inputCode.setError(null);

        boolean isValid = true;

        String code = inputCode.getEditText().getText().toString();
        if (TextUtils.isEmpty(code)) {
            isValid = false;
            inputCode.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (dataApiCall != null) dataApiCall.cancel();
            dataApiCall = Promotions.activatePromotion(new PromotionActivationOptions(new PromotionIdentifier(PromotionActivationKey.CODE, code)));
            dataApiCall.execute(this::onSuccessWithResponse, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientActivatePromotionByCodeFragment.this.onFailure(apiError);
                }
            });
        }
    }
    private void onSuccessWithResponse(SinglePromotionResponse response) {
        super.onSuccess();
    }
}
