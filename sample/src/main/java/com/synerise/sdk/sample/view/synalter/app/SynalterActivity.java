package com.synerise.sdk.sample.view.synalter.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.synerise.sdk.sample.R;

public class SynalterActivity extends Activity {

    public static Intent createIntent(Context context) {
        return new Intent(context, SynalterActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synalter);

        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SynalterActivity.this, "Button 1 clicked", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SynalterActivity.this, "Button 2 clicked", Toast.LENGTH_SHORT).show();
            }
        });
        getFragmentManager().beginTransaction().add(R.id.fragment_container_1, new TestFragment()).commit();
        getFragmentManager().beginTransaction().add(R.id.fragment_container_2, new TestFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
