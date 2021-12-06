package com.synerise.sdk.sample.ui.dev.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.synerise.sdk.content.model.BaseModel;
import com.synerise.sdk.content.widgets.ContentWidget;
import com.synerise.sdk.content.widgets.action.ImageButtonCustomAction;
import com.synerise.sdk.content.widgets.action.PredefinedActionType;
import com.synerise.sdk.content.widgets.dataModel.ContentWidgetBadgeDataModel;
import com.synerise.sdk.content.widgets.dataModel.ContentWidgetRecommendationDataModel;
import com.synerise.sdk.content.widgets.dataModel.Recommendation;
import com.synerise.sdk.content.widgets.layout.ContentWidgetBadge;
import com.synerise.sdk.content.widgets.layout.ContentWidgetHorizontalSliderLayout;
import com.synerise.sdk.content.widgets.layout.ContentWidgetBasicProductItemLayout;
import com.synerise.sdk.content.widgets.listener.OnActionItemStateListener;
import com.synerise.sdk.content.widgets.listener.OnContentWidgetListener;
import com.synerise.sdk.content.widgets.listener.OnRecommendationModelMapper;
import com.synerise.sdk.content.widgets.model.ContentWidgetAppearance;
import com.synerise.sdk.content.widgets.model.ContentWidgetOptions;
import com.synerise.sdk.content.widgets.model.ContentWidgetRecommendationsOptions;
import com.synerise.sdk.core.Synerise;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;
import com.synerise.sdk.sample.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
        String productId = "0000206438331-XS";
        String slug = "recommend2";
        if (!productInputId.getEditText().getText().toString().matches(""))
            productId = productInputId.getEditText().getText().toString();
        ContentWidgetOptions options = new ContentWidgetRecommendationsOptions(this, slug, new OnRecommendationModelMapper() {
            @Override
            public ContentWidgetRecommendationDataModel onRecommendationMapping(Recommendation recommendation) {
                HashMap<String, Object> data = recommendation.getFeed();
                String imageLink = (String) data.get("imageLink");
                String productName = (String) data.get("title");

                String price = null;
                String salePrice = null;
                try {
                    JSONObject json = new JSONObject(data.get("price").toString());
                    price = json.getString("value");
                    if (data.containsKey("salePrice")) {
                        JSONObject jsonSalePrice = new JSONObject(data.get("salePrice").toString());
                        salePrice = jsonSalePrice.getString("value");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ContentWidgetBadgeDataModel badgeDataModel = new ContentWidgetBadgeDataModel("Example badge", Color.BLACK, Color.RED);
                ContentWidgetRecommendationDataModel dataModel = new ContentWidgetRecommendationDataModel(productName, imageLink, price, salePrice, "PLN");
                dataModel.setBadgeDataModel(badgeDataModel);
                dataModel.setLabel("Black Week");
                return dataModel;
            }
        });
        options.attributes.put(ContentWidgetOptions.ContentWidgetOptionsAttributeKeyProductId, productId);
        ContentWidgetBasicProductItemLayout itemLayoutDetails = new ContentWidgetBasicProductItemLayout();
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
        itemLayoutDetails.imageScaleType = ImageView.ScaleType.CENTER_CROP;
        itemLayoutDetails.imageMargin = 0; //have to be set when you set cardViewElevation

        //TextView product label
        itemLayoutDetails.itemLabelSize = 12;
        itemLayoutDetails.itemLabelColor = Color.GREEN;
        itemLayoutDetails.setItemLabelMargins(0,0,2,0);

        //TextView product name
        itemLayoutDetails.itemTitleStyle = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
        itemLayoutDetails.itemTitleSize = 12;
        itemLayoutDetails.itemTitleColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.charcoal);
        itemLayoutDetails.setItemTitleMargins(10, 0, 0, 0);

        //TextView Product price
        itemLayoutDetails.itemPriceStyle = Typeface.create("sans-serif-light", Typeface.BOLD);
        itemLayoutDetails.itemPriceSize = 13;
        itemLayoutDetails.itemPriceColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.charcoal);
        itemLayoutDetails.setItemPriceMargins(10, 0, 3, 0);

        //TextView Product Sale price
        itemLayoutDetails.isItemSalePriceVisible = true;
        itemLayoutDetails.itemSalePriceStyle = Typeface.create("sans-serif", Typeface.BOLD);
        itemLayoutDetails.itemSalePriceSize = 13;
        itemLayoutDetails.itemSalePriceColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.red);
        itemLayoutDetails.itemSalePriceOrientation = LinearLayout.HORIZONTAL;
        itemLayoutDetails.setItemSalePriceMargins(4, 0, 3, 0);
        // CrossedOut Price color
        itemLayoutDetails.itemRegularPriceColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.com_facebook_blue);
        itemLayoutDetails.priceDecimalSeparator = ',';
        itemLayoutDetails.priceGroupSeparator = ' ';

        // DiscountLabel
        itemLayoutDetails.itemDiscountPercentageLabelSize = 13;
        itemLayoutDetails.isItemDiscountPercentageLabelVisible = true;
        itemLayoutDetails.itemDiscountPercentageLabelColor = Color.BLUE;
        itemLayoutDetails.itemDiscountPercentageLabelStyle = Typeface.create("sans-serif", Typeface.BOLD);
        itemLayoutDetails.setItemDiscountLabelMargins(4, 0, 3, 0);

        // Badge
        ContentWidgetBadge badge = new ContentWidgetBadge();
        badge.textSize = 12;
        badge.rule = RelativeLayout.ALIGN_LEFT; // left or right
        badge.setMargins(0,0,0,20);
        badge.setPaddings(10, 10, 0, 0);

        //ImageButton
        ImageButtonCustomAction favouriteIcon = new ImageButtonCustomAction();
        Drawable likeHeart = ContextCompat.getDrawable(this, R.drawable.ic_heart);
        Drawable unlikeHeart = ContextCompat.getDrawable(this, R.drawable.ic_heart_outline);
        favouriteIcon.setStateDrawables(unlikeHeart, likeHeart);
        favouriteIcon.setImageButtonCustomActionMargins(0, 0, 0, 0);
        favouriteIcon.predefinedAction = PredefinedActionType.SEND_LIKE_EVENT;
        favouriteIcon.setOnItemActionListener(new OnActionItemStateListener() {
            @Override
            public void onReceiveClickAction(BaseModel model, boolean isSelected, ImageButton imageButton) {
                Recommendation recommendationModel = (Recommendation) model;
                ViewUtils.pulse(imageButton);
            }

            @Override
            public boolean onStateCheck(BaseModel model) {
                return false;
            }
        });

        itemLayoutDetails.setBadge(badge);
        itemLayoutDetails.setItemAction(favouriteIcon);

        ContentWidgetAppearance contentWidgetAppearance = new ContentWidgetAppearance(layout, itemLayoutDetails);
        ContentWidget widget = new ContentWidget(options, contentWidgetAppearance);

        widget.setOnContentWidgetListener(new OnContentWidgetListener() {
            @Override
            public void onLoading(ContentWidget contentWidget, boolean isLoading) {

            }

            @Override
            public void onLoadingError(ContentWidget contentWidget, ApiError apiError) {
                Toast.makeText(getApplicationContext(), apiError.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoad(ContentWidget contentWidget) {
                insertPoint.removeAllViews();
                View view = widget.getView(); // our widget
                insertPoint.addView(view); // your view which will receive widget
            }

            @Override
            public void onClickActionReceive(ContentWidget contentWidget, BaseModel model) {
                Recommendation recommendationModel = (Recommendation) model;
                String itemId = recommendationModel.getItemId();
                startActivity(WidgetRecommendedProductDetailsActivity.createIntent(getApplicationContext(), itemId));
            }

            @Override
            public void onSizeChange(ContentWidget contentWidget, ViewGroup.LayoutParams size) {
                ViewGroup.LayoutParams params = insertPoint.getLayoutParams();
                params.height = size.height;
                insertPoint.setLayoutParams(params);
            }
        });
    }
}
