package com.synerise.sdk.sample.ui.section.indicators;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.synerise.sdk.R;
import com.synerise.sdk.core.utils.ViewUtils;

public class IndicatorDot extends FrameLayout {

    private static final int TRANSITION_DURATION = 250;
    @ColorInt private final int activeColor;
    @ColorInt private final int inactiveColor;

    public IndicatorDot(Context context, @ColorInt int activeColor, @ColorInt int inactiveColor, int size) {
        super(context);
        this.activeColor = activeColor;
        this.inactiveColor = inactiveColor;
        size = (int) ViewUtils.dp2px(size / getResources().getDisplayMetrics().density, getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
        int margin = (size / 4);
        layoutParams.setMargins(margin, margin, margin, margin);
        setLayoutParams(layoutParams);
        activateDot(false);
    }

    public static IndicatorDot create(Context context, @ColorInt int activeColor, @ColorInt int inactiveColor, int size) {
        return new IndicatorDot(context, activeColor, inactiveColor, size);
    }

    public void activateDot(boolean isActivating) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                isActivating ? inactiveColor : activeColor,
                isActivating ? activeColor : inactiveColor);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.synerise_indicator);
                DrawableCompat.setTint(drawable, (Integer) animator.getAnimatedValue());
                setBackground(drawable);
            }
        });
        colorAnimation.setDuration(TRANSITION_DURATION);
        float activeScale = 1.0f;
        float inactiveScale = 0.5f;
        ScaleAnimation scaleAnimation = new ScaleAnimation(isActivating ? inactiveScale : activeScale,
                isActivating ? activeScale : inactiveScale,
                isActivating ? inactiveScale : activeScale,
                isActivating ? activeScale : inactiveScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(TRANSITION_DURATION);
        scaleAnimation.setFillAfter(true);
        colorAnimation.start();
        startAnimation(scaleAnimation);
    }
}

