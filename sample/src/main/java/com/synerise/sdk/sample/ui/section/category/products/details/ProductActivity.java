package com.synerise.sdk.sample.ui.section.category.products.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.event.model.interaction.HitTimerEvent;
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent;
import com.synerise.sdk.event.model.model.UnitPrice;
import com.synerise.sdk.event.model.products.AddedToFavoritesEvent;
import com.synerise.sdk.event.model.products.cart.AddedToCartEvent;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.data.Section;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;
import com.synerise.sdk.sample.util.ViewUtils;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class ProductActivity extends BaseActivity {

    @Inject AccountManager accountManager;

    private Product product;
    private ImageView favIcon;

    public static Intent createIntent(Context context, Product product) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(Args.SERIALIZABLE, product);
        return intent;
    }

    // ****************************************************************************************************************************************

    @Override
    @SuppressLint("StringFormatMatches")
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

        RatingBar ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setRating((float) product.getRating());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Tracker.send(new CustomEvent("product.rating",
                                             getString(product.getName()),
                                             new TrackerParams.Builder()
                                                     .add("productName", getString(product.getName()))
                                                     .add("sku", product.getSKU())
                                                     .add("price", product.getPrice())
                                                     .add("rating", rating)
                                                     .build()));
            }
        });

        findViewById(R.id.product_add).setOnClickListener(v -> {
            if (accountManager.getCartItems().isEmpty()) {
                // send HitTimerEvent when adding first item to the cart
                Tracker.send(new HitTimerEvent("Shopping Cart process - start"));
            }
            accountManager.addCartItem(product);
            Snackbar.make(v, R.string.added_to_cart, Snackbar.LENGTH_SHORT).show();
            Tracker.send(createCartEvent());
        });

        favIcon = findViewById(R.id.fav_icon);
        favIcon.setSelected(accountManager.isProductFavourite(product));
        favIcon.setOnClickListener(v -> {
            ViewUtils.pulse(favIcon);
            favIcon.setSelected(!favIcon.isSelected());
            if (favIcon.isSelected()) {
                accountManager.addProductAsFavourite(product);
                Tracker.send(new AddedToFavoritesEvent(getString(product.getName()),
                                                       buildParamsForProductEvent(product)));
            } else {
                accountManager.removeProductFromFavourite(product);
                Tracker.send(new CustomEvent("removed-from-favorites",
                                             getString(product.getName()),
                                             buildParamsForProductEvent(product)));
            }
        });
    }

    private TrackerParams buildParamsForProductEvent(Product product) {
        return new TrackerParams.Builder()
                .add("productName", getString(product.getName()))
                .add("SKU", product.getSKU())
                .add("price", product.getPrice())
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.send(new VisitedScreenEvent(getClass().getSimpleName(),
                                            new TrackerParams.Builder().add("sku", product.getSKU()).build()));
    }

    // ****************************************************************************************************************************************

    private AddedToCartEvent createCartEvent() {
        UnitPrice unitPrice = new UnitPrice(product.getPrice(), Currency.getInstance(Locale.US));
        AddedToCartEvent cartEvent = new AddedToCartEvent(getString(product.getName()), product.getSKU(), unitPrice, 1);
        cartEvent.setName(getString(product.getName()));
        cartEvent.setProducer(getString(product.getBrand()));

        Category category = Category.getCategory(product);
        if (category != null) {
            cartEvent.setCategory(getString(category.getText()));
            List<String> categories = new ArrayList<>();
            categories.add(getString(category.getText()));
            Section section = Section.getSection(category);
            if (section != null) categories.add(getString(section.getName()));
            cartEvent.setCategories(categories);
        }
        cartEvent.setOffline(false);
        //        cartEvent.setDiscountedPrice(unitPrice);
        //        cartEvent.setRegularPrice(unitPrice);
        return cartEvent;
    }
}
