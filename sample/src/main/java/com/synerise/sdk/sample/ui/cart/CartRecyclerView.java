package com.synerise.sdk.sample.ui.cart;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.synerise.sdk.core.utils.ViewUtils;
import com.synerise.sdk.sample.R;

public class CartRecyclerView extends RecyclerView {

    private final int maxHeightDp;

    public CartRecyclerView(Context context) {
        super(context);
        maxHeightDp = (int) ViewUtils.px2dp(getResources().getDimensionPixelOffset(R.dimen.cart_default_height), context);
    }

    public CartRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CartRecyclerView, 0, 0);
        try {
            int dimensionPixelSize = array.getDimensionPixelSize(R.styleable.CartRecyclerView_max_height,
                                                                 getResources().getDimensionPixelSize(R.dimen.cart_default_height));
            maxHeightDp = (int) ViewUtils.px2dp(dimensionPixelSize, context);
        } finally {
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec((int) ViewUtils.dp2px(maxHeightDp, getContext()), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
