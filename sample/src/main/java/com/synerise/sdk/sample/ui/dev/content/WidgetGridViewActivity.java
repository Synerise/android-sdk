package com.synerise.sdk.sample.ui.dev.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.synerise.sdk.content.model.BaseModel;
import com.synerise.sdk.content.widgets.ContentWidget;
import com.synerise.sdk.content.widgets.action.ImageButtonCustomAction;
import com.synerise.sdk.content.widgets.action.PredefinedActionType;
import com.synerise.sdk.content.widgets.dataModel.ContentWidgetRecommendationDataModel;
import com.synerise.sdk.content.widgets.dataModel.Recommendation;
import com.synerise.sdk.content.widgets.layout.ContentWidgetGridLayout;
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

public class WidgetGridViewActivity extends BaseActivity {

    private LinearLayout insertPoint;
    private TextInputLayout cardViewElevation;
    private TextInputLayout cardViewWidth;
    private TextInputLayout cardViewHeight;
    private TextInputLayout productInputId;
    private TextInputLayout imageWidthRatio;
    private TextInputLayout imageHeightRatio;
    private TextInputLayout horizontalSpacing;
    private TextInputLayout verticalSpacing;
    private TextInputLayout cornerRadius;

    public static Intent createIntent(Context context) {
        return new Intent(context, WidgetGridViewActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_gridview);

        ToolbarHelper.setUpChildToolbar(this, R.string.content_api);

        productInputId = findViewById(R.id.input_product_id);
        cardViewElevation = findViewById(R.id.input_elevation);
        cardViewWidth = findViewById(R.id.input_item_width);
        cardViewHeight = findViewById(R.id.input_item_height);
        imageWidthRatio = findViewById(R.id.input_image_width_ratio);
        imageHeightRatio = findViewById(R.id.input_image_height_ratio);
        horizontalSpacing = findViewById(R.id.input_horizontal_spacing);
        verticalSpacing = findViewById(R.id.input_vertical_spacing);
        cornerRadius = findViewById(R.id.input_corner_radius);
        findViewById(R.id.load_fullscreen_widget).setOnClickListener(v -> loadFullScreenWidget());
        findViewById(R.id.load_halfscreen_widget).setOnClickListener(v -> loadHalfScreenWidget());
        insertPoint = findViewById(R.id.insert_point);
    }

    private void loadFullScreenWidget() {
        String slug = "recommend2";
        String productId = "100004";
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
                return new ContentWidgetRecommendationDataModel(productName, imageLink, price, salePrice, "USD");
            }});
        options.attributes.put(ContentWidgetOptions.ContentWidgetOptionsAttributeKeyProductId, productId);
        ContentWidgetBasicProductItemLayout itemLayoutDetails = new ContentWidgetBasicProductItemLayout();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float screenWidthDp = displayMetrics.widthPixels;
        ContentWidgetGridLayout layout = new ContentWidgetGridLayout(screenWidthDp);

        //CardView parameters
        if (!cardViewWidth.getEditText().getText().toString().matches("") && !cardViewHeight.getEditText().getText().toString().matches(""))
            layout.setCardViewSize(Integer.parseInt(cardViewWidth.getEditText().getText().toString()), Integer.parseInt(cardViewHeight.getEditText().getText().toString()));
        if (!cardViewElevation.getEditText().getText().toString().matches(""))
            itemLayoutDetails.cardViewElevation = Integer.parseInt(cardViewElevation.getEditText().getText().toString());
        if (!cornerRadius.getEditText().getText().toString().matches(""))
            itemLayoutDetails.cardViewCornerRadius = Integer.parseInt(cornerRadius.getEditText().getText().toString());
        if (!horizontalSpacing.getEditText().getText().toString().matches(""))
            layout.cardViewHorizontalSpacing = Integer.parseInt(horizontalSpacing.getEditText().getText().toString());
        if (!verticalSpacing.getEditText().getText().toString().matches(""))
            layout.cardViewVerticalSpacing = Integer.parseInt(verticalSpacing.getEditText().getText().toString());
        layout.includeEdgeSpacing = false;
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
        itemLayoutDetails.setItemTitleMargins(10, 0, 10, 0);

        //TextView Product price
        itemLayoutDetails.itemPriceStyle = Typeface.create("sans-serif-light", Typeface.BOLD);
        itemLayoutDetails.itemPriceSize = 12;
        itemLayoutDetails.itemPriceColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.charcoal);
        itemLayoutDetails.setItemPriceMargins(10, 0, 10, 0);

        //TextView Product Sale price
        itemLayoutDetails.isItemSalePriceVisible = true;
        itemLayoutDetails.itemSalePriceStyle = Typeface.create("sans-serif", Typeface.BOLD);
        itemLayoutDetails.itemSalePriceSize = 13;
        itemLayoutDetails.itemSalePriceColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.red);
        itemLayoutDetails.itemSalePriceOrientation = LinearLayout.VERTICAL;
        itemLayoutDetails.setItemSalePriceMargins(0, 0, 10, 0);

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
                return true;
            }
        });

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
                View view = widget.getView();
                insertPoint.addView(view);
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

    private void loadHalfScreenWidget() {
        ViewGroup.LayoutParams params = insertPoint.getLayoutParams();
        params.height = 450; //constant height for half screen
        insertPoint.setLayoutParams(params);
        String slug = "recommend2";
        String productId = "100004";
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
                    JSONObject jsonPrice = new JSONObject(data.get("price").toString());
                    price = jsonPrice.getString("value");
                    if (data.containsKey("salePrice")) {
                        JSONObject jsonSalePrice = new JSONObject(data.get("salePrice").toString());
                        salePrice = jsonSalePrice.getString("value");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new ContentWidgetRecommendationDataModel(productName, imageLink, price, salePrice, null);
            }});
        options.attributes.put(ContentWidgetOptions.ContentWidgetOptionsAttributeKeyProductId, productId);
        ContentWidgetBasicProductItemLayout itemLayoutDetails = new ContentWidgetBasicProductItemLayout();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float screenWidthDp = displayMetrics.widthPixels;
        ContentWidgetGridLayout layout = new ContentWidgetGridLayout(screenWidthDp);

        //CardView parameters
        if (!cardViewWidth.getEditText().getText().toString().matches("") && !cardViewHeight.getEditText().getText().toString().matches(""))
            layout.setCardViewSize(Integer.parseInt(cardViewWidth.getEditText().getText().toString()), Integer.parseInt(cardViewHeight.getEditText().getText().toString()));
        if (!cardViewElevation.getEditText().getText().toString().matches(""))
            itemLayoutDetails.cardViewElevation = Integer.parseInt(cardViewElevation.getEditText().getText().toString());
        if (!cornerRadius.getEditText().getText().toString().matches(""))
            itemLayoutDetails.cardViewCornerRadius = Integer.parseInt(cornerRadius.getEditText().getText().toString());
        if (!horizontalSpacing.getEditText().getText().toString().matches(""))
            layout.cardViewHorizontalSpacing = Integer.parseInt(horizontalSpacing.getEditText().getText().toString());
        if (!verticalSpacing.getEditText().getText().toString().matches(""))
            layout.cardViewVerticalSpacing = Integer.parseInt(verticalSpacing.getEditText().getText().toString());
        layout.includeEdgeSpacing = false;
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
        itemLayoutDetails.setItemTitleMargins(10, 0, 10, 0);

        //TextView Product price
        itemLayoutDetails.itemPriceStyle = Typeface.create("sans-serif-light", Typeface.BOLD);
        itemLayoutDetails.itemPriceSize = 12;
        itemLayoutDetails.itemPriceColor = ContextCompat.getColor(Synerise.getApplicationContext(), R.color.charcoal);
        itemLayoutDetails.setItemPriceMargins(10, 0, 10, 0);

        //TextView Product Sale price
        itemLayoutDetails.isItemSalePriceVisible = false;

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
                View view = widget.getView();
                insertPoint.addView(view);
            }

            @Override
            public void onClickActionReceive(ContentWidget contentWidget, BaseModel model) {
                Recommendation recommendationModel = (Recommendation) model;
                String itemId = recommendationModel.getItemId();
                startActivity(WidgetRecommendedProductDetailsActivity.createIntent(getApplicationContext(), itemId));
            }

            @Override
            public void onSizeChange(ContentWidget contentWidget, ViewGroup.LayoutParams size) {

            }
        });
    }
}
