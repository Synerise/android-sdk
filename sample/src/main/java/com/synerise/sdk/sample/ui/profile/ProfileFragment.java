package com.synerise.sdk.sample.ui.profile;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.injector.net.exception.InvalidPasswordException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.client.ClientProfile;
import com.synerise.sdk.profile.model.client.CreateClient;
import com.synerise.sdk.profile.model.client.RegisterClient;
import com.synerise.sdk.profile.model.client.UpdateClient;
import com.synerise.sdk.profile.model.password.PasswordResetConfirmation;
import com.synerise.sdk.profile.model.password.PasswordResetRequest;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.util.SystemUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressWarnings("ConstantConditions")
public class ProfileFragment extends BaseFragment {

    @BindView(R.id.client_id) TextView textClientId;
    @BindView(R.id.client_email) TextView textClientEmail;

    @BindView(R.id.input_email_get) TextInputLayout inputEmailGet;
    @BindView(R.id.get_client) Button getClient;

    @BindView(R.id.input_email_create) TextInputLayout inputEmailCreate;
    @BindView(R.id.input_city_create) TextInputLayout inputCityCreate;
    @BindView(R.id.create_client) Button createClient;

    @BindView(R.id.input_email_register) TextInputLayout inputEmailRegister;
    @BindView(R.id.input_password_register) TextInputLayout inputPasswordRegister;
    @BindView(R.id.register_client) Button registerClient;
    @BindView(R.id.register_client_section) LinearLayout registerClientSection;

    @BindView(R.id.input_id_update) TextInputLayout inputIdUpdate;
    @BindView(R.id.input_name_update) TextInputLayout inputNameUpdate;
    @BindView(R.id.update_client) Button updateClient;

    @BindView(R.id.input_id_delete) TextInputLayout inputIdDelete;
    @BindView(R.id.delete_client) Button deleteClient;

    @BindView(R.id.input_email_reset) TextInputLayout inputEmailReset;
    @BindView(R.id.reset_password) Button resetPassword;

    @BindView(R.id.input_password_confirm) TextInputLayout inputPasswordConfirm;
    @BindView(R.id.input_token_confirm) TextInputLayout inputTokenConfirm;
    @BindView(R.id.confirm_password) Button confirmResetPassword;

    @BindView(R.id.get_token) Button getToken;

    private Unbinder unbinder;

    private IApiCall call;
    private IDataApiCall<String> getTokenCall;
    private IDataApiCall<ClientProfile> getClientCall;

    public static ProfileFragment newInstance() { return new ProfileFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        textClientId.setOnClickListener(v -> SystemUtils.copyTextToClipboard(getContext(), textClientId.getText()));
        textClientEmail.setOnClickListener(v -> SystemUtils.copyTextToClipboard(getContext(), textClientEmail.getText()));

        getClient.setOnClickListener(v -> getClient());
        createClient.setOnClickListener(v -> createClient());
        registerClient.setOnClickListener(v -> registerClient());
        updateClient.setOnClickListener(v -> updateClient());
        deleteClient.setOnClickListener(v -> deleteClient());
        resetPassword.setOnClickListener(v -> resetPassword());
        confirmResetPassword.setOnClickListener(v -> confirmResetPassword());
        getToken.setOnClickListener(v -> getProfileToken());

        if (Client.isSignedIn()) {
            registerClientSection.setVisibility(View.GONE);
        } else {
            registerClientSection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
        if (getTokenCall != null) getTokenCall.cancel();
        if (getClientCall != null) getClientCall.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // ****************************************************************************************************************************************

    private void getClient() {

        inputEmailGet.setError(null);

        String email = inputEmailGet.getEditText().getText().toString();

        try {
            getClientCall = Profile.getClient(email);
            textClientEmail.setText(email);
        } catch (InvalidEmailException e) {
            inputEmailGet.setError(getString(R.string.error_invalid_email));
            getClientCall = null;
        }

        if (getClientCall != null) {
            getClientCall.cancel();

            EspressoTestingIdlingResource.increment();
            getClientCall.execute(clientProfile -> {
                onSuccess();
                textClientId.setText(String.valueOf(clientProfile.getClientId()));
                textClientEmail.setText(clientProfile.getEmail());
            }, this::onFailure);
        }
    }

    private void createClient() {

        boolean isValid = true;

        inputEmailCreate.setError(null);
        inputCityCreate.setError(null);

        String email = inputEmailCreate.getEditText().getText().toString();
        String city = inputCityCreate.getEditText().getText().toString();

        CreateClient createClient = new CreateClient();
        try {
            createClient.setEmail(email);
            textClientEmail.setText(email);
        } catch (InvalidEmailException e) {
            inputEmailCreate.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        if (TextUtils.isEmpty(city)) {
            inputCityCreate.setError(getString(R.string.error_empty));
            isValid = false;
        } else {
            createClient.setCity(city);
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.createClient(createClient);
            EspressoTestingIdlingResource.increment();
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void registerClient() {

        boolean isValid = true;

        inputEmailRegister.setError(null);
        inputPasswordRegister.setError(null);

        String email = inputEmailRegister.getEditText().getText().toString();
        String password = inputPasswordRegister.getEditText().getText().toString();

        RegisterClient registerClient = null;
        try {
            registerClient = new RegisterClient(email, password);
            textClientEmail.setText(email);
        } catch (InvalidEmailException e) {
            inputEmailRegister.setError(getString(R.string.error_invalid_email));
            isValid = false;
        } catch (InvalidPasswordException e) {
            inputPasswordRegister.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.registerClient(registerClient);
            EspressoTestingIdlingResource.increment();
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void updateClient() {

        boolean isValid = true;

        inputNameUpdate.setError(null);
        inputIdUpdate.setError(null);

        String idString = inputIdUpdate.getEditText().getText().toString();
        String name = inputNameUpdate.getEditText().getText().toString();

        UpdateClient updateClient = new UpdateClient();

        if (TextUtils.isEmpty(name)) {
            inputNameUpdate.setError(getString(R.string.error_empty));
            isValid = false;
        } else {
            updateClient.setFirstName(name);
        }

        long id = -1;
        try {
            id = Long.valueOf(idString);
            textClientId.setText(idString);
        } catch (NumberFormatException e) {
            inputIdUpdate.setError(getString(R.string.error_invalid_id));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.updateClient(id, updateClient);
            EspressoTestingIdlingResource.increment();
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void deleteClient() {

        boolean isValid = true;

        inputIdDelete.setError(null);

        String idString = inputIdDelete.getEditText().getText().toString();

        long id = -1;
        try {
            id = Long.valueOf(idString);
            textClientId.setText(idString);
        } catch (NumberFormatException e) {
            inputIdDelete.setError(getString(R.string.error_invalid_id));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.deleteClient(id);
            EspressoTestingIdlingResource.increment();
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void resetPassword() {

        boolean isValid = true;

        inputEmailReset.setError(null);

        String email = inputEmailReset.getEditText().getText().toString();

        PasswordResetRequest resetRequest = null;

        try {
            resetRequest = new PasswordResetRequest(email);
            textClientEmail.setText(email);
        } catch (InvalidEmailException e) {
            inputEmailReset.setError(getString(R.string.error_invalid_email));
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

    private void confirmResetPassword() {

        boolean isValid = true;

        inputTokenConfirm.setError(null);
        inputPasswordConfirm.setError(null);

        String password = inputPasswordConfirm.getEditText().getText().toString();
        String token = inputTokenConfirm.getEditText().getText().toString();

        PasswordResetConfirmation confirmation = null;

        if (TextUtils.isEmpty(token)) {
            inputTokenConfirm.setError(getString(R.string.error_empty));
            isValid = false;
        }

        try {
            confirmation = new PasswordResetConfirmation(password, token);
        } catch (InvalidPasswordException e) {
            inputPasswordConfirm.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.confirmResetPassword(confirmation);
            EspressoTestingIdlingResource.increment();
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    public void getProfileToken() {
        if (getTokenCall != null) getTokenCall.cancel();
        getTokenCall = Profile.getToken();
        EspressoTestingIdlingResource.increment();
        getTokenCall.execute(token -> onSuccess(), this::onFailure);
    }

    // ****************************************************************************************************************************************

    private void onSuccess() {
        EspressoTestingIdlingResource.decrement();
        Snackbar.make(textClientId, R.string.default_success, Snackbar.LENGTH_SHORT).show();
    }

    private void onFailure(ApiError apiError) {
        EspressoTestingIdlingResource.decrement();
        Snackbar.make(textClientId, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
    }
}