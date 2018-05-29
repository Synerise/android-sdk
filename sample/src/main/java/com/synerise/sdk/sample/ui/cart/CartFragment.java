package com.synerise.sdk.sample.ui.cart;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;

import java.util.List;

import javax.inject.Inject;

public class CartFragment extends BaseFragment implements OnCartItemRemoved {

    @Inject AccountManager accountManager;
    private CartRecyclerView cartRecycler;
    private View cartContent;
    private View emptyCart;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    // ****************************************************************************************************************************************

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioButton expressRadio = view.findViewById(R.id.express_radio);
        RadioButton postRadio = view.findViewById(R.id.post_radio);

        expressRadio.setOnClickListener(v -> toggleChoice(expressRadio, postRadio));
        postRadio.setOnClickListener(v -> toggleChoice(postRadio, expressRadio));
        view.findViewById(R.id.express_layout).setOnClickListener(v -> toggleChoice(expressRadio, postRadio));
        view.findViewById(R.id.post_layout).setOnClickListener(v -> toggleChoice(postRadio, expressRadio));

        RadioButton creditRadio = view.findViewById(R.id.credit_radio);
        RadioButton payPalRadio = view.findViewById(R.id.pay_pal_radio);

        creditRadio.setOnClickListener(v -> toggleChoice(creditRadio, payPalRadio));
        payPalRadio.setOnClickListener(v -> toggleChoice(payPalRadio, creditRadio));
        view.findViewById(R.id.credit_layout).setOnClickListener(v -> toggleChoice(creditRadio, payPalRadio));
        view.findViewById(R.id.pay_pal_layout).setOnClickListener(v -> toggleChoice(payPalRadio, creditRadio));

        cartRecycler = view.findViewById(R.id.cart_recycler);
        cartContent = view.findViewById(R.id.cart_content);
        emptyCart = view.findViewById(R.id.empty_cart);

        handleLayoutVisibility();

        Button placeOrder = view.findViewById(R.id.place_order);
        ProgressBar progressBar = view.findViewById(R.id.place_order_progress_bar);
        placeOrder.setOnClickListener(v -> {
            placeOrder.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                placeOrder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                accountManager.clearCartItems();
                showDialog();
                handleLayoutVisibility();
            }, 2000);
        });
    }

    // ****************************************************************************************************************************************

    private void handleLayoutVisibility() {
        List<CartItem> cartItems = accountManager.getCartItems();
        CartAdapter cartAdapter = new CartAdapter(getContext(), cartItems, this);
        if (cartItems.size() > 0) {
            cartContent.setVisibility(View.VISIBLE);
            emptyCart.setVisibility(View.GONE);

            cartRecycler.setAdapter(cartAdapter);
            cartRecycler.setHasFixedSize(true);
            cartRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                    ContextCompat.getDrawable(getContext(), R.drawable.divider));
            cartRecycler.addItemDecoration(dividerItemDecoration);
        } else {
            cartContent.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRemoved(CartItem cartItem) {
        ViewGroup.LayoutParams layoutParams = cartRecycler.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        cartRecycler.setLayoutParams(layoutParams);
        accountManager.removeCartItem(cartItem);
        handleLayoutVisibility();
    }

    private void showDialog() {
        CheckoutDialog checkoutDialog = CheckoutDialog.newInstance();
        checkoutDialog.show(getChildFragmentManager(), CheckoutDialog.class.getSimpleName());
    }

    // ****************************************************************************************************************************************

    private void toggleChoice(RadioButton button1, RadioButton button2) {
        button1.setChecked(true);
        button2.setChecked(false);
    }
}