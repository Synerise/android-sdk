package com.synerise.sdk.sample.ui.section.adapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public abstract class InfinitePagerAdapter<T> extends FragmentPagerAdapter {

    private final boolean loopEnabled;

    public InfinitePagerAdapter(FragmentManager fm, boolean loopEnabled, List<T> items) {
        super(fm);
        this.loopEnabled = loopEnabled;
        if (loopEnabled) {
            items.add(0, items.get(items.size() - 1));
            items.add(items.get(1));
        }
    }

    public int getLastPagePosition() {
        return getCount() - 1;
    }

    public int getLastNotDummyPagePosition() {
        return getCount() - 2;
    }

    public boolean isLoopEnabled() {
        return loopEnabled;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (!(loopEnabled && (position == 1 || position == getCount() - 2)))
            super.destroyItem(container, position, object);
    }
}

