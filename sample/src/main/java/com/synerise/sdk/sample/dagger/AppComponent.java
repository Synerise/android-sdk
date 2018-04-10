package com.synerise.sdk.sample.dagger;

import com.synerise.sdk.sample.ui.auth.SignInFragment;
import com.synerise.sdk.sample.ui.auth.SignUpFragment;
import com.synerise.sdk.sample.ui.client.ClientFragment;
import com.synerise.sdk.sample.ui.settings.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainModule.class, ConfigModule.class})
public interface AppComponent {
    void inject(SettingsFragment settingsFragment);
    void inject(SignInFragment signInFragment);
    void inject(SignUpFragment signUpFragment);
    void inject(ClientFragment clientFragment);
}
