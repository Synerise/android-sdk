package com.synerise.sdk.sample.ui.dev.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.util.DataActionListener;

import static com.synerise.sdk.sample.ui.dev.client.adapter.ClientApiRecyclerAdapter.*;

class ClientApiViewHolder extends RecyclerView.ViewHolder {

    private final TextView label;
    private ClientApi clientApi;

    // ****************************************************************************************************************************************

    ClientApiViewHolder(View itemView, DataActionListener<ClientApi> listener) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (clientApi != null) listener.onDataAction(clientApi);
        });
        this.label = itemView.findViewById(R.id.client_api_label);
    }

    // ****************************************************************************************************************************************

    void populateData(ClientApi clientApi) {
        this.clientApi = clientApi;
        this.label.setText(clientApi.getTitle());
    }
}
