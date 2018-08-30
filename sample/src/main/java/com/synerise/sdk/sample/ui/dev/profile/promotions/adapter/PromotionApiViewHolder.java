package com.synerise.sdk.sample.ui.dev.profile.promotions.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.util.DataActionListener;

public class PromotionApiViewHolder extends RecyclerView.ViewHolder {

    private final TextView label;
    private PromotionApi promotionApi;

    // ****************************************************************************************************************************************

    public PromotionApiViewHolder(View itemView, DataActionListener<PromotionApi> listener) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (promotionApi != null) listener.onDataAction(promotionApi);
        });
        this.label = itemView.findViewById(R.id.profile_api_label);
    }

    // ****************************************************************************************************************************************

    public void populateData(PromotionApi promotionApi) {
        this.promotionApi = promotionApi;
        this.label.setText(promotionApi.getTitle());
    }
}
