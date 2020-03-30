package com.synerise.sdk.sample.ui.promotion;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.promotions.Promotions;
import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.promotions.model.promotion.PromotionResponse;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.promotion.adapter.PromotionsRecyclerAdapter;
import com.synerise.sdk.sample.ui.promotion.details.PromotionActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PromotionsFragment extends BaseFragment {

    @Inject AccountManager accountManager;
    private IDataApiCall<PromotionResponse> apiCall;
    private PromotionsRecyclerAdapter recyclerAdapter;

    public static PromotionsFragment newInstance() {
        return new PromotionsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promotions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerAdapter = new PromotionsRecyclerAdapter(getActivity(), this::onPromotionSelected, accountManager.getPromotions());

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPromotions();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    private void getPromotions() {
        Toast.makeText(getActivity(), R.string.default_refreshing, Toast.LENGTH_SHORT).show();
        if (apiCall != null) apiCall.cancel();
        apiCall = Promotions.getPromotions();
        apiCall.execute(response -> {
            if (response != null) {
                List<Promotion> promotions = response.getPromotions();
                recyclerAdapter.update(promotions);
                accountManager.updatePromotions(promotions);
            }
        }, this::showAlertError);
    }

    private void onPromotionSelected(Promotion promotion) {
        startActivity(PromotionActivity.createIntent(getActivity(), promotion));
    }
}
