package com.synerise.sdk.sample.ui.dev.profile.pager.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.client.ClientProfile;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevGetClientFragment extends BaseDevFragment {

    private TextInputLayout inputEmail;
    private IDataApiCall<ClientProfile> getClientCall;

    public static DevGetClientFragment newInstance() { return new DevGetClientFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_get_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.input_email_get);
        view.findViewById(R.id.get_client).setOnClickListener(v -> getClient());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getClientCall != null) getClientCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void getClient() {

        inputEmail.setError(null);

        String email = inputEmail.getEditText().getText().toString();

        try {
            getClientCall = Profile.getClient(email);
            //textClientEmail.setText(email);
        } catch (InvalidEmailException e) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            getClientCall = null;
        }

        if (getClientCall != null) {
            getClientCall.cancel();
            EspressoTestingIdlingResource.increment();
            getClientCall.execute(clientProfile -> {
                onSuccess();
            }, this::onFailure);
        }
    }
}
