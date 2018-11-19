package com.synerise.sdk.sample.persistence.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.ui.cart.adapter.item.CartItem;

import java.util.ArrayList;
import java.util.List;

public class StoragePOJO {

    @SerializedName("user_name") private String name;
    @SerializedName("user_last_name") private String lastName;
    @SerializedName("user_email") private String email;

    @SerializedName("client_api_key") private String clientApiKey;
    @SerializedName("is_signed_in") private boolean isSignedIn;

    @SerializedName("cart_items") private List<CartItem> cartItems;
    @SerializedName("favourite_products") private List<Product> favouriteProducts;

    @SerializedName("promotions") private List<Promotion> promotions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        isSignedIn = signedIn;
    }

    @NonNull
    public List<CartItem> getCartItems() {
        return cartItems == null ? new ArrayList<>() : cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getClientApiKey() {
        return clientApiKey == null ? "" : clientApiKey;
    }

    public void setClientApiKey(String clientProfileApiKey) {
        this.clientApiKey = clientProfileApiKey;
    }

    @NonNull
    public List<Product> getFavouriteProducts() {
        return favouriteProducts == null ? new ArrayList<>() : favouriteProducts;
    }

    public void setFavouriteProducts(List<Product> favouriteProducts) {
        this.favouriteProducts = favouriteProducts;
    }

    @NonNull
    public List<Promotion> getPromotions() {
        return promotions == null ? new ArrayList<>() : promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }
}