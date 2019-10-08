package com.synerise.sdk.sample.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.client.RegisterClient;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignUpActivity extends BaseActivity implements OnPhoneConfirmedListener {

    private TextInputLayout textName;
    private TextInputLayout textLastName;
    private TextInputLayout textLogin;
    private TextInputLayout textPassword;
    private Button signUpButton;
    private ProgressBar signUpProgress;

    private IApiCall signUpCall;

    @Inject AccountManager accountManager;

    public static Intent createIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ((App) getApplication()).getComponent().inject(this);

        ToolbarHelper.setUpChildToolbar(this, R.string.sign_up_title);

        textName = findViewById(R.id.text_name);
        textLastName = findViewById(R.id.text_last_name);
        textLogin = findViewById(R.id.text_login);
        textPassword = findViewById(R.id.text_password);
        signUpButton = findViewById(R.id.sign_up_button);
        signUpProgress = findViewById(R.id.sign_up_progress);

        signUpButton.setOnClickListener(this::onSignInButtonClicked);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (signUpCall != null) signUpCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void onSignInButtonClicked(View v) {

        textName.setError(null);
        textLastName.setError(null);

        boolean isValid = true;

        String name = textName.getEditText().getText().toString();
        String lastName = textLastName.getEditText().getText().toString();
        String login = textLogin.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        RegisterClient registerClient = new RegisterClient().setPassword(password).setEmail(login);

        if (TextUtils.isEmpty(name)) {
            textName.setError(getString(R.string.error_empty));
            isValid = false;
        } else if (registerClient != null) {
            registerClient.setFirstName(name);
        }

        if (TextUtils.isEmpty(lastName)) {
            textLastName.setError(getString(R.string.error_empty));
            isValid = false;
        } else if (registerClient != null) {
            registerClient.setLastName(lastName);
        }

        if (isValid) {
            signUp(registerClient);
        }
    }

    private void signUp(RegisterClient registerClient) {
        if (signUpCall != null) signUpCall.cancel();
        signUpCall = Client.registerAccount(registerClient);
        signUpCall.onSubscribe(() -> toggleLoading(true))
                  .doFinally(() -> toggleLoading(false))
                  .execute(this::onSignUpSuccessful, new DataActionListener<ApiError>() {
                      @Override
                      public void onDataAction(ApiError apiError) {
                          onSignUpFailure(apiError);
                      }
                  });
    }

    private void onSignUpSuccessful() {
        Toast.makeText(this, R.string.sign_up_email_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onSignUpFailure(ApiError apiError) {
        showAlertError(apiError);
    }

    private void toggleLoading(boolean isLoading) {
        if (isLoading) {
            signUpProgress.setVisibility(VISIBLE);
            signUpButton.setVisibility(GONE);
        } else {
            signUpButton.setVisibility(VISIBLE);
            signUpProgress.setVisibility(GONE);
        }
    }

    @Override
    public void onPhoneNumberConfirmed() {
        Snackbar.make(textLogin, R.string.default_success, Snackbar.LENGTH_SHORT).show();
        finish();
    }
}