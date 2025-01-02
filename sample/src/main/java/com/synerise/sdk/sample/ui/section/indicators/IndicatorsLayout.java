package com.synerise.sdk.sample.ui.section.indicators;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.synerise.sdk.R;
import com.synerise.sdk.sample.ui.section.adapter.InfinitePagerAdapter;

import java.util.ArrayList;

public class IndicatorsLayout extends LinearLayout {

    private final int size;
    @ColorInt private int activeColor;
    @ColorInt private int inactiveColor;
    private ArrayList<IndicatorDot> indicatorDots;
    private IndicatorDot activeIndicatorDot;

    public IndicatorsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SyneriseIndicatorDot, 0, 0);
        try {
            activeColor = a.getColor(R.styleable.SyneriseIndicatorDot_active_color,
                    ContextCompat.getColor(context, R.color.syneriseWhite));
            inactiveColor = a.getColor(R.styleable.SyneriseIndicatorDot_inactive_color,
                    ContextCompat.getColor(context, R.color.syneriseTranslucent));
            size = (int) a.getDimension(R.styleable.SyneriseIndicatorDot_size,
                    getResources().getDimension(R.dimen.synerise_space_small));
        } finally {
            a.recycle();
        }
    }

    public void createDots(int count) {
        indicatorDots = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            IndicatorDot indicatorDot = IndicatorDot.create(getContext(), activeColor, inactiveColor, size);
            addView(indicatorDot);
            indicatorDots.add(indicatorDot);
        }
    }

    public void activateIndicator(int position) {
        deactivateRecentIndicator();
        indicatorDots.get(position).activateDot(true);
        activeIndicatorDot = indicatorDots.get(position);
    }

    private void deactivateRecentIndicator() {
        if (activeIndicatorDot != null)
            activeIndicatorDot.activateDot(false);
    }

    public void init(ViewPager viewPager) {
        if (viewPager.getAdapter() instanceof InfinitePagerAdapter &&
                ((InfinitePagerAdapter) viewPager.getAdapter()).isLoopEnabled()) {
            createDots(viewPager.getAdapter().getCount() - 2);
        } else {
            createDots(viewPager.getAdapter().getCount());
        }
        activateIndicator(0);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                activateIndicator(position);
            }
        });
    }
}
