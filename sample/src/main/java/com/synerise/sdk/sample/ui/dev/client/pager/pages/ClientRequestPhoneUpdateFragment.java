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
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidPhoneNumberException;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientRequestPhoneUpdateFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputPhone;

    public static ClientRequestPhoneUpdateFragment newInstance() { return new ClientRequestPhoneUpdateFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_request_phone_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPhone = view.findViewById(R.id.input_phone);
        view.findViewById(R.id.phone_update).setOnClickListener(v -> requestPhoneUpdate());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void requestPhoneUpdate() {

        inputPhone.setError(null);

        boolean isValid = true;

        String phone = inputPhone.getEditText().getText().toString();

        try {
            apiCall = Client.requestPhoneUpdate(phone);
        } catch (InvalidPhoneNumberException e) {
            isValid = false;
            inputPhone.setError(getString(R.string.error_invalid_phone));
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