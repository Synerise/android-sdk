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
import com.synerise.sdk.client.model.UpdateAccountInformation;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientUpdateAccountFragment extends BaseDevFragment {

    private TextInputLayout inputCity;
    private TextInputLayout inputCompany;

    private IApiCall apiCall;

    public static ClientUpdateAccountFragment newInstance() { return new ClientUpdateAccountFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_update_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputCity = view.findViewById(R.id.input_city_update);
        inputCompany = view.findViewById(R.id.input_company_update);
        view.findViewById(R.id.update_account).setOnClickListener(v -> updateAccount());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void updateAccount() {

        boolean isValid = true;

        inputCity.setError(null);
        inputCompany.setError(null);

        String city = inputCity.getEditText().getText().toString();
        String company = inputCompany.getEditText().getText().toString();

        if (TextUtils.isEmpty(city)) {
            inputCity.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (TextUtils.isEmpty(company)) {
            inputCompany.setError(getString(R.string.error_empty));
            isValid = false;
        }

        if (isValid) {
            UpdateAccountInformation accountInformation = new UpdateAccountInformation();
            accountInformation.setCity(city).setCompany(company);

            if (apiCall != null) apiCall.cancel();
            apiCall = Client.updateAccount(accountInformation);
            EspressoTestingIdlingResource.increment();
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientUpdateAccountFragment.this.onFailure(apiError);
                }
            });
        }
    }
}
