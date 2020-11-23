package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.promotions.Promotions;
import com.synerise.sdk.promotions.model.promotion.SinglePromotionResponse;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientGetPromotionByUuidFragment extends BaseDevFragment {
    private IDataApiCall<SinglePromotionResponse> getPromotionApiCall;
    private TextInputLayout inputUuid;

    public static ClientGetPromotionByUuidFragment newInstance() { return new ClientGetPromotionByUuidFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_get_promotion_by_uuid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUuid = view.findViewById(R.id.input_uuid);
        view.findViewById(R.id.get_promotion_by_uuid).setOnClickListener(v -> getPromotionByUuid());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getPromotionApiCall != null) getPromotionApiCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void getPromotionByUuid() {

        inputUuid.setError(null);

        boolean isValid = true;

        String uuid = inputUuid.getEditText().getText().toString();
        if (TextUtils.isEmpty(uuid)) {
            isValid = false;
            inputUuid.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (getPromotionApiCall != null) getPromotionApiCall.cancel();
            getPromotionApiCall = Promotions.getPromotionByUuid(uuid);
            getPromotionApiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientGetPromotionByUuidFragment.this.onFailure(apiError);
                }
            });
        }
    }

    private void onSuccess(SinglePromotionResponse singlePromotionResponse) {
    }
}
