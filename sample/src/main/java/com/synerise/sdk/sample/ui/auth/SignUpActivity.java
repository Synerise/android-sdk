package com.synerise.sdk.sample.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.injector.net.exception.InvalidPasswordException;
import com.synerise.sdk.injector.net.exception.InvalidPhoneNumberException;
import com.synerise.sdk.profile.LoginType;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.client.RegisterClient;
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

    private LoginType loginType = LoginType.EMAIL;

    private IApiCall signUpCall;

    @Inject AccountManager accountManager;

    public static Intent createIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    // ****************************************************************************************************************************************

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

        ImageView iconMail = findViewById(R.id.icon_mail);
        ImageView iconPhone = findViewById(R.id.icon_phone);

        iconMail.setOnClickListener(v -> {
            loginType = LoginType.EMAIL;
            iconMail.setImageResource(R.drawable.ic_mail_active);
            iconPhone.setImageResource(R.drawable.ic_phone_inactive);

            textLogin.setHint(getString(R.string.hint_email));
            textLogin.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        });

        iconPhone.setOnClickListener(v -> {
            loginType = LoginType.PHONE;
            iconMail.setImageResource(R.drawable.ic_mail_inactive);
            iconPhone.setImageResource(R.drawable.ic_phone_active);

            textLogin.setHint(getString(R.string.hint_phone));
            textLogin.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        });

        signUpButton.setOnClickListener(this::onSignInButtonClicked);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (signUpCall != null) signUpCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void onSignInButtonClicked(View v) {

        textName.setError(null);
        textLastName.setError(null);
        textLogin.setError(null);
        textPassword.setError(null);

        boolean isValid = true;

        String name = textName.getEditText().getText().toString();
        String lastName = textLastName.getEditText().getText().toString();
        String login = textLogin.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        RegisterClient registerClient = new RegisterClient();

        try {
            registerClient = new RegisterClient().setPassword(password);
        } catch (InvalidPasswordException e) {
            textPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }

        if (loginType == LoginType.EMAIL) {
            try {
                registerClient.setEmail(login);
            } catch (InvalidEmailException e) {
                textLogin.setError(getString(R.string.error_invalid_email));
                isValid = false;
            }
        } else if (loginType == LoginType.PHONE) {
            try {
                registerClient.setPhone(login);
            } catch (InvalidPhoneNumberException e) {
                textLogin.setError(getString(R.string.error_invalid_phone));
                isValid = false;
            }
        }

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

    // ****************************************************************************************************************************************

    private void signUp(RegisterClient registerClient) {
        if (signUpCall != null) signUpCall.cancel();
        signUpCall = loginType == LoginType.EMAIL ?
                Profile.registerClientByEmail(registerClient) :
                Profile.registerClientByPhone(registerClient);
        signUpCall.onSubscribe(() -> toggleLoading(true))
                  .doFinally(() -> toggleLoading(false))
                  .execute(() -> onSignUpSuccessful(registerClient.getPhone()), new DataActionListener<ApiError>() {
                      @Override
                      public void onDataAction(ApiError apiError) {
                          onSignUpFailure(apiError);
                      }
                  });
    }

    private void onSignUpSuccessful(String phone) {
        if (loginType == LoginType.EMAIL) {
            Snackbar.make(textLogin, R.string.sign_up_email_success, Snackbar.LENGTH_SHORT).show();
            finish();
        } else if (loginType == LoginType.PHONE) {
            ConfirmNumberDialog confirmNumberDialog = ConfirmNumberDialog.newInstance(phone);
            confirmNumberDialog.show(getSupportFragmentManager(), ConfirmNumberDialog.class.getSimpleName());
        }
    }

    private void onSignUpFailure(ApiError apiError) {
        Snackbar.make(textLogin, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
    }

    // ****************************************************************************************************************************************

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