package com.synerise.sdk.sample.ui.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.splash.SplashActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_last_name) TextView userLastName;
    @BindView(R.id.user_email) TextView userEmail;
    @BindView(R.id.sign_out) Button signOut;
    @BindView(R.id.show_walkthrough) Button showWalkthrough;
    private Unbinder unbinder;

    @Inject AccountManager accountManager;

    public static SettingsFragment newInstance() { return new SettingsFragment(); }

    // ****************************************************************************************************************************************

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        userName.setText(accountManager.getFirstName());
        userLastName.setText(accountManager.getLastName());
        userEmail.setText(accountManager.getEmail());

        if (Client.isSignedIn()) {
            signOut.setVisibility(VISIBLE);
            signOut.setOnClickListener(v -> {
                Client.signOut();
                accountManager.signOut();
                startActivity(SplashActivity.createIntent(getContext()));
                getActivity().finishAffinity();
            });
        } else {
            signOut.setVisibility(GONE);
        }

        showWalkthrough.setOnClickListener(v -> Injector.showWalkthrough());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}