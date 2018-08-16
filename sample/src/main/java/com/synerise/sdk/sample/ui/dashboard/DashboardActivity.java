package com.synerise.sdk.sample.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.auth.SignInActivity;
import com.synerise.sdk.sample.ui.cart.CartFragment;
import com.synerise.sdk.sample.ui.dev.DeveloperFragment;
import com.synerise.sdk.sample.ui.profile.ProfileFragment;
import com.synerise.sdk.sample.ui.section.SectionsFragment;
import com.synerise.sdk.sample.ui.section.UpdateStatusBarColorInterface;
import com.synerise.sdk.sample.util.KeyboardHelper;
import com.synerise.sdk.sample.util.ToolbarHelper;

import javax.inject.Inject;

import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.CART;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.DEV_TOOLS;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.PROFILE;
import static com.synerise.sdk.sample.ui.dashboard.DrawerSection.SECTIONS;

public class DashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                               UpdateStatusBarColorInterface,
                                                               ProfileUpdatedListener {

    private static final int SIGN_IN_REQUEST_CODE = 830;
    public static final int BARCODE_SCANNER_REQUEST_CODE = 531;

    private MenuItem menuProfile;
    private MenuItem menuSignOut;
    private View signedInNavHeader;
    private View signInButtonNavHeader;
    private BaseFragment currentFragment;

    @Inject AccountManager accountManager;
    private DrawerLayout drawer;
    private TextView nameNavHeader;
    private TextView emailNavHeader;
    private NavigationView navigationView;

    public static Intent createIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ((App) getApplication()).getComponent().inject(this);

        ToolbarHelper.setUpToolbar(this);

        drawer = findViewById(R.id.drawer_layout);

        /*mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();*/

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        Menu menu = navigationView.getMenu();

        menuProfile = menu.findItem(R.id.menu_profile);
        menuSignOut = menu.findItem(R.id.menu_sign_out);
        signedInNavHeader = headerView.findViewById(R.id.signed_in_nav_header);
        signInButtonNavHeader = headerView.findViewById(R.id.sign_in_nav_header_button);

        nameNavHeader = headerView.findViewById(R.id.header_name_textview);
        emailNavHeader = headerView.findViewById(R.id.header_email_textview);

        updateNavHeader();

        signInButtonNavHeader.setOnClickListener(v -> startActivityForResult(SignInActivity.createIntent(this), SIGN_IN_REQUEST_CODE));

        handleSigningVisibility();

        changeFragment(SECTIONS);
    }

    private void updateNavHeader() {
        if (accountManager.getFirstName() != null && accountManager.getLastName() != null)
            nameNavHeader.setText(accountManager.getFirstName() + " " + accountManager.getLastName());
        if (accountManager.getEmail() != null)
            emailNavHeader.setText(accountManager.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!(currentFragment instanceof SectionsFragment)) {
            changeFragment(SECTIONS);
        } else {
            super.onBackPressed();
        }
    }

    // ****************************************************************************************************************************************

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START, true);

        int id = item.getItemId();
        switch (id) {
            //            case R.id.menu_sections:
            //                if (!(currentFragment instanceof SectionsFragment)) changeFragment(SECTIONS);
            //                break;
            case R.id.menu_profile:
                if (!(currentFragment instanceof ProfileFragment)) changeFragment(PROFILE);
                break;
            case R.id.menu_cart:
                if (!(currentFragment instanceof CartFragment)) changeFragment(CART);
                break;
            case R.id.menu_dev:
                if (!(currentFragment instanceof DeveloperFragment)) changeFragment(DEV_TOOLS);
                break;
            case R.id.menu_sign_out:
                Client.signOut();
                if (!(currentFragment instanceof SectionsFragment)) changeFragment(SECTIONS);
                handleSigningVisibility();
                break;
        }

        return true;
    }

    private void changeFragment(DrawerSection section) {
        uncheckAllNavigationViewItems();
        switch (section) {
            case SECTIONS:
                currentFragment = SectionsFragment.newInstance();
                break;
            case DEV_TOOLS:
                currentFragment = DeveloperFragment.newInstance();
                navigationView.setCheckedItem(R.id.menu_dev);
                break;
            case CART:
                currentFragment = CartFragment.newInstance();
                navigationView.setCheckedItem(R.id.menu_cart);
                break;
            case PROFILE:
                currentFragment = ProfileFragment.newInstance();
                navigationView.setCheckedItem(R.id.menu_profile);
                break;
            default:
                return;
        }

        if (!(currentFragment instanceof SectionsFragment)) {
            ToolbarHelper.updateToolbar(this, section.getTitle());
            drawer.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.primary));
            showBackIcon();
        } else {
            showHamburgerIcon();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                //                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.container, currentFragment)
                .commit();
    }

    private void uncheckAllNavigationViewItems() {
        for (int i = 0; i < navigationView.getMenu().size(); i++) { navigationView.getMenu().getItem(i).setChecked(false); }
    }

    private void handleSigningVisibility() {
        if (Client.isSignedIn()) {
            updateNavHeader();
            signedInNavHeader.setVisibility(View.VISIBLE);
            signInButtonNavHeader.setVisibility(View.GONE);
            menuProfile.setVisible(true);
            menuSignOut.setVisible(true);
        } else {
            signedInNavHeader.setVisibility(View.GONE);
            signInButtonNavHeader.setVisibility(View.VISIBLE);
            menuProfile.setVisible(false);
            menuSignOut.setVisible(false);
        }
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                handleSigningVisibility();
            }
            KeyboardHelper.hideKeyboard(this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateStatusBarColor(int color) {
        drawer.setStatusBarBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(color);
        }
    }

    @Override
    public void profileUpdated() {
        updateNavHeader();
    }

    public void showHamburgerIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
    }

    public void showBackIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (currentFragment instanceof SectionsFragment)
                drawer.openDrawer(GravityCompat.START);
            else
                changeFragment(SECTIONS);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}