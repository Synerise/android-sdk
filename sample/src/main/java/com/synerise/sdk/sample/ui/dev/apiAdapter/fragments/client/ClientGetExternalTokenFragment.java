package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.utils.ValidationUtils;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientGetExternalTokenFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputValue;
    private RadioButton radioUuid, radioEmail, radioCustomId;

    public static ClientGetExternalTokenFragment newInstance() { return new ClientGetExternalTokenFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_get_external_token, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputValue = view.findViewById(R.id.input_value);
        radioUuid = view.findViewById(R.id.radio_uuid);
        radioEmail = view.findViewById(R.id.radio_email);
        radioCustomId = view.findViewById(R.id.radio_custom_id);
        view.findViewById(R.id.create_token_button).setOnClickListener(v -> getPromotions());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void getPromotions() {

        boolean isValid = true;
        inputValue.setError(null);

        String value = inputValue.getEditText().getText().toString();

        if (ValidationUtils.isEmpty(value)) {
            isValid = false;
            inputValue.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (apiCall != null) apiCall.cancel();
            if (radioUuid.isChecked()) {
                apiCall = Client.createAuthTokenByUuid(value);
            } else if (radioEmail.isChecked()) {
                apiCall = Client.createAuthTokenByEmail(value);
            } else if (radioCustomId.isChecked()) {
                apiCall = Client.createAuthTokenByCustomId(value);
            }
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientGetExternalTokenFragment.this.onFailure(apiError);
                }
            });
        }
    }
}