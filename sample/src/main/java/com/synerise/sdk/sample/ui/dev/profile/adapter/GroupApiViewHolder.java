package com.synerise.sdk.sample.ui.dev.profile.adapter;

import android.view.View;
import android.widget.TextView;

import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseViewHolder;

class GroupApiViewHolder extends BaseViewHolder<ProfileApi> {

    private final TextView label;
    private ProfileApi profileApi;

    // ****************************************************************************************************************************************

    GroupApiViewHolder(View itemView, ActionListener listener) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (profileApi != null) listener.onAction();
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
