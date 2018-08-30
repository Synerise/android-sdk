package com.synerise.sdk.sample.ui.dev.client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.util.DataActionListener;

public class ClientApiRecyclerAdapter extends RecyclerView.Adapter<ClientApiViewHolder> {

    private final ClientApi[] clientApis;
    private final DataActionListener<ClientApi> listener;
    private final LayoutInflater inflater;

    // ****************************************************************************************************************************************

    public ClientApiRecyclerAdapter(Context context, DataActionListener<ClientApi> listener, ClientApi[] clientApis) {
        this.inflater = LayoutInflater.from(context);
        this.clientApis = clientApis;
        this.listener = listener;
    }

    // ****************************************************************************************************************************************

    @NonNull
    @Override
    public ClientApiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_client_api, parent, false);
        return new ClientApiViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientApiViewHolder holder, int position) {
        holder.populateData(clientApis[position]);
    }

    @Override
    public int getItemCount() {
        return clientApis.length;
    }
}
