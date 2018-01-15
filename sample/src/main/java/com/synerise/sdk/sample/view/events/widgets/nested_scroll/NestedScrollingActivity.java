package com.synerise.sdk.sample.view.events.widgets.nested_scroll;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.synerise.sdk.sample.R;

public class NestedScrollingActivity extends AppCompatActivity {

    private static final String TAG = NestedScrollingActivity.class.getSimpleName();

    public static Intent createIntent(Context context) {
        return new Intent(context, NestedScrollingActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_nested);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG,
                      "NestedScrollView~onScrollChange: [" + oldScrollX + "," + oldScrollY + "] -> [" + scrollX + "," + scrollY + "]");
            }
        });
    }
}
