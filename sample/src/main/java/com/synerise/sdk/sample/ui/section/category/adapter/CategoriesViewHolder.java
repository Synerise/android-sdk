package com.synerise.sdk.sample.ui.section.category.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.util.DataActionListener;

class CategoriesViewHolder extends RecyclerView.ViewHolder {

    private final ImageView image;
    private final TextView name;

    private final Context context;
    private Category category;

    CategoriesViewHolder(View itemView, DataActionListener<Category> listener) {
        super(itemView);
        this.context = itemView.getContext();
        itemView.setOnClickListener(v -> {
            if (category != null) listener.onDataAction(category);
        });
        this.image = itemView.findViewById(R.id.category_image);
        this.name = itemView.findViewById(R.id.category_name);
    }

    void populateData(Category category) {
        this.category = category;
        image.setImageResource(category.getImage());
        name.setText(category.getText());
        ((CardView) itemView).setCardBackgroundColor(ContextCompat.getColor(context, category.getBackground()));
    }
}
