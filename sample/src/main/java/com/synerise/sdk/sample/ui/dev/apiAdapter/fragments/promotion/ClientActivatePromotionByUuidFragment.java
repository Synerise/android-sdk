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
import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.promotions.model.promotion.PromotionActivationKey;
import com.synerise.sdk.promotions.model.promotion.PromotionActivationOptions;
import com.synerise.sdk.promotions.model.promotion.PromotionIdentifier;
import com.synerise.sdk.promotions.model.promotion.SinglePromotionResponse;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientActivatePromotionByUuidFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private IDataApiCall<SinglePromotionResponse> dataApiCall;
    private TextInputLayout inputUuid;

    public static ClientActivatePromotionByUuidFragment newInstance() { return new ClientActivatePromotionByUuidFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_activate_promotion_by_uuid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUuid = view.findViewById(R.id.input_uuid);
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

        inputUuid.setError(null);

        boolean isValid = true;

        String uuid = inputUuid.getEditText().getText().toString();
        if (TextUtils.isEmpty(uuid)) {
            isValid = false;
            inputUuid.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (apiCall != null) apiCall.cancel();
            apiCall = Promotions.activatePromotionByUuid(uuid);
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientActivatePromotionByUuidFragment.this.onFailure(apiError);
                }
            });
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void activatePromotionWithResponse() {

        inputUuid.setError(null);

        boolean isValid = true;

        String uuid = inputUuid.getEditText().getText().toString();
        if (TextUtils.isEmpty(uuid)) {
            isValid = false;
            inputUuid.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (dataApiCall != null) dataApiCall.cancel();
            PromotionActivationOptions options = new PromotionActivationOptions(new PromotionIdentifier(PromotionActivationKey.UUID, uuid));
            options.pointsToUse = 12;
            dataApiCall = Promotions.activatePromotion(options);
            dataApiCall.execute(this::onSuccessWithResponse, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientActivatePromotionByUuidFragment.this.onFailure(apiError);
                }
            });
        }
    }

    private void onSuccessWithResponse(SinglePromotionResponse response) {
        super.onSuccess();
    }
}
