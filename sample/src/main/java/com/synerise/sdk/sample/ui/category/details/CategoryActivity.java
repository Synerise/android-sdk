package com.synerise.sdk.sample.ui.category.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.category.details.adapter.ProductsRecyclerAdapter;
import com.synerise.sdk.sample.ui.product.ProductActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity {

    @BindView(R.id.recycler) RecyclerView recycler;

    private Category category;

    public static Intent createIntent(Context context, Category category) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(Args.PARCEL, category);
        return intent;
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        category = getIntent().getParcelableExtra(Args.PARCEL);

        ToolbarHelper.setUpChildToolbar(this, category.getName());

        ProductsRecyclerAdapter recyclerAdapter = new ProductsRecyclerAdapter(this, this::onProductSelected, category.getProducts());

        recycler.setHasFixedSize(true);
        recycler.setAdapter(recyclerAdapter);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.send(new VisitedScreenEvent(getClass().getSimpleName(),
                                            new TrackerParams.Builder().add("category_id", category.getId()).build()));
    }

    private void onProductSelected(Product product) {
        startActivity(ProductActivity.createIntent(this, product));
    }
}
