package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.promotions.Promotions;
import com.synerise.sdk.promotions.model.VoucherCodesResponse;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientGetAssignedVoucherCodesFragment extends BaseDevFragment {

    private IDataApiCall<VoucherCodesResponse> call;

    public static ClientGetAssignedVoucherCodesFragment newInstance() { return new ClientGetAssignedVoucherCodesFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_get_assigned_voucher_codes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.get_assigned_voucher_codes).setOnClickListener(v -> getVoucherCodes());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void getVoucherCodes() {
        if (call != null) call.cancel();
        call = Promotions.getAssignedVoucherCodes();
        call.execute(this::onSuccess, new DataActionListener<ApiError>() {
            @Override
            public void onDataAction(ApiError apiError) {
                onFailure(apiError);
            }
        });
    }

    private void onSuccess(VoucherCodesResponse response) {
        super.onSuccess();
    }
}