package com.synerise.sdk.sample.ui.promotion.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;
import android.widget.Toast;

import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.promotions.Promotions;
import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.promotions.model.promotion.PromotionDiscountType;
import com.synerise.sdk.promotions.model.promotion.PromotionImage;
import com.synerise.sdk.promotions.model.promotion.PromotionStatus;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;
import com.synerise.sdk.sample.util.ViewUtils;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PromotionActivity extends BaseActivity {

    private IApiCall apiCall;
    private FloatingActionButton fab;

    public static Intent createIntent(Context context, Promotion promotion) {
        Intent intent = new Intent(context, PromotionActivity.class);
        intent.putExtra(Args.SERIALIZABLE, promotion);
        return intent;
    }

    @Override
    @SuppressLint("StringFormatMatches")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        ToolbarHelper.setUpChildToolbar(this);

        Intent intent = getIntent();
        Promotion promotion = ((Promotion) (intent.getSerializableExtra(Args.SERIALIZABLE)));
        if (promotion == null) return;

        List<PromotionImage> images = promotion.getImages();
        if (!images.isEmpty())
            ViewUtils.loadImage(images.get(0).getUrl(), findViewById(R.id.parallax_image), findViewById(R.id.image_progress_bar));

        String headline = promotion.getHeadline();
        ToolbarHelper.setUpCollapsingToolbar(this, headline);
        ((TextView) findViewById(R.id.promotion_name)).setText(headline);
        ((TextView) findViewById(R.id.promotion_code)).setText(promotion.getCode());

        ((TextView) findViewById(R.id.promotion_description)).setText(promotion.getDescription());

        TextView discount = findViewById(R.id.promotion_discount);
        if (promotion.getDiscountType() == PromotionDiscountType.AMOUNT) {
            discount.setText(getString(R.string.default_value_dollar, promotion.getDiscountValue()));
        } else if (promotion.getDiscountType() == PromotionDiscountType.PERCENT) {
            discount.setText(getString(R.string.default_value_percent, promotion.getDiscountValue()));
        } else {
            discount.setText(promotion.getDiscountValue());
        }

        fab = findViewById(R.id.promotion_activate);
        if (promotion.getStatus() == PromotionStatus.ASSIGNED) {
            fab.setVisibility(VISIBLE);
            //fab.show();
            fab.setOnClickListener(v -> activatePromotion(promotion.getCode()));
        } else {
            fab.setVisibility(GONE);
            //fab.hide();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    private void activatePromotion(String code) {
        Toast.makeText(this, R.string.default_activating, Toast.LENGTH_SHORT).show();
        if (apiCall != null) apiCall.cancel();
        apiCall = Promotions.activatePromotionByCode(code);
        apiCall.execute(() -> {
                            Toast.makeText(this, R.string.default_success, Toast.LENGTH_SHORT).show();
                            fab.setVisibility(GONE);
                            //fab.hide();
                        },
                        new DataActionListener<ApiError>() {
                            @Override
                            public void onDataAction(ApiError data) {
                                showAlertError(data);
                            }
                        });
    }
}