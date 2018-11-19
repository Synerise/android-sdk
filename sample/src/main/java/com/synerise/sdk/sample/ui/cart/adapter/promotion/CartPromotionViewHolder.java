package com.synerise.sdk.sample.ui.cart.adapter.promotion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.promotions.model.promotion.PromotionDiscountType;
import com.synerise.sdk.promotions.model.promotion.PromotionStatus;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.cart.adapter.item.CartItem;
import com.synerise.sdk.sample.util.DataActionListener;

public class CartPromotionViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView product;
    private final TextView discount;
    private final RadioButton radio;

    private Pair<Promotion, CartItem> promotionPair;
    private final Context context;

    private static int lastSelectedPosition = -1; // shared among all items

    CartPromotionViewHolder(View view, DataActionListener<Pair<Promotion, CartItem>> listener,
                            CartPromotionAdapter.OnInternalActionListener internalListener) {
        super(view);
        context = view.getContext();
        name = view.findViewById(R.id.name);
        product = view.findViewById(R.id.product);
        discount = view.findViewById(R.id.discount);
        radio = view.findViewById(R.id.radio);
        View.OnClickListener clickListener = v -> {
            lastSelectedPosition = getAdapterPosition();
            internalListener.onRadioClicked();
            if (promotionPair != null) listener.onDataAction(promotionPair);
        };
        radio.setOnClickListener(clickListener);
        view.setOnClickListener(clickListener);
    }

    public void populateData(Pair<Promotion, CartItem> promotionPair, int position) {
        this.promotionPair = promotionPair;
        Promotion promotion = promotionPair.first;
        CartItem cartItem = promotionPair.second;

        if (promotion.getStatus() == PromotionStatus.REDEEMED) this.radio.setEnabled(false);
        this.radio.setChecked(lastSelectedPosition == position);
        this.name.setText(promotion.getHeadline());
        this.product.setText(cartItem.getProduct().getName());
        if (promotion.getDiscountType() == PromotionDiscountType.AMOUNT) {
            this.discount.setText(context.getString(R.string.default_value_dollar, promotion.getDiscountValue()));
        } else if (promotion.getDiscountType() == PromotionDiscountType.PERCENT) {
            this.discount.setText(context.getString(R.string.default_value_percent, promotion.getDiscountValue()));
        } else {
            this.discount.setText(promotion.getDiscountValue());
        }
    }
}