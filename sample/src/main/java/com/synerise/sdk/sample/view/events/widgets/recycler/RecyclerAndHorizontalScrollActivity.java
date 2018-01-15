package com.synerise.sdk.sample.view.events.widgets.recycler;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.view.events.widgets.recycler.adapter.Customer;
import com.synerise.sdk.sample.view.events.widgets.recycler.adapter.CustomersRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Marcel Skotnicki on 1/10/18.
 */

public class RecyclerAndHorizontalScrollActivity extends AppCompatActivity {

    private static final String TAG = RecyclerAndHorizontalScrollActivity.class.getSimpleName();

    public static Intent createIntent(Context context) {
        return new Intent(context, RecyclerAndHorizontalScrollActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_and_horizontal_scroll);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.horizontal_scroll_view).setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Log.d(TAG,
                          "HorizontalScrollView~onScrollChange: [" + oldScrollX + "," + oldScrollY + "] -> [" + scrollX + "," + scrollY + "]");
                }
            });
        }

        CustomersRecyclerAdapter adapter = new CustomersRecyclerAdapter(this, new CustomersRecyclerAdapter.OnCustomerSelected() {
            @Override
            public void onCustomerSelected(Customer customer, int position) {
                Log.d(TAG, "CustomersRecyclerAdapter~onCustomerSelected: " + customer + " | on position: " + position);
            }
        }, new ArrayList<Customer>() {{
            for (int i = 0; i < 40; i++) { add(new Customer("Joe", "Doe", "JoDo", 30)); }
        }});

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}
