package com.synerise.sdk.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.view.client.ClientSignInActivity;
import com.synerise.sdk.sample.view.events.TrackerActivity;
import com.synerise.sdk.sample.view.injector.InjectorActivity;
import com.synerise.sdk.sample.view.profile.ProfileFeaturesActivity;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.menu_button_tracker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view3) {MainActivity.this.onButtonClick(view3);}
        });
        findViewById(R.id.menu_button_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {MainActivity.this.onButtonClick(view2);}
        });
        findViewById(R.id.menu_button_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {MainActivity.this.onButtonClick(view1);}
        });
        findViewById(R.id.menu_button_injector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {MainActivity.this.onButtonClick(view);}
        });
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
            case R.id.menu_button_injector:
                startActivity(InjectorActivity.createIntent(this));
                break;
            default:
        }
    }
}