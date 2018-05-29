package com.synerise.sdk.sample.ui.dev.profile.pager.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

public class DevGetTokenFragment extends BaseDevFragment {

    private IDataApiCall<String> getTokenCall;

    public static DevGetTokenFragment newInstance() { return new DevGetTokenFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_get_token, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.get_token).setOnClickListener(v -> getToken());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getTokenCall != null) getTokenCall.cancel();
    }

    // ****************************************************************************************************************************************

    @SuppressWarnings("ConstantConditions")
    public void getToken() {
        if (getTokenCall != null) getTokenCall.cancel();
        getTokenCall = Profile.getToken();
        EspressoTestingIdlingResource.increment();
        getTokenCall.execute(token -> onSuccess(), this::onFailure);
    }
}
