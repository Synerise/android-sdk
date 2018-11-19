package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevActivateClientFragment extends BaseDevFragment {

    private TextInputLayout inputEmail;

    private IApiCall call;

    public static DevActivateClientFragment newInstance() { return new DevActivateClientFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_activate_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.input_email_activate);
        view.findViewById(R.id.activate_client).setOnClickListener(v -> activateClient());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void activateClient() {
        if (call != null) call.cancel();
        String email = inputEmail.getEditText().getText().toString();
        call = Client.activateAccount(email);
        EspressoTestingIdlingResource.increment();
        call.execute(this::onSuccess, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {
                onFailure(apiError);
            }
        });
    }
}