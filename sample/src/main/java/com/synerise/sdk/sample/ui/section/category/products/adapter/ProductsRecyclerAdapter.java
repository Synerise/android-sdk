package com.synerise.sdk.sample.ui.section.category.products.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.util.DataActionListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    private final LayoutInflater inflater;
    private final List<Product> products;
    private final DataActionListener<Product> listener;

    public ProductsRecyclerAdapter(Context context, DataActionListener<Product> listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.products = new ArrayList<>();
    }

    public ProductsRecyclerAdapter(Context context, DataActionListener<Product> listener, List<Product> products) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductsViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        holder.populateData(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(List<Product> favouriteProducts) {
        products.clear();
        products.addAll(favouriteProducts);
        notifyDataSetChanged();
    }
}
