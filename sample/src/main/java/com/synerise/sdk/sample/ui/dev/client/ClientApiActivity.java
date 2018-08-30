package com.synerise.sdk.sample.ui.dev.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.dev.client.adapter.ClientApi;
import com.synerise.sdk.sample.ui.dev.client.adapter.ClientApiRecyclerAdapter;
import com.synerise.sdk.sample.ui.dev.client.adapter.DevClientActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class ClientApiActivity extends BaseActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, ClientApiActivity.class);
    }

    // ****************************************************************************************************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_api);

        RecyclerView recycler = findViewById(R.id.client_api_recycler);

        ToolbarHelper.setUpChildToolbar(this, R.string.client_api);

        ClientApiRecyclerAdapter adapter = new ClientApiRecyclerAdapter(this, this::onClientApiClick,
                                                                        ClientApi.values());

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    // ****************************************************************************************************************************************

    private void onClientApiClick(ClientApi clientApi) {
        startActivity(DevClientActivity.createIntent(this, clientApi.ordinal()));
    }
}