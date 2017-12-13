package com.synerise.sdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.synerise.sdk.sample.view.client.ClientSignInActivity;
import com.synerise.sdk.sample.view.events.TrackerActivity;
import com.synerise.sdk.sample.view.profile.ProfileFeaturesActivity;

/**
 * Created by Jerzy Wierzchowski on 11/27/17.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.menu_button_tracker).setOnClickListener(this::onButtonClick);
        findViewById(R.id.menu_button_client).setOnClickListener(this::onButtonClick);
        findViewById(R.id.menu_button_profile).setOnClickListener(this::onButtonClick);
    }

    // ****************************************************************************************************************************************

    private void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.menu_button_client:
                startActivity(ClientSignInActivity.createIntent(this));
                break;
            case R.id.menu_button_profile:
                startActivity(ProfileFeaturesActivity.createIntent(this));
                break;
            case R.id.menu_button_tracker:
                startActivity(TrackerActivity.createIntent(this));
                break;
            default:
        }
    }
}