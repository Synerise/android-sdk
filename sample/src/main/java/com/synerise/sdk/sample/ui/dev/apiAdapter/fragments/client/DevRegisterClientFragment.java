package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.client.RegisterClient;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevRegisterClientFragment extends BaseDevFragment {

    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private CheckBox registerWithoutActivation;

    private IApiCall call;

    public static DevRegisterClientFragment newInstance() { return new DevRegisterClientFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_register_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.input_email_register);
        inputPassword = view.findViewById(R.id.input_password_register);
        registerWithoutActivation = view.findViewById(R.id.register_without_activation);
        view.findViewById(R.id.register_client).setOnClickListener(v -> registerClient());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void registerClient() {
        String email = inputEmail.getEditText().getText().toString();
        String password = inputPassword.getEditText().getText().toString();
        RegisterClient registerClient = new RegisterClient().setEmail(email).setPassword(password);
        if (call != null) call.cancel();
        call = registerWithoutActivation.isChecked() ?
                Client.registerAccountWithoutActivation(registerClient) :
                Client.registerAccount(registerClient);
        EspressoTestingIdlingResource.increment();
        call.execute(this::onSuccess, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {
                onFailure(apiError);
            }
        });
    }
}