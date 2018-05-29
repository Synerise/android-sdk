package com.synerise.sdk.sample.ui.profile;

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
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.client.ClientProfile;
import com.synerise.sdk.profile.model.client.UpdateClient;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.dashboard.ProfileUpdatedListener;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileFragment extends BaseFragment {

    private IDataApiCall<ClientProfile> getClientCall;
    private IApiCall updateCall;
    private ProfileUpdatedListener profileUpdatedListener;

    private TextInputLayout textName;
    private TextInputLayout textLastName;
    private TextInputLayout textEmail;

    private Button saveChangesButton;
    private ProgressBar saveChangesProgress;

    @Inject AccountManager accountManager;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    // ****************************************************************************************************************************************

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String email = accountManager.getEmail();
        String firstName = accountManager.getFirstName();
        String lastName = accountManager.getLastName();

        textName = view.findViewById(R.id.text_name);
        textName.getEditText().setText(firstName);

        textLastName = view.findViewById(R.id.text_last_name);
        textLastName.getEditText().setText(lastName);

        textEmail = view.findViewById(R.id.text_email);
        textEmail.getEditText().setText(email);

        saveChangesButton = view.findViewById(R.id.save_changes_button);
        saveChangesProgress = view.findViewById(R.id.save_changes_progress);

        saveChangesButton.setOnClickListener(this::saveChangesButtonClicked);

        toggleLoading(false);
    }

    private void saveChangesButtonClicked(View view) {
        textName.setError(null);
        textLastName.setError(null);
        textEmail.setError(null);

        boolean isValid = true;

        String name = textName.getEditText().getText().toString();
        String lastName = textLastName.getEditText().getText().toString();
        String email = textEmail.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)) {
            textName.setError(getString(R.string.error_empty));
            isValid = false;
        }
        if (TextUtils.isEmpty(lastName)) {
            textLastName.setError(getString(R.string.error_empty));
            isValid = false;
        }
        if (TextUtils.isEmpty(email)) {
            textEmail.setError(getString(R.string.error_empty));
            isValid = false;
        }

        UpdateClient updateClient = new UpdateClient();
        updateClient.setFirstName(name);
        updateClient.setLastName(lastName);
        try {
            updateClient.setEmail(email);
        } catch (InvalidEmailException e) {
            e.printStackTrace();
            isValid = false;
            textEmail.setError(getString(R.string.error_invalid_email));
        }

        if (isValid) {
            updateClient(updateClient,email, name, lastName);
        }
    }

    // ****************************************************************************************************************************************

    private void updateClient(UpdateClient updateClient, String email, String name, String lastName) {
        try {
            getClientCall = Profile.getClient(accountManager.getEmail());
        } catch (InvalidEmailException e) {
            e.printStackTrace();
        }

        if (getClientCall != null) {
            getClientCall.cancel();
            getClientCall.onSubscribe(() -> toggleLoading(true))
                    .doFinally(() -> toggleLoading(false))
                    .execute(data -> {
                        updateCall = Profile.updateClient(data.getClientId(),updateClient);
                        updateCall.onSubscribe(() -> toggleLoading(true))
                                .doFinally(() -> toggleLoading(false))
                                .execute(() -> onUpdateClientSuccessful(email,name,lastName), new DataActionListener<ApiError>() {
                                    @Override
                                    public void onDataAction(ApiError data) {
                                        onUpdateClientFailure(data);
                                    }

                                });
                    }, data -> toggleLoading(false));
        }
    }

    private void onUpdateClientSuccessful(String email, String name, String lastName) {
        accountManager.setUserEmail(email);
        accountManager.setUserName(name);
        accountManager.setUserLastName(lastName);
        Snackbar.make(textEmail, R.string.default_success, Snackbar.LENGTH_SHORT).show();
        profileUpdatedListener.profileUpdated();
    }

    private void onUpdateClientFailure(ApiError apiError) {
        Snackbar.make(textEmail, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
    }

    // ****************************************************************************************************************************************

    private void toggleLoading(boolean isLoading) {
        if (isLoading) {
            saveChangesProgress.setVisibility(VISIBLE);
            saveChangesButton.setVisibility(GONE);
        } else {
            saveChangesButton.setVisibility(VISIBLE);
            saveChangesProgress.setVisibility(GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getClientCall.cancel();
        updateCall.cancel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileUpdatedListener) {
            profileUpdatedListener = (ProfileUpdatedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        profileUpdatedListener = ProfileUpdatedListener.NULL;
    }
}
