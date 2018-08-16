package com.synerise.sdk.sample.ui.dev.client.pager.pages;

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
import com.synerise.sdk.injector.net.exception.InvalidPhoneNumberException;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientConfirmPhoneUpdateFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputPhone;
    private TextInputLayout inputCode;

    public static ClientConfirmPhoneUpdateFragment newInstance() { return new ClientConfirmPhoneUpdateFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_confirm_phone_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPhone = view.findViewById(R.id.input_phone);
        inputCode = view.findViewById(R.id.input_code);
        view.findViewById(R.id.phone_update).setOnClickListener(v -> confirmPhoneUpdate());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void confirmPhoneUpdate() {

        inputPhone.setError(null);
        inputCode.setError(null);

        boolean isValid = true;

        String phone = inputPhone.getEditText().getText().toString();
        String code = inputCode.getEditText().getText().toString();

        try {
            apiCall = Client.confirmPhoneUpdate(phone, code);
        } catch (InvalidPhoneNumberException e) {
            isValid = false;
            inputPhone.setError(getString(R.string.error_invalid_phone));
        }

        if (ValidationUtils.isEmpty(code)) {
            isValid = false;
            inputCode.setError(getString(R.string.error_empty));
        }

        if (isValid && apiCall != null) {
            apiCall.cancel();
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}