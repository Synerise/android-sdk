package com.synerise.sdk.sample.ui.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;
import com.synerise.sdk.sample.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductActivity extends BaseActivity {

    @BindView(R.id.parallax_image) ImageView parallaxImage;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.product_description) TextView productDescription;
    @BindView(R.id.product_price) TextView productPrice;

    public static Intent createIntent(Context context, Product product) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(Args.PARCEL, product);
        return intent;
    }

    private Product product;

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        product = getIntent().getParcelableExtra(Args.PARCEL);

        ToolbarHelper.setUpChildToolbar(this);

        ViewUtils.loadImage(product.getImage(), parallaxImage);

        String name = product.getName();
        if (name == null) {
            productName.setText(product.getHardcodedName());
            ToolbarHelper.setUpCollapsingToolbar(this, product.getHardcodedName());
        } else {
            productName.setText(name);
            ToolbarHelper.setUpCollapsingToolbar(this, name);
        }

        String description = product.getDescription();
        if (description == null) {
            productDescription.setText(product.getHardcodedDescription());
        } else {
            productDescription.setText(description);
        }

        productPrice.setText(getString(R.string.default_price_USD, product.getPrice()));
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.send(new VisitedScreenEvent(getClass().getSimpleName(), new TrackerParams.Builder().add("sku", product.getSKU()).build()));
    }
}
