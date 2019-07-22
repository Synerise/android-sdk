package com.synerise.sdk.sample.ui.dev.apiAdapter.fragments.promotion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.synerise.sdk.core.types.enums.ApiQuerySortingOrder;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.promotions.Promotions;
import com.synerise.sdk.promotions.model.promotion.PromotionResponse;
import com.synerise.sdk.promotions.model.promotion.PromotionSortingKey;
import com.synerise.sdk.promotions.model.promotion.PromotionStatus;
import com.synerise.sdk.promotions.model.promotion.PromotionType;
import com.synerise.sdk.promotions.model.promotion.PromotionsApiQuery;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ClientGetPromotionsFragment extends BaseDevFragment {

    private IDataApiCall<PromotionResponse> apiCall;
    private RadioButton includeButton;
    private CheckBox activeBox, assignedBox, redeemedBox;
    private CheckBox generalBox, customBox, membersOnlyBox;
    private TextInputLayout inputLimit, inputPage;

    public static ClientGetPromotionsFragment newInstance() { return new ClientGetPromotionsFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_get_promotions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        includeButton = view.findViewById(R.id.radio_include);
        activeBox = view.findViewById(R.id.checkbox_active);
        assignedBox = view.findViewById(R.id.checkbox_assigned);
        redeemedBox = view.findViewById(R.id.checkbox_redeemed);
        generalBox = view.findViewById(R.id.checkbox_general);
        customBox = view.findViewById(R.id.checkbox_custom);
        membersOnlyBox = view.findViewById(R.id.checkbox_members_only);
        inputLimit = view.findViewById(R.id.text_limit);
        inputPage = view.findViewById(R.id.text_page);
        view.findViewById(R.id.get_promotions).setOnClickListener(v -> getPromotions());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    private void getPromotions() {

        boolean isValid = true;

        List<PromotionStatus> statuses = new ArrayList<>();
        if (activeBox.isChecked()) statuses.add(PromotionStatus.ACTIVE);
        if (assignedBox.isChecked()) statuses.add(PromotionStatus.ASSIGNED);
        if (redeemedBox.isChecked()) statuses.add(PromotionStatus.REDEEMED);

        List<PromotionType> types = new ArrayList<>();
        if (generalBox.isChecked()) types.add(PromotionType.GENERAL);
        if (customBox.isChecked()) types.add(PromotionType.CUSTOM);
        if (membersOnlyBox.isChecked()) types.add(PromotionType.MEMBERS_ONLY);

        inputLimit.setError(null);
        int limit = 100;

        try {
            limit = Integer.parseInt(inputLimit.getEditText().getText().toString());
            if (limit < 0) limit = 0;
        } catch (Exception ignored) { }
        inputLimit.getEditText().setText(String.valueOf(limit));

        inputPage.setError(null);
        int page = 1;

        try {
            page = Integer.parseInt(inputPage.getEditText().getText().toString());
            if (page < 1) page = 1;
        } catch (Exception ignored) { }
        inputPage.getEditText().setText(String.valueOf(page));

        if (isValid) {
            if (apiCall != null) apiCall.cancel();
            PromotionsApiQuery query = new PromotionsApiQuery();
            query.limit = limit;
            query.statuses = statuses;
            query.page = page;
            query.includeMeta = includeButton.isChecked();
            //Hardcoded parameters
            LinkedHashMap<PromotionSortingKey, ApiQuerySortingOrder> sortParams = new LinkedHashMap<>();
            sortParams.put(PromotionSortingKey.TYPE, ApiQuerySortingOrder.ASCENDING);
            sortParams.put(PromotionSortingKey.CREATED_AT, ApiQuerySortingOrder.ASCENDING);
            sortParams.put(PromotionSortingKey.EXPIRE_AT, ApiQuerySortingOrder.DESCENDING);
            query.setSortParameters(sortParams);
            apiCall = Promotions.getPromotions(query);
            apiCall.execute(this::onSuccess, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    ClientGetPromotionsFragment.this.onFailure(apiError);
                }
            });
        }
    }

    private void onSuccess(PromotionResponse promotionResponse) {
        super.onSuccess();
    }
}