package com.synerise.sdk.sample.ui.section.category.products.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.synerise.sdk.content.model.BaseModel;
import com.synerise.sdk.content.model.recommendation.Recommendation;
import com.synerise.sdk.content.widgets.ContentWidget;
import com.synerise.sdk.content.widgets.layout.ContentWidgetHorizontalSliderLayout;
import com.synerise.sdk.content.widgets.layout.ContentWidgetItemLayout;
import com.synerise.sdk.content.widgets.listener.OnContentWidgetListener;
import com.synerise.sdk.content.widgets.model.ContentWidgetAppearance;
import com.synerise.sdk.content.widgets.model.ContentWidgetOptions;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.error.ApiError;
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
import com.synerise.sdk.sample.ui.dev.content.WidgetRecommendedProductDetailsActivity;
import com.synerise.sdk.sample.ui.splash.SplashActivity;
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
    private LinearLayout insertPoint;

    public static Intent createIntent(Context context, String productSKU) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(Args.SERIALIZABLE, productSKU);
        return intent;
    }

    @Override
    @SuppressLint("StringFormatMatches")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_product);

        ToolbarHelper.setUpChildToolbar(this);

        insertPoint = findViewById(R.id.insert_point);

        loadWidget();

        Intent intent = getIntent();
        String sku = intent.getStringExtra(Args.SERIALIZABLE);
        if (sku == null) {
            String data = intent.getDataString();
            if (data != null) {
                Uri uri = Uri.parse(data);
                sku = uri.getQueryParameter("sku");
            }
        }

        product = Product.getProduct(sku);
        if (product == null) return;

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

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.send(new VisitedScreenEvent(getClass().getSimpleName(),
                                            new TrackerParams.Builder().add("sku", product.getSKU()).build()));
    }

    private TrackerParams buildParamsForProductEvent(Product product) {
        return new TrackerParams.Builder()
                .add("productName", getString(product.getName()))
                .add("SKU", product.getSKU())
                .add("price", product.getPrice())
                .build();
    }

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

    public void loadWidget() {
        String productId = "10214";
        String slug = "similar";

        ContentWidgetOptions options = new ContentWidgetOptions(this, slug, productId);

        ContentWidgetItemLayout itemLayoutDetails = new ContentWidgetItemLayout();
        ContentWidgetHorizontalSliderLayout layout = new ContentWidgetHorizontalSliderLayout();
        //CardView parameters

        layout.setCardViewSize(160, 250);
        itemLayoutDetails.cardViewElevation = 5;
        itemLayoutDetails.cardViewCornerRadius = 5;
        layout.cardViewBackgroundColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.regent);
        //Image size as ratio to cardview size
        itemLayoutDetails.imageHeightToCardHeightRatio = 0.6;
        itemLayoutDetails.imageWidthToCardWidthRatio = 1;
        itemLayoutDetails.imageMargin = 0; //have to be set when you set cardViewElevation
        //TextView product name
        itemLayoutDetails.itemTitleStyle = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
        itemLayoutDetails.itemTitleSize = 12;
        itemLayoutDetails.itemTitleColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.charcoal);
        itemLayoutDetails.itemTitleGravity = Gravity.LEFT;
        itemLayoutDetails.setItemTitleMargins(10, 0, 10, 0);
        //TextView Product price
        itemLayoutDetails.itemPriceStyle = Typeface.create("sans-serif-light", Typeface.BOLD);
        itemLayoutDetails.itemPriceSize = 12;
        itemLayoutDetails.itemPriceColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.charcoal);
        itemLayoutDetails.itemPriceGravity = Gravity.LEFT;
        itemLayoutDetails.setItemPriceMargins(10, 0, 10, 0);
        ContentWidgetAppearance contentWidgetAppearance = new ContentWidgetAppearance(new ContentWidgetHorizontalSliderLayout(), itemLayoutDetails);
        ContentWidget widget = new ContentWidget(options, contentWidgetAppearance);

        widget.setOnContentWidgetListener(new OnContentWidgetListener() {
            @Override
            public void widgetIsLoading(ContentWidget contentWidget, boolean isLoading) {

            }

            @Override
            public void widgetDidNotLoad(ContentWidget contentWidget, ApiError apiError) {

            }

            @Override
            public void widgetDidLoad(ContentWidget contentWidget) {

            }

            @Override
            public void widgetDidReceiveClickAction(ContentWidget contentWidget, BaseModel itemSelected) {
                Recommendation recommendation = (Recommendation)itemSelected;
                startActivity(WidgetRecommendedProductDetailsActivity.createIntent(getApplicationContext(), recommendation.getProductRetailerPartNo()));
            }

            @Override
            public void widgetSizeDidChange(ContentWidget contentWidget, ViewGroup.LayoutParams scrollableSize) {

            }
        });
        View view = widget.getView();
        insertPoint.addView(view);
    }

    @Override
    public void onBackPressed() {
        // Override onBackPressed in deeplink activities to start an app when it is shut down.
        if (isTaskRoot()) {
            // Start the app before finishing
            Intent startAppIntent = new Intent(getApplicationContext(), SplashActivity.class);
            startAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startAppIntent);
            finish();
        } else
            super.onBackPressed();
    }
}
