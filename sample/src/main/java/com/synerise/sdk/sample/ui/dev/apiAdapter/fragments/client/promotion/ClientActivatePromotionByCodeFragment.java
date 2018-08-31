package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.promotion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.utils.ValidationUtils;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientActivatePromotionByCodeFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputCode;

    public static ClientActivatePromotionByCodeFragment newInstance() { return new ClientActivatePromotionByCodeFragment(); }

    // ****************************************************************************************************************************************

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
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void activatePromotion() {

        inputCode.setError(null);

        boolean isValid = true;

        String code = inputCode.getEditText().getText().toString();
        if(ValidationUtils.isEmpty(code)){
            isValid = false;
            inputCode.setError(getString(R.string.error_empty));
        }

        if(isValid) {
            if (apiCall != null) apiCall.cancel();
            apiCall = Client.activatePromotionByCode(code);
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientActivatePromotionByCodeFragment.this.onFailure(apiError);
                }
            });
        }
    }
}
