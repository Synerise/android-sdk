package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientConfirmEmailChangeFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputPassword, inputToken;
    private CheckBox newsletterAgreement;

    public static ClientConfirmEmailChangeFragment newInstance() {
        return new ClientConfirmEmailChangeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_confirm_email_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPassword = view.findViewById(R.id.input_password);
        inputToken = view.findViewById(R.id.input_token);
        newsletterAgreement = view.findViewById(R.id.newsletter_agreement);
        view.findViewById(R.id.email_change).setOnClickListener(v -> confirmEmailChange());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void confirmEmailChange() {

        inputPassword.setError(null);
        inputToken.setError(null);

        boolean isValid = true;

        String password = inputPassword.getEditText().getText().toString();
        String token = inputToken.getEditText().getText().toString();

        if (TextUtils.isEmpty(password)) {
            isValid = false;
            inputPassword.setError(getString(R.string.error_empty));
        }

        if (TextUtils.isEmpty(token)) {
            isValid = false;
            inputToken.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (apiCall != null) apiCall.cancel();
            apiCall = Client.confirmEmailChange(password, token, newsletterAgreement.isChecked());
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}