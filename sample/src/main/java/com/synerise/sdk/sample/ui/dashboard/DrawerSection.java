package com.synerise.sdk.sample.ui.dashboard;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.synerise.sdk.sample.R;

public enum DrawerSection {

    CATEGORIES(R.string.categories_title, R.color.dodger),
    SIGN_IN(R.string.sign_in_title, R.color.scooter),
    SIGN_UP(R.string.sign_up_title, R.color.outrageous),
    TRACKER(R.string.menu_button_tracker, R.color.radical),
    CLIENT(R.string.menu_button_client_api, R.color.atlantis),
    PROFILE(R.string.menu_button_profile_api, R.color.coral),
    INJECTOR(R.string.menu_button_injector_api, R.color.eminence),
    SETTINGS(R.string.settings_title, R.color.space);

    // ****************************************************************************************************************************************

    @StringRes private int title;
    @ColorRes private int color;

    DrawerSection(@StringRes int title, @ColorRes int color) {
        this.title = title;
        this.color = color;
    }

    @StringRes
    public int getTitle() { return title; }

    public int getColor() {
        return color;
    }
}
