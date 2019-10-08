package com.synerise.sdk.sample.ui.cart.adapter.promotion;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.cart.adapter.item.CartItem;
import com.synerise.sdk.sample.util.DataActionListener;

import java.util.ArrayList;
import java.util.List;

public class CartPromotionAdapter extends RecyclerView.Adapter<CartPromotionViewHolder> {

    private final LayoutInflater inflater;
    private final List<Pair<Promotion, CartItem>> promotions;
    private final DataActionListener<Pair<Promotion, CartItem>> listener;

    public CartPromotionAdapter(LayoutInflater layoutInflater, DataActionListener<Pair<Promotion, CartItem>> listener,
                                List<Pair<Promotion, CartItem>> promotions) {
        this.inflater = layoutInflater;
        this.listener = listener;
        this.promotions = new ArrayList<>(promotions);
    }

    @NonNull
    @Override
    public CartPromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_cart_promotion, parent, false);
        return new CartPromotionViewHolder(itemView, listener, this::onViewHolderRadioClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull CartPromotionViewHolder holder, int position) {
        holder.populateData(promotions.get(position), position);
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public void update(List<Pair<Promotion, CartItem>> promotions) {
        this.promotions.clear();
        this.promotions.addAll(promotions);
        notifyDataSetChanged();
    }

    private void onViewHolderRadioClicked() {
        notifyDataSetChanged();
    }

    interface OnInternalActionListener {
        void onRadioClicked();
    }
}