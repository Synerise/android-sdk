package com.synerise.sdk.sample.ui.promotion.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.util.DataActionListener;

import java.util.ArrayList;
import java.util.List;

public class PromotionsRecyclerAdapter extends RecyclerView.Adapter<PromotionsViewHolder> {

    private final LayoutInflater inflater;
    private final List<Promotion> promotions;
    private final DataActionListener<Promotion> listener;

    public PromotionsRecyclerAdapter(Context context, DataActionListener<Promotion> listener, List<Promotion> promotions) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.promotions = new ArrayList<>(promotions);
    }

    @NonNull
    @Override
    public PromotionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_promotion, parent, false);
        return new PromotionsViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionsViewHolder holder, int position) {
        holder.populateData(promotions.get(position));
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public void update(List<Promotion> promotions) {
        this.promotions.clear();
        this.promotions.addAll(promotions);
        notifyDataSetChanged();
    }
}
