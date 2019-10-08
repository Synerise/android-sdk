package com.synerise.sdk.sample.util;

import android.content.Context;
import android.os.Build;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Window;

import com.synerise.sdk.sample.R;

public class ToolbarHelper {

    public static Toolbar setUpToolbar(AppCompatActivity activity) {
        Toolbar toolbar = getToolbar(activity);
        toolbar.setTitle(R.string.app_name);
        activity.setSupportActionBar(toolbar);
        return toolbar;
    }

    public static void setUpChildToolbar(AppCompatActivity activity) {
        setActionBar(activity, R.drawable.ic_arrow_back);
    }

    public static void setUpChildToolbar(AppCompatActivity activity, String title) {
        setActionBar(activity, R.drawable.ic_arrow_back, title);
    }

    public static void setUpChildToolbar(AppCompatActivity activity, @StringRes int title) {
        setActionBar(activity, R.drawable.ic_arrow_back, activity.getString(title));
    }

    public static void setUpChildToolbar(AppCompatActivity activity, @StringRes int title, @ColorRes int color) {
        setActionBar(activity, R.drawable.ic_arrow_back, activity.getString(title), color);
    }

    public static void updateToolbar(AppCompatActivity activity, @StringRes int title) {
        Toolbar toolbar = getSupportToolbar(activity);
        toolbar.setTitle(activity.getString(title));
        setDefaultToolbarColor(activity, toolbar);
    }

    private static void setDefaultToolbarColor(AppCompatActivity activity, Toolbar toolbar) {
        int color = ContextCompat.getColor(activity, R.color.primary);
        toolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }
    }

    public static void updateToolbarColor(AppCompatActivity activity, int evaluatedColor) {
        Toolbar toolbar = getToolbar(activity);
        toolbar.setBackgroundColor(evaluatedColor);
    }

    public static void updateToolbarTitle(AppCompatActivity activity, int title) {
        Toolbar toolbar = getToolbar(activity);
        toolbar.setTitle(title);
    }

    public static void setUpCollapsingToolbar(AppCompatActivity activity, String title) {
        CollapsingToolbarLayout collapsingToolbarLayout = activity.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);
    }

    private static Toolbar getToolbar(AppCompatActivity activity) {
        return activity.findViewById(R.id.toolbar);
    }

    private static Toolbar getSupportToolbar(AppCompatActivity activity) {
        Toolbar toolbar = getToolbar(activity);
        activity.setSupportActionBar(toolbar);
        return toolbar;
    }

    private static void setActionBar(AppCompatActivity activity, @DrawableRes int icon) {
        Toolbar toolbar = createToolbar(activity, icon);
        activity.setSupportActionBar(toolbar);
    }

    private static void setActionBar(AppCompatActivity activity, @DrawableRes int icon, String title) {
        Toolbar toolbar = createToolbar(activity, icon);
        if (title != null) toolbar.setTitle(title); // set title must be before setSupportActionBar
        activity.setSupportActionBar(toolbar);
    }

    private static void setActionBar(AppCompatActivity activity, @DrawableRes int icon, String title, @ColorRes int color) {
        Toolbar toolbar = createToolbar(activity, icon);
        if (title != null) toolbar.setTitle(title); // set title must be before setSupportActionBar
        toolbar.setBackgroundColor(activity.getResources().getColor(color));
        activity.setSupportActionBar(toolbar);
    }

    private static Toolbar createToolbar(AppCompatActivity activity, @DrawableRes int icon) {
        Toolbar toolbar = getSupportToolbar(activity);
        toolbar.setNavigationIcon(icon);
        toolbar.setPadding(0, getStatusBarHeight(activity), 0, 0);
        return toolbar;
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}