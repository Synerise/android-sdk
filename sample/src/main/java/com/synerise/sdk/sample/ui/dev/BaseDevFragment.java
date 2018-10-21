package com.synerise.sdk.sample.ui.dev;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseFragment;

public class BaseDevFragment extends BaseFragment {

    private View view;

    // ****************************************************************************************************************************************

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    // ****************************************************************************************************************************************

    protected void onSuccess() {
        Snackbar.make(view, R.string.default_success, Snackbar.LENGTH_SHORT).show();
    }

    protected void onFailure(ApiError apiError) {
        showAlertError(apiError);
    }
}