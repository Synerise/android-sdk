package com.synerise.sdk.sample.ui.cart;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.synerise.sdk.client.Client;
import com.synerise.sdk.client.model.Promotion;
import com.synerise.sdk.client.model.PromotionResponse;
import com.synerise.sdk.core.listeners.DataActionListener;
import com.synerise.sdk.core.net.IApiCall;
import com.synerise.sdk.core.net.IDataApiCall;
import com.synerise.sdk.error.ApiError;
import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.model.interaction.HitTimerEvent;
import com.synerise.sdk.event.model.model.UnitPrice;
import com.synerise.sdk.event.model.products.cart.RemovedFromCartEvent;
import com.synerise.sdk.event.model.transaction.CompletedTransactionEvent;
import com.synerise.sdk.profile.Profile;
import com.synerise.sdk.sample.App;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.data.Section;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.cart.adapter.item.CartAdapter;
import com.synerise.sdk.sample.ui.cart.adapter.item.CartItem;
import com.synerise.sdk.sample.ui.cart.adapter.promotion.CartPromotionAdapter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class CartFragment extends BaseFragment {

    @Inject AccountManager accountManager;
    private IDataApiCall<PromotionResponse> apiCall;
    private IApiCall redeemApiCall;

    private Promotion chosenPromotion;
    private CartRecyclerView cartRecycler;
    private CartPromotionAdapter promotionAdapter;
    private View cartContent;
    private View emptyCart;

    private Button placeOrder;
    private ProgressBar progressBar;

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

        promotionAdapter = new CartPromotionAdapter(LayoutInflater.from(getActivity()), this::onPromotionClicked,
                                                    filterPromotions(accountManager.getCartPromotions()));
        CartRecyclerView promotionsRecycler = view.findViewById(R.id.promotions_recycler);
        promotionsRecycler.setAdapter(promotionAdapter);
        promotionsRecycler.setHasFixedSize(true);
        promotionsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        promotionsRecycler.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable.divider)));

        progressBar = view.findViewById(R.id.place_order_progress_bar);
        placeOrder = view.findViewById(R.id.place_order);
        placeOrder.setOnClickListener(v -> onPlaceOrderClicked());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Client.isSignedIn()) getPromotions();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiCall != null) apiCall.cancel();
        if (redeemApiCall != null) redeemApiCall.cancel();
    }

    // ****************************************************************************************************************************************

    private void getPromotions() {
        Toast.makeText(getActivity(), R.string.default_refreshing_promotions, Toast.LENGTH_SHORT).show();
        if (apiCall != null) apiCall.cancel();
        apiCall = Client.getPromotions(true);
        apiCall.execute(response -> {
            if (response != null) {
                List<Promotion> promotions = response.getPromotions();
                promotionAdapter.update(filterPromotions(promotions));
                accountManager.updateCartPromotions(promotions);
            }
        }, error -> Toast.makeText(getActivity(), getErrorMessage(error), Toast.LENGTH_SHORT).show());
    }

    private List<Pair<Promotion, CartItem>> filterPromotions(List<Promotion> promotions) {
        List<Pair<Promotion, CartItem>> results = new ArrayList<>();
        List<CartItem> cartItems = accountManager.getCartItems();
        for (Promotion promotion : promotions) {
            List<String> catalogIndexItems = promotion.getCatalogIndexItems();
            if (catalogIndexItems.isEmpty()) {
                results.add(new Pair<>(promotion, null));
            } else if (!cartItems.isEmpty()) {
                for (String catalogIndexItem : catalogIndexItems) {
                    for (CartItem cartItem : cartItems) {
                        if (catalogIndexItem.equals(cartItem.getProduct().getSKU()))
                            results.add(new Pair<>(promotion, cartItem));
                    }
                }
            }
        }
        return results;
    }

    private void onPromotionClicked(Pair<Promotion, CartItem> promotionPair) {
        chosenPromotion = promotionPair.first;
    }

    // ****************************************************************************************************************************************

    private void handleLayoutVisibility() {
        List<CartItem> cartItems = accountManager.getCartItems();
        CartAdapter cartAdapter = new CartAdapter(LayoutInflater.from(getActivity()), cartItems, new OnCartItemListener() {
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
            cartRecycler.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable.divider)));
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
        RemovedFromCartEvent cartEvent = new RemovedFromCartEvent(getString(product.getName()), product.getSKU(), unitPrice, quantity);
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
                products.add(cartProduct.getEventProduct(getActivity(), cartItem.getQuantity()));
            }
        }
        transactionEvent.setValue(new UnitPrice(finalPrice, Currency.getInstance(Locale.getDefault())));
        transactionEvent.setProducts(products);
        return transactionEvent;
    }

    // ****************************************************************************************************************************************

    private void onPlaceOrderClicked() {
        placeOrder.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if (chosenPromotion != null) {
            if (redeemApiCall != null) redeemApiCall.cancel();
            String email = accountManager.getEmail();
            if (email != null) {
                redeemApiCall = Profile.redeemPromotionByEmail(chosenPromotion.getCode(), email);
            } else {
                redeemApiCall = Profile.redeemPromotionByPhone(chosenPromotion.getCode(), accountManager.getPhone());
            }
            redeemApiCall.execute(this::onOrderPlaced, new DataActionListener<ApiError>() {
                @Override
                public void onDataAction(ApiError apiError) {
                    placeOrder.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(placeOrder, getErrorMessage(apiError), Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            new Handler().postDelayed(this::onOrderPlaced, 2000);
        }
    }

    private void onOrderPlaced() {
        placeOrder.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        Tracker.send(createTransactionEvent(accountManager.getCartItems()));
        accountManager.clearCartItems();
        showDialog();
        handleLayoutVisibility();
        Tracker.send(new HitTimerEvent("Shopping Cart process - end"));
    }

    private void toggleChoice(RadioButton button1, RadioButton button2) {
        button1.setChecked(true);
        button2.setChecked(false);
    }
}