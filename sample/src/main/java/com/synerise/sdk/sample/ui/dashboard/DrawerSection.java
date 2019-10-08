package com.synerise.sdk.sample.ui.dashboard;

import androidx.annotation.StringRes;

import com.synerise.sdk.sample.R;

public enum DrawerSection {

    SECTIONS(R.string.categories_title),
    PROFILE(R.string.profile_title),
    PROMOTIONS(R.string.promotions_title),
    CART(R.string.cart_title),
    FAVOURITE(R.string.favourite_title),
    DEV_TOOLS(R.string.dev_tools_title),
    SETTINGS(R.string.settings_title);

    @StringRes private int title;

    DrawerSection(@StringRes int title) {
        this.title = title;
    }

    @StringRes
    public int getTitle() { return title; }
}
