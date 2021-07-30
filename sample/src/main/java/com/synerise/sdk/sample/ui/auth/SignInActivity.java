package com.synerise.sdk.sample.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.AuthConditions;
import com.synerise.sdk.client.model.ClientIdentityProvider;
import com.synerise.sdk.client.model.GetAccountInformation;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.core.persistence.manager.CacheManager;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignInActivity extends BaseActivity {

    private TextInputLayout textLogin;
    private TextInputLayout textPassword;
    private Button signInButton, facebookButton;
    private SignInButton googleButton;
    private ProgressBar signInProgress, facebookProgress;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String POINTS = "points";
    private static final int RC_SIGN_IN = 1;

    @Inject
    AccountManager accountManager;
    private CallbackManager callbackManager;

    private IApiCall signInCall, signInFacebookCall, googleSignInCall;
    private IDataApiCall<GetAccountInformation> getAccountCall;
    private IDataApiCall<AuthConditions> conditionalLoginApiCall;

    public static Intent createIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

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
        facebookButton = findViewById(R.id.sign_in_facebook);
        facebookProgress = findViewById(R.id.sign_in_facebook_progress);
        googleButton = findViewById(R.id.sign_in_google_button);

        signInButton.setOnClickListener(this::onSignInButtonClicked);
        facebookButton.setOnClickListener(this::onSignInFacebookButtonClicked);
        googleButton.setOnClickListener(this::onGoogleSignInClicked);

        setUpFacebook();
        setUpGoogle();

        findViewById(R.id.create_new_account).setOnClickListener(v -> startActivity(SignUpActivity.createIntent(this)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (signInCall != null) signInCall.cancel();
        if (signInFacebookCall != null) signInFacebookCall.cancel();
        if (getAccountCall != null) getAccountCall.cancel();
        if (googleSignInCall != null) googleSignInCall.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void setUpGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_login_server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            assert account != null;

            googleSignInCall = Client.authenticate(Objects.requireNonNull(account.getIdToken()), ClientIdentityProvider.GOOGLE, null, null, null);
            googleSignInCall.onSubscribe(() -> toggleLoading(true));
            googleSignInCall.execute(() -> onSignInSuccessful(account.getDisplayName()), new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onSignInFailure(apiError);
                }
            });
        } catch (ApiException e) {
            e.printStackTrace();
            onSignInFailure(new ApiError(Objects.requireNonNull(e.getCause())));
        }
    }

    private void setUpFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance()
                .registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String token = loginResult.getAccessToken().getToken();
                        signInFacebook(token);
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });
    }

    @SuppressWarnings("ConstantConditions")
    private void onSignInButtonClicked(View v) {

        String login = textLogin.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        signIn(login, password);
    }

    private void onSignInFacebookButtonClicked(View v) {
        LoginManager.getInstance().logOut();
        List<String> permissions = Arrays.asList("email", "public_profile", "user_friends");
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }

    private void onGoogleSignInClicked(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signIn(String login, String password) {
        if (signInCall != null) signInCall.cancel();
        signInCall = Client.signIn(login, password);
        signInCall.onSubscribe(() -> toggleLoading(true))
                .execute(() -> onSignInSuccessful(login), new DataActionListener<ApiError>() {
                    @Override
                    public void onDataAction(ApiError apiError) {
                        onSignInFailure(apiError);
                    }
                });
    }

    private void onSignInSuccessful(String login) {
        accountManager.setUserEmail(login);
        getAccount(false);
    }

    private void onSignInFailure(ApiError apiError) {
        toggleLoading(false);
        showAlertError(apiError);
    }

    private void signInFacebook(String facebookToken) {
        if (signInFacebookCall != null) signInFacebookCall.cancel();
        signInFacebookCall = Client.authenticate(facebookToken, ClientIdentityProvider.FACEBOOK, null, null, null);
        signInFacebookCall.onSubscribe(() -> toggleFacebookLoading(true))
                .execute(this::onSignInFacebookSuccess, new DataActionListener<ApiError>() {
                    @Override
                    public void onDataAction(ApiError apiError) {
                        onSignInFacebookError(apiError);
                    }
                });
    }

    private void onSignInFacebookSuccess() {
        getAccount(true);
    }

    private void onSignInFacebookError(ApiError apiError) {
        toggleFacebookLoading(false);
        showAlertError(apiError);
    }

    private void getAccount(boolean isFacebook) {
        if (getAccountCall != null) getAccountCall.cancel();
        getAccountCall = Client.getAccount();
        getAccountCall.execute(this::onGetAccountSuccessful, apiError -> {
            GetAccountInformation model = (GetAccountInformation) CacheManager.getInstance().get(GetAccountInformation.class);
            if (model != null) {
                onGetAccountSuccessful(model);
            } else {
                if (isFacebook) onSignInFacebookError(apiError);
                else onSignInFailure(apiError);
            }
        });
    }

    private void onGetAccountSuccessful(GetAccountInformation accountInformation) {
        accountManager.setUserName(accountInformation.getFirstName());
        accountManager.setUserLastName(accountInformation.getLastName());
        toggleLoading(false);
        Toast.makeText(this, R.string.default_success, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void toggleLoading(boolean isLoading) {
        if (isLoading) {
            signInProgress.setVisibility(VISIBLE);
            signInButton.setVisibility(GONE);
        } else {
            signInButton.setVisibility(VISIBLE);
            signInProgress.setVisibility(GONE);
        }
    }

    private void toggleFacebookLoading(boolean isLoading) {
        if (isLoading) {
            facebookProgress.setVisibility(VISIBLE);
            facebookButton.setVisibility(GONE);
        } else {
            facebookButton.setVisibility(VISIBLE);
            facebookProgress.setVisibility(GONE);
        }
    }
}