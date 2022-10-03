package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientChangeApiKeyFragment extends BaseDevFragment {

    private TextInputLayout inputApiKey;

    public static ClientChangeApiKeyFragment newInstance() {
        return new ClientChangeApiKeyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_change_api_key, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputApiKey = view.findViewById(R.id.input_api_key);
        view.findViewById(R.id.change_api_key).setOnClickListener(this::changeApiKey);
    }

    @SuppressWarnings("ConstantConditions")
    private void changeApiKey(View view) {
        boolean isValid = true;

        inputApiKey.setError(null);

        String apiKey = inputApiKey.getEditText().getText().toString();

        if (TextUtils.isEmpty(apiKey)) {
            inputApiKey.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            Client.changeApiKey(apiKey);
            onSuccess();
        }
    }
}