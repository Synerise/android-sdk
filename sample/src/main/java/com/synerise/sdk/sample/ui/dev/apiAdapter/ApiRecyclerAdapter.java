package com.synerise.sdk.sample.ui.dev.apiAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseViewHolder;
import com.synerise.sdk.sample.util.DataActionListener;

import java.util.List;

public class ApiRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder<SyneriseSdkApi>> {

    private static final int GROUP_VIEW_TYPE = 1;
    private static final int API_VIEW_TYPE = 2;

    private final List<SyneriseSdkApi> syneriseSdkApis;
    private final DataActionListener<SyneriseSdkApi> listener;
    private final LayoutInflater inflater;

    // ****************************************************************************************************************************************

    public ApiRecyclerAdapter(LayoutInflater inflater,
                              DataActionListener<SyneriseSdkApi> listener,
                              List<SyneriseSdkApi> syneriseSdkApis) {
        this.inflater = inflater;
        this.syneriseSdkApis = syneriseSdkApis;
        this.listener = listener;
    }

    // ****************************************************************************************************************************************

    @NonNull
    @Override
    public BaseViewHolder<SyneriseSdkApi> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == API_VIEW_TYPE)
            return new ApiViewHolder(inflater.inflate(R.layout.item_api, parent, false), listener);
        else
            return new GroupApiViewHolder(inflater.inflate(R.layout.item_group_api, parent, false), listener);
    }

    @Override
    public int getItemViewType(int position) {
        SyneriseSdkApi syneriseSdkApi = syneriseSdkApis.get(position);
        if (syneriseSdkApi.isGroup()) return GROUP_VIEW_TYPE;
        else return API_VIEW_TYPE;
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
