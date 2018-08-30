package com.synerise.sdk.sample.ui.dev.profile.promotions.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.util.DataActionListener;

public class PromotionApiRecyclerAdapter extends RecyclerView.Adapter<PromotionApiViewHolder> {

    private final PromotionApi[] promotionApis = PromotionApi.values();
    private final DataActionListener<PromotionApi> listener;
    private final LayoutInflater inflater;

    // ****************************************************************************************************************************************

    public PromotionApiRecyclerAdapter(LayoutInflater inflater, DataActionListener<PromotionApi> listener) {
        this.inflater = inflater;
        this.listener = listener;
    }

    // ****************************************************************************************************************************************

    @NonNull
    @Override
    public PromotionApiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PromotionApiViewHolder(inflater.inflate(R.layout.item_profile_api, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionApiViewHolder holder, int position) {
        holder.populateData(promotionApis[position]);
    }

    @Override
    public int getItemCount() {
        return promotionApis.length;
    }
}
