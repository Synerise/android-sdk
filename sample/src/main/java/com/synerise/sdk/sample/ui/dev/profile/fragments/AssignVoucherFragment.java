package com.synerise.sdk.sample.ui.dev.profile.fragments;

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
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class AssignVoucherFragment extends BaseDevFragment {

    private TextInputLayout inputPool;
    private IDataApiCall<AssignVoucherResponse> call;

    public static AssignVoucherFragment newInstance() { return new AssignVoucherFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_assign_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPool = view.findViewById(R.id.input_pool);
        view.findViewById(R.id.assign_voucher).setOnClickListener(v -> assignVoucher());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void assignVoucher() {
        boolean isValid = true;

        inputPool.setError(null);

        String pool = inputPool.getEditText().getText().toString();

        if (ValidationUtils.isEmpty(pool)) {
            isValid = false;
            inputPool.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.getOrAssignVoucher(pool, null);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void onSuccess(AssignVoucherResponse response) {
        super.onSuccess();
    }
}