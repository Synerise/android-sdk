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

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.interaction.HitTimerEvent;
import com.synerise.sdk.event.model.model.UnitPrice;
import com.synerise.sdk.event.model.products.cart.RemovedFromCartEvent;
import com.synerise.sdk.event.model.transaction.CompletedTransactionEvent;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.data.Section;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class CartFragment extends BaseFragment {

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
                Tracker.send(createTransactionEvent(accountManager.getCartItems()));
                accountManager.clearCartItems();
                showDialog();
                handleLayoutVisibility();
                Tracker.send(new HitTimerEvent("Shopping Cart process - end"));
            }, 2000);
        });
    }

    // ****************************************************************************************************************************************

    private void handleLayoutVisibility() {
        List<CartItem> cartItems = accountManager.getCartItems();
        CartAdapter cartAdapter = new CartAdapter(LayoutInflater.from(getContext()), cartItems, new OnCartItemListener() {
            @Override
            public void onItemQuantityReduced(CartItem cartItem) {
                CartFragment.this.onItemQuantityReduced(cartItem);
            }

            @Override
            public void onItemRemoved(CartItem cartItem) {
                CartFragment.this.onItemRemoved(cartItem);
            }
        });
        if (cartItems.size() > 0) {
            cartContent.setVisibility(View.VISIBLE);
            emptyCart.setVisibility(View.GONE);

            cartRecycler.setAdapter(cartAdapter);
            cartRecycler.setHasFixedSize(true);
            cartRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ContextCompat.getDrawable(getContext(),
                                                                                                              R.drawable.divider));
            cartRecycler.addItemDecoration(dividerItemDecoration);
        } else {
            cartContent.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
        }
    }

    private void onItemQuantityReduced(CartItem cartItem) {
        Tracker.send(createCartEvent(cartItem.getProduct(), 1));
    }

    private void onItemRemoved(CartItem cartItem) {
        ViewGroup.LayoutParams layoutParams = cartRecycler.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        cartRecycler.setLayoutParams(layoutParams);
        accountManager.removeCartItem(cartItem);
        handleLayoutVisibility();
        Tracker.send(createCartEvent(cartItem.getProduct(), cartItem.getQuantity()));
    }

    private void showDialog() {
        CheckoutDialog checkoutDialog = CheckoutDialog.newInstance();
        checkoutDialog.show(getChildFragmentManager(), CheckoutDialog.class.getSimpleName());
    }

    // ****************************************************************************************************************************************

    private RemovedFromCartEvent createCartEvent(Product product, int quantity) {
        UnitPrice unitPrice = new UnitPrice(product.getPrice(), Currency.getInstance(Locale.getDefault()));
        RemovedFromCartEvent cartEvent = new RemovedFromCartEvent(getString(product.getName()), product.getSKU(), unitPrice, 1,
                                                                  new TrackerParams.Builder().add("quantity", quantity).build());
        cartEvent.setName(getString(product.getName()));
        cartEvent.setProducer(getString(product.getBrand()));

        Category category = Category.getCategory(product);
        if (category != null) {
            cartEvent.setCategory(getString(category.getText()));
            List<String> categories = new ArrayList<>();
            categories.add(getString(category.getText()));
            Section section = Section.getSection(category);
            if (section != null) categories.add(getString(section.getName()));
            cartEvent.setCategories(categories);
        }
        cartEvent.setOffline(false);
        //        cartEvent.setDiscountedPrice(unitPrice);
        //        cartEvent.setRegularPrice(unitPrice);
        return cartEvent;
    }

    private CompletedTransactionEvent createTransactionEvent(List<CartItem> cartItems) {
        CompletedTransactionEvent transactionEvent = new CompletedTransactionEvent("Transaction completed");
        List<com.synerise.sdk.event.model.model.Product> products = new ArrayList<>();
        float finalPrice = 0;
        for (CartItem cartItem : cartItems) {
            Product cartProduct = cartItem.getProduct();
            for (int i = 0; i < cartItem.getQuantity(); i++) {
                finalPrice += cartProduct.getPrice();
                products.add(cartProduct.getEventProduct(getContext(), cartItem.getQuantity()));
            }
        }
        transactionEvent.setValue(new UnitPrice(finalPrice, Currency.getInstance(Locale.getDefault())));
        transactionEvent.setProducts(products);
        return transactionEvent;
    }

    // ****************************************************************************************************************************************

    private void toggleChoice(RadioButton button1, RadioButton button2) {
        button1.setChecked(true);
        button2.setChecked(false);
    }
}