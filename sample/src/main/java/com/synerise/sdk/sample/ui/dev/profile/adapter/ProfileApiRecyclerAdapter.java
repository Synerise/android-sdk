package com.synerise.sdk.sample.ui.dev.profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.synerise.sdk.core.listeners.ActionListener;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseViewHolder;
import com.synerise.sdk.sample.util.DataActionListener;

public class ProfileApiRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder<ProfileApi>> {

    private static final int PROMOTION_VIEW_TYPE = 1;
    private static final int DEFAULT_VIEW_TYPE = 2;

    private final ProfileApi[] profileApis;
    private final DataActionListener<ProfileApi> listener;
    private final ActionListener promotionListener;
    private final LayoutInflater inflater;

    // ****************************************************************************************************************************************

    public ProfileApiRecyclerAdapter(Context context,
                                     DataActionListener<ProfileApi> listener,
                                     ActionListener promotionListener,
                                     ProfileApi[] profileApis) {
        this.inflater = LayoutInflater.from(context);
        this.profileApis = profileApis;
        this.listener = listener;
        this.promotionListener = promotionListener;
    }

    // ****************************************************************************************************************************************

    @NonNull
    @Override
    public BaseViewHolder<ProfileApi> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DEFAULT_VIEW_TYPE)
            return new ProfileApiViewHolder(inflater.inflate(R.layout.item_profile_api, parent, false), listener);
        else
            return new GroupApiViewHolder(inflater.inflate(R.layout.item_promotion_api, parent, false), promotionListener);
    }

    @Override
    public int getItemViewType(int position) {
        ProfileApi profileApi = profileApis[position];
        if (profileApi == ProfileApi.PROMOTIONS) return PROMOTION_VIEW_TYPE;
        else return DEFAULT_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.populateData(profileApis[position]);
    }

    @Override
    public int getItemCount() {
        return profileApis.length;
    }
}
