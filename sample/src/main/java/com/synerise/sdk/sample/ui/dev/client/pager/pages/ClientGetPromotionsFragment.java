package com.synerise.sdk.sample.ui.dev.client.pager.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.PromotionResponse;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientGetPromotionsFragment extends BaseDevFragment {

    private IDataApiCall<PromotionResponse> apiCall;

    public static ClientGetPromotionsFragment newInstance() { return new ClientGetPromotionsFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_get_promotions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.get_promotions).setOnClickListener(v -> getPromotions());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void getPromotions() {
            if (apiCall != null) apiCall.cancel();
            apiCall = Client.getPromotions();
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientGetPromotionsFragment.this.onFailure(apiError);
                }
            });
    }

    private void onSuccess(PromotionResponse promotionResponse) {
        super.onSuccess();
    }
}
