package com.synerise.sdk.sample.ui.dev.profile.pager.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.injector.net.exception.InvalidPasswordException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.client.RegisterClient;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevRegisterClientFragment extends BaseDevFragment {

    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;

    private IApiCall call;

    public static DevRegisterClientFragment newInstance() { return new DevRegisterClientFragment(); }

    // ****************************************************************************************************************************************

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
        view.findViewById(R.id.register_client).setOnClickListener(v -> registerClient());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void registerClient() {

        boolean isValid = true;

        inputEmail.setError(null);
        inputPassword.setError(null);

        String email = inputEmail.getEditText().getText().toString();
        String password = inputPassword.getEditText().getText().toString();

        RegisterClient registerClient = null;
        try {
            registerClient = new RegisterClient().setEmail(email)
                                                 .setPassword(password);
        } catch (InvalidEmailException e) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        } catch (InvalidPasswordException e) {
            inputPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.registerClientByEmail(registerClient);
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