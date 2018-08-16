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
import com.synerise.sdk.injector.net.exception.InvalidPasswordException;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientChangePasswordFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputPassword;

    public static ClientChangePasswordFragment newInstance() { return new ClientChangePasswordFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPassword = view.findViewById(R.id.input_password);
        view.findViewById(R.id.change_password).setOnClickListener(v -> changePassword());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void changePassword() {

        inputPassword.setError(null);

        boolean isValid = true;

        String password = inputPassword.getEditText().getText().toString();

        try {
            apiCall = Client.changePassword(password);
        } catch (InvalidPasswordException e) {
            isValid = false;
            inputPassword.setError(getString(R.string.error_invalid_password));
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