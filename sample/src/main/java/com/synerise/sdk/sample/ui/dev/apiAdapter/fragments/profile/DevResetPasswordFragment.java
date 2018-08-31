package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile;

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
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.password.PasswordResetRequest;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevResetPasswordFragment extends BaseDevFragment {

    private TextInputLayout inputEmail;

    private IApiCall call;

    public static DevResetPasswordFragment newInstance() { return new DevResetPasswordFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.input_email_reset);
        view.findViewById(R.id.reset_password).setOnClickListener(v -> resetPassword());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void resetPassword() {

        boolean isValid = true;

        inputEmail.setError(null);

        String email = inputEmail.getEditText().getText().toString();

        PasswordResetRequest resetRequest = null;

        try {
            resetRequest = new PasswordResetRequest(email);
            //textClientEmail.setText(email);
        } catch (InvalidEmailException e) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.requestPasswordReset(resetRequest);
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