package com.synerise.sdk.sample.ui.category.details.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.util.DataActionListener;
import com.synerise.sdk.sample.util.ViewUtils;

class ProductsViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView price;
    private final ImageView image;

    private final Context context;
    private Product product;

    // ****************************************************************************************************************************************

    ProductsViewHolder(View itemView, final DataActionListener<Product> listener) {
        super(itemView);
        context = itemView.getContext();
        itemView.setOnClickListener(view -> {
            if (product != null) listener.onDataAction(product);
        });
        name = itemView.findViewById(R.id.product_name);
        price = itemView.findViewById(R.id.product_price);
        image = itemView.findViewById(R.id.product_image);
    }

    // ****************************************************************************************************************************************

    void populateData(@NonNull Product product) {
        this.product = product;
        this.name.setText(product.getHardcodedName());
        this.price.setText(context.getString(R.string.default_price_USD, product.getPrice()));
        ViewUtils.loadImage(product.getImage(), image);
    }
}
