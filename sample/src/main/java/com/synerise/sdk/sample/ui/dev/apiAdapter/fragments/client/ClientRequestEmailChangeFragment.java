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
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientRequestEmailChangeFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputEmail, inputPassword, inputUuid;

    public static ClientRequestEmailChangeFragment newInstance() {
        return new ClientRequestEmailChangeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_request_email_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.input_email);
        inputPassword = view.findViewById(R.id.input_password);
        inputUuid = view.findViewById(R.id.input_uuid);
        view.findViewById(R.id.email_change).setOnClickListener(v -> requestEmailChange());
        view.findViewById(R.id.generate_uuid).setOnClickListener(this::onGenerateUuidClicked);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void requestEmailChange() {

        inputPassword.setError(null);
        inputUuid.setError(null);

        boolean isValid = true;

        String email = inputEmail.getEditText().getText().toString();
        String password = inputPassword.getEditText().getText().toString();
        String uuid = inputUuid.getEditText().getText().toString();

        if (TextUtils.isEmpty(password)) {
            isValid = false;
            inputPassword.setError(getString(R.string.error_empty));
        }

        if (TextUtils.isEmpty(uuid)) {
            isValid = false;
            inputUuid.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (apiCall != null) apiCall.cancel();
            apiCall = Client.requestEmailChange(email, password, uuid);
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void onGenerateUuidClicked(View v) {
        inputUuid.getEditText().setText(Client.getUuid());
    }
}