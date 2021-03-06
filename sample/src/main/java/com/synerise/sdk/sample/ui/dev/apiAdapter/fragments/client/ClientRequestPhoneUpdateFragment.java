package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientRequestPhoneUpdateFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputPhone;

    public static ClientRequestPhoneUpdateFragment newInstance() { return new ClientRequestPhoneUpdateFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_request_phone_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPhone = view.findViewById(R.id.input_phone);
        view.findViewById(R.id.phone_update).setOnClickListener(v -> requestPhoneUpdate());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void requestPhoneUpdate() {

        String phone = inputPhone.getEditText().getText().toString();

        if (apiCall != null) apiCall.cancel();
        apiCall = Client.requestPhoneUpdate(phone);
        apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {
                onFailure(apiError);
            }
        });
    }
}