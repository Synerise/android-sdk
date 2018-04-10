package com.synerise.sdk.sample.ui.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.injector.net.exception.InvalidPasswordException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.client.RegisterClient;
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

public class SignUpFragment extends BaseFragment {

    private IDashboardContext contextCallback = IDashboardContext.NULL;

    @BindView(R.id.text_name) TextInputLayout textName;
    @BindView(R.id.text_last_name) TextInputLayout textLastName;
    @BindView(R.id.text_email) TextInputLayout textEmail;
    @BindView(R.id.text_password) TextInputLayout textPassword;
    @BindView(R.id.sign_up_button) Button signUpButton;
    @BindView(R.id.sign_up_progress) ProgressBar signUpProgress;
    private Unbinder unbinder;

    private IApiCall signUpCall;

    @Inject AccountManager accountManager;

    public static SignUpFragment newInstance() { return new SignUpFragment(); }

    // ****************************************************************************************************************************************

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        signUpButton.setOnClickListener(this::onSignInButtonClicked);
    }

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

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void onSignInButtonClicked(View v) {

        textName.setError(null);
        textLastName.setError(null);
        textEmail.setError(null);
        textPassword.setError(null);

        boolean isValid = true;

        String name = textName.getEditText().getText().toString();
        String lastName = textLastName.getEditText().getText().toString();
        String email = textEmail.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        RegisterClient registerClient = null;

        try {
            registerClient = new RegisterClient(email, password);
        } catch (InvalidEmailException e) {
            textEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        } catch (InvalidPasswordException e) {
            textPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
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
            signUp(registerClient, name, lastName);
        }
    }

    // ****************************************************************************************************************************************

    private void signUp(RegisterClient registerClient, String name, String lastName) {
        if (signUpCall != null) signUpCall.cancel();
        signUpCall = Profile.registerClient(registerClient.setFirstName(name).setLastName(lastName));
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
        Snackbar.make(textEmail, R.string.default_success, Snackbar.LENGTH_SHORT).show();
        contextCallback.handleSignUpSuccess();
    }

    private void onSignUpFailure(ApiError apiError) {
        Snackbar.make(textEmail, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
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

    // ****************************************************************************************************************************************

    @Override
    public void onStop() {
        super.onStop();
        if (signUpCall != null) signUpCall.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}