package com.synerise.sdk.sample.ui.dev.apiAdapter;

import android.view.View;
import android.widget.TextView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseViewHolder;
import com.synerise.sdk.sample.util.DataActionListener;

public class ApiViewHolder extends BaseViewHolder<SyneriseSdkApi> {

    private final TextView label;
    private SyneriseSdkApi syneriseSdkApi;

    // ****************************************************************************************************************************************

    public ApiViewHolder(View itemView, DataActionListener<SyneriseSdkApi> listener) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (syneriseSdkApi != null) listener.onDataAction(syneriseSdkApi);
        });
        this.label = itemView.findViewById(R.id.api_label);
    }

    // ****************************************************************************************************************************************

    @Override
    public void populateData(SyneriseSdkApi syneriseSdkApi) {
        this.syneriseSdkApi = syneriseSdkApi;
        this.label.setText(syneriseSdkApi.getTitle());
    }
}
