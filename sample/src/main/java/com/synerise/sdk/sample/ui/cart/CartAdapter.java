package com.synerise.sdk.sample.ui.cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private final LayoutInflater inflater;
    private final List<CartItem> cartItems = new ArrayList<>();
    private final OnCartItemRemoved cartItemRemoved;

    // ****************************************************************************************************************************************

    CartAdapter(Context context, List<CartItem> cartItems, OnCartItemRemoved cartItemRemoved) {
        this.inflater = LayoutInflater.from(context);
        if (cartItems != null) this.cartItems.addAll(cartItems);
        this.cartItemRemoved = cartItemRemoved;
    }

    // ****************************************************************************************************************************************

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView, new CartViewHolder.OnCartItemRemoved() {
            @Override
            public void onSingleItemRemoved(int position) {
                CartItem cartItem = cartItems.get(position);
                cartItem.decrementQuantity();
                if (cartItem.getQuantity() == 0) {
                    cartItemRemoved.onRemoved(cartItem);
                    notifyItemRemoved(position);
                } else {
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onAllItemsRemoved(int position) {
                cartItemRemoved.onRemoved(cartItems.get(position));
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.populateData(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
