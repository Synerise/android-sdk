package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevConfirmClientFragment extends BaseDevFragment {

    private TextInputLayout inputToken;
    private TextInputLayout inputPin;
    private TextInputLayout inputEmail;

    private IApiCall call;

    public static DevConfirmClientFragment newInstance() {
        return new DevConfirmClientFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_confirm_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputToken = view.findViewById(R.id.input_token_confirm);
        inputPin = view.findViewById(R.id.input_pin_confirm);
        inputEmail = view.findViewById(R.id.input_email_confirm);
        view.findViewById(R.id.confirm_client).setOnClickListener(v -> confirmClient());
        view.findViewById(R.id.request_pin_confirmation).setOnClickListener(v -> requestPin());
        view.findViewById(R.id.confirm_client_by_pin).setOnClickListener(v -> confirmPin());
    }


    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }


    @SuppressWarnings("ConstantConditions")
    private void confirmClient() {
        boolean isValid = true;

        inputToken.setError(null);

        String token = inputToken.getEditText().getText().toString();

        if (TextUtils.isEmpty(token)) {
            inputToken.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Client.confirmAccount(token);
            EspressoTestingIdlingResource.increment();
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void requestPin() {
        boolean isValid = true;

        inputEmail.setError(null);

        String email = inputEmail.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Client.requestAccountActivationByPin(email);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    String code = apiError.getErrorBody().getErrorCode();
                    onFailure(apiError);
                }
            });
        }
    }

    private void confirmPin() {
        boolean isValid = true;

        inputPin.setError(null);
        inputEmail.setError(null);

        String pin = inputPin.getEditText().getText().toString();
        String email = inputEmail.getEditText().getText().toString();

        if (TextUtils.isEmpty(pin)) {
            inputPin.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Client.confirmAccountActivationByPin(pin, email);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}