package com.synerise.sdk.sample.view.synalter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.view.synalter.app.SynalterActivity;
import com.synerise.sdk.sample.view.synalter.support.SynalterSupportActivity;

public class SynalterApiActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, SynalterApiActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synalter_api);

        findViewById(R.id.menu_button_synalter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onButtonClick(view);}
        });
        findViewById(R.id.menu_button_synalter_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onButtonClick(view);}
        });
    }

    // ****************************************************************************************************************************************

    private void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.menu_button_synalter:
                startActivity(SynalterActivity.createIntent(this));
                break;
            case R.id.menu_button_synalter_support:
                startActivity(SynalterSupportActivity.createIntent(this));
                break;
        }
    }
}
