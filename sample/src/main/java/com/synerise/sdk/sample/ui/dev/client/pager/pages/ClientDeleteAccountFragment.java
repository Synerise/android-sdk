package com.synerise.sdk.sample.ui.dev.client.pager.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class ClientDeleteAccountFragment extends BaseDevFragment {

    private IApiCall deleteCall;

    public static ClientDeleteAccountFragment newInstance() { return new ClientDeleteAccountFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_delete_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.delete_account).setOnClickListener(v -> getAccount());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (deleteCall != null) deleteCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void getAccount() {
        if (deleteCall != null) deleteCall.cancel();
        deleteCall = Client.deleteAccount();
        EspressoTestingIdlingResource.increment();
        deleteCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {
                onFailure(apiError);
            }
        });
    }
}