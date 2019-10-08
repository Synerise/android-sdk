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
import com.synerise.sdk.client.model.password.PasswordResetConfirmation;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevConfirmResetFragment extends BaseDevFragment {

    private TextInputLayout inputToken;
    private TextInputLayout inputPassword;

    private IApiCall call;

    public static DevConfirmResetFragment newInstance() { return new DevConfirmResetFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_confirm_reset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputToken = view.findViewById(R.id.input_token_confirm);
        inputPassword = view.findViewById(R.id.input_password_confirm);
        view.findViewById(R.id.confirm_password).setOnClickListener(v -> confirmResetPassword());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void confirmResetPassword() {

        boolean isValid = true;

        inputToken.setError(null);
        inputPassword.setError(null);

        String password = inputPassword.getEditText().getText().toString();
        String token = inputToken.getEditText().getText().toString();

        PasswordResetConfirmation confirmation = new PasswordResetConfirmation(password, token);

        if (TextUtils.isEmpty(token)) {
            inputToken.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Client.confirmPasswordReset(confirmation);
            EspressoTestingIdlingResource.increment();
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}