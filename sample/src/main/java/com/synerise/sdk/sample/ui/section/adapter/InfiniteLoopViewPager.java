package com.synerise.sdk.sample.ui.section.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;

import java.util.ArrayList;

public class InfiniteLoopViewPager extends ViewPager {

    private ArrayList<OnPageChangeListener> onPageChangeListeners = new ArrayList<>();
    private boolean isLoopEnabled;

    public InfiniteLoopViewPager(Context context) {
        super(context);
    }

    public InfiniteLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * must be called so {@link InfiniteLoopViewPager#addOnPageChangeListener(OnPageChangeListener)} works properly.
     * must be called after {@link InfiniteLoopViewPager#setAdapter(PagerAdapter)}.
     */
    public void init(final InfinitePagerAdapter adapter) {
        super.addOnPageChangeListener(new OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isLoopEnabled) {
                    if (position == 0 && positionOffset == 0) {
                        InfiniteLoopViewPager.this.setCurrentItem(adapter.getLastNotDummyPagePosition(), false);
                    } else if (position == adapter.getLastPagePosition() && positionOffset == 0) {
                        InfiniteLoopViewPager.this.setCurrentItem(1, false);
                    } else {
                        onPageScrolledCallback(position != 0 ? position - 1 : adapter.getLastNotDummyPagePosition() - 1,
                                positionOffset, positionOffsetPixels);
                    }
                } else {
                    onPageScrolledCallback(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                InfinitePagerAdapter adapter = (InfinitePagerAdapter) getAdapter();
                if (adapter != null) {
                    if (!(isLoopEnabled && (position == 0 || position == adapter.getLastPagePosition())))
                        onPageSelectedCallback(isLoopEnabled ? position - 1 : position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                onPageScrollStateChangedCallback(state);
            }
        });
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        if (isLoopEnabled) {
            setCurrentItem(1);//must be fired after setting adapter. set to 1 because 0 is dummy page.
        } else {
            onPageSelectedCallback(0); //by default page selected callback is not called.
        }
        if (adapter instanceof InfinitePagerAdapter)
            init((InfinitePagerAdapter) adapter);
    }

    @Override
    public void addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        onPageChangeListeners.add(listener);
    }

    public void setIsLoopEnabled(boolean isLoopEnabled) {
        this.isLoopEnabled = isLoopEnabled;
    }

    private void onPageScrolledCallback(int position, float positionOffset, int positionOffsetPixels) {
        for (OnPageChangeListener onPageChangeListener : onPageChangeListeners) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    private void onPageSelectedCallback(int position) {
        for (OnPageChangeListener onPageChangeListener : onPageChangeListeners) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    private void onPageScrollStateChangedCallback(int state) {
        for (OnPageChangeListener onPageChangeListener : onPageChangeListeners) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}
