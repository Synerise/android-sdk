package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

import java.util.HashMap;

public class ClientRecognizeAnonymusFragment extends BaseDevFragment {

    private TextInputLayout inputEmail, inputCustomIdentify;

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
        view.findViewById(R.id.recognize_anonymus).setOnClickListener(v -> recognizeAnonymous());
    }

    private void recognizeAnonymous() {
        String email = inputEmail.getEditText().getText().toString();
        String customIdentify = inputCustomIdentify.getEditText().getText().toString();
        if (customIdentify.matches("")) {
            customIdentify = null;
        }
        if (email.matches("")) {
            email = null;
        }
        HashMap <String, Object> parameters = new HashMap <String,Object>();
        parameters.put("firstName", "Jan");
        parameters.put("lastName", "Kowalski");
        try {
            Client.recognizeAnonymous(email, customIdentify, parameters);
        } catch (Exception exception) {
            Toast.makeText(Synerise.getApplicationContext(),exception.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
