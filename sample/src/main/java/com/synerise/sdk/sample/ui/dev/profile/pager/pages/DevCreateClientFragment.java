package com.synerise.sdk.sample.ui.dev.profile.pager.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.net.exception.InvalidEmailException;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.client.CreateClient;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevCreateClientFragment extends BaseDevFragment {

    private TextInputLayout inputEmail;
    private TextInputLayout inputCity;

    private IApiCall call;

    public static DevCreateClientFragment newInstance() { return new DevCreateClientFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_create_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.input_email_create);
        inputCity = view.findViewById(R.id.input_city_create);
        view.findViewById(R.id.create_client).setOnClickListener(v -> createClient());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void createClient() {

        boolean isValid = true;

        inputEmail.setError(null);
        inputCity.setError(null);

        String email = inputEmail.getEditText().getText().toString();
        String city = inputCity.getEditText().getText().toString();

        CreateClient createClient = new CreateClient();

        try {
            createClient.setEmail(email);
            //textClientEmail.setText(email);
        } catch (InvalidEmailException e) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        if (TextUtils.isEmpty(city)) {
            inputCity.setError(getString(R.string.error_empty));
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
}