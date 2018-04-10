package com.synerise.sdk.sample.ui.category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.category.adapter.CategoriesRecyclerAdapter;
import com.synerise.sdk.sample.ui.category.details.CategoryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesFragment extends BaseFragment {

    @BindView(R.id.recycler) RecyclerView recycler;
    private Unbinder unbinder;

    public static CategoriesFragment newInstance() { return new CategoriesFragment(); }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        CategoriesRecyclerAdapter recyclerAdapter = new CategoriesRecyclerAdapter(getContext(), this::onCategorySelected);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycler.setAdapter(recyclerAdapter);
    }

    // ****************************************************************************************************************************************

    private void onCategorySelected(Category category) {
        startActivity(CategoryActivity.createIntent(getActivity(), category));
    }

    // ****************************************************************************************************************************************

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}