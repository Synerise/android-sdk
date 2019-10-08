package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.GetAccountInformation;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientGetAccountFragment extends BaseDevFragment {

    private IDataApiCall<GetAccountInformation> accountInfoCall;

    public static ClientGetAccountFragment newInstance() { return new ClientGetAccountFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_get_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.get_account).setOnClickListener(v -> getAccount());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (accountInfoCall != null) accountInfoCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void getAccount() {
        if (accountInfoCall != null) accountInfoCall.cancel();
        accountInfoCall = Client.getAccount();
        EspressoTestingIdlingResource.increment();
        accountInfoCall.execute(success -> onSuccess(), this::onFailure);
    }
}