package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.profile.voucher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.core.utils.ValidationUtils;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.profile.model.client.AssignVoucherResponse;
import com.synerise.sdk.profile.model.client.VoucherCodesResponse;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ProfileGetVoucherCodesFragment extends BaseDevFragment {

    private TextInputLayout inputClientId;
    private IDataApiCall<VoucherCodesResponse> call;

    public static ProfileGetVoucherCodesFragment newInstance() { return new ProfileGetVoucherCodesFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_get_voucher_codes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputClientId = view.findViewById(R.id.input_client_id);
        view.findViewById(R.id.get_voucher_codes).setOnClickListener(v -> getVoucherCodes());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void getVoucherCodes() {
        boolean isValid = true;

        inputClientId.setError(null);

        String clientId = inputClientId.getEditText().getText().toString();

        if (ValidationUtils.isEmpty(clientId)) {
            isValid = false;
            inputClientId.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.getClientVoucherCodes(clientId);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void onSuccess(VoucherCodesResponse response) {
        super.onSuccess();
    }
}