package com.synerise.sdk.sample.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.synerise.sdk.client.Client;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.injector.callback.OnBannerListener;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.auth.SignInFragment;
import com.synerise.sdk.sample.ui.auth.SignUpFragment;
import com.synerise.sdk.sample.ui.category.CategoriesFragment;
import com.synerise.sdk.sample.ui.client.ClientFragment;
import com.synerise.sdk.sample.ui.events.TrackerFragment;
import com.synerise.sdk.sample.ui.injector.InjectorFragment;
import com.synerise.sdk.sample.ui.profile.ProfileFragment;
import com.synerise.sdk.sample.ui.settings.SettingsFragment;
import com.synerise.sdk.sample.util.KeyboardHelper;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.CATEGORIES;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.CLIENT;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.INJECTOR;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.PROFILE;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.SETTINGS;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.SIGN_IN;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.SIGN_UP;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.TRACKER;

public class DashboardActivity extends BaseActivity implements IDashboardContext {

    @BindView(R.id.flowing_drawer) FlowingDrawer drawer;
    @BindView(R.id.menu_sign_in) TextView signInTextView;
    @BindView(R.id.menu_sign_up) TextView signUpTextView;

    private BaseFragment currentFragment;

    public static Intent createIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        Injector.setOnBannerListener(new OnBannerListener() {
            @Override
            public boolean shouldPresent(Map<String, String> data) {
                return true; // allow to show banner
            }

            @Override
            public void onPresented() {
                Log.d("DashboardActivity", "Banner has been presented.");
            }

            @Override
            public void onClosed() {
                Log.d("DashboardActivity", "Banner has been closed.");
            }
        });

        ToolbarHelper.setUpNavToolbar(this, R.string.app_name);
        changeFragment(CATEGORIES);
        handleSigningVisibility();
    }

    private void handleSigningVisibility() {
        if (Client.isSignedIn()) {
            signInTextView.setVisibility(GONE);
            signUpTextView.setVisibility(GONE);
        } else {
            signInTextView.setVisibility(VISIBLE);
            signUpTextView.setVisibility(VISIBLE);
        }
    }

    // ****************************************************************************************************************************************

    @OnClick({R.id.menu_categories, R.id.menu_sign_in, R.id.menu_sign_up, R.id.menu_tracker,
              R.id.menu_client, R.id.menu_profile, R.id.menu_injector, R.id.menu_settings})
    public void onDrawerItemSelected(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.menu_categories:
                if (!(currentFragment instanceof CategoriesFragment)) changeFragment(CATEGORIES);
                break;
            case R.id.menu_sign_in:
                if (!(currentFragment instanceof SignInFragment)) changeFragment(SIGN_IN);
                break;
            case R.id.menu_sign_up:
                if (!(currentFragment instanceof SignUpFragment)) changeFragment(SIGN_UP);
                break;
            case R.id.menu_tracker:
                if (!(currentFragment instanceof TrackerFragment)) changeFragment(TRACKER);
                break;
            case R.id.menu_client:
                if (!(currentFragment instanceof ClientFragment)) changeFragment(CLIENT);
                break;
            case R.id.menu_profile:
                if (!(currentFragment instanceof ProfileFragment)) changeFragment(PROFILE);
                break;
            case R.id.menu_injector:
                if (!(currentFragment instanceof InjectorFragment)) changeFragment(INJECTOR);
                break;
            case R.id.menu_settings:
                if (!(currentFragment instanceof SettingsFragment)) changeFragment(SETTINGS);
                break;
            default:
                return;
        }
        drawer.closeMenu(true);
    }

    // ****************************************************************************************************************************************

    private void changeFragment(DrawerSection section) {
        switch (section) {
            case CATEGORIES:
                currentFragment = CategoriesFragment.newInstance();
                break;
            case SIGN_IN:
                currentFragment = SignInFragment.newInstance();
                break;
            case SIGN_UP:
                currentFragment = SignUpFragment.newInstance();
                break;
            case TRACKER:
                currentFragment = TrackerFragment.newInstance();
                break;
            case CLIENT:
                currentFragment = ClientFragment.newInstance();
                break;
            case PROFILE:
                currentFragment = ProfileFragment.newInstance();
                break;
            case INJECTOR:
                currentFragment = InjectorFragment.newInstance();
                break;
            case SETTINGS:
                currentFragment = SettingsFragment.newInstance();
                break;
            default:
                return;
        }

        ToolbarHelper.updateToolbar(this, section.getTitle(), section.getColor());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, currentFragment)
                .commit();
    }

    // ****************************************************************************************************************************************

    @Override
    public void handleSignUpSuccess() {
        changeFragment(SIGN_IN);
    }

    @Override
    public void handleSignInSuccess() {
        changeFragment(CATEGORIES);
        KeyboardHelper.hideKeyboard(this);
        handleSigningVisibility();
    }

    // ****************************************************************************************************************************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openMenu(true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isMenuVisible()) {
            drawer.closeMenu(true);
        } else if (!(currentFragment instanceof CategoriesFragment)) {
            changeFragment(CATEGORIES);
        } else {
            super.onBackPressed();
        }
    }
}
