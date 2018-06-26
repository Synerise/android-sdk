package com.synerise.sdk.sample.ui.dev.profile.pager.pages;

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
import com.synerise.sdk.profile.model.promotion.ProfilePromotionResponse;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class GetPromotionsByUuidFragment extends BaseDevFragment {

    private TextInputLayout inputUuid;

    private IDataApiCall<ProfilePromotionResponse> call;

    public static GetPromotionsByUuidFragment newInstance() { return new GetPromotionsByUuidFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_get_promotion_by_uuid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUuid = view.findViewById(R.id.input_uuid);
        view.findViewById(R.id.get_promotion).setOnClickListener(v -> getPromotion());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) call.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    private void getPromotion() {
        boolean isValid = true;

        inputUuid.setError(null);

        String uuid = inputUuid.getEditText().getText().toString();

        if(ValidationUtils.isEmpty(uuid)){
            isValid = false;
            inputUuid.setError(getString(R.string.error_empty));
        }

        if (isValid) {
            if (call != null) call.cancel();
            call = Profile.getPromotionByUuid(uuid);
            call.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    onFailure(apiError);
                }
            });
        }
    }

    private void onSuccess(ProfilePromotionResponse profilePromotionResponse) {
        super.onSuccess();
    }
}