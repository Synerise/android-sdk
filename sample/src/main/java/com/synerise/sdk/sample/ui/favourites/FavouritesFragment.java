package com.synerise.sdk.sample.ui.favourites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.dev.BaseDevFragment;
import com.synerise.sdk.sample.ui.section.category.products.adapter.ProductsRecyclerAdapter;
import com.synerise.sdk.sample.ui.section.category.products.details.ProductActivity;

import javax.inject.Inject;

public class FavouritesFragment extends BaseDevFragment {

    @Inject AccountManager accountManager;
    private ProductsRecyclerAdapter productsRecyclerAdapter;
    private RecyclerView recyclerView;
    private TextView noFavourite;

    public FavouritesFragment() { }

    public static FavouritesFragment newInstance() { return new FavouritesFragment(); }

    // ****************************************************************************************************************************************

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productsRecyclerAdapter = new ProductsRecyclerAdapter(getActivity(), this::onProductSelected);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(productsRecyclerAdapter);

        noFavourite = view.findViewById(R.id.no_favourite_text);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accountManager.getFavouriteProducts().size() == 0) {
            noFavourite.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noFavourite.setVisibility(View.GONE);
            productsRecyclerAdapter.update(accountManager.getFavouriteProducts());
        }
    }

    // ****************************************************************************************************************************************

    private void onProductSelected(Product product) {
        startActivity(ProductActivity.createIntent(getActivity(), product.getSKU()));
    }
}
