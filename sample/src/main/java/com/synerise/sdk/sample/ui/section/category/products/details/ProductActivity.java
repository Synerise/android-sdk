package com.synerise.sdk.sample.ui.section.category.products.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent;
import com.synerise.sdk.event.model.model.UnitPrice;
import com.synerise.sdk.event.model.products.cart.AddedToCartEvent;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;
import com.synerise.sdk.sample.util.ViewUtils;

import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

public class ProductActivity extends BaseActivity {

    @Inject AccountManager accountManager;

    private Product product;

    public static Intent createIntent(Context context, Product product) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(Args.SERIALIZABLE, product);
        return intent;
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_product);

        ToolbarHelper.setUpChildToolbar(this);

        product = (Product) getIntent().getSerializableExtra(Args.SERIALIZABLE);

        ViewUtils.loadImage(product.getImage(), findViewById(R.id.parallax_image), findViewById(R.id.image_progress_bar));

        String name = getString(product.getName());
        ((TextView) findViewById(R.id.product_name)).setText(name);
        ((TextView) findViewById(R.id.product_brand)).setText(product.getBrand());
        ((TextView) findViewById(R.id.product_rating_count)).setText(String.valueOf(product.getRatingCount()));
        ToolbarHelper.setUpCollapsingToolbar(this, name);

        String description = getString(product.getDescription());
        ((TextView) findViewById(R.id.product_description)).setText(description);

        ((TextView) findViewById(R.id.product_price)).setText(getString(R.string.default_price_USD, product.getPrice()));

        ((RatingBar) findViewById(R.id.rating_bar)).setRating((float) product.getRating());

        findViewById(R.id.product_add).setOnClickListener(v -> {
            accountManager.addCartItem(product);
            Snackbar.make(v, R.string.added_to_cart, Snackbar.LENGTH_SHORT).show();
            Tracker.send(createCartEvent());
        });
    }

    // ****************************************************************************************************************************************

    private AddedToCartEvent createCartEvent() {
        AddedToCartEvent cartEvent = new AddedToCartEvent(getString(product.getName()),
                                                          product.getSKU(),
                                                          new UnitPrice(product.getPrice(), Currency.getInstance(Locale.getDefault())),
                                                          1);
        cartEvent.setName(getString(product.getName()));
        cartEvent.setProducer(getString(product.getBrand()));
        return cartEvent;
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.send(new VisitedScreenEvent(getClass().getSimpleName(),
                                            new TrackerParams.Builder().add("sku", product.getSKU()).build()));
    }
}
