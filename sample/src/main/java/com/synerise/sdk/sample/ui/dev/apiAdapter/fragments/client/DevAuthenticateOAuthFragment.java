package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevAuthenticateOAuthFragment extends BaseDevFragment {

    private TextInputLayout inputToken;

    private IApiCall call;

    public static DevAuthenticateOAuthFragment newInstance() {
        return new DevAuthenticateOAuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_authenticate_oauth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputToken = view.findViewById(R.id.input_access_token);
        view.findViewById(R.id.authenticate_oauth).setOnClickListener(v -> authenticateByOAuth());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void authenticateByOAuth() {
        boolean isValid = true;

        inputToken.setError(null);

        String token = inputToken.getEditText().getText().toString();

        if (TextUtils.isEmpty(token)) {
            inputToken.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Client.authenticateByOAuth(token, null, null);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }
}