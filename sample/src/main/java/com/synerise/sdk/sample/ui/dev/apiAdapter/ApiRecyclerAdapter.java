package com.synerise.sdk.sample.ui.dev.apiAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseViewHolder;
import com.synerise.sdk.sample.util.DataActionListener;

import java.util.List;

public class ApiRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder<SyneriseSdkApi>> {

    private final List<SyneriseSdkApi> syneriseSdkApis;
    private final DataActionListener<SyneriseSdkApi> listener;
    private final LayoutInflater inflater;

    public ApiRecyclerAdapter(LayoutInflater inflater, DataActionListener<SyneriseSdkApi> listener,
                              List<SyneriseSdkApi> syneriseSdkApis) {
        this.inflater = inflater;
        this.syneriseSdkApis = syneriseSdkApis;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder<SyneriseSdkApi> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ApiViewHolder(inflater.inflate(R.layout.item_api, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.populateData(syneriseSdkApis.get(position));
    }

    @Override
    public int getItemCount() {
        return syneriseSdkApis.size();
    }
}
