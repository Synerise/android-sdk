package com.synerise.sdk.sample.ui.category.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.util.DataActionListener;

import java.util.List;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {

    private final LayoutInflater inflater;
    private final List<Category> categories = Category.getCategories();
    private final DataActionListener<Category> listener;

    // ****************************************************************************************************************************************

    public CategoriesRecyclerAdapter(@NonNull Context context, @NonNull DataActionListener<Category> listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    // ****************************************************************************************************************************************

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_category, parent, false);
        return new CategoriesViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        holder.populateData(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
