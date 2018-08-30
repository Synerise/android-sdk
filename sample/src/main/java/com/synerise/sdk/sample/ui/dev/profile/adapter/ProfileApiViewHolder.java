package com.synerise.sdk.sample.ui.dev.profile.adapter;

import android.view.View;
import android.widget.TextView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseViewHolder;
import com.synerise.sdk.sample.util.DataActionListener;

public class ProfileApiViewHolder extends BaseViewHolder<ProfileApi> {

    private final TextView label;
    private ProfileApi profileApi;

    // ****************************************************************************************************************************************

    public ProfileApiViewHolder(View itemView, DataActionListener<ProfileApi> listener) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (profileApi != null) listener.onDataAction(profileApi);
        });
        this.label = itemView.findViewById(R.id.profile_api_label);
    }

    // ****************************************************************************************************************************************

    @Override
    public void populateData(ProfileApi profileApi) {
        this.profileApi = profileApi;
        this.label.setText(profileApi.getTitle());
    }
}
