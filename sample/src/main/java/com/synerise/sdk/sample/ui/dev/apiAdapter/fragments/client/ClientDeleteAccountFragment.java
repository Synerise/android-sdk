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

public class ClientDeleteAccountFragment extends BaseDevFragment {

    private IApiCall deleteCall;
    private TextInputLayout inputPassword;

    public static ClientDeleteAccountFragment newInstance() { return new ClientDeleteAccountFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_delete_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPassword = view.findViewById(R.id.input_password);
        view.findViewById(R.id.delete_account).setOnClickListener(v -> deleteAccount());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (deleteCall != null) deleteCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void deleteAccount() {

        boolean isValid = true;

        inputPassword.setError(null);

        String password = inputPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            if (deleteCall != null) deleteCall.cancel();
            deleteCall = Client.deleteAccount(password);
            EspressoTestingIdlingResource.increment();
            deleteCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}