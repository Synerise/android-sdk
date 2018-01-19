package com.synerise.sdk.sample.view.events.widgets.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.synerise.sdk.sample.R;


class CustomerViewHolder extends RecyclerView.ViewHolder {

    private final TextView name, lastname, nickname, age;
    private Customer customer;

    // ****************************************************************************************************************************************

    CustomerViewHolder(View itemView, final CustomersRecyclerAdapter.OnCustomerSelected selectionListener) {
        super(itemView);
        name = itemView.findViewById(R.id.customer_name);
        lastname = itemView.findViewById(R.id.customer_lastname);
        nickname = itemView.findViewById(R.id.customer_nickname);
        age = itemView.findViewById(R.id.customer_age);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer != null) selectionListener.onCustomerSelected(customer, getAdapterPosition());
            }
        });
    }

    // ****************************************************************************************************************************************

    void populateData(Customer customer) {
        this.customer = customer;
        name.setText(customer.getName());
        lastname.setText(customer.getLastName());
        nickname.setText(customer.getNickname());
        age.setText(String.valueOf(customer.getAge()));
    }
}
