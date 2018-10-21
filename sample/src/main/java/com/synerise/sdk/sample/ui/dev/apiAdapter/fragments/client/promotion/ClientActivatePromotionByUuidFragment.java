package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.client.promotion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.utils.ValidationUtils;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class ClientActivatePromotionByUuidFragment extends BaseDevFragment {

    private IApiCall apiCall;
    private TextInputLayout inputUuid;

    public static ClientActivatePromotionByUuidFragment newInstance() { return new ClientActivatePromotionByUuidFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_activate_promotion_by_uuid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUuid = view.findViewById(R.id.input_uuid);
        view.findViewById(R.id.activate_promotion).setOnClickListener(v -> activatePromotion());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void activatePromotion() {

        inputUuid.setError(null);

        boolean isValid = true;

        String uuid = inputUuid.getEditText().getText().toString();
        if (ValidationUtils.isEmpty(uuid)) {
            isValid = false;
            inputUuid.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (apiCall != null) apiCall.cancel();
            apiCall = Client.activatePromotionByUuid(uuid);
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientActivatePromotionByUuidFragment.this.onFailure(apiError);
                }
            });
        }
    }
}
