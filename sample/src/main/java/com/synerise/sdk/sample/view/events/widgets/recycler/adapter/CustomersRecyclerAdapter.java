package com.synerise.sdk.sample.view.events.widgets.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.sample.R;

import java.util.ArrayList;
import java.util.List;



public class CustomersRecyclerAdapter extends RecyclerView.Adapter<CustomerViewHolder> {

    public interface OnCustomerSelected {
        void onCustomerSelected(Customer customer, int position);
    }

    private final LayoutInflater inflater;
    private final OnCustomerSelected itemSelectedListener;
    private final List<Customer> customers = new ArrayList<>();

    // ************************************************************************************************************************

    public CustomersRecyclerAdapter(Context context, OnCustomerSelected listener, List<Customer> customers) {
        inflater = LayoutInflater.from(context);
        itemSelectedListener = listener;
        if (customers != null) this.customers.addAll(customers);
    }

    // ************************************************************************************************************************

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(itemView, itemSelectedListener);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        holder.populateData(customers.get(position));
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
}