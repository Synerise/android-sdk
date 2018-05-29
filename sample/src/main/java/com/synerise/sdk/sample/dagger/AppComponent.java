package com.synerise.sdk.sample.dagger;

import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.ui.auth.ConfirmNumberDialog;
import com.synerise.sdk.sample.ui.auth.SignInActivity;
import com.synerise.sdk.sample.ui.auth.SignUpActivity;
import com.synerise.sdk.sample.ui.cart.CartFragment;
import com.synerise.sdk.sample.ui.dashboard.DashboardActivity;
import com.synerise.sdk.sample.ui.dev.qr.QRScannerActivity;
import com.synerise.sdk.sample.ui.profile.ProfileFragment;
import com.synerise.sdk.sample.ui.section.category.products.details.ProductActivity;
import com.synerise.sdk.sample.ui.settings.SettingsFragment;
import com.synerise.sdk.sample.ui.splash.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainModule.class, ConfigModule.class})
public interface AppComponent {
    void inject(SettingsFragment settingsFragment);
    void inject(SignInActivity signInActivity);
    void inject(SignUpActivity signUpActivity);
    void inject(ProductActivity productActivity);
    void inject(CartFragment cartFragment);
    void inject(ProfileFragment profileFragment);
    void inject(DashboardActivity dashboardActivity);
    void inject(App app);
    void inject(SplashActivity splashActivity);
    void inject(QRScannerActivity QRScannerActivity);
    void inject(ConfirmNumberDialog confirmNumberDialog);
}
