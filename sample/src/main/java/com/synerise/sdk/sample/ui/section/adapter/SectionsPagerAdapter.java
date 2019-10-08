package com.synerise.sdk.sample.ui.section.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.synerise.sdk.injector.ui.walkthrough.pager.InfinitePagerAdapter;
import com.synerise.sdk.sample.data.Section;

import java.util.List;

public class SectionsPagerAdapter extends InfinitePagerAdapter<Section> {

    private final List<Section> categories;

    public SectionsPagerAdapter(FragmentManager fm, List<Section> categories) {
        super(fm, true, categories);
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        return SectionsPagerFragment.newInstance(categories.get(position));
    }

    @Override
    public int getCount() {
        return categories.size();
    }
}
