package com.synerise.sdk.sample.ui.category.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.util.DataActionListener;
import com.synerise.sdk.sample.util.ViewUtils;

class CategoriesViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final SimpleDraweeView image;

    private Category category;

    // ****************************************************************************************************************************************

    CategoriesViewHolder(View itemView, final DataActionListener<Category> listener) {
        super(itemView);
        itemView.setOnClickListener(view -> {
            if (category != null) listener.onDataAction(category);
        });
        name = itemView.findViewById(R.id.category_name);
        image = itemView.findViewById(R.id.category_image);
    }

    // ****************************************************************************************************************************************

    void populateData(@NonNull Category category) {
        this.category = category;
        this.name.setText(category.getName());
        ViewUtils.loadImage(category.getImage(), image);
    }
}
