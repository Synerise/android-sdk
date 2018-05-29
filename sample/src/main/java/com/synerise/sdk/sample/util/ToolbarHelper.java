package com.synerise.sdk.sample.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.synerise.sdk.sample.R;

public class ToolbarHelper {

    public static void setUpChildToolbar(AppCompatActivity activity) {
        setActionBar(activity, R.drawable.ic_arrow_back, null);
    }

    public static void setUpChildToolbar(AppCompatActivity activity, @StringRes int title) {
        setActionBar(activity, R.drawable.ic_arrow_back, activity.getString(title));
    }

    public static void setUpChildToolbar(AppCompatActivity activity, String title) {
        setActionBar(activity, R.drawable.ic_arrow_back, title);
    }

    public static void setUpNavToolbar(AppCompatActivity activity, @StringRes int title) {
        setActionBar(activity, R.drawable.ic_navigation, activity.getString(title));
    }

    public static void updateToolbar(AppCompatActivity activity, @StringRes int title) {
        Toolbar toolbar = getSupportToolbar(activity);
        toolbar.setTitle(activity.getString(title));
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

    // ****************************************************************************************************************************************

    private static Toolbar getToolbar(AppCompatActivity activity) {
        return activity.findViewById(R.id.toolbar);
    }

    private static Toolbar getSupportToolbar(AppCompatActivity activity) {
        Toolbar toolbar = getToolbar(activity);
        activity.setSupportActionBar(toolbar);
        return toolbar;
    }

    private static Toolbar getActionToolbar(AppCompatActivity activity) {
        Toolbar toolbar = getSupportToolbar(activity);
        return toolbar;
    }

    // ****************************************************************************************************************************************

    public static Toolbar setActionBar(AppCompatActivity activity) {
        Toolbar toolbar = getToolbar(activity);
        toolbar.setTitle(R.string.app_name);
        activity.setSupportActionBar(toolbar);
        return toolbar;
    }

    private static void setActionBar(AppCompatActivity activity, @DrawableRes int icon, String title) {
        Toolbar toolbar = getSupportToolbar(activity);
        toolbar.setNavigationIcon(icon);
        if(title != null) toolbar.setTitle(title);
        toolbar.setPadding(0, getStatusBarHeight(activity), 0, 0);
        activity.setSupportActionBar(toolbar); // set title must be before setSupportActionBar
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