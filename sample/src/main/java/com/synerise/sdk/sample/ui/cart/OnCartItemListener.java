package com.synerise.sdk.sample.ui.cart;

public interface OnCartItemListener {

    void onItemQuantityReduced(CartItem cartItem);

    void onItemRemoved(CartItem cartItem);
}
