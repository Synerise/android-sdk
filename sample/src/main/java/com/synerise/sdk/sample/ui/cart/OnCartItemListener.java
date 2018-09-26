package com.synerise.sdk.sample.ui.cart;

import com.synerise.sdk.sample.ui.cart.adapter.item.CartItem;

public interface OnCartItemListener {

    void onItemQuantityReduced(CartItem cartItem);

    void onItemRemoved(CartItem cartItem);
}
