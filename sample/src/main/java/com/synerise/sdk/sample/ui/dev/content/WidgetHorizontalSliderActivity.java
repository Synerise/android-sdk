package com.synerise.sdk.sample.ui.dev.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class WidgetHorizontalSliderActivity extends BaseActivity {

    private LinearLayout insertPoint;
    private TextInputLayout cardViewElevation;
    private TextInputLayout cardViewWidth;
    private TextInputLayout cardViewHeight;
    private TextInputLayout productInputId;
    private TextInputLayout imageWidthRatio;
    private TextInputLayout imageHeightRatio;
    private TextInputLayout horizontalSpacing;
    private TextInputLayout cornerRadius;

    public static Intent createIntent(Context context) {
        return new Intent(context, WidgetHorizontalSliderActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_slider);

        ToolbarHelper.setUpChildToolbar(this, R.string.content_api);
        findViewById(R.id.load_widget).setOnClickListener(v -> loadWidget());
        productInputId = findViewById(R.id.input_product_id);
        cardViewElevation = findViewById(R.id.input_elevation);
        cardViewWidth = findViewById(R.id.input_item_width);
        cardViewHeight = findViewById(R.id.input_item_height);
        insertPoint = findViewById(R.id.slider_insert_point);
        imageWidthRatio = findViewById(R.id.input_image_width_ratio);
        imageHeightRatio = findViewById(R.id.input_image_height_ratio);
        horizontalSpacing = findViewById(R.id.input_horizontal_spacing);
        cornerRadius = findViewById(R.id.input_corner_radius);
    }

    public void loadWidget() {
        String productId = "10214";
        String slug = "similar";
        if (!productInputId.getEditText().getText().toString().matches(""))
            productId = productInputId.getEditText().getText().toString();
        ContentWidgetOptions options = new ContentWidgetOptions(this, slug, productId);

        ContentWidgetItemLayout itemLayoutDetails = new ContentWidgetItemLayout();
        ContentWidgetHorizontalSliderLayout layout = new ContentWidgetHorizontalSliderLayout();

        //CardView parameters
        if (!cardViewWidth.getEditText().getText().toString().matches("") && !cardViewHeight.getEditText().getText().toString().matches(""))
            layout.setCardViewSize(Integer.parseInt(cardViewWidth.getEditText().getText().toString()), Integer.parseInt(cardViewHeight.getEditText().getText().toString()));
        if (!cardViewElevation.getEditText().getText().toString().matches(""))
            itemLayoutDetails.cardViewElevation = Integer.parseInt(cardViewElevation.getEditText().getText().toString());
        if (!cornerRadius.getEditText().getText().toString().matches(""))
            itemLayoutDetails.cardViewCornerRadius = Integer.parseInt(cornerRadius.getEditText().getText().toString());
        if (!horizontalSpacing.getEditText().getText().toString().matches(""))
            layout.cardViewHorizontalSpacing = Integer.parseInt(horizontalSpacing.getEditText().getText().toString());
        layout.cardViewBackgroundColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.regent);

        //Image size as ratio to cardview size
        if (!imageHeightRatio.getEditText().getText().toString().matches(""))
            itemLayoutDetails.imageHeightToCardHeightRatio = Float.parseFloat(imageHeightRatio.getEditText().getText().toString());
        if (!imageWidthRatio.getEditText().getText().toString().matches(""))
            itemLayoutDetails.imageWidthToCardWidthRatio = Float.parseFloat(imageWidthRatio.getEditText().getText().toString());
        itemLayoutDetails.imageMargin = 0; //have to be set when you set cardViewElevation

        //TextView product name
        itemLayoutDetails.itemTitleStyle = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
        itemLayoutDetails.itemTitleSize = 12;
        itemLayoutDetails.itemTitleColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.charcoal);
        itemLayoutDetails.itemTitleGravity = Gravity.LEFT;
        itemLayoutDetails.setItemTitleMargins(10, 0, 0, 0);

        //TextView Product price
        itemLayoutDetails.itemPriceStyle = Typeface.create("sans-serif-light", Typeface.BOLD);
        itemLayoutDetails.itemPriceSize = 12;
        itemLayoutDetails.itemPriceColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.charcoal);
        itemLayoutDetails.itemPriceGravity = Gravity.LEFT;
        itemLayoutDetails.setItemPriceMargins(10, 0, 3, 0);
        ContentWidgetAppearance contentWidgetAppearance = new ContentWidgetAppearance(layout, itemLayoutDetails);
        ContentWidget widget = new ContentWidget(options, contentWidgetAppearance);

        widget.setOnContentWidgetListener(new OnContentWidgetListener() {
            @Override
            public void widgetIsLoading(ContentWidget contentWidget, boolean isLoading) {

            }

            @Override
            public void widgetDidNotLoad(ContentWidget contentWidget, ApiError apiError) {
                Toast.makeText(getApplicationContext(), apiError.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void widgetDidLoad(ContentWidget contentWidget) {
                insertPoint.removeAllViews();
                View view = widget.getView(); // our widget
                insertPoint.addView(view); // your view which will receive widget
            }

            @Override
            public void widgetDidReceiveClickAction(ContentWidget contentWidget, BaseModel itemSelected) {
                Recommendation recommendation = (Recommendation)itemSelected;
                startActivity(WidgetRecommendedProductDetailsActivity.createIntent(getApplicationContext(), recommendation.getProductRetailerPartNo()));
            }

            @Override
            public void widgetSizeDidChange(ContentWidget contentWidget, ViewGroup.LayoutParams scrollableSize) {
                ViewGroup.LayoutParams params = insertPoint.getLayoutParams();
                params.height = scrollableSize.height;
                insertPoint.setLayoutParams(params);
            }
        });
    }
}
