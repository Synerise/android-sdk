package com.synerise.sdk.sample.ui.section.category.products.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.util.DataActionListener;
import com.synerise.sdk.sample.util.ViewUtils;

class ProductsViewHolder extends RecyclerView.ViewHolder {

    private final SimpleDraweeView image;
    private final TextView name;
    private final TextView brand;
    private final TextView price;
    private final ProgressBar imageProgressBar;

    private final Context context;
    private Product product;

    ProductsViewHolder(View itemView, final DataActionListener<Product> listener) {
        super(itemView);
        context = itemView.getContext();
        itemView.setOnClickListener(view -> {
            if (product != null) listener.onDataAction(product);
        });
        image = itemView.findViewById(R.id.product_image);
        imageProgressBar = itemView.findViewById(R.id.image_progress_bar);
        name = itemView.findViewById(R.id.product_name);
        brand = itemView.findViewById(R.id.product_brand);
        price = itemView.findViewById(R.id.product_price);
    }

    void populateData(@NonNull Product product) {
        this.product = product;
        ViewUtils.loadImage(product.getImage(), image, imageProgressBar);
        this.name.setText(product.getName());
        this.brand.setText(product.getBrand());
        this.price.setText(context.getString(R.string.default_price_USD, product.getPrice()));
    }
}
