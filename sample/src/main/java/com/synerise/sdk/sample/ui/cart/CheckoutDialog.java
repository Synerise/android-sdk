package com.synerise.sdk.sample.ui.cart;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.ui.BaseDialog;

public class CheckoutDialog extends BaseDialog {

    public static CheckoutDialog newInstance() {
        return new CheckoutDialog();
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_checkout;
    }

    @Override
    protected boolean setCancelable() {
        return true;
    }

    @Override
    protected void setViewContent(Context context, View view) {
        Button trackOrder = view.findViewById(R.id.track_order_button);
        TextView continueShopping = view.findViewById(R.id.continue_shopping);

        trackOrder.setOnClickListener(v -> dismiss());
        continueShopping.setOnClickListener(v -> dismiss());
    }
}
