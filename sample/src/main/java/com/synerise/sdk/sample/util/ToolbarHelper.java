package com.synerise.sdk.sample.util;

import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.synerise.sdk.sample.R;

public class ToolbarHelper {

    public static void setUpChildToolbar(AppCompatActivity activity) {
        setActionBar(activity, R.drawable.ic_back);
    }

    public static void setUpChildToolbar(AppCompatActivity activity, @StringRes int title) {
        setActionBar(activity, R.drawable.ic_back, title);
    }

    public static void setUpNavToolbar(AppCompatActivity activity, @StringRes int title) {
        setActionBar(activity, R.drawable.ic_navigation, title);
    }

    public static void updateToolbar(AppCompatActivity activity, @StringRes int title, @ColorRes int color) {
        Toolbar toolbar = getToolbar(activity);
        toolbar.setTitle(activity.getString(title));
        toolbar.setBackgroundColor(ContextCompat.getColor(activity, color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
            window.setNavigationBarColor(ContextCompat.getColor(activity, color));
        }
    }

    public static void setUpCollapsingToolbar(AppCompatActivity activity, @StringRes int title) {
        CollapsingToolbarLayout collapsingToolbarLayout = activity.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(activity.getString(title));
    }

    public static void setUpCollapsingToolbar(AppCompatActivity activity, String title) {
        CollapsingToolbarLayout collapsingToolbarLayout = activity.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);
    }

    // ****************************************************************************************************************************************

    private static Toolbar getToolbar(AppCompatActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }

    private static Toolbar getActionToolbar(AppCompatActivity activity) {
        Toolbar toolbar = getToolbar(activity);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return toolbar;
    }

    // ****************************************************************************************************************************************

    private static void setActionBar(AppCompatActivity activity, @DrawableRes int icon) {
        Toolbar toolbar = getActionToolbar(activity);
        toolbar.setNavigationIcon(icon);
    }

    private static void setActionBar(AppCompatActivity activity, @DrawableRes int icon, @StringRes int title) {
        Toolbar toolbar = getActionToolbar(activity);
        toolbar.setNavigationIcon(icon);
        toolbar.setTitle(activity.getString(title));
    }
}