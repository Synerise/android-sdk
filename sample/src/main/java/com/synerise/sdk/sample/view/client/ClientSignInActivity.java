package com.synerise.sdk.sample.view.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;

public class ClientSignInActivity extends AppCompatActivity {

    public static final String TAG = ClientSignInActivity.class.getSimpleName();
    private TextInputLayout emailInput, passwordInput;
    private IApiCall call;

    public static Intent createIntent(Context context) {
        return new Intent(context, ClientSignInActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_sign_in);

        emailInput = findViewById(R.id.client_email_input);
        passwordInput = findViewById(R.id.client_password_input);

        Button signInButton = findViewById(R.id.client_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ClientSignInActivity.this.onSubmitClick();}
        });
    }

    //*****************************************************************************************************************************************

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void onSubmitClick() {

        boolean isValid = true;
        emailInput.setError(null);
        passwordInput.setError(null);

        String email = emailInput.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailInput.setError(getString(R.string.error_required));
            isValid = false;
        }

        String password = passwordInput.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError(getString(R.string.error_required));
            isValid = false;
        }

        if (isValid) {
            String deviceId = null;

            if (call != null) call.cancel();
            call = Client.logIn(email, password, deviceId);
            EspressoTestingIdlingResource.increment();
            call.execute(new ActionListener() {
                @Override
                public void onAction() {onSignInSuccessful();}
            }, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {onSignInFailure(apiError);}
            });
        }
    }

    private void onSignInSuccessful() {
        EspressoTestingIdlingResource.decrement();
        Snackbar.make(emailInput, R.string.message_sing_in_successful, Snackbar.LENGTH_SHORT).show();
        startActivity(ClientAccountActivity.createIntent(this));
    }

    // ****************************************************************************************************************************************

    private void onSignInFailure(ApiError apiError) {
        EspressoTestingIdlingResource.decrement();
        Log.w(TAG, "onSignInFailure " + apiError.getErrorBody());
        Snackbar.make(emailInput, R.string.message_sing_in_failure, Snackbar.LENGTH_SHORT).show();
    }
}