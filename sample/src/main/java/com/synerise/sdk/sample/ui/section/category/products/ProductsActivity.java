package com.synerise.sdk.sample.ui.section.category.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.section.category.products.adapter.ProductsRecyclerAdapter;
import com.synerise.sdk.sample.ui.section.category.products.details.ProductActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class ProductsActivity extends BaseActivity {

    public static Intent createIntent(Context context, Category category) {
        Intent intent = new Intent(context, ProductsActivity.class);
        intent.putExtra(Args.SERIALIZABLE, category);
        return intent;
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Category category = (Category) getIntent().getSerializableExtra(Args.SERIALIZABLE);

        ToolbarHelper.setUpChildToolbar(this, category.getText());

        ProductsRecyclerAdapter productsRecyclerAdapter = new ProductsRecyclerAdapter(this, this::onProductSelected,
                                                                                      category.getProducts());

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(productsRecyclerAdapter);
    }

    // ****************************************************************************************************************************************

    private void onProductSelected(Product product) {
        startActivity(ProductActivity.createIntent(this, product));
    }
}
