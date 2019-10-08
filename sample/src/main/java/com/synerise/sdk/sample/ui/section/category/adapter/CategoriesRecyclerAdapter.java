package com.synerise.sdk.sample.ui.section.category.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.util.DataActionListener;

import java.util.List;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {

    private final List<Category> categories;
    private final DataActionListener<Category> listener;
    private final LayoutInflater inflater;

    public CategoriesRecyclerAdapter(Context context, DataActionListener<Category> listener, List<Category> categories) {
        this.inflater = LayoutInflater.from(context);
        this.categories = categories;
        this.listener = listener;
    }

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
