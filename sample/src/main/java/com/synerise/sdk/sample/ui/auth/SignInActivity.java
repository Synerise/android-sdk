package com.synerise.sdk.sample.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.GetAccountInformation;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.injector.net.exception.InvalidPhoneNumberException;
import com.synerise.sdk.profile.LoginType;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignInActivity extends BaseActivity {

    private TextInputLayout textLogin;
    private TextInputLayout textPassword;
    private Button signInButton;
    private ProgressBar signInProgress;

    @Inject AccountManager accountManager;

    private IApiCall signInCall;
    private IDataApiCall<GetAccountInformation> getAccountCall;
    private LoginType loginType = LoginType.EMAIL;

    public static Intent createIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ((App) getApplication()).getComponent().inject(this);

        ToolbarHelper.setUpChildToolbar(this, R.string.sign_in_title);

        textLogin = findViewById(R.id.text_login);
        textPassword = findViewById(R.id.text_password);
        signInButton = findViewById(R.id.sign_in_button);
        signInProgress = findViewById(R.id.sign_in_progress);

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

        signInButton.setOnClickListener(this::onSignInButtonClicked);

        findViewById(R.id.create_new_account).setOnClickListener(v -> startActivity(SignUpActivity.createIntent(this)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (signInCall != null) signInCall.cancel();
        if (getAccountCall != null) getAccountCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void onSignInButtonClicked(View v) {

        textLogin.setError(null);
        textPassword.setError(null);

        String login = textLogin.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        try {
            signIn(login, password);
        } catch (InvalidEmailException e) {
            textLogin.setError(getString(R.string.error_invalid_email));
        } catch (InvalidPhoneNumberException e) {
            textLogin.setError(getString(R.string.error_invalid_phone));
        }
    }

    // ****************************************************************************************************************************************

    private void signIn(String login, String password) throws
                                                       InvalidEmailException,
                                                       InvalidPhoneNumberException {
        if (loginType == LoginType.EMAIL) {
            signInCall = Client.signInByEmail(login, password, null);
        } else if (loginType == LoginType.PHONE) {
            signInCall = Client.signInByPhone(login, password, null);
        }
        if (signInCall != null) {
            signInCall.cancel();
            signInCall.onSubscribe(() -> toggleLoading(true))
                      .execute(() -> onSignInSuccessful(login), new DataActionListener<ApiError>() {
                          @Override
                          public void onDataAction(ApiError apiError) {SignInActivity.this.onSignInFailure(apiError);}
                      });
        }
    }

    private void onSignInSuccessful(String login) {
        if (loginType == LoginType.EMAIL)
            accountManager.setUserEmail(login);
        else if (loginType == LoginType.PHONE)
            accountManager.setUserPhone(login);
        getAccount();
    }

    private void onSignInFailure(ApiError apiError) {
        toggleLoading(false);
        Snackbar.make(textLogin, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
    }

    // ****************************************************************************************************************************************

    private void getAccount() {
        if (getAccountCall != null) getAccountCall.cancel();
        getAccountCall = Client.getAccount();
        getAccountCall.execute(this::onGetAccountSuccessful, this::onSignInFailure);
    }

    private void onGetAccountSuccessful(GetAccountInformation accountInformation) {
        accountManager.setUserName(accountInformation.getFirstName());
        accountManager.setUserLastName(accountInformation.getLastName());
        toggleLoading(false);
        Snackbar.make(textLogin, R.string.default_success, Snackbar.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    // ****************************************************************************************************************************************

    private void toggleLoading(boolean isLoading) {
        if (isLoading) {
            signInProgress.setVisibility(VISIBLE);
            signInButton.setVisibility(GONE);
        } else {
            signInButton.setVisibility(VISIBLE);
            signInProgress.setVisibility(GONE);
        }
    }
}
