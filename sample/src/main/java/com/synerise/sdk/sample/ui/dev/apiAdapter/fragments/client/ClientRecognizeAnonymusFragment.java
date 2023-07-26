package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.simpleAuth.ClientData;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.types.enums.ClientSignOutMode;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

import java.util.HashMap;

public class ClientRecognizeAnonymusFragment extends BaseDevFragment {

    private TextInputLayout inputEmail, inputCustomIdentify, inputAuthId;

    public static ClientRecognizeAnonymusFragment newInstance() {
        return new ClientRecognizeAnonymusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_recognize_anonymus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.input_email);
        inputCustomIdentify = view.findViewById(R.id.input_custom_identify);
        inputAuthId = view.findViewById(R.id.input_auth_id);
        view.findViewById(R.id.recognize_anonymus).setOnClickListener(v -> recognizeAnonymous());
        view.findViewById(R.id.sign_out).setOnClickListener(v -> signOut());
    }

    private void recognizeAnonymous() {
        String email = inputEmail.getEditText().getText().toString();
        String customIdentify = inputCustomIdentify.getEditText().getText().toString();
        String authId = inputAuthId.getEditText().getText().toString();
        if (customIdentify.matches("")) {
            customIdentify = null;
        }
        if (email.matches("")) {
            email = null;
        }
        try {
            ClientData data = new ClientData();
            data.setCustomId(customIdentify).setEmail(email);
            Client.simpleAuthentication(data, authId).execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError data) {
                    onFailure(data);
                }
            });
        } catch (Exception exception) {
            Toast.makeText(Synerise.getApplicationContext(),exception.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void signOut() {
        Client.signOut(ClientSignOutMode.SIGN_OUT, false).execute(new ActionListener() {
            @Override
            public void onAction() {
                onSuccess();
            }
        }, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError data) {
                onFailure(data);
            }
        });
    }
}
