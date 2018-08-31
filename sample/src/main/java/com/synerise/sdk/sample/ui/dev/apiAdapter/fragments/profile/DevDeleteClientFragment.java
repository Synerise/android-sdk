package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevDeleteClientFragment extends BaseDevFragment {

    private TextInputLayout inputId;

    private IApiCall call;

    public static DevDeleteClientFragment newInstance() { return new DevDeleteClientFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_delete_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputId = view.findViewById(R.id.input_id_delete);
        view.findViewById(R.id.delete_client).setOnClickListener(v -> deleteClient());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void deleteClient() {

        boolean isValid = true;

        inputId.setError(null);

        String idString = inputId.getEditText().getText().toString();

        long id = -1;
        try {
            id = Long.valueOf(idString);
            //textClientId.setText(idString);
        } catch (NumberFormatException e) {
            inputId.setError(getString(R.string.error_invalid_id));
            isValid = false;
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.deleteClient(id);
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