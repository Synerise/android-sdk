package com.synerise.sdk.sample.ui.settings;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.types.enums.ClientSignOutMode;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.injector.Injector;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.splash.SplashActivity;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SettingsFragment extends BaseFragment {

    @Inject AccountManager accountManager;

    public static SettingsFragment newInstance() { return new SettingsFragment(); }

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

        ((TextView) view.findViewById(R.id.user_name)).setText(accountManager.getFirstName());
        ((TextView) view.findViewById(R.id.user_last_name)).setText(accountManager.getLastName());
        ((TextView) view.findViewById(R.id.user_email)).setText(accountManager.getEmail());

        Button signOut = view.findViewById(R.id.sign_out);

        if (Client.isSignedIn()) {
            signOut.setVisibility(VISIBLE);
            signOut.setOnClickListener(v -> {
                IApiCall call = Client.signOut(ClientSignOutMode.SIGN_OUT, false);
                call.execute(new ActionListener() {
                    @Override
                    public void onAction() {
                        accountManager.signOut();
                        startActivity(SplashActivity.createIntent(getActivity()));
                        getActivity().finishAffinity();
                    }
                }, new DataActionListener<ApiError>() {
                    @Override
                    public void onDataAction(ApiError data) {
                    }
                });
            });
        } else {
            signOut.setVisibility(GONE);
        }
    }
}