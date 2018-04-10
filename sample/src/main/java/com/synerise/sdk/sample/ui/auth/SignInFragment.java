package com.synerise.sdk.sample.ui.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.AccountInformation;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.injector.net.exception.InvalidPasswordException;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.dashboard.IDashboardContext;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignInFragment extends BaseFragment {

    @BindView(R.id.text_email) TextInputLayout textEmail;
    @BindView(R.id.text_password) TextInputLayout textPassword;
    @BindView(R.id.sign_in_button) Button signInButton;
    @BindView(R.id.sign_in_progress) ProgressBar signInProgress;
    private Unbinder unbinder;

    @Inject AccountManager accountManager;

    private IDashboardContext contextCallback = IDashboardContext.NULL;
    private IApiCall signInCall;
    private IDataApiCall<AccountInformation> getAccountCall;

    public static SignInFragment newInstance() { return new SignInFragment(); }

    // ****************************************************************************************************************************************

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contextCallback = (IDashboardContext) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contextCallback = IDashboardContext.NULL;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        signInButton.setOnClickListener(this::onSignInButtonClicked);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (signInCall != null) signInCall.cancel();
        if (getAccountCall != null) getAccountCall.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void onSignInButtonClicked(View v) {

        textEmail.setError(null);
        textPassword.setError(null);

        String email = textEmail.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        signIn(email, password);
    }

    private void signIn(String email, String password) {
        try {
            signInCall = Client.signIn(email, password, null);
        } catch (InvalidEmailException e) {
            textEmail.setError(getString(R.string.error_invalid_email));
        } catch (InvalidPasswordException e) {
            textPassword.setError(getString(R.string.error_invalid_password));
        }
        if (signInCall != null) {
            signInCall.cancel();
            signInCall.onSubscribe(() -> toggleLoading(true))
                      .execute(() -> onSignInSuccessful(email), new DataActionListener<ApiError>() {
                          @Override
                          public void onDataAction(ApiError apiError) {SignInFragment.this.onSignInFailure(apiError);}
                      });
        }
    }

    // ****************************************************************************************************************************************

    private void onSignInSuccessful(String email) {
        accountManager.setSignedIn(true);
        accountManager.setUserEmail(email);
        getAccount();
    }

    private void onSignInFailure(ApiError apiError) {
        toggleLoading(false);
        Snackbar.make(textEmail, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
    }

    // ****************************************************************************************************************************************

    private void getAccount() {
        if (getAccountCall != null) getAccountCall.cancel();
        getAccountCall = Client.getAccount();
        getAccountCall.execute(this::onGetAccountSuccessful, this::onSignInFailure);
    }

    // ****************************************************************************************************************************************

    private void onGetAccountSuccessful(AccountInformation accountInformation) {
        accountManager.setUserName(accountInformation.getFirstName());
        accountManager.setUserLastName(accountInformation.getLastName());
        toggleLoading(false);
        Snackbar.make(textEmail, R.string.default_success, Snackbar.LENGTH_SHORT).show();
        contextCallback.handleSignInSuccess();
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