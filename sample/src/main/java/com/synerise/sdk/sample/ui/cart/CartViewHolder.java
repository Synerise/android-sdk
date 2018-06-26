package com.synerise.sdk.sample.ui.cart;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Product;

import java.util.Locale;

public class CartViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView brand;
    private final TextView price;
    private final SimpleDraweeView image;

    // ****************************************************************************************************************************************

    CartViewHolder(View itemView, OnCartItemRemoved decrementCartItems) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        brand = itemView.findViewById(R.id.brand);
        price = itemView.findViewById(R.id.price);
        image = itemView.findViewById(R.id.image);
        itemView.findViewById(R.id.cart_options).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(itemView.getContext(), v);
            popup.inflate(R.menu.cart_options_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.remove_single:
                        decrementCartItems.onSingleItemRemoved(getAdapterPosition());
                        return true;
                    case R.id.remove_all:
                        decrementCartItems.onAllItemsRemoved(getAdapterPosition());
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
    }

    // ****************************************************************************************************************************************

    public void populateData(CartItem cartItem) {
        Product product = cartItem.getProduct();
        name.setText(product.getName());
        brand.setText(product.getBrand());
        price.setText(String.format(Locale.getDefault(), "%dx " + product.getPrice() + " $", cartItem.getQuantity()));
        image.setImageURI(product.getImage());
    }

    // ****************************************************************************************************************************************

    protected interface OnCartItemRemoved {

        void onSingleItemRemoved(int position);

        void onAllItemsRemoved(int position);
    }
}
