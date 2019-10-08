package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.promotions.Promotions;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientDeactivatePromotionByCodeFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputCode;

    public static ClientDeactivatePromotionByCodeFragment newInstance() { return new ClientDeactivatePromotionByCodeFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_deactivate_promotion_by_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputCode = view.findViewById(R.id.input_code);
        view.findViewById(R.id.deactivate_promotion).setOnClickListener(v -> activatePromotion());
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
            apiCall = Promotions.deactivatePromotionByCode(code);
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientDeactivatePromotionByCodeFragment.this.onFailure(apiError);
                }
            });
        }
    }
}
