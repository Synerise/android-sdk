package com.synerise.sdk.sample.view.synalter.support;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.synerise.sdk.sample.R;

public class SynalterSupportActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, SynalterSupportActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synalter_support);

        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SynalterSupportActivity.this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SynalterSupportActivity.this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_1, new TestSupportFragment()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_2, new TestSupportFragment()).commit();
    }
}
